package com.qwerty.nexus.domain.game.reward.service;

import com.qwerty.nexus.domain.game.item.entity.ItemEntity;
import com.qwerty.nexus.domain.game.item.entity.UserItemInstanceEntity;
import com.qwerty.nexus.domain.game.item.entity.UserItemStackEntity;
import com.qwerty.nexus.domain.game.item.repository.ItemRepository;
import com.qwerty.nexus.domain.game.item.repository.UserItemInstanceRepository;
import com.qwerty.nexus.domain.game.item.repository.UserItemStackRepository;
import com.qwerty.nexus.domain.game.reward.dto.GrantDto;
import com.qwerty.nexus.domain.game.reward.dto.RewardDto;
import com.qwerty.nexus.domain.game.reward.entity.RewardGrantEntity;
import com.qwerty.nexus.domain.game.reward.entity.RewardGrantItemEntity;
import com.qwerty.nexus.domain.game.reward.repository.RewardRepository;
import com.qwerty.nexus.global.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jooq.JSONB;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class RewardService {
    private static final String REWARD_SERVICE_ACTOR = "NEXUS-SYSTEM";

    private final ItemRepository itemRepository;
    private final UserItemStackRepository userItemStackRepository;
    private final UserItemInstanceRepository userItemInstanceRepository;

    private final RewardRepository rewardRepository;

    /**
     * 보상 지급 처리
     * @param grantDto 보상지급처리 dto
     * @return 성공 및 실패 여부
     */
    @Transactional
    public boolean grant(GrantDto grantDto) {
        int grantId = 0;
        int grantItemId = 0;

        // 지급 요청 정보 검증처리
        validateGrantRequest(grantDto);

        // 아이템 지급 트랜잭션 등록 - PENDING
        RewardGrantEntity rewardGrantEntity = RewardGrantEntity.builder()
                .gameId(grantDto.getGameId())
                .userId(grantDto.getUserId())
                .status("PENDING")
                .idempotencyKey(generateIdempotencyKey(grantDto))
                .sourceId(grantDto.getSourceId())
                .sourceType(grantDto.getSourceType())
                .createdBy(REWARD_SERVICE_ACTOR)
                .updatedBy(REWARD_SERVICE_ACTOR)
                .build();

        try{
            // 트랜잭션 저장시 UNIQUE KEY(GAME_ID, IDEMPOTENCY_KEY) 존재 유무로 멱등성 자동 체크
            grantId = rewardRepository.insertGrant(rewardGrantEntity);
        } catch (DuplicateKeyException e){
            return false;
        }

        // 보상 리스트 순회
        for(RewardDto reward : grantDto.getRewards()){
            // 아이템 테이블 조회
            ItemEntity itemEntity = ItemEntity.builder()
                    .gameId(grantDto.getGameId())
                    .itemId(reward.getItemId())
                    .itemCode(CommonUtil.normalizeText(reward.getItemCode()))
                    .build();

            Optional<ItemEntity> rst = itemRepository.findByItemIdAndItemCodeAndGameId(itemEntity);

            if(rst.isPresent()){
                ItemEntity rewardItemInfo = rst.get();
                String isStackable = rewardItemInfo.getIsStackable();

                // 아이템 지급 내역 등록 - PENDING
                RewardGrantItemEntity rewardGrantItemEntity = RewardGrantItemEntity.builder()
                        .grantId(grantId)
                        .itemId(rewardItemInfo.getItemId())
                        .isStackable(isStackable)
                        .itemCode(rewardItemInfo.getItemCode())
                        .itemType(rewardItemInfo.getItemType().name())
                        .qty(reward.getQty())
                        .status("PENDING")
                        .createdBy(REWARD_SERVICE_ACTOR)
                        .updatedBy(REWARD_SERVICE_ACTOR)
                        .build();

                grantItemId = rewardRepository.insertGrantItem(rewardGrantItemEntity);

                if(isStackable.equalsIgnoreCase("Y")){
                    // STACK
                    UserItemStackEntity userItemStackEntity = UserItemStackEntity.builder()
                            .userId(grantDto.getUserId())
                            .itemId(reward.getItemId())
                            .createdBy(REWARD_SERVICE_ACTOR)
                            .updatedBy(REWARD_SERVICE_ACTOR)
                            .build();

                    userItemStackRepository.updateUserItemQtyAddByUserIdAndItemId(userItemStackEntity, reward.getQty());
                }else{
                    // NON-STACK (INSTANCE)
                    UserItemInstanceEntity userItemInstanceEntity = UserItemInstanceEntity.builder()
                            .userId(grantDto.getUserId())
                            .itemId(reward.getItemId())
                            .stateJson(JSONB.jsonb("{}")) // TODO : 이거는 어떤 아이템이냐에 따라서 기본 값을 줘야 할듯 (예를들어 무기면 {강화:0, 레벨:1} 이런식으로)
                            .acquiredAt(OffsetDateTime.now())
                            .createdBy(REWARD_SERVICE_ACTOR)
                            .updatedBy(REWARD_SERVICE_ACTOR)
                            .build();

                    userItemInstanceRepository.insertUserItemInstance(userItemInstanceEntity);
                }

                // 아이템 지급 내역 갱신 - SUCCESS
                rewardRepository.updateGrantItem(RewardGrantItemEntity.builder()
                        .grantItemId(grantItemId)
                        .status("SUCCESS")
                        .updatedBy(REWARD_SERVICE_ACTOR)
                        .build());

            }else{
                // 아이템 지급 내역 갱신 - FAILED
                rewardRepository.updateGrantItem(RewardGrantItemEntity.builder()
                        .grantItemId(grantItemId)
                        .status("FAILED")
                        .failReason("보상 아이템이 존재하지 않습니다.")
                        .updatedBy(REWARD_SERVICE_ACTOR)
                        .build());

                // 아이템 지급 트랜잭션 갱신 - FAILED
                rewardRepository.updateGrant(RewardGrantEntity.builder()
                        .grantId(grantId)
                        .status("FAILED")
                        .failReason("일부 혹은 전체 보상 아이템이 존재하지 않습니다.")
                        .updatedBy(REWARD_SERVICE_ACTOR)
                        .build());
            }
        }

        // 아이템 지급 트랜잭션 갱신 - SUCCESS
        rewardRepository.updateGrant(RewardGrantEntity.builder()
                .grantId(grantId)
                .status("SUCCESS")
                .updatedBy(REWARD_SERVICE_ACTOR)
                .build());

        return true;
    }

    /**
     * 멱등키 생성
     * @param grantDto
     * @return
     */
    private String generateIdempotencyKey(GrantDto grantDto){
        String idempotencyKeyTemplate = "%s:%s:%s:%s";

        return String.format(idempotencyKeyTemplate, grantDto.getSourceType(), grantDto.getGameId(), grantDto.getUserId(), grantDto.getSourceId());
    }

    /**
     * 지급 요청 정보 검증
     * @param grantDto
     */
    private void validateGrantRequest(GrantDto grantDto) {
        if (grantDto.getGameId() == null || grantDto.getGameId() <= 0) {
            throw new IllegalArgumentException("게임 ID가 올바르지 않습니다.");
        }

        if (grantDto.getUserId() == null || grantDto.getUserId() <= 0) {
            throw new IllegalArgumentException("유저 ID가 올바르지 않습니다.");
        }

        if (grantDto.getSourceType() == null) {
            throw new IllegalArgumentException("보상 출처는 필수입니다.");
        }

        if (grantDto.getRewards() == null || grantDto.getRewards().isEmpty()) {
            throw new IllegalArgumentException("보상 정보가 비어 있습니다.");
        }

        for (RewardDto reward : grantDto.getRewards()) {
            if (reward.getItemId() == null || reward.getItemId() <= 0) {
                throw new IllegalArgumentException("보상 itemId가 올바르지 않습니다.");
            }

            if (reward.getQty() == null || reward.getQty() <= 0) {
                throw new IllegalArgumentException("보상 qty는 1 이상이어야 합니다.");
            }
        }
    }
}

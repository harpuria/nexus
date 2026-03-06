package com.qwerty.nexus.domain.game.reward.service;

import com.qwerty.nexus.domain.game.item.entity.ItemEntity;
import com.qwerty.nexus.domain.game.item.entity.UserItemInstanceEntity;
import com.qwerty.nexus.domain.game.item.entity.UserItemStackEntity;
import com.qwerty.nexus.domain.game.item.repository.ItemRepository;
import com.qwerty.nexus.domain.game.item.repository.UserItemInstanceRepository;
import com.qwerty.nexus.domain.game.item.repository.UserItemStackRepository;
import com.qwerty.nexus.domain.game.reward.dto.RewardDto;
import com.qwerty.nexus.domain.game.reward.entity.RewardGrantEntity;
import com.qwerty.nexus.domain.game.reward.repository.RewardRepository;
import com.qwerty.nexus.global.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jooq.JSONB;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class RewardService {
    private static final String REWARD_SERVICE_ACTOR = "reward-service";

    private final ItemRepository itemRepository;
    private final UserItemStackRepository userItemStackRepository;
    private final UserItemInstanceRepository userItemInstanceRepository;

    private final RewardRepository rewardRepository;

    /**
     * 보상 지급 처리
     * @param gameId 게임 ID
     * @param userId 유저 ID
     * @param rewards 보상 목록
     * @param sourceType 보상 출처 타입 (ex: COUPON, SHOP 등)
     * @param sourceId 출처 식별자
     * @param requestId 멱등성 키
     * @return 보상 지급 트랜잭션 ID, 실패여부도 주면 좋으려나
     */
    @Transactional
    public void grant(Integer gameId, Integer userId, List<RewardDto> rewards, String sourceType, String sourceId, String requestId) {
        // validation check
        validateGrantRequest(gameId, userId, rewards, sourceType, sourceId, requestId);

        // 멱등성 체크 및 로그 저장
        RewardGrantEntity rewardGrantEntity = RewardGrantEntity.builder()
                .gameId(gameId)
                .userId(userId)
                .requestId(requestId)
                .sourceId(sourceId)
                .sourceType(sourceType)
                .createdBy("NEXUS_SYSTEM")
                .updatedBy("NEXUS_SYSTEM")
                .build();

        try{
            // 로그 저장시 UNIQUE KEY 존재 유무로 멱등성 자동 체크
            rewardRepository.insertGrant(rewardGrantEntity);
        } catch (DuplicateKeyException e){
            return;
        }

        // 보상 리스트 순회
        for(RewardDto reward : rewards){
            // 아이템 마스터 테이블 조회
            ItemEntity itemEntity = ItemEntity.builder()
                    .gameId(gameId)
                    .itemId(reward.getItemId())
                    .itemCode(CommonUtil.normalizeText(reward.getItemCode()))
                    .build();

            Optional<ItemEntity> rst = itemRepository.findByItemIdAndItemCodeAndGameId(itemEntity);
            if(rst.isPresent()){
                String isStackable = rst.get().getIsStackable();
                if(isStackable.equalsIgnoreCase("Y")){
                    // STACK
                    UserItemStackEntity userItemStackEntity = UserItemStackEntity.builder()
                            .userId(userId)
                            .itemId(reward.getItemId())
                            .createdBy("NEXUS_SYSTEM")
                            .updatedBy("NEXUS_SYSTEM")
                            .build();

                    userItemStackRepository.updateUserItemAmountAddByUserIdAndItemId(userItemStackEntity, reward.getAmount());
                }else{
                    // NON-STACK (INSTANCE)
                    UserItemInstanceEntity userItemInstanceEntity = UserItemInstanceEntity.builder()
                            .userId(userId)
                            .itemId(reward.getItemId())
                            .stateJson(JSONB.jsonb("{}"))
                            .acquiredAt(OffsetDateTime.now())
                            .createdBy("NEXUS_SYSTEM")
                            .updatedBy("NEXUS_SYSTEM")
                            .build();

                    userItemInstanceRepository.insertUserItemInstance(userItemInstanceEntity);
                }
            }
        }
    }

    private void validateGrantRequest(Integer gameId, Integer userId, List<RewardDto> rewards, String sourceType, String sourceId, String requestId) {
        if (gameId == null || gameId <= 0) {
            throw new IllegalArgumentException("게임 ID가 올바르지 않습니다.");
        }

        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("유저 ID가 올바르지 않습니다.");
        }

        if (!StringUtils.hasText(sourceType) || !StringUtils.hasText(sourceId) || !StringUtils.hasText(requestId)) {
            throw new IllegalArgumentException("보상 출처와 requestId는 필수입니다.");
        }

        if (rewards == null || rewards.isEmpty()) {
            throw new IllegalArgumentException("보상 정보가 비어 있습니다.");
        }

        for (RewardDto reward : rewards) {
            if (reward.getItemId() == null || reward.getItemId() <= 0) {
                throw new IllegalArgumentException("보상 itemId가 올바르지 않습니다.");
            }

            if (reward.getAmount() == null || reward.getAmount() <= 0) {
                throw new IllegalArgumentException("보상 amount는 1 이상이어야 합니다.");
            }
        }
    }
}

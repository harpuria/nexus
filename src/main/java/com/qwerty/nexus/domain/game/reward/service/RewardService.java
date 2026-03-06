package com.qwerty.nexus.domain.game.reward.service;

import com.qwerty.nexus.domain.game.item.entity.ItemEntity;
import com.qwerty.nexus.domain.game.item.repository.ItemRepository;
import com.qwerty.nexus.domain.game.reward.RewardGrantItemResultType;
import com.qwerty.nexus.domain.game.reward.RewardGrantStatus;
import com.qwerty.nexus.domain.game.reward.dto.RewardDto;
import com.qwerty.nexus.domain.game.reward.entity.RewardGrantEntity;
import com.qwerty.nexus.domain.game.reward.entity.RewardGrantItemEntity;
import com.qwerty.nexus.domain.game.reward.repository.RewardLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class RewardService {
    private static final String REWARD_SERVICE_ACTOR = "reward-service";

    private final ItemRepository itemRepository;
    private final RewardLogRepository rewardLogRepository;

    /**
     * 보상 지급 처리
     * @param gameId 게임 ID
     * @param userId 유저 ID
     * @param rewards 보상 목록
     * @param sourceType 보상 출처 타입
     * @param sourceId 출처 식별자
     * @param requestId 멱등성 키
     * @return 보상 지급 트랜잭션 ID
     */
    @Transactional
    public Integer grant(Integer gameId, Integer userId, List<RewardDto> rewards, String sourceType, String sourceId, String requestId) {
        validateGrantRequest(gameId, userId, rewards, sourceType, sourceId, requestId);

        Optional<RewardGrantEntity> existingGrant = rewardLogRepository.findByGameIdAndRequestId(gameId, requestId);
        if (existingGrant.isPresent()) {
            return existingGrant.get().getGrantId();
        }

        long totalAmount = rewards.stream()
                .map(RewardDto::getAmount)
                .filter(amount -> amount != null && amount > 0)
                .mapToLong(Long::longValue)
                .sum();

        RewardGrantEntity grantEntity = RewardGrantEntity.builder()
                .gameId(gameId)
                .userId(userId)
                .requestId(requestId)
                .sourceType(sourceType)
                .sourceId(sourceId)
                .status(RewardGrantStatus.SUCCESS)
                .itemCount(rewards.size())
                .totalAmount(totalAmount)
                .createdBy(REWARD_SERVICE_ACTOR)
                .updatedBy(REWARD_SERVICE_ACTOR)
                .build();

        Integer grantId = rewardLogRepository.insertRewardGrant(grantEntity);
        if (grantId == null) {
            throw new IllegalStateException("보상 지급 트랜잭션 생성에 실패했습니다.");
        }

        List<RewardGrantItemEntity> grantItems = rewards.stream()
                .map(reward -> buildRewardGrantItem(gameId, userId, grantId, reward))
                .toList();

        rewardLogRepository.insertRewardGrantItems(grantItems);

        return grantId;
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

    private RewardGrantItemEntity buildRewardGrantItem(Integer gameId, Integer userId, Integer grantId, RewardDto rewardDto) {
        Optional<ItemEntity> itemOptional = itemRepository.findByItemId(ItemEntity.builder().itemId(rewardDto.getItemId()).build());
        if (itemOptional.isEmpty()) {
            throw new IllegalArgumentException("보상 itemId에 해당하는 아이템을 찾을 수 없습니다.");
        }

        ItemEntity item = itemOptional.get();
        return RewardGrantItemEntity.builder()
                .grantId(grantId)
                .gameId(gameId)
                .userId(userId)
                .itemCode(item.getItemCode())
                .itemId(item.getItemId())
                .amount(rewardDto.getAmount())
                .resultType(RewardGrantItemResultType.OK)
                .createdBy(REWARD_SERVICE_ACTOR)
                .updatedBy(REWARD_SERVICE_ACTOR)
                .build();
    }
}

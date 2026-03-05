package com.qwerty.nexus.domain.reward.service;

import com.qwerty.nexus.domain.reward.dto.RewardDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class RewardService {

    /**
     *      // 각 재화와 관련된 서비스들
     *     private final ItemService itemService;
     *     private final CurrencyService currencyService;
     *     private final InventoryService inventoryService;
     *
     *     // 보상 로그 관련 처리
     *     private final RewardLogRepository rewardLogRepository;
     */

    /**
     * 보상 지급 처리
     * @param gameId
     * @param userId
     * @param rewards
     * @param sourceType
     * @param sourceId
     * @param requestId
     */
    @Transactional
    public void grant(int gameId, int userId, List<RewardDto> rewards, String sourceType, String sourceId, String requestId){
        // 1) 멱등성 체크 (실제 지급 여부 체크)
        // 멱등키 (예: MAIL:uid:mailId, SHOP:orderId)'
        // Idempotency Key 멱등키 영문명임
        // 멱등키 만드는 메소드를 따로 만들어놔야할듯 (여기에 만들던지)

        // 2) ITEM_MASTER 조회

        // 3) ITEM 타입별 아이템 지급

        // 4) 지급 로그 기록
    }
}

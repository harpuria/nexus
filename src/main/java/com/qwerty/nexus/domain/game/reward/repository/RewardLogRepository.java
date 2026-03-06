package com.qwerty.nexus.domain.game.reward.repository;

import com.qwerty.nexus.domain.game.reward.entity.RewardGrantEntity;
import com.qwerty.nexus.domain.game.reward.entity.RewardGrantItemEntity;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JRewardGrant;
import org.jooq.generated.tables.JRewardGrantItem;
import org.jooq.generated.tables.daos.RewardGrantDao;
import org.jooq.generated.tables.daos.RewardGrantItemDao;
import org.jooq.generated.tables.records.RewardGrantItemRecord;
import org.jooq.generated.tables.records.RewardGrantRecord;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RewardLogRepository {
    private final DSLContext dslContext;
    private final JRewardGrant REWARD_GRANT = JRewardGrant.REWARD_GRANT;
    private final JRewardGrantItem REWARD_GRANT_ITEM = JRewardGrantItem.REWARD_GRANT_ITEM;
    private final RewardGrantDao rewardGrantDao;
    private final RewardGrantItemDao rewardGrantItemDao;

    public RewardLogRepository(Configuration configuration, DSLContext dslContext) {
        this.dslContext = dslContext;
        this.rewardGrantDao = new RewardGrantDao(configuration);
        this.rewardGrantItemDao = new RewardGrantItemDao(configuration);
    }

    public Optional<RewardGrantEntity> findByGameIdAndRequestId(Integer gameId, String requestId) {
        return Optional.ofNullable(dslContext.selectFrom(REWARD_GRANT)
                .where(REWARD_GRANT.GAME_ID.eq(gameId))
                .and(REWARD_GRANT.REQUEST_ID.eq(requestId))
                .and(REWARD_GRANT.IS_DEL.eq("N"))
                .fetchOneInto(RewardGrantEntity.class));
    }

    public Integer insertRewardGrant(RewardGrantEntity entity) {
        RewardGrantRecord record = dslContext.newRecord(REWARD_GRANT, entity);
        record.store();

        return record.getGrantId();
    }

    public int updateRewardGrant(RewardGrantEntity entity) {
        RewardGrantRecord record = dslContext.newRecord(REWARD_GRANT, entity);
        record.changed(REWARD_GRANT.STATUS, entity.getStatus() != null);
        record.changed(REWARD_GRANT.FAIL_CODE, entity.getFailCode() != null);
        record.changed(REWARD_GRANT.FAIL_MESSAGE, entity.getFailMessage() != null);
        record.changed(REWARD_GRANT.ITEM_COUNT, entity.getItemCount() != null);
        record.changed(REWARD_GRANT.TOTAL_AMOUNT, entity.getTotalAmount() != null);
        record.changed(REWARD_GRANT.UPDATED_BY, entity.getUpdatedBy() != null);
        record.changed(REWARD_GRANT.IS_DEL, entity.getIsDel() != null);

        return record.update();
    }

    public Integer insertRewardGrantItem(RewardGrantItemEntity entity) {
        RewardGrantItemRecord record = dslContext.newRecord(REWARD_GRANT_ITEM, entity);
        record.store();

        return record.getGrantItemId();
    }

    public void insertRewardGrantItems(List<RewardGrantItemEntity> entities) {
        for (RewardGrantItemEntity entity : entities) {
            insertRewardGrantItem(entity);
        }
    }
}

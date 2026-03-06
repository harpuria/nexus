package com.qwerty.nexus.domain.game.reward.repository;

import com.qwerty.nexus.domain.game.reward.entity.RewardGrantEntity;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JRewardGrant;
import org.jooq.generated.tables.JRewardGrantItem;
import org.jooq.generated.tables.daos.RewardGrantDao;
import org.jooq.generated.tables.daos.RewardGrantItemDao;
import org.jooq.generated.tables.records.RewardGrantRecord;
import org.springframework.stereotype.Repository;

@Repository
public class RewardRepository {
    private final DSLContext dslContext;
    private final JRewardGrant REWARD_GRANT = JRewardGrant.REWARD_GRANT;
    private final JRewardGrantItem REWARD_GRANT_ITEM = JRewardGrantItem.REWARD_GRANT_ITEM;
    private final RewardGrantDao rewardGrantDao;
    private final RewardGrantItemDao rewardGrantItemDao;

    public RewardRepository(Configuration configuration, DSLContext dslContext) {
        this.dslContext = dslContext;
        this.rewardGrantDao = new RewardGrantDao(configuration);
        this.rewardGrantItemDao = new RewardGrantItemDao(configuration);
    }

    /**
     * 트랜잭션 등록
     * @param rewardGrantEntity
     * @return
     */
    public Integer insertGrant(RewardGrantEntity rewardGrantEntity) {
        RewardGrantRecord record = dslContext.newRecord(REWARD_GRANT, rewardGrantEntity);
        record.store();

        return record.getGrantId();
    }
}

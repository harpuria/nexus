package com.qwerty.nexus.domain.game.data.item.repository;

import com.qwerty.nexus.domain.game.data.item.entity.UserItemInstanceEntity;
import com.qwerty.nexus.domain.game.data.item.result.UserItemInstanceListResult;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.paging.PagingEntity;
import org.jooq.*;
import org.jooq.generated.tables.JItemMaster;
import org.jooq.generated.tables.JUserItemInstance;
import org.jooq.generated.tables.daos.UserItemInstanceDao;
import org.jooq.generated.tables.records.UserItemInstanceRecord;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Repository
public class UserItemInstanceRepository {
    private final DSLContext dslContext;
    private final JUserItemInstance USER_ITEM_INSTANCE = JUserItemInstance.USER_ITEM_INSTANCE;
    private final JItemMaster ITEM = JItemMaster.ITEM_MASTER;
    private final UserItemInstanceDao dao;

    public UserItemInstanceRepository(Configuration configuration, DSLContext dslContext) {
        this.dslContext = dslContext;
        this.dao = new UserItemInstanceDao(configuration);
    }

    public Integer insertUserItemInstance(UserItemInstanceEntity entity) {
        UserItemInstanceRecord record = dslContext.newRecord(USER_ITEM_INSTANCE, entity);
        record.store();
        return record.getUserItemId();
    }

    public int updateUserItemInstance(UserItemInstanceEntity entity) {
        UserItemInstanceRecord record = dslContext.newRecord(USER_ITEM_INSTANCE, entity);
        record.changed(USER_ITEM_INSTANCE.USER_ID, entity.getUserId() != null);
        record.changed(USER_ITEM_INSTANCE.ITEM_ID, entity.getItemId() != null);
        record.changed(USER_ITEM_INSTANCE.STATE_JSON, entity.getStateJson() != null);
        record.changed(USER_ITEM_INSTANCE.ACQUIRED_AT, entity.getAcquiredAt() != null);
        record.changed(USER_ITEM_INSTANCE.UPDATED_BY, entity.getUpdatedBy() != null);
        record.changed(USER_ITEM_INSTANCE.IS_DEL, entity.getIsDel() != null);
        return record.update();
    }

    public Optional<UserItemInstanceEntity> findByUserItemId(UserItemInstanceEntity entity) {
        return Optional.ofNullable(dslContext.selectFrom(USER_ITEM_INSTANCE)
                .where(USER_ITEM_INSTANCE.USER_ITEM_ID.eq(entity.getUserItemId()))
                .and(USER_ITEM_INSTANCE.IS_DEL.eq("N"))
                .fetchOneInto(UserItemInstanceEntity.class));
    }

    public List<UserItemInstanceListResult> findAllByUserIdAndItemId(PagingEntity pagingEntity, Integer userId, Integer itemId, Integer gameId) {
        Condition condition = DSL.noCondition()
                .and(USER_ITEM_INSTANCE.IS_DEL.eq("N"))
                .and(ITEM.IS_DEL.eq("N"));

        if (userId != null) condition = condition.and(USER_ITEM_INSTANCE.USER_ID.eq(userId));
        if (itemId != null) condition = condition.and(USER_ITEM_INSTANCE.ITEM_ID.eq(itemId));
        if (gameId != null) condition = condition.and(ITEM.GAME_ID.eq(gameId));

        int size = pagingEntity.getSize() > 0 ? pagingEntity.getSize() : ApiConstants.Pagination.DEFAULT_PAGE_SIZE;
        int page = Math.max(pagingEntity.getPage(), ApiConstants.Pagination.DEFAULT_PAGE_NUMBER);

        return dslContext.select(USER_ITEM_INSTANCE.USER_ITEM_ID, ITEM.NAME.as("itemName"), USER_ITEM_INSTANCE.STATE_JSON)
                .from(USER_ITEM_INSTANCE)
                .innerJoin(ITEM).on(USER_ITEM_INSTANCE.ITEM_ID.eq(ITEM.ITEM_ID))
                .where(condition)
                .orderBy(resolveSortField(pagingEntity.getSort(), pagingEntity.getDirection()))
                .limit(size)
                .offset(page * size)
                .fetchInto(UserItemInstanceListResult.class);
    }

    public long countByUserIdAndItemId(Integer userId, Integer itemId, Integer gameId) {
        Condition condition = DSL.noCondition()
                .and(USER_ITEM_INSTANCE.IS_DEL.eq("N"))
                .and(ITEM.IS_DEL.eq("N"));

        if (userId != null) condition = condition.and(USER_ITEM_INSTANCE.USER_ID.eq(userId));
        if (itemId != null) condition = condition.and(USER_ITEM_INSTANCE.ITEM_ID.eq(itemId));
        if (gameId != null) condition = condition.and(ITEM.GAME_ID.eq(gameId));

        Long totalCount = dslContext.selectCount()
                .from(USER_ITEM_INSTANCE)
                .innerJoin(ITEM).on(USER_ITEM_INSTANCE.ITEM_ID.eq(ITEM.ITEM_ID))
                .where(condition)
                .fetchOne(0, Long.class);

        return totalCount != null ? totalCount : 0L;
    }

    private SortField<?> resolveSortField(String sort, String direction) {
        String sortKey = Optional.ofNullable(sort).orElse(ApiConstants.Pagination.DEFAULT_SORT_FIELD).toLowerCase(Locale.ROOT);
        Field<?> sortField = switch (sortKey) {
            case "useritemid", "user_item_id" -> USER_ITEM_INSTANCE.USER_ITEM_ID;
            case "userid", "user_id" -> USER_ITEM_INSTANCE.USER_ID;
            case "itemid", "item_id" -> USER_ITEM_INSTANCE.ITEM_ID;
            case "name", "itemname" -> ITEM.NAME;
            case "updatedat", "updated_at" -> USER_ITEM_INSTANCE.UPDATED_AT;
            case "createdat", "created_at" -> USER_ITEM_INSTANCE.CREATED_AT;
            default -> USER_ITEM_INSTANCE.CREATED_AT;
        };
        boolean isAsc = ApiConstants.Pagination.SORT_ASC.equalsIgnoreCase(direction);
        return isAsc ? sortField.asc() : sortField.desc();
    }
}

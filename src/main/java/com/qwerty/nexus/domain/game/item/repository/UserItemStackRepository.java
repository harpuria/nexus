package com.qwerty.nexus.domain.game.item.repository;

import com.qwerty.nexus.domain.game.item.entity.UserItemStackEntity;
import com.qwerty.nexus.domain.game.item.result.UserItemStackListResult;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.paging.PagingEntity;
import org.jooq.*;
import org.jooq.generated.tables.JItemMaster;
import org.jooq.generated.tables.JUserItemStack;
import org.jooq.generated.tables.daos.UserItemStackDao;
import org.jooq.generated.tables.records.UserItemStackRecord;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Repository
public class UserItemStackRepository {
    private final DSLContext dslContext;
    private final JUserItemStack USER_ITEM_STACK = JUserItemStack.USER_ITEM_STACK;
    private final JItemMaster ITEM = JItemMaster.ITEM_MASTER;
    private final UserItemStackDao dao;

    public UserItemStackRepository(Configuration configuration, DSLContext dslContext) {
        this.dslContext = dslContext;
        this.dao = new UserItemStackDao(configuration);
    }

    public Integer insertUserItemStack(UserItemStackEntity entity) {
        UserItemStackRecord record = dslContext.newRecord(USER_ITEM_STACK, entity);
        record.store();
        return record.getUserItemStackId();
    }

    public int updateUserItemStack(UserItemStackEntity entity) {
        UserItemStackRecord record = dslContext.newRecord(USER_ITEM_STACK, entity);
        record.changed(USER_ITEM_STACK.USER_ID, entity.getUserId() != null);
        record.changed(USER_ITEM_STACK.ITEM_ID, entity.getItemId() != null);
        record.changed(USER_ITEM_STACK.AMOUNT, entity.getAmount() != null);
        record.changed(USER_ITEM_STACK.UPDATED_BY, entity.getUpdatedBy() != null);
        record.changed(USER_ITEM_STACK.IS_DEL, entity.getIsDel() != null);
        return record.update();
    }

    public Optional<UserItemStackEntity> findByUserItemStackId(UserItemStackEntity entity) {
        return Optional.ofNullable(dslContext.selectFrom(USER_ITEM_STACK)
                .where(USER_ITEM_STACK.USER_ITEM_STACK_ID.eq(entity.getUserItemStackId()))
                .and(USER_ITEM_STACK.IS_DEL.eq("N"))
                .fetchOneInto(UserItemStackEntity.class));
    }

    public Optional<UserItemStackEntity> findByUserIdAndItemId(UserItemStackEntity entity) {
        return Optional.ofNullable(dslContext.selectFrom(USER_ITEM_STACK)
                .where(USER_ITEM_STACK.USER_ID.eq(entity.getUserId()))
                .and(USER_ITEM_STACK.ITEM_ID.eq(entity.getItemId()))
                .and(USER_ITEM_STACK.IS_DEL.eq("N"))
                .fetchOneInto(UserItemStackEntity.class));
    }

    public int updateUserItemAmountAddByUserIdAndItemId(UserItemStackEntity entity, Long amount) {
        if (entity.getUserId() == null || entity.getItemId() == null || amount == null) {
            return 0;
        }

        var query = dslContext.update(USER_ITEM_STACK)
                .set(USER_ITEM_STACK.AMOUNT, USER_ITEM_STACK.AMOUNT.plus(amount));

        if (entity.getUpdatedBy() != null) {
            query.set(USER_ITEM_STACK.UPDATED_BY, entity.getUpdatedBy());
        }

        return query.where(USER_ITEM_STACK.USER_ID.eq(entity.getUserId()))
                .and(USER_ITEM_STACK.ITEM_ID.eq(entity.getItemId()))
                .and(USER_ITEM_STACK.IS_DEL.eq("N"))
                .execute();
    }

    public int updateUserItemAmountSubtractByUserIdAndItemId(UserItemStackEntity entity, Long amount) {
        if (entity.getUserId() == null || entity.getItemId() == null || amount == null || amount <= 0) {
            return 0;
        }

        var query = dslContext.update(USER_ITEM_STACK)
                .set(USER_ITEM_STACK.AMOUNT, USER_ITEM_STACK.AMOUNT.minus(amount));

        if (entity.getUpdatedBy() != null) {
            query.set(USER_ITEM_STACK.UPDATED_BY, entity.getUpdatedBy());
        }

        return query.where(USER_ITEM_STACK.USER_ID.eq(entity.getUserId()))
                .and(USER_ITEM_STACK.ITEM_ID.eq(entity.getItemId()))
                .and(USER_ITEM_STACK.AMOUNT.ge(amount))
                .and(USER_ITEM_STACK.IS_DEL.eq("N"))
                .execute();
    }

    public List<UserItemStackListResult> findAllByUserIdAndItemId(PagingEntity pagingEntity, Integer userId, Integer itemId, Integer gameId) {
        Condition condition = DSL.noCondition()
                .and(USER_ITEM_STACK.IS_DEL.eq("N"))
                .and(ITEM.IS_DEL.eq("N"));

        if (userId != null) condition = condition.and(USER_ITEM_STACK.USER_ID.eq(userId));
        if (itemId != null) condition = condition.and(USER_ITEM_STACK.ITEM_ID.eq(itemId));
        if (gameId != null) condition = condition.and(ITEM.GAME_ID.eq(gameId));

        int size = pagingEntity.getSize() > 0 ? pagingEntity.getSize() : ApiConstants.Pagination.DEFAULT_PAGE_SIZE;
        int page = Math.max(pagingEntity.getPage(), ApiConstants.Pagination.DEFAULT_PAGE_NUMBER);

        return dslContext.select(USER_ITEM_STACK.USER_ITEM_STACK_ID, ITEM.NAME.as("itemName"), USER_ITEM_STACK.AMOUNT)
                .from(USER_ITEM_STACK)
                .innerJoin(ITEM).on(USER_ITEM_STACK.ITEM_ID.eq(ITEM.ITEM_ID))
                .where(condition)
                .orderBy(resolveSortField(pagingEntity.getSort(), pagingEntity.getDirection()))
                .limit(size)
                .offset(page * size)
                .fetchInto(UserItemStackListResult.class);
    }

    public long countByUserIdAndItemId(Integer userId, Integer itemId, Integer gameId) {
        Condition condition = DSL.noCondition()
                .and(USER_ITEM_STACK.IS_DEL.eq("N"))
                .and(ITEM.IS_DEL.eq("N"));

        if (userId != null) condition = condition.and(USER_ITEM_STACK.USER_ID.eq(userId));
        if (itemId != null) condition = condition.and(USER_ITEM_STACK.ITEM_ID.eq(itemId));
        if (gameId != null) condition = condition.and(ITEM.GAME_ID.eq(gameId));

        Long totalCount = dslContext.selectCount()
                .from(USER_ITEM_STACK)
                .innerJoin(ITEM).on(USER_ITEM_STACK.ITEM_ID.eq(ITEM.ITEM_ID))
                .where(condition)
                .fetchOne(0, Long.class);

        return totalCount != null ? totalCount : 0L;
    }

    private SortField<?> resolveSortField(String sort, String direction) {
        String sortKey = Optional.ofNullable(sort).orElse(ApiConstants.Pagination.DEFAULT_SORT_FIELD).toLowerCase(Locale.ROOT);
        Field<?> sortField = switch (sortKey) {
            case "useritemstackid", "user_item_stack_id" -> USER_ITEM_STACK.USER_ITEM_STACK_ID;
            case "userid", "user_id" -> USER_ITEM_STACK.USER_ID;
            case "itemid", "item_id" -> USER_ITEM_STACK.ITEM_ID;
            case "amount" -> USER_ITEM_STACK.AMOUNT;
            case "name", "itemname" -> ITEM.NAME;
            case "updatedat", "updated_at" -> USER_ITEM_STACK.UPDATED_AT;
            case "createdat", "created_at" -> USER_ITEM_STACK.CREATED_AT;
            default -> USER_ITEM_STACK.CREATED_AT;
        };
        boolean isAsc = ApiConstants.Pagination.SORT_ASC.equalsIgnoreCase(direction);
        return isAsc ? sortField.asc() : sortField.desc();
    }
}

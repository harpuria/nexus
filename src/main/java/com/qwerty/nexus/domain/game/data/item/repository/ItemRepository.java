package com.qwerty.nexus.domain.game.data.item.repository;

import com.qwerty.nexus.domain.game.data.item.entity.ItemEntity;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.paging.PagingEntity;
import org.jooq.*;
import org.jooq.generated.tables.JItemMaster;
import org.jooq.generated.tables.daos.ItemMasterDao;
import org.jooq.generated.tables.records.ItemMasterRecord;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Repository
public class ItemRepository {
    private final DSLContext dslContext;
    private final JItemMaster ITEM = JItemMaster.ITEM_MASTER;
    private final ItemMasterDao dao;

    public ItemRepository(Configuration configuration, DSLContext dslContext) {
        this.dslContext = dslContext;
        this.dao = new ItemMasterDao(configuration);
    }

    public Integer insertItem(ItemEntity entity) {
        ItemMasterRecord record = dslContext.newRecord(ITEM, entity);
        record.store();
        return record.getItemId();
    }

    public int updateItem(ItemEntity entity) {
        ItemMasterRecord record = dslContext.newRecord(ITEM, entity);
        record.changed(ITEM.NAME, entity.getName() != null);
        record.changed(ITEM.DESC, entity.getDesc() != null);
        record.changed(ITEM.ITEM_TYPE, entity.getItemType() != null);
        record.changed(ITEM.IS_STACKABLE, entity.getIsStackable() != null);
        record.changed(ITEM.MAX_STACK, entity.getMaxStack() != null);
        record.changed(ITEM.RARITY, entity.getRarity() != null);
        record.changed(ITEM.ICON_PATH, entity.getIconPath() != null);
        record.changed(ITEM.META_JSON, entity.getMetaJson() != null);
        record.changed(ITEM.UPDATED_BY, entity.getUpdatedBy() != null);
        record.changed(ITEM.IS_DEL, entity.getIsDel() != null);
        return record.update();
    }

    public Optional<ItemEntity> findByItemId(ItemEntity entity) {
        return Optional.ofNullable(dslContext.selectFrom(ITEM)
                .where(ITEM.ITEM_ID.eq(entity.getItemId()))
                .and(ITEM.IS_DEL.eq("N"))
                .fetchOneInto(ItemEntity.class));
    }

    public List<ItemEntity> findAllByGameId(PagingEntity pagingEntity, Integer gameId) {
        Condition condition = DSL.noCondition().and(ITEM.IS_DEL.eq("N"));
        if (gameId != null) {
            condition = condition.and(ITEM.GAME_ID.eq(gameId));
        }
        if (StringUtils.hasText(pagingEntity.getKeyword())) {
            String keyword = "%" + pagingEntity.getKeyword().trim() + "%";
            condition = condition.and(ITEM.NAME.likeIgnoreCase(keyword).or(ITEM.ITEM_CODE.likeIgnoreCase(keyword)));
        }

        int size = pagingEntity.getSize() > 0 ? pagingEntity.getSize() : ApiConstants.Pagination.DEFAULT_PAGE_SIZE;
        int page = Math.max(pagingEntity.getPage(), ApiConstants.Pagination.DEFAULT_PAGE_NUMBER);

        return dslContext.selectFrom(ITEM)
                .where(condition)
                .orderBy(resolveSortField(pagingEntity.getSort(), pagingEntity.getDirection()))
                .limit(size)
                .offset(page * size)
                .fetchInto(ItemEntity.class);
    }

    public long countByGameIdAndKeyword(PagingEntity pagingEntity, Integer gameId) {
        Condition condition = DSL.noCondition().and(ITEM.IS_DEL.eq("N"));
        if (gameId != null) {
            condition = condition.and(ITEM.GAME_ID.eq(gameId));
        }
        if (StringUtils.hasText(pagingEntity.getKeyword())) {
            String keyword = "%" + pagingEntity.getKeyword().trim() + "%";
            condition = condition.and(ITEM.NAME.likeIgnoreCase(keyword).or(ITEM.ITEM_CODE.likeIgnoreCase(keyword)));
        }

        Long totalCount = dslContext.selectCount().from(ITEM).where(condition).fetchOne(0, Long.class);
        return totalCount != null ? totalCount : 0L;
    }

    private SortField<?> resolveSortField(String sort, String direction) {
        String sortKey = Optional.ofNullable(sort).orElse(ApiConstants.Pagination.DEFAULT_SORT_FIELD).toLowerCase(Locale.ROOT);
        Field<?> sortField = switch (sortKey) {
            case "itemid", "item_id" -> ITEM.ITEM_ID;
            case "name" -> ITEM.NAME;
            case "itemcode", "item_code" -> ITEM.ITEM_CODE;
            case "updatedat", "updated_at" -> ITEM.UPDATED_AT;
            case "createdat", "created_at" -> ITEM.CREATED_AT;
            default -> ITEM.CREATED_AT;
        };
        boolean isAsc = ApiConstants.Pagination.SORT_ASC.equalsIgnoreCase(direction);
        return isAsc ? sortField.asc() : sortField.desc();
    }
}

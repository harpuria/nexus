package com.qwerty.nexus.domain.management.game.repository;

import com.qwerty.nexus.domain.management.game.entity.GameEntity;
import com.qwerty.nexus.global.paging.entity.PagingEntity;
import lombok.extern.log4j.Log4j2;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.generated.tables.JGame;
import org.jooq.generated.tables.daos.GameDao;
import org.jooq.generated.tables.records.GameRecord;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Log4j2
@Repository
public class GameRepository {
    private final DSLContext dslContext;
    private final JGame GAME = JGame.GAME;
    private final GameDao dao;

    public GameRepository(Configuration configuration, DSLContext dslContext) {
        this.dslContext = dslContext;
        this.dao = new GameDao(configuration);
    }

    /**
     * 게임 생성
     * @param game
     * @return
     */
    public Integer insertGame(GameEntity game) {
        GameRecord record = dslContext.newRecord(GAME, game);
        record.store();
        return record.getGameId();
    }

    /**
     * 게임 수정
     * @param game
     * @return
     */
    public int updateGame(GameEntity game) {
        GameRecord record = dslContext.newRecord(GAME, game);
        record.changed(GAME.ORG_ID, game.getOrgId() != null);
        record.changed(GAME.NAME, game.getName() != null);
        record.changed(GAME.CLIENT_APP_ID, game.getClientAppId() != null);
        record.changed(GAME.SIGNATURE_KEY, game.getSignatureKey() != null);
        record.changed(GAME.STATUS, game.getStatus() != null);
        record.changed(GAME.VERSION, game.getVersion() != null);
        record.changed(GAME.UPDATED_BY, game.getUpdatedBy() != null);
        record.changed(GAME.IS_DEL, game.getIsDel() != null);
        return record.update();
    }

    /**
     * 하나의 게임 정보 조회
     * @param id
     * @return
     */
    public GameEntity findByGameId(Integer id){
        if (id == null) {
            return null;
        }

        Condition condition = GAME.IS_DEL.eq("N")
                .and(GAME.GAME_ID.eq(id));

        return dslContext.selectFrom(GAME)
                .where(condition)
                .fetchOneInto(GameEntity.class);
    }

    /**
     * 게임 목록 조회 (페이징)
     *
     */
    public List<GameEntity> findAllByKeyword(PagingEntity pagingEntity){
        // 조건 설정
        Condition condition = DSL.noCondition();

        // 삭제되지 않은 게임만 조회
        condition = condition.and(GAME.IS_DEL.eq("N"));

        // 키워드 검색 (이름검색 <추후 필요시 검색 조건 나눠서 검색하는 부분 만들것>)
        String keywordInput = pagingEntity.getKeyword();
        if (keywordInput != null && !keywordInput.isBlank()) {
            String keyword = "%" + keywordInput.trim() + "%";
            condition = condition.and(
                    GAME.NAME.likeIgnoreCase(keyword)
            );
        }

        // 정렬 기준 설정
        String sortDirection = Optional.ofNullable(pagingEntity.getDirection()).orElse("DESC");
        int size = pagingEntity.getSize();
        int page = pagingEntity.getPage();
        int offset = page * size;

        return dslContext.selectFrom(GAME)
                .where(condition)
                .orderBy(resolveSortField(pagingEntity.getSort(), sortDirection))
                .limit(size)
                .offset(offset)
                .fetchInto(GameEntity.class);
    }

    /**
     * 삭제되지 않은 전체 게임 개수 조회
     *
     * @return 전체 건수
     */
    public long countActiveGames() {
        Long count = dslContext.selectCount()
                .from(GAME)
                .where(GAME.IS_DEL.eq("N"))
                .fetchOneInto(Long.class);

        return count != null ? count : 0L;
    }

    /**
     * 정렬 필드 설정
     * @param sort
     * @param direction
     * @return
     */
    private SortField<?> resolveSortField(String sort, String direction) {
        Field<?> sortField;
        String sortKey = Optional.ofNullable(sort).orElse("createdAt").toLowerCase(java.util.Locale.ROOT);

        sortField = switch (sortKey) {
            case "gameid" -> GAME.GAME_ID;
            case "orgid" -> GAME.ORG_ID;
            case "name" -> GAME.NAME;
            case "status" -> GAME.STATUS;
            case "version" -> GAME.VERSION;
            case "updatedat" -> GAME.UPDATED_AT;
            default -> GAME.CREATED_AT;
        };

        boolean isAsc = "ASC".equalsIgnoreCase(direction);
        return isAsc ? sortField.asc() : sortField.desc();
    }
}

package com.qwerty.nexus.domain.management.game.repository;

import com.qwerty.nexus.domain.management.game.entity.GameEntity;
import lombok.extern.log4j.Log4j2;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JGame;
import org.jooq.generated.tables.daos.GameDao;
import org.jooq.generated.tables.records.GameRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public GameEntity insertGame(GameEntity game) {
        GameRecord record = dslContext.newRecord(GAME, game);
        record.store();
        return game;
    }

    /**
     * 게임 수정
     * @param game
     * @return
     */
    public GameEntity updateGame(GameEntity game) {
        GameRecord record = dslContext.newRecord(GAME, game);
        record.changed(GAME.ORG_ID, game.getOrgId() != null);
        record.changed(GAME.NAME, game.getName() != null);
        record.changed(GAME.CLIENT_APP_ID, game.getClientAppId() != null);
        record.changed(GAME.SIGNATURE_KEY, game.getSignatureKey() != null);
        record.changed(GAME.STATUS, game.getStatus() != null);
        record.changed(GAME.VERSION, game.getVersion() != null);
        record.changed(GAME.CREATED_BY, game.getCreatedBy() != null);
        record.changed(GAME.UPDATED_BY, game.getUpdatedBy() != null);
        record.changed(GAME.IS_DEL, game.getIsDel() != null);
        record.update();
        return game;
    }

    /**
     * 하나의 게임 정보 조회
     * @param id
     * @return
     */
    public GameEntity selectOneGame(Integer id){
        return dslContext.selectFrom(GAME)
                .where(GAME.GAME_ID.eq(id))
                .fetchOneInto(GameEntity.class);
    }

    /**
     * 게임 목록 조회 (페이징)
     *
     * @param page 페이지 번호 (0부터 시작)
     * @param size 페이지 크기
     * @return 요청한 페이지에 해당하는 게임 목록
     */
    public List<GameEntity> selectGameList(int page, int size){
        int safePage = Math.max(page, 0);
        int safeSize = Math.max(size, 1);

        return dslContext.selectFrom(GAME)
                .where(GAME.IS_DEL.eq("N"))
                .orderBy(GAME.CREATED_AT.desc())
                .limit(safeSize)
                .offset(safePage * safeSize)
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
}

package com.qwerty.nexus.domain.game;

import lombok.extern.log4j.Log4j2;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JGame;
import org.jooq.generated.tables.daos.GameDao;
import org.jooq.generated.tables.daos.OrganizationDao;
import org.jooq.generated.tables.records.AdminRecord;
import org.jooq.generated.tables.records.GameRecord;
import org.springframework.boot.context.annotation.Configurations;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
public class GameRepository {
    private DSLContext dslContext;
    private JGame GAME = JGame.GAME;
    private GameDao dao;

    public GameRepository(Configuration configuration, DSLContext dslContext) {
        this.dslContext = dslContext;
        this.dao = new GameDao(configuration);
    }

    /**
     * 게임 생성
     * @param game
     * @return
     */
    public GameRecord insertGame(GameRecord game) {
        GameRecord record = dslContext.newRecord(GAME, game);
        record.store();
        return record;
    }

    /**
     * 게임 수정
     * @param game
     * @return
     */
    public GameRecord updateGame(GameRecord game) {
        GameRecord record = dslContext.newRecord(GAME, game);
        record.changed(GAME.GAME_ID, game.getGameId() != null);
        record.changed(GAME.ORG_ID, game.getOrgId() != null);
        record.changed(GAME.NAME, game.getName() != null);
        record.changed(GAME.CLIENT_APP_ID, game.getClientAppId() != null);
        record.changed(GAME.SIGNATURE_KEY, game.getSignatureKey() != null);
        record.changed(GAME.STATUS, game.getStatus() != null);
        record.changed(GAME.CREATED_BY, game.getCreatedBy() != null);
        record.changed(GAME.UPDATED_BY, game.getUpdatedBy() != null);
        record.changed(GAME.IS_DEL, game.getIsDel() != null);
        record.update();
        return record;
    }

    /**
     * 하나의 게임 정보 조회
     * @param id
     * @return
     */
    public GameRecord selectOneGame(Integer id){
        return dslContext.selectFrom(GAME)
                .where(GAME.GAME_ID.eq(id))
                .fetchOne();
    }
}

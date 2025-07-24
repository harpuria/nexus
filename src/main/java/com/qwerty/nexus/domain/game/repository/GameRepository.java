package com.qwerty.nexus.domain.game.repository;

import com.qwerty.nexus.domain.game.entity.GameEntity;
import lombok.extern.log4j.Log4j2;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JGame;
import org.jooq.generated.tables.daos.GameDao;
import org.jooq.generated.tables.records.GameRecord;
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
}

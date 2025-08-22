package com.qwerty.nexus.domain.game.user.repository;

import com.qwerty.nexus.domain.game.user.entity.GameUserEntity;
import lombok.extern.log4j.Log4j2;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JGameUser;
import org.jooq.generated.tables.daos.GameUserDao;
import org.jooq.generated.tables.records.GameUserRecord;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
public class GameUserRepository {
    private final DSLContext dslContext;
    private final JGameUser GAME_USER = JGameUser.GAME_USER;
    private final GameUserDao dao;

    public GameUserRepository(Configuration configuration, DSLContext dslContext){
        this.dslContext = dslContext;
        this.dao = new GameUserDao(configuration);
    }

    /**
     * 게임 유저 생성
     * @param gameUser
     * @return
     */
    public GameUserEntity createGameUser(GameUserEntity gameUser){
        GameUserRecord record = dslContext.newRecord(GAME_USER, gameUser);
        record.store();
        return gameUser;
    }

    /**
     * 게임 유저 수정
     * @param gameUser
     * @return
     */
    public GameUserEntity updateGameUser(GameUserEntity gameUser){
        GameUserRecord record = dslContext.newRecord(GAME_USER, gameUser);
        record.changed(GAME_USER.USER_ID, gameUser.getUserId() != null);
        record.changed(GAME_USER.GAME_ID, gameUser.getGameId() != null);
        record.changed(GAME_USER.USER_L_ID, gameUser.getUserLId() != null);
        record.changed(GAME_USER.USER_L_PW,  gameUser.getUserLPw() != null);
        record.changed(GAME_USER.NICKNAME,  gameUser.getNickname() != null);
        record.changed(GAME_USER.LOGIN_TYPE,  gameUser.getLoginType() != null);
        record.changed(GAME_USER.DEVICE,  gameUser.getDevice() != null);
        record.changed(GAME_USER.BLOCK_START_DATE,  gameUser.getBlockStartDate() != null);
        record.changed(GAME_USER.BLOCK_END_DATE,  gameUser.getBlockEndDate() != null);
        record.changed(GAME_USER.BLOCK_REASON,  gameUser.getBlockReason() != null);
        record.changed(GAME_USER.IS_WITHDRAWAL,  gameUser.getIsWithdrawal() != null);
        record.changed(GAME_USER.WITHDRAWAL_DATE,  gameUser.getWithdrawalDate() != null);
        record.changed(GAME_USER.WITHDRAWAL_REASON,  gameUser.getWithdrawalReason() != null);
        record.changed(GAME_USER.CREATED_BY,  gameUser.getCreatedBy() != null);
        record.changed(GAME_USER.UPDATED_BY,  gameUser.getUpdatedBy() != null);
        record.changed(GAME_USER.IS_DEL,  gameUser.getIsDel() != null);
        record.update();
        return gameUser;
    }
}

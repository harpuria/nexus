package com.qwerty.nexus.domain.gameUser;

import lombok.extern.log4j.Log4j2;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JGameUser;
import org.jooq.generated.tables.daos.GameUserDao;
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
}

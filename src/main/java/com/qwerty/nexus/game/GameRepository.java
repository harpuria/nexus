package com.qwerty.nexus.game;

import lombok.extern.log4j.Log4j2;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JGame;
import org.jooq.generated.tables.daos.GameDao;
import org.jooq.generated.tables.daos.OrganizationDao;
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
}

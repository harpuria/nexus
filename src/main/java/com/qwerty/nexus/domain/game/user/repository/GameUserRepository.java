package com.qwerty.nexus.domain.game.user.repository;

import com.qwerty.nexus.domain.game.user.entity.GameUserEntity;
import com.qwerty.nexus.global.constant.ApiConstants;
import lombok.extern.log4j.Log4j2;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.generated.tables.JGameUser;
import org.jooq.generated.tables.daos.GameUserDao;
import org.jooq.generated.tables.records.GameUserRecord;
import org.jooq.SortField;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import com.qwerty.nexus.global.paging.entity.PagingEntity;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

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
        return GameUserEntity.builder()
                .userId(record.getUserId())
                .build();
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
        record.changed(GAME_USER.PROVIDER,  gameUser.getProvider() != null);
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

    /**
     * 게임 유저 등록 여부 확인
     * @param entity
     * @return
     */
    public Integer isUserAlreadyRegistered(GameUserEntity entity) {
        return dslContext.selectCount().from(GAME_USER)
                .where(GAME_USER.GAME_ID.eq(entity.getGameId())
                .and(GAME_USER.SOCIAL_ID.eq(entity.getSocialId())))
                .fetchOneInto(Integer.class);
    }

    /**
     * 현재 게임의 유저 ID 전체 가져오기
     * @param entity
     * @return
     */
    public List<Integer> selectAllUserId(GameUserEntity entity){
        return dslContext.select(GAME_USER.USER_ID).from(GAME_USER)
                .where(GAME_USER.GAME_ID.eq((entity.getGameId())))
                .fetch(GAME_USER.USER_ID); // 개별 컬럼을 가져올 때는 이런식으로 반환 처리
    }

    public List<GameUserEntity> selectGameUsers(PagingEntity paging, int gameId) {
        Condition condition = DSL.noCondition();

        condition = condition.and(GAME_USER.IS_DEL.isNull().or(GAME_USER.IS_DEL.eq("N")));
        condition = condition.and(GAME_USER.GAME_ID.eq(gameId));

        if (paging.getKeyword() != null && !paging.getKeyword().isBlank()) {
            String keyword = "%" + paging.getKeyword().trim() + "%";
            condition = condition.and(
                    GAME_USER.NICKNAME.likeIgnoreCase(keyword)
                            .or(GAME_USER.USER_L_ID.likeIgnoreCase(keyword))
            );
        }

        int size = paging.getSize() > 0 ? paging.getSize() : ApiConstants.Pagination.DEFAULT_PAGE_SIZE;
        int page = Math.max(paging.getPage(), ApiConstants.Pagination.DEFAULT_PAGE_NUMBER);
        int offset = page * size;

        return dslContext.selectFrom(GAME_USER)
                .where(condition)
                .orderBy(resolveSortField(paging.getSort(), paging.getDirection()))
                .limit(size)
                .offset(offset)
                .fetchInto(GameUserEntity.class);
    }

    private SortField<?> resolveSortField(String sort, String direction) {
        String sortKey = Optional.ofNullable(sort)
                .orElse(ApiConstants.Pagination.DEFAULT_SORT_FIELD)
                .toLowerCase(Locale.ROOT);

        Field<?> sortField = switch (sortKey) {
            case "userid" -> GAME_USER.USER_ID;
            case "nickname" -> GAME_USER.NICKNAME;
            case "userlid", "user_l_id" -> GAME_USER.USER_L_ID;
            case "updatedat" -> GAME_USER.UPDATED_AT;
            case "createdat" -> GAME_USER.CREATED_AT;
            default -> GAME_USER.CREATED_AT;
        };

        boolean isAsc = ApiConstants.Pagination.SORT_ASC.equalsIgnoreCase(direction);
        return isAsc ? sortField.asc() : sortField.desc();
    }
}
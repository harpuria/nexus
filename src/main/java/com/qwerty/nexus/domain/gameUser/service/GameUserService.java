package com.qwerty.nexus.domain.gameUser.service;

import com.qwerty.nexus.domain.gameUser.dto.request.GameUserRequestDTO;
import com.qwerty.nexus.domain.gameUser.dto.response.GameUserResponseDTO;
import com.qwerty.nexus.domain.gameUser.repository.GameUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jooq.generated.tables.records.GameUserRecord;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class GameUserService {
    private final GameUserRepository gameUserRepository;

    /**
     * 게임 유저 생성
     * @param gameUserRequestDTO
     * @return
     */
    public GameUserResponseDTO createGameUser(GameUserRequestDTO gameUserRequestDTO) {
        GameUserResponseDTO rst = new GameUserResponseDTO();

        Optional<GameUserRecord> insertRst = Optional.ofNullable(gameUserRepository.createGameUser(gameUserRequestDTO.toGameUserRecord()));

        if(insertRst.isPresent()){
            rst.convertPojoToDTO(insertRst.get());
            rst.setMessage("유저가 정상적으로 생성되었습니다.");
        }else{
            rst.setMessage("유저 생성 중 오류가 발생하였습니다. 넥서스 관리자에게 문의해주세요.");
        }

        return rst;
    }

    /**
     * 게임 유저 수정
     * @param gameUserRequestDTO
     * @return
     */
    public GameUserResponseDTO updateGameUser(GameUserRequestDTO gameUserRequestDTO) {
        GameUserResponseDTO rst = new GameUserResponseDTO();

        Optional<GameUserRecord> updateRst = Optional.ofNullable(gameUserRepository.updateGameUser(gameUserRequestDTO.toGameUserRecord()));

        if(updateRst.isPresent()){
            rst.setMessage("유저 정보가 정상적으로 수정되었습니다.");
            rst.convertPojoToDTO(updateRst.get());
        }else{
            rst.setMessage("유저 정보 수정 중 오류가 발생하였습니다. 넥서스 관리자에게 문의해주세요.");
        }

        return rst;
    }
}

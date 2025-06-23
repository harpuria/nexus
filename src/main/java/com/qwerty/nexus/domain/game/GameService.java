package com.qwerty.nexus.domain.game;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jooq.generated.tables.records.GameRecord;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;

    /**
     * 게임 정보 생성
     * @param gameRequestDTO
     * @return
     */
    public GameResponseDTO createGame(GameRequestDTO gameRequestDTO) {
        GameResponseDTO rst = new GameResponseDTO();

        gameRequestDTO.setStatus(GameStatus.STOPPED.name());

        // clientAppId, signatureKey 생성
        gameRequestDTO.setClientAppId(UUID.randomUUID());
        gameRequestDTO.setSignatureKey(UUID.randomUUID());

        Optional<GameRecord> insertRst = Optional.ofNullable(gameRepository.insertGame(gameRequestDTO.toAdminRecord()));

        if(insertRst.isPresent()) {
            rst.convertPojoToDTO(insertRst.get());
            rst.setMessage("정상적으로 게임이 생성되었습니다");
        }
        else{
            rst.setMessage("게임이 정상적으로 생성되지 않았습니다. 넥서스 관리자에게 문의해주세요.");
        }

        return rst;
    }

    /**
     * 게임 정보 수정
     * @param gameRequestDTO
     * @return
     */
    public GameResponseDTO updateGame(GameRequestDTO gameRequestDTO){
        GameResponseDTO rst = new GameResponseDTO();
        Optional<GameRecord> updateRst = Optional.ofNullable(gameRepository.updateGame(gameRequestDTO.toAdminRecord()));

        if(updateRst.isPresent()){
            rst.convertPojoToDTO(updateRst.get());
            rst.setMessage("게임 정보 수정이 정상적으로 진행되었습니다.");
        }
        else{
            rst.setMessage("게임 정보 수정이 실패했습니다. 넥서스 관리자에게 문의해주세요.");
        }

        return rst;
    }

    /**
     *
     * @param id
     * @return
     */
    public GameResponseDTO selectOneGame(Integer id){
        GameResponseDTO rst = new GameResponseDTO();
        GameRecord game = gameRepository.selectOneGame(id);
        return rst;
    }
}

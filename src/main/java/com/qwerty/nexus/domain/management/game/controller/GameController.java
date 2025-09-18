package com.qwerty.nexus.domain.management.game.controller;

import com.qwerty.nexus.domain.management.game.command.GameCreateCommand;
import com.qwerty.nexus.domain.management.game.command.GameUpdateCommand;
import com.qwerty.nexus.domain.management.game.dto.request.GameCreateRequestDto;
import com.qwerty.nexus.domain.management.game.dto.request.GameUpdateRequestDto;
import com.qwerty.nexus.domain.management.game.dto.response.GameResponseDto;
import com.qwerty.nexus.domain.management.game.service.GameService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.response.ApiResponse;
import com.qwerty.nexus.global.response.ResponseEntityUtils;
import com.qwerty.nexus.global.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping(ApiConstants.Path.GAME_PATH)
@RequiredArgsConstructor
@Tag(name = "게임", description = "게임 관련 API")
public class GameController {
    private final GameService service;

    /**
     * 게임 정보 생성
     * @param dto 생성할 게임 정보를 담은 객체 (DTO)
     * @return 성공 혹은 실패 메시지, 오류코드 (실패시)
     */
    @PostMapping
    @Operation(summary = "게임 정보 생성")
    public ResponseEntity<ApiResponse<Void>> createGame(@RequestBody GameCreateRequestDto dto){
        Result<Void> result = service.createGame(GameCreateCommand.from(dto));

        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.CREATED);
    }

    /**
     * 게임 정보 수정
     * @param gameId 게임 아이디 (PK)
     * @param dto 수정할 게임 정보를 담은 객체 (DTO)
     * @return 성공 혹은 실패 메시지, 오류코드 (실패시)
     */
    @PatchMapping("/{gameId}")
    @Operation(summary = "게임 정보 수정")
    public ResponseEntity<ApiResponse<Void>> updateGame(@PathVariable("gameId") int gameId, @RequestBody GameUpdateRequestDto dto){

        dto.setGameId(gameId);

        Result<Void> result = service.updateGame(GameUpdateCommand.from(dto));

        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 한 건의 게임 정보 가져오기
     * @param gameId 게임 아이디 (PK)
     * @return 한 건의 게임 정보를 담은 객체 (DTO)
     */
    @GetMapping("/{gameId}")
    @Operation(summary = "한 건의 게임 정보 조회")
    public ResponseEntity<ApiResponse<GameResponseDto>> selectOneGame(@PathVariable("gameId") Integer gameId){
        Result<GameResponseDto> result = service.selectOneGame(gameId);

        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 게임 목록 조회
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "게임 목록 조회 (개발중)")
    public ResponseEntity<ApiResponse<List<GameResponseDto>>> selectGameList(){
        return null;
    }
}

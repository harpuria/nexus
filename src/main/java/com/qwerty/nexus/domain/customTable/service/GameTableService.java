package com.qwerty.nexus.domain.customTable.service;

import com.qwerty.nexus.domain.customTable.repository.GameTableRepository;
import com.qwerty.nexus.global.response.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class GameTableService {
    private final GameTableRepository gameTableRepository;

    /*
     * 만들 서비스 (매우 심플)
     * 테이블 생성
     * 테이블 수정 or 삭제 (논리적 삭제라 하나로 써도 됨)
     *
     */

    public Result<Void> createTable(){
        return null;
    }

    public Result<Void> updateTable(){
        return null;
    }
}

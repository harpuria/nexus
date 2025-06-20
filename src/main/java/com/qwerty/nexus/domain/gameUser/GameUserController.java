package com.qwerty.nexus.domain.gameUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/gameUser")
@RequiredArgsConstructor
public class GameUserController {
    private final GameUserService gameUserService;
}

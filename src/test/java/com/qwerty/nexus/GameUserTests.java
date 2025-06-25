package com.qwerty.nexus;

import com.qwerty.nexus.domain.gameUser.GameUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GameUserTests {
    @Autowired
    private GameUserService gameUserService;
}

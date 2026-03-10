package com.qwerty.nexus.domain.game.mail.user.service;

import com.qwerty.nexus.domain.game.mail.user.repository.UserMailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserMailService {
    private final UserMailRepository userMailRepository;
}

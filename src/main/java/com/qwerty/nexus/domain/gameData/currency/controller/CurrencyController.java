package com.qwerty.nexus.domain.gameData.currency.controller;

import com.qwerty.nexus.domain.gameData.currency.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RequestMapping()
@RestController
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;
}

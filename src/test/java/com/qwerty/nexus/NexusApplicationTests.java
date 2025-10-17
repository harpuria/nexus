package com.qwerty.nexus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qwerty.nexus.domain.game.product.dto.ProductInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
class NexusApplicationTests {
	@Test
	void contextLoads() throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        List<ProductInfo> test = objectMapper.readValue("[{\"currencyId\" : 1, \"amount\" : 100}]", new TypeReference<List<ProductInfo>>() {});

        for(ProductInfo t : test){
            System.out.println(t.getCurrencyId());
            System.out.println(t.getAmount());
        }
	}
}

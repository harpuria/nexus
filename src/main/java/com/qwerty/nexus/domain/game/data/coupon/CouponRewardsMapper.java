package com.qwerty.nexus.domain.game.data.coupon;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qwerty.nexus.domain.game.data.coupon.dto.CouponRewardsInfo;
import com.qwerty.nexus.domain.game.product.dto.ProductRewardsInfo;
import org.jooq.JSONB;

import java.util.Collections;
import java.util.List;

public class CouponRewardsMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<CouponRewardsInfo> toDto(JSONB jsonb) {
        if (jsonb == null) {
            return Collections.emptyList();
        }

        try {
            return objectMapper.readValue(jsonb.data(), new TypeReference<List<CouponRewardsInfo>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert JSONB to RewardsInfo", e);
        }
    }
}

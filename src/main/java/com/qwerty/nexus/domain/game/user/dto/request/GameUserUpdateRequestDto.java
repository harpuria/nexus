package com.qwerty.nexus.domain.game.user.dto.request;

import com.qwerty.nexus.domain.game.auth.Provider;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
public class GameUserUpdateRequestDto {
    @Schema(example = "1")
    private Integer userId;

    @Schema(example = "1")
    private Integer gameId;

    @Schema(example = "testUser01")
    private String userLId;

    @Schema(example = "P@ssw0rd!")
    private String userLPw;

    @Schema(example = "테스트유저")
    private String nickname;

    @Schema(example = "google-oauth2|1234567890")
    private String socialId;

    @Schema(example = "APPLE")
    private Provider provider;

    @Schema(example = "iPhone 16 Pro")
    private String device;

    @Schema(example = "2026-03-18T09:00:00+09:00")
    private OffsetDateTime blockStartDate;

    @Schema(example = "2026-03-25T23:59:59+09:00")
    private OffsetDateTime blockEndDate;

    @Schema(example = "비정상 결제 시도")
    private String blockReason;

    @Schema(example = "N")
    private String isWithdrawal;

    @Schema(example = "2026-03-18T09:00:00+09:00")
    private OffsetDateTime withdrawalDate;

    @Schema(example = "콘텐츠 소진")
    private String withdrawalReason;

    @Schema(example = "admin")
    private String updatedBy;

    @Schema(example = "N")
    private String isDel;
}

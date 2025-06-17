package com.qwerty.nexus.global.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/***
 * 상속용 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class BaseResponseDTO {
    private String message;
    private int code;
}

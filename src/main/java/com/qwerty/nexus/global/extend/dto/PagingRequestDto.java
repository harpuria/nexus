package com.qwerty.nexus.global.extend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PagingRequestDto {
    @Min(value = 0, message = "페이지는 0 이상이어야 합니다.")
    private int page = 0;

    @Min(value = 1, message = "크기는 1 이상이어야 합니다.")
    @Max(value = 100, message = "크기는 100 이하여야 합니다.")
    private int size = 10;

    private String sort = "createdAt";

    @Pattern(regexp = "^(asc|desc)$", message = "정렬 방향은 asc 또는 desc만 가능합니다.")
    private String direction = "desc";


    @Size(max = 100, message = "검색어는 100자 이하여야 합니다.")
    private String keyword;

    public boolean hasKeyword() {
        return keyword != null && !keyword.trim().isEmpty();
    }

    public String getTrimmedKeyword() {
        return hasKeyword() ? keyword.trim() : "";
    }

    public long getOffset() {
        return (long) page * size;
    }

    public boolean isAsc() {
        return "asc".equalsIgnoreCase(direction);
    }
}

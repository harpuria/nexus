package com.qwerty.nexus.global.util;

import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.paging.entity.PagingEntity;
import lombok.extern.log4j.Log4j2;

/**
 * PagingUtil
 */

@Log4j2
public class PagingUtil {
    /**
     * 기본 페이징 정보를 가지고와서 가공한 뒤 엔티티 반환
     * @param data 페이징 정보가 담긴 데이터
     * @return 반환 엔티티
     */
    public static PagingEntity getPagingEntity(Object data){
        try {
            Class<?> clazz = data.getClass();

            int size = (int)clazz.getMethod(ApiConstants.Pagination.GET_SIZE).invoke(data);
            int page = (int)clazz.getMethod(ApiConstants.Pagination.GET_PAGE).invoke(data);
            String direction = (String)clazz.getMethod(ApiConstants.Pagination.GET_DIRECTION).invoke(data);
            String keyword = (String)clazz.getMethod(ApiConstants.Pagination.GET_KEYWORD).invoke(data);
            String sort = (String)clazz.getMethod(ApiConstants.Pagination.GET_SORT).invoke(data);

            int validationSize = Math.min(size, ApiConstants.Pagination.MAX_PAGE_SIZE);
            if (size < ApiConstants.Pagination.MIN_PAGE_SIZE) {
                validationSize = ApiConstants.Pagination.DEFAULT_PAGE_SIZE;
            }

            int safePage = Math.max(page, ApiConstants.Pagination.DEFAULT_PAGE_NUMBER);

            return PagingEntity.builder()
                    .page(safePage)
                    .size(validationSize)
                    .direction(direction)
                    .keyword(keyword)
                    .sort(sort)
                    .build();

        }catch(Exception e){
            log.error(e.getLocalizedMessage());
        }

        return null;
    }

    /**
     * 페이지 사이즈 검증 및 조정
     *
     * @param size 요청된 페이지 사이즈
     * @return 검증된 페이지 사이즈
     */
    public static int validatePageSize(int size) {
        if (size < ApiConstants.Pagination.MIN_PAGE_SIZE) {
            return ApiConstants.Pagination.DEFAULT_PAGE_SIZE;
        }
        return Math.min(size, ApiConstants.Pagination.MAX_PAGE_SIZE);
    }

}

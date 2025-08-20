package com.qwerty.nexus.admin;

import com.qwerty.nexus.domain.management.admin.controller.AdminController;
import com.qwerty.nexus.domain.management.admin.service.AdminService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AdminController.class)
public class AdminControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdminService adminService;

    @Test
    @DisplayName("관리자 생성 OK")
    void createAdmin_shouldReturnOK() throws Exception{

    }
}

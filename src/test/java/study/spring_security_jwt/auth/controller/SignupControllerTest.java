package study.spring_security_jwt.auth.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import study.spring_security_jwt.auth.dto.MemberDto;
import study.spring_security_jwt.auth.service.SignupService;
import study.spring_security_jwt.global.response.ResponseCode;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf; // CSRF 임포트
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(SignupController.class)
class SignupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SignupService signupService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("회원가입 - 성공")
    @WithMockUser(username = "testUser") // 인증된 사용자
    void signupMember_ShouldReturnSuccess() throws Exception {
        // Given
        MemberDto.Request memberRequestDto = MemberDto.Request.builder()
                .id(1)
                .username("testUser")
                .password("password123")
                .build();

        // When
        doNothing().when(signupService).signupMember(any(MemberDto.Request.class));

        // Then
        mockMvc.perform(post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberRequestDto))
                        .with(csrf())) // CSRF 토큰 추가
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.REGISTER_SUCCESS.getCode())) // 응답 코드 확인
                .andExpect(jsonPath("$.data").value(memberRequestDto.getUsername())); // 응답 데이터 확인
    }
}

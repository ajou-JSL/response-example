package study.spring_security_jwt.auth.controller;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import study.spring_security_jwt.auth.service.ReissueService;
import study.spring_security_jwt.global.error.ErrorCode;
import study.spring_security_jwt.global.error.ErrorResponse;
import study.spring_security_jwt.global.response.ResponseCode;
import study.spring_security_jwt.global.response.ResultResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class ReissueControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ReissueController reissueController;

    @Mock
    private ReissueService reissueService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reissueController).build();
    }

    @Test
    @DisplayName("리프레시 토큰 재발급 - 성공")
    void reissue_ShouldReturnOk_WhenRefreshTokenIsValid() throws Exception {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Cookie refreshTokenCookie = new Cookie("refresh", "validRefreshToken");
        request.setCookies(refreshTokenCookie);

        ResultResponse resultResponse = ResultResponse.of(ResponseCode.REISSUE_SUCCESS, null);

        when(reissueService.reissue(any(HttpServletRequest.class), any(HttpServletResponse.class)))
                .thenReturn(resultResponse);

        // When & Then
        mockMvc.perform(post("/reissue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("request", request)
                        .requestAttr("response", response))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.REISSUE_SUCCESS.getCode()));
    }

    @Test
    @DisplayName("리프레시 토큰 재발급 - 실패 (만료된 토큰)")
    void reissue_ShouldReturnUnauthorized_WhenRefreshTokenIsExpired() throws Exception {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Cookie refreshTokenCookie = new Cookie("refresh", "expiredRefreshToken");
        request.setCookies(refreshTokenCookie);

        doThrow(new ExpiredJwtException(null, null, "Token expired"))
                .when(reissueService).reissue(any(HttpServletRequest.class), any(HttpServletResponse.class));

        // When & Then
        mockMvc.perform(post("/reissue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("request", request)
                        .requestAttr("response", response))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ErrorCode.JWT_TOKEN_EXPIRED.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.JWT_TOKEN_EXPIRED.getMessage()));
    }

    @Test
    @DisplayName("리프레시 토큰 재발급 - 실패 (유효하지 않은 토큰)")
    void reissue_ShouldReturnBadRequest_WhenRefreshTokenIsInvalid() throws Exception {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Cookie refreshTokenCookie = new Cookie("refresh", "invalidRefreshToken");
        request.setCookies(refreshTokenCookie);

        doThrow(new IllegalArgumentException("Invalid refresh token"))
                .when(reissueService).reissue(any(HttpServletRequest.class), any(HttpServletResponse.class));

        // When & Then
        mockMvc.perform(post("/reissue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("request", request)
                        .requestAttr("response", response))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.REFRESH_TOKEN_INVALID.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.REFRESH_TOKEN_INVALID.getMessage()));
    }
}

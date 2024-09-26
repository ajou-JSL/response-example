package study.spring_security_jwt.auth.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import study.spring_security_jwt.auth.domain.entity.RefreshEntity;
import study.spring_security_jwt.auth.domain.repository.RefreshRepository;
import study.spring_security_jwt.auth.jwt.JwtUtil;
import study.spring_security_jwt.global.response.ResponseCode;
import study.spring_security_jwt.global.response.ResultResponse;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ReissueServiceTest {

    @InjectMocks
    private ReissueService reissueService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RefreshRepository refreshRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("리프레시 토큰 재발급 - 성공")
    void reissue_ShouldReturnNewTokens_WhenValidRefreshTokenProvided() {
        // Given
        String refreshToken = "validRefreshToken";
        String username = "testUser";
        String role = "ROLE_USER";
        String newAccessToken = "newAccessToken";
        String newRefreshToken = "newRefreshToken";

        // Mock HttpServletRequest and HttpServletResponse
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setCookies(new Cookie("refresh", refreshToken));

        // Mock JWT Util and Repository behavior
        when(jwtUtil.isExpired(anyString())).thenReturn(false);
        when(jwtUtil.getCategory(anyString())).thenReturn("refresh");
        when(jwtUtil.getUsername(anyString())).thenReturn(username);
        when(jwtUtil.getRole(anyString())).thenReturn(role);
        when(refreshRepository.existsByRefresh(anyString())).thenReturn(true);
        when(jwtUtil.createJwt(eq("access"), eq(username), eq(role), anyLong())).thenReturn(newAccessToken);
        when(jwtUtil.createJwt(eq("refresh"), eq(username), eq(role), anyLong())).thenReturn(newRefreshToken);

        // When
        ResultResponse result = reissueService.reissue(request, response);

        // Then
        assertThat(result.getCode()).isEqualTo(ResponseCode.REISSUE_SUCCESS.getCode());
        assertThat(response.getHeader("access")).isEqualTo(newAccessToken);

        Cookie[] cookies = response.getCookies();
        assertThat(cookies).isNotNull();
        assertThat(cookies.length).isEqualTo(1);
        assertThat(cookies[0].getName()).isEqualTo("refresh");
        assertThat(cookies[0].getValue()).isEqualTo(newRefreshToken);

        verify(refreshRepository).deleteByRefresh(refreshToken);
        verify(refreshRepository).save(any(RefreshEntity.class));
    }

    @Test
    @DisplayName("리프레시 토큰 재발급 - 실패 (유효하지 않은 토큰)")
    void reissue_ShouldThrowException_WhenInvalidRefreshTokenProvided() {
        // Given
        String refreshToken = "invalidRefreshToken";

        // Mock HttpServletRequest and HttpServletResponse
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setCookies(new Cookie("refresh", refreshToken));

        // Mock JWT Util behavior
        when(jwtUtil.isExpired(anyString())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> reissueService.reissue(request, response))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid refresh token");

        verify(refreshRepository, never()).deleteByRefresh(anyString());
        verify(refreshRepository, never()).save(any(RefreshEntity.class));
    }
}

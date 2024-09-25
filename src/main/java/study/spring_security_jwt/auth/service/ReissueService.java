package study.spring_security_jwt.auth.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.spring_security_jwt.auth.domain.entity.RefreshEntity;
import study.spring_security_jwt.auth.domain.repository.RefreshRepository;
import study.spring_security_jwt.auth.jwt.JwtUtil;
import study.spring_security_jwt.global.response.ResponseCode;
import study.spring_security_jwt.global.response.ResultResponse;
import study.spring_security_jwt.global.error.ErrorCode;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ReissueService {

    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public ResultResponse reissue(HttpServletRequest request, HttpServletResponse response) {
        // Get refresh token from cookies
        String refresh = getRefreshTokenFromCookies(request);

        // Refresh token invalid
        if (refresh == null || !isRefreshTokenValid(refresh)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        // Make new JWTs
        String newAccess = jwtUtil.createJwt("access", username, role, 600000L); // 10m
        String newRefresh = jwtUtil.createJwt("refresh", username, role, 86400000L); // 24h

        // Save new refresh token to DB
        refreshRepository.deleteByRefresh(refresh);
        addRefreshEntity(username, newRefresh, 86400000L);

        // Set response headers and cookies
        response.setHeader("access", newAccess);
        response.addCookie(createCookie("refresh", newRefresh));

        return ResultResponse.of(ResponseCode.REISSUE_SUCCESS, null);
    }

    private String getRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private boolean isRefreshTokenValid(String refresh) {
        try {
            return !jwtUtil.isExpired(refresh) && "refresh".equals(jwtUtil.getCategory(refresh)) && refreshRepository.existsByRefresh(refresh);
        } catch (Exception e) {
            return false;
        }
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);
        return cookie;
    }

    private void addRefreshEntity(String username, String refresh, Long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs);
        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setUsername(username);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());
        refreshRepository.save(refreshEntity);
    }
}

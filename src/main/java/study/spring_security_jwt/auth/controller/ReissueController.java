package study.spring_security_jwt.auth.controller;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import study.spring_security_jwt.auth.service.ReissueService;
import study.spring_security_jwt.global.error.ErrorCode;
import study.spring_security_jwt.global.error.ErrorResponse;
import study.spring_security_jwt.global.response.ResponseCode;
import study.spring_security_jwt.global.response.ResultResponse;

@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final ReissueService reissueService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        try {
            ResultResponse resultResponse = reissueService.reissue(request, response);
            return new ResponseEntity<>(resultResponse, HttpStatus.valueOf(resultResponse.getStatus()));
        } catch (ExpiredJwtException e) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.JWT_TOKEN_EXPIRED);
            return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorResponse.getStatus()));
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.REFRESH_TOKEN_INVALID);
            return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorResponse.getStatus()));
        }
    }
}

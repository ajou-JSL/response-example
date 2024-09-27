package study.spring_security_jwt.email.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import study.spring_security_jwt.auth.dto.MemberDto;
import study.spring_security_jwt.email.dto.EmailDto;
import study.spring_security_jwt.email.service.EmailService;
import study.spring_security_jwt.global.error.ErrorCode;
import study.spring_security_jwt.global.error.exception.CustomException;
import study.spring_security_jwt.global.response.ResponseCode;
import study.spring_security_jwt.global.response.ResultResponse;
import study.spring_security_jwt.redis.util.RedisUtil;

@RestController
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/send-mail")
    public ResponseEntity<?> emailAuthentification (@RequestBody EmailDto.Request emailRequestDto) throws Exception {
        String verifyCodeId = emailService.sendCertificationMail(emailRequestDto.getEmail());

        ResultResponse resultResponse = ResultResponse.of(ResponseCode.EMAIL_SEND_SUCCESS, verifyCodeId);
        return new ResponseEntity<>(resultResponse, HttpStatus.valueOf(resultResponse.getStatus()));
    }
}

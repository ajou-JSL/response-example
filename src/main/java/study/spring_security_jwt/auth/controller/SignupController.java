package study.spring_security_jwt.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import study.spring_security_jwt.auth.dto.MemberDto;
import study.spring_security_jwt.auth.service.SignupService;
import study.spring_security_jwt.global.response.ResponseCode;
import study.spring_security_jwt.global.response.ResultResponse;

@RestController
@RequiredArgsConstructor
public class SignupController {
    private final SignupService signupService;

    @PostMapping("/join")
    public ResponseEntity<ResultResponse> signupMember(@Valid @RequestBody MemberDto.Request memberRequestDto) {

        signupService.signupMember(memberRequestDto);
        ResultResponse resultResponse = ResultResponse.of(ResponseCode.REGISTER_SUCCESS, memberRequestDto.getUsername());

        return new ResponseEntity<>(resultResponse, HttpStatus.valueOf(resultResponse.getStatus()));
    }
}

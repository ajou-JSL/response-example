package study.spring_security_jwt.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.spring_security_jwt.auth.domain.CustomUserDetails;
import study.spring_security_jwt.global.response.ResultResponse;

@RestController
public class TestController {

    @GetMapping("/test")
    public ResponseEntity<ResultResponse> testPage(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        // System.out.println(customUserDetails.getUsername());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

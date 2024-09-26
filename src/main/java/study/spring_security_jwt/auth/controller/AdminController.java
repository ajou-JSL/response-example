package study.spring_security_jwt.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @GetMapping("/admin")
    public String adminPage(){
        return "admin controller";
    }

}

/*
        //    @GetMapping("/admin")
//    public ResponseEntity<ResultResponse> adminPage(@AuthenticationPrincipal CustomUserDetails customUserDetails){
//
//        ResultResponse resultResponse = ResultResponse.of(ResponseCode.LOGIN_SUCCESS, customUserDetails.getUsername());
//        return new ResponseEntity<>(resultResponse, HttpStatus.valueOf(resultResponse.getStatus()));
//    }
        @GetMapping("/admin")
        public ResponseEntity<?> adminPage(){

            // ResultResponse resultResponse = ResultResponse.of(ResponseCode.LOGIN_SUCCESS, customUserDetails.getUsername());
            // return new ResponseEntity<>(resultResponse, HttpStatus.valueOf(resultResponse.getStatus()));
            return new ResponseEntity<>(HttpStatus.OK);
        }
 */
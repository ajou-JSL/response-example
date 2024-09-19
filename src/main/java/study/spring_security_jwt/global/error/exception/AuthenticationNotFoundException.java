package study.spring_security_jwt.global.error.exception;


import study.spring_security_jwt.global.error.ErrorCode;

public class AuthenticationNotFoundException extends CustomException {
    public AuthenticationNotFoundException() {
        super(ErrorCode.AUTHENTICATION_NOT_FOUND);
    }
}
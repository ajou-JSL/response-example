package study.spring_security_jwt.global.error.exception;


import study.spring_security_jwt.global.error.ErrorCode;

public class 기타등등Exception extends CustomException{
    public 기타등등Exception() {
        super("etc error message", ErrorCode.NEED_LOGIN);
    }
}

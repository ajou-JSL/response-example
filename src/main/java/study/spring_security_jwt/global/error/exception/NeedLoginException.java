package study.spring_security_jwt.global.error.exception;

import study.spring_security_jwt.global.error.ErrorCode;

public class NeedLoginException extends CustomException{
    public NeedLoginException() {
        super(ErrorCode.NEED_LOGIN);
    }
}

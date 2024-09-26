package study.spring_security_jwt.global.error.exception;

import study.spring_security_jwt.global.error.ErrorCode;

public class DuplicateUsernameException extends CustomException {
    public DuplicateUsernameException() {
        super(ErrorCode.USER_NAME_ALREADY_EXISTS);
    }
}

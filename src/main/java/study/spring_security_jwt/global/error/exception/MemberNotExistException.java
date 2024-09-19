package study.spring_security_jwt.global.error.exception;


import study.spring_security_jwt.global.error.ErrorCode;

public class MemberNotExistException extends CustomException{
    public MemberNotExistException(){super(ErrorCode.MEMBER_NOT_EXIST);}
}

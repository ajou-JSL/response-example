package study.spring_security_jwt.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    // Member
    REGISTER_SUCCESS(200, "M001", "회원가입 되었습니다."),
    LOGIN_SUCCESS(200, "M002", "로그인 되었습니다."),
    REISSUE_SUCCESS(200, "M003", "재발급 되었습니다."),
    LOGOUT_SUCCESS(200, "M004", "로그아웃 되었습니다."),
    GET_MY_INFO_SUCCESS(200, "M005", "내 정보 조회 완료"),

    // Jwt
    ACCESS_TOKEN(200, "J001", "액세스 토큰 발급 성공"),

    // Email
    EMAIL_SEND_SUCCESS(200,"E001","인증 이메일 발송 성공하였습니다."),
    EMAIL_VERIFY_SUCCESS(200,"E001","이메일 인증 성공하였습니다.");

    private final int status;
    private final String code;
    private final String message;
}
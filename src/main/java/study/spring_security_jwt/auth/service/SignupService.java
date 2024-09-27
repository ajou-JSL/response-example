package study.spring_security_jwt.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import study.spring_security_jwt.auth.domain.entity.MemberEntity;
import study.spring_security_jwt.auth.domain.repository.MemberRepository;
import study.spring_security_jwt.auth.dto.MemberDto;
import study.spring_security_jwt.email.service.EmailService;
import study.spring_security_jwt.global.error.ErrorCode;
import study.spring_security_jwt.global.error.exception.CustomException;
import study.spring_security_jwt.global.error.exception.DuplicateUsernameException;
import study.spring_security_jwt.redis.util.RedisUtil;

@Service
@RequiredArgsConstructor
public class SignupService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RedisUtil redisUtil;

    public void signupMember(MemberDto.Request memberRequestDto){

        Boolean isExist = memberRepository.existsByUsername(memberRequestDto.getUsername());
        if(isExist){
            throw new DuplicateUsernameException();
        }
        // dto -> entity
        MemberEntity memberEntity = MemberEntity.builder()
                .id(memberRequestDto.getId())
                .username(memberRequestDto.getUsername())
                .role("ROLE_ADMIN")
                .password(bCryptPasswordEncoder.encode(memberRequestDto.getPassword()))
                .build();

//        // 인증 코드 검증
//        String verifyCode = redisUtil.getData(memberRequestDto.getEmail()); // Redis에서 이메일로 인증 코드 가져오기
//        if (verifyCode == null || !verifyCode.equals(memberRequestDto.getVerifyCode())) {
//            throw new CustomException(ErrorCode.EMAIL_VERIFY_FAILED);
//        }


        memberRepository.save(memberEntity);

    }

}

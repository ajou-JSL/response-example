package study.spring_security_jwt.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import study.spring_security_jwt.auth.domain.entity.MemberEntity;
import study.spring_security_jwt.auth.domain.repository.MemberRepository;
import study.spring_security_jwt.auth.dto.MemberDto;
import study.spring_security_jwt.global.error.ErrorCode;
import study.spring_security_jwt.global.error.exception.CustomException;

@Service
@RequiredArgsConstructor
public class SignupService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void signupMember(MemberDto.Request memberRequestDto){

        Boolean isExist = memberRepository.existsByUsername(memberRequestDto.getUsername());
        if(isExist){
            throw new CustomException(ErrorCode.USER_NAME_ALREADY_EXISTS);
        }
        // dto -> entity
        MemberEntity memberEntity = MemberEntity.builder()
                .id(memberRequestDto.getId())
                .username(memberRequestDto.getUsername())
                .role("ROLE_ADMIN")
                .password(bCryptPasswordEncoder.encode(memberRequestDto.getPassword()))
                .build();

        memberRepository.save(memberEntity);
    }
}

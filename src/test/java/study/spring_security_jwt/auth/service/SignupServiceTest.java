package study.spring_security_jwt.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import study.spring_security_jwt.auth.domain.entity.MemberEntity;
import study.spring_security_jwt.auth.domain.repository.MemberRepository;
import study.spring_security_jwt.auth.dto.MemberDto;
import study.spring_security_jwt.global.error.ErrorCode;
import study.spring_security_jwt.global.error.exception.CustomException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SignupServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private SignupService signupService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - 중복된 유저네임")
    void signupMember_ShouldThrowException_WhenUsernameAlreadyExists() {
        // Given
        MemberDto.Request memberRequestDto = MemberDto.Request.builder()
                .id(1)
                .username("existingUser")
                .password("password123")
                .build();

        // When
        when(memberRepository.existsByUsername(memberRequestDto.getUsername())).thenReturn(true);

        // Then
        CustomException thrown = assertThrows(CustomException.class, () -> {
            signupService.signupMember(memberRequestDto);
        });

        // Check the exception message
        assertEquals(ErrorCode.USER_NAME_ALREADY_EXISTS, thrown.getErrorCode());
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signupMember_ShouldSaveMember_WhenUsernameDoesNotExist() {
        // Given
        MemberDto.Request memberRequestDto = MemberDto.Request.builder()
                .id(1)
                .username("newUser")
                .password("password123")
                .build();

        // When
        when(memberRepository.existsByUsername(memberRequestDto.getUsername())).thenReturn(false);
        when(bCryptPasswordEncoder.encode(memberRequestDto.getPassword())).thenReturn("encodedPassword");

        signupService.signupMember(memberRequestDto);

        // Then
        // verify that save was called on memberRepository
        // additional assertions can be done here if needed
    }
}

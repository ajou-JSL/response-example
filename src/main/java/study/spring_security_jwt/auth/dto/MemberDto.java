package study.spring_security_jwt.auth.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import study.spring_security_jwt.auth.domain.entity.MemberEntity;

public class MemberDto {

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Request{
        private int id;

        @NotEmpty @NotNull
        @Size(min=3, max=10)
        private String username;

        @NotEmpty @NotNull
        private String password;

        public MemberEntity toEntity(){
            return MemberEntity.builder()
                    .id(id)
                    .username(username)
                    .password(password)
                    .build();
        }
    }

    @Getter
    public static class Response{
        private final int id;
        private final String username;

        public Response(MemberEntity member){
            this.id = member.getId();
            this.username = member.getUsername();
        }
    }

}

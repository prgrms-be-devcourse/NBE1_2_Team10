package core.application.users.models.dto;

import core.application.users.models.entities.UserEntity;
import core.application.users.models.entities.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.UUID;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class UserDTO {
    private UUID userId;
    @Email
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message="이메일 주소 양식을 확인해주세요")
    private String userEmail;
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$")
    private String userPw;
    private UserRole role;
    private String alias;
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$")
    private String phoneNum;
    private String userName;

    // UserDTO -> UserEntity 변환 메서드
    public UserEntity toEntity() {
        return UserEntity.builder()
                .userId(this.userId)
                .userEmail(this.userEmail)
                .userPw(this.userPw)
                .role(this.role)
                .alias(this.alias)
                .phoneNum(this.phoneNum)
                .userName(this.userName)
                .build();
    }
}
package core.application.users.models.dto;

import core.application.users.models.entities.UserEntity;
import core.application.users.models.entities.UserRole;
import lombok.*;

import java.util.UUID;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class UserDTO {
    private UUID userId;
    private String userEmail;
    private String userPw;
    private UserRole role;
    private String alias;
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
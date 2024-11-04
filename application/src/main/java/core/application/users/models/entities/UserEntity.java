package core.application.users.models.entities;

import lombok.*;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class UserEntity {
    private UUID     userId;
    private String   userEmail;
    private String   userPw;
    private UserRole role;
    private String   alias;
    private String   phoneNum;
    private String   userName;
}

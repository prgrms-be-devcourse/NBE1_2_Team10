package core.application.users.models.dto;

import core.application.users.models.entities.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    // private String refreshToken
}
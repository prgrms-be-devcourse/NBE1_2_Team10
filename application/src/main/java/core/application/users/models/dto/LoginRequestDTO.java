package core.application.users.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class LoginRequestDTO {
    String userEmail;
    String userPw;
}

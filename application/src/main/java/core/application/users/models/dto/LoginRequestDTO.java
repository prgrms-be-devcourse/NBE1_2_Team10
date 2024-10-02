package core.application.users.models.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor

public class LoginRequestDTO {
    String userEmail;
    String userPw;
}

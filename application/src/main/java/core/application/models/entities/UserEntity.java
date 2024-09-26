package core.application.models.entities;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UserEntity {
    private UUID     userId;
    private String   userEmail;
    private String   userPw;
    private UserRole role;
    private String   refreshToken;
    private String   alias;
    private String   phone_num;
    private String   userName;

    /**
     * 해당 유저가 소유한 찜 목록들
     */
    private List<DibEntity> dibEntityList;
}

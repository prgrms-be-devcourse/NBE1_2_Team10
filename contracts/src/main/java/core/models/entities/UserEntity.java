package core.models.entities;

import java.util.List;
import java.util.UUID;

public class UserEntity {
    private UUID     userId;
    private String   userEmail;
    private String   userPw;
    private UserRole role;
    private String   refreshToken;
    private String   alias;
    private String   phone_num;
    private String   userName;

    // TODO 유저와 연관된 찜 목록 아래처럼 하는게 좋을까?
    /**
     * 해당 유저가 소유한 찜 목록들
     */
    @Deprecated
    private List<DibEntity> dibEntityList;
}

package core.application.users.models.dto;

import java.util.UUID;

public class DibCreateReqDTO {
    /*
     * dibId : 찜 아이디
     * userId : 유저 아이디
     * movieId : 영화 아이디
     */
    private Long dibId;
    private UUID userId;
    private String movieId;
}

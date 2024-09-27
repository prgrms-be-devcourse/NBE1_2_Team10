package core.application.users.models.dto;

import java.util.UUID;

public class DibCancelResDTO {
    /*
    * message : 찜 삭제 완료 메시지
    * userId : 유저 아이디
    * movieId : 찜 삭제한 영화 아이디
     */
    private String message;
    private UUID userId;
    private String movieId;
}

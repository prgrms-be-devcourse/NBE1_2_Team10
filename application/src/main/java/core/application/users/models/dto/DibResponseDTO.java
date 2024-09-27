package core.application.users.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class DibResponseDTO {

    /*
    찜 목록에 사용될 DTO
     * movieId : 영화 아이디
     * movieTitle : 영화 제목
     * moviePost : 영화 포스터 URL
    */

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DibDTO{
        String movieId;
        String movieTitle;
        String moviePost;
    }

}

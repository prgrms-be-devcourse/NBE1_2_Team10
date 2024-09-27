package core.application.users.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class DibRequestDTO {

    /*
     * dibId : 찜 아이디
     * userId : 유저 아이디
     * movieId : 영화 아이디
     */

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DibCreateDTO {
        Long dibId;
        UUID userId;
        String movieId;
    }
}

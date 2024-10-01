package core.application.users.service;

import core.application.users.models.dto.DibRespDTO;

import java.util.UUID;

public interface DibService {

    /*
     * 찜하기 기능
     * @return{"찜 완료" 메시지, dibId, movieId 반환}
     */
    DibRespDTO createDib(UUID userId, String movieId);

    /*
     * 찜 삭제하는 기능
     * @return{"찜 삭제 완료" 메시지, dibId, movieId 반환}
     */
    DibRespDTO cancelDib(UUID userId, Long dibId);
}

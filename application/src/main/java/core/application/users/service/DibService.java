package core.application.users.service;

import core.application.users.models.dto.DibRespDTO;

public interface DibService {

    /*
     * 찜하기 기능
     * @return{"찜 완료" 메시지, dibId, movieId 반환}
     */
    DibRespDTO createDib(Long dibId);

    /*
     * 찜 삭제하는 기능
     * @return{"찜 삭제 완료" 메시지, dibId, movieId 반환}
     */
    DibRespDTO cancelDib(Long dibId);
}

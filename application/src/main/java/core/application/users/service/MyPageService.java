package core.application.users.service;

import core.application.users.models.dto.DibCancelRespDTO;
import core.application.users.models.dto.MyPageRespDTO;

public interface MyPageService {
    /*
     * 사용자의 마이페이지를 조회하는 기능
     * @return{@link MyPageRespnseDTO.MyPageDTO}
     */
    MyPageRespDTO getMyPage();

    /*
     * 마이페이지에서 찜 삭제하는 기능
     * @return{"찜 삭제 완료" 메시지 반환}
     */
    DibCancelRespDTO cancelDib(Long dibId);
}

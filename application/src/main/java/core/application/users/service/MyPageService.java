package core.application.users.service;

import core.application.users.models.dto.DibCancelResDTO;
import core.application.users.models.dto.MyPageResDTO;

public interface MyPageService {
    /*
     * 사용자의 마이페이지를 조회하는 기능
     * @return{@link MyPageRespnseDTO.MyPageDTO}
     */
    MyPageResDTO getMyPage();

    /*
     * 마이페이지에서 찜 삭제하는 기능
     * @return{"찜 삭제 완료" 메시지 반환}
     */
    DibCancelResDTO cancelDib(Long dibId);
}

package core.application.users.service;

import core.application.users.models.dto.MyPageResponseDTO;

public interface MyPageService {
    /*
     * 사용자의 마이페이지를 조회하는 기능
     * @return{@link MyPageRespnseDTO.MyPageDTO}
     */
    MyPageResponseDTO.MyPageDTO getMyPage();

    /*
     * 마이페이지에서 찜 삭제하는 기능
     * @return{"찜 삭제 완료" 메시지 반환}
     */
    String cancelDib(Long dibId);
}

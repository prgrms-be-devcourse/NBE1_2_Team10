package core.application.users.models.dto;

import core.application.users.models.entities.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


public class MyPageResponseDTO {

    /*
    마이페이지 조회에 사용되는 DTO
     * userEmail : 유저 이메일
     * alias : 유저 별명
     * phoneNum : 유저 전화번호
     * role : 유저 역할
    */
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyPageDTO {
        String userEmail;
        String alias;
        String phoneNum;
        String userName;
        UserRole role;
        List<DibResponseDTO.DibDTO> dibDTOList;
    }
}
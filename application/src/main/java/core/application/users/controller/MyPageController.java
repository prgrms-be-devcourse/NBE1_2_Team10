package core.application.users.controller;

import core.application.api.response.ApiResponse;
import core.application.users.models.dto.MyPageRespDTO;
import core.application.users.service.MyPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "MyPage", description = "마이 페이지 API")
public class MyPageController {

    private final MyPageService myPageService;

    @Operation(summary = "유저 마이페이지")
    @GetMapping("/mypage")
    public ApiResponse<MyPageRespDTO> getMyPage() {
        UUID userId = UUID.fromString("991c95d6-808a-11ef-8da5-467268b55380");
        MyPageRespDTO myPage = myPageService.getMyPage(userId);
        return ApiResponse.onSuccess(myPage);
    }
}

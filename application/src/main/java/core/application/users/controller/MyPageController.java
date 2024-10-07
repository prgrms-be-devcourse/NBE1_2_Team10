package core.application.users.controller;

import core.application.users.models.dto.MyPageRespDTO;
import core.application.users.service.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/mypage")
    public MyPageRespDTO getMyPage() {
        UUID userId = UUID.fromString("991c95d6-808a-11ef-8da5-467268b55380");
        return myPageService.getMyPage(userId);
    }
}

package core.application.users.controller;

import core.application.users.models.dto.DibRespDTO;
import core.application.users.service.DibService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/movies")
public class DibController {

    private final DibService dibService;

    @PutMapping("/{movieId}/dib")
    public DibRespDTO dibPRocess(@PathVariable String movieId) {
        UUID userId = UUID.fromString("991c95d6-808a-11ef-8da5-467268b55380");
        return dibService.dibProcess(userId, movieId);
    }
}

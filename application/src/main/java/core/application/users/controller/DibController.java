package core.application.users.controller;

import core.application.api.response.ApiResponse;
import core.application.users.models.dto.DibRespDTO;
import core.application.users.service.DibService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/movies")
@Tag(name = "Dib", description = "영화 찜 API")
public class DibController {

    private final DibService dibService;

    @Operation(summary = "영화 찜 등록/취소")
    @PutMapping("/{movieId}/dib")
    public ApiResponse<DibRespDTO> dibPRocess(@PathVariable String movieId) {
        UUID userId = UUID.fromString("991c95d6-808a-11ef-8da5-467268b55380");
        DibRespDTO dibRespDTO = dibService.dibProcess(userId, movieId);
        return ApiResponse.onSuccess(dibRespDTO);
    }
}

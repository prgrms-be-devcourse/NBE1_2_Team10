package core.application.models.entities;

import lombok.Data;

import java.util.UUID;

@Data
public class DibEntity {
    private Long   DibId;
    private UUID   userId;
    private String movieId;     // 영화 API 에 따라 달라질 수 있음.
}

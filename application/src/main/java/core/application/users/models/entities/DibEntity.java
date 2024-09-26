package core.application.users.models.entities;

import lombok.Data;

import java.util.UUID;

@Data
public class DibEntity {
    private Long   DibId;
    private UUID   userId;
    private String movieId;
}

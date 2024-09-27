package core.application.users.models.entities;

import lombok.*;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class DibEntity {
    private Long   dibId;
    private UUID   userId;
    private String movieId;
}

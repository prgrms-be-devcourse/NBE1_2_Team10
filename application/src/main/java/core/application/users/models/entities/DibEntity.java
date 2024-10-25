package core.application.users.models.entities;

import java.util.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class DibEntity {
    private Long   dibId;
    private UUID   userId;
    private String movieId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DibEntity dibEntity = (DibEntity) o;
        return Objects.equals(dibId, dibEntity.dibId) && Objects.equals(userId,
                dibEntity.userId) && Objects.equals(movieId, dibEntity.movieId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(dibId);
        result = 31 * result + Objects.hashCode(userId);
        result = 31 * result + Objects.hashCode(movieId);
        return result;
    }
}

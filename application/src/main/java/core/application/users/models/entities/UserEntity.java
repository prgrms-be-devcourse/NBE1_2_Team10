package core.application.users.models.entities;

import core.application.config.jpa.UUIDConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Entity
@Table(name = "user_table")
public class UserEntity {
    @Id
    @Convert(converter = UUIDConverter.class)
    @Column(columnDefinition = "binary(16)")
    private UUID     userId;
    private String   userEmail;
    private String   userPw;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    private String   alias;
    private String   phoneNum;
    private String   userName;
}

package core.application.users.models.entities;

import java.util.UUID;

import core.application.config.jpa.UUIDConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Entity(name = "user_table")
public class UserEntity {
    @Id
    @Convert(converter = UUIDConverter.class)
    @GeneratedValue(strategy = GenerationType.UUID)
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

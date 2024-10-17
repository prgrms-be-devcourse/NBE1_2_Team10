package core.application.users.models.entities;

import core.application.config.jpa.UUIDConverter;
import core.application.users.models.dto.UserRequestDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
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

    @PrePersist
    public void prePersist() {
        this.userId = UUID.randomUUID();
    }

    public void editInfo(UserRequestDTO userRequestDTO) {
        this.userPw = (userRequestDTO.getUserPw() != null ? userRequestDTO.getUserPw() : this.userPw); // userPw 업데이트
        this.role = (userRequestDTO.getRole() != null ? userRequestDTO.getRole() : this.getRole()); // role 업데이트
        this.alias = (userRequestDTO.getAlias() != null ? userRequestDTO.getAlias() : this.getAlias()); // alias 업데이트
        this.phoneNum = (userRequestDTO.getPhoneNum() != null ? userRequestDTO.getPhoneNum() : this.getPhoneNum()); // phoneNum 업데이트
        this.userName = (userRequestDTO.getUserName() != null ? userRequestDTO.getUserName() : this.getUserName()); // userName 업데이트

    }
}

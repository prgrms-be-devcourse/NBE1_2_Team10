package core.application.users.service;

import core.application.users.models.dto.LoginRequestDTO;
import core.application.users.models.dto.MessageResponseDTO;
import core.application.users.models.dto.UserDTO;
import core.application.users.models.entities.UserEntity;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    public MessageResponseDTO signup (UserDTO userDTO);

    public MessageResponseDTO login (LoginRequestDTO loginRequestDTO);

    public MessageResponseDTO logout ();

    public MessageResponseDTO updateUserInfo (UserDTO userDTO);

    public MessageResponseDTO deleteUser ();

    public Optional<UserEntity> getUserByUserId(UUID userId);
}

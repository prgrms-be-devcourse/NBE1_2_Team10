package core.application.users.service;

import core.application.users.models.dto.LoginRequestDTO;
import core.application.users.models.dto.MessageResponseDTO;
import core.application.users.models.dto.UserDTO;

import java.util.UUID;

public interface UserService {

    public MessageResponseDTO signup (UserDTO userDTO);

    // TokenDTO 추후 반환하는 식으로 수정하게 될 것 같습니다.
    public MessageResponseDTO login (LoginRequestDTO loginRequestDTO);

    public MessageResponseDTO logout (UserDTO userDTO);

    public MessageResponseDTO updateUserInfo (UserDTO userDTO);

    public MessageResponseDTO deleteUser (UUID userId);
}

package core.application.users.service;

import core.application.users.models.dto.MessageResponseDTO;
import core.application.users.models.dto.UserDTO;

public interface UserService {
    public MessageResponseDTO signup (UserDTO user);

    // TokenDTO 추후 반환하는 식으로 수정하게 될 것 같습니다.
    public MessageResponseDTO login (String username, String password);

    public MessageResponseDTO logout (UserDTO user);

    public MessageResponseDTO updateUserInfo (UserDTO user);

    public MessageResponseDTO deleteUser (UserDTO user);
}

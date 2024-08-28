package kr.co.ifit.service;

import kr.co.ifit.domain.dto.UserDTO;
import kr.co.ifit.domain.entity.User;
import kr.co.ifit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(UserDTO userDTO) {
        if(userRepository.existsByLoginId(userDTO.getLoginId())) {
            throw new IllegalArgumentException("이미 존재하는 ID입니다.");
        }

        User user = new User(
                userDTO.getLoginId(),
                userDTO.getPassword(),
                userDTO.getUserName(),
                userDTO.getPhoneNumber(),
                userDTO.getEmail()
        );

        userRepository.save(user);
    }

    public boolean checkIdAvailability(String loginId) {
        return !userRepository.existsByLoginId(loginId);
    }
}

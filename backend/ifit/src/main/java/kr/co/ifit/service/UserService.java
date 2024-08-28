package kr.co.ifit.service;

import kr.co.ifit.domain.dto.UserDTO;
import kr.co.ifit.domain.entity.User;
import kr.co.ifit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(UserDTO userDTO) {
        if (userRepository.existsByLoginId(userDTO.getLoginId())) {
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


    // 로그인 메서드 추가
    public boolean loginUser(String loginId, String password) {
        // 사용자 ID로 사용자 정보 조회
        Optional<User> user = userRepository.findByLoginId(loginId);

        // 사용자 정보가 존재하고, 비밀번호가 일치하는지 확인
        return user.isPresent() && user.get().getPassword().equals(password);
    }
}
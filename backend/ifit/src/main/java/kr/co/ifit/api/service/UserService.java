package kr.co.ifit.api.service;

import kr.co.ifit.api.request.LoginDTO;
import kr.co.ifit.api.request.UserDTO;
import kr.co.ifit.db.entity.User;
import kr.co.ifit.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(UserDTO userDTO) {
        if (userRepository.existsByLoginId(userDTO.getLoginId())) {
            throw new IllegalArgumentException("이미 존재하는 ID입니다.");
        }

        User user = new User(
                userDTO.getLoginId(),
                passwordEncoder.encode(userDTO.getPassword()), // 비밀번호 암호화
                userDTO.getUserName(),
                userDTO.getPhoneNumber(),
                userDTO.getEmail()
        );

        userRepository.save(user);
    }

    public boolean checkIdAvailability(String loginId) {
        return !userRepository.existsByLoginId(loginId);
    }

    public boolean loginUser(String loginId, String password) {
        Optional<User> userOpt = userRepository.findByLoginId(loginId);
        return userOpt.isPresent() && userOpt.get().getPassword().equals(password);
    }
}
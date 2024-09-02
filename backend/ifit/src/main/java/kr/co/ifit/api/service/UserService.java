package kr.co.ifit.api.service;

import kr.co.ifit.api.request.UserDTO;
import kr.co.ifit.db.entity.User;
import kr.co.ifit.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(UserDTO userDTO) {
        if (userRepository.existsByLoginId(userDTO.getLoginId())) {
            throw new IllegalArgumentException("이미 존재하는 ID입니다.");
        }

        User user = new User();
        user.setLoginId(userDTO.getLoginId());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setUserName(userDTO.getUserName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setEmail(userDTO.getEmail());

        userRepository.save(user);
    }

    public boolean checkIdAvailability(String loginId) {
        return !userRepository.existsByLoginId(loginId);
    }

    public boolean loginUser(String loginId, String password, HttpSession session) {
        Optional<User> userOpt = userRepository.findByLoginId(loginId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                session.setAttribute("user", user); // 세션에 사용자 정보 저장
                return true;
            }
        }
        return false;
    }

    public void logoutUser(HttpSession session) {
        session.invalidate(); // 세션 무효화
    }
}
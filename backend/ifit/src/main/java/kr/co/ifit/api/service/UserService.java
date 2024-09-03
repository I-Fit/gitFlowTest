package kr.co.ifit.api.service;

import kr.co.ifit.api.request.UserDTO;
import kr.co.ifit.db.entity.User;
import kr.co.ifit.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 회원가입
    public void registerUser(UserDTO userDTO) {
        if (userRepository.existsByLoginId(userDTO.getLoginId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        User user = new User(
                userDTO.getLoginId(),
                passwordEncoder.encode(userDTO.getPassword()),
                userDTO.getUserName(),
                userDTO.getPhoneNumber(),
                userDTO.getEmail()
        );
        userRepository.save(user);
    }

    // 로그인
    public boolean loginUser(String loginId, String password, HttpSession session) {
        Optional<User> optionalUser = userRepository.findByLoginId(loginId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                session.setAttribute("user", user);
                return true;
            }
        }
        return false;
    }

    // 로그아웃
    public void logoutUser(HttpSession session) {
        session.invalidate();
    }

    // 아이디 중복 확인
    public boolean checkIdAvailability(String id) {
        return !userRepository.existsByLoginId(id);
    }
}
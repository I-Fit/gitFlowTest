package kr.co.ifit.controller;

import kr.co.ifit.domain.dto.UserDTO;
import kr.co.ifit.service.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SignupController {

    private final UserService userService;

    @Autowired
    public SignupController(UserService userService) {
        this.userService = userService;
    }
    // 회원가입 하는 부분
    @PostMapping("/user-signup")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
        try {
            userService.registerUser(userDTO);
            return ResponseEntity.ok("회원가입 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
    // 아이디 중복 확인
    @GetMapping("/check-id")
    public ResponseEntity<IdCheckResponse> checkId(@RequestParam("id") String id) {
        boolean available = userService.checkIdAvailability(id);
        return ResponseEntity.ok(new IdCheckResponse(available));
    }

    @Setter
    @Getter
    public static class IdCheckResponse {
        private boolean available;

        public IdCheckResponse(boolean available) {
            this.available = available;
        }

    }

}

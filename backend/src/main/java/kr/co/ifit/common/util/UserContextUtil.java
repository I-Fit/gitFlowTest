package kr.co.ifit.common.util;

import kr.co.ifit.api.service.UserService;
import kr.co.ifit.db.entity.User;
import kr.co.ifit.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserContextUtil {

    private final UserRepository userRepository;
    private final UserService userService;

    //  SecurityContext에서 인증된 로그인 ID를 가져온다
    public String getAuthenticatedLoginId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            return null;
        }
        return userDetails.getUsername();
    }

    //  로그인 ID로 사용자 식별 ID를 조회
    public Long getUserIdByLoginId(String loginId) {
        User user = userService.findByLoginId(loginId);
        return user != null ? user.getUserId() : null;
    }

    //  사용자 ID를 가져오는 메서드
    public Long getAuthenticatedUserId() {
        String loginId = getAuthenticatedLoginId();
        if (loginId == null) {
            return null;
        }
        return getUserIdByLoginId(loginId);
    }
}

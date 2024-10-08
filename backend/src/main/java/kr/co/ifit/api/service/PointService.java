package kr.co.ifit.api.service;

import kr.co.ifit.common.util.UserContextUtil;
import kr.co.ifit.db.entity.User;
import kr.co.ifit.db.repository.PointRepository;
import kr.co.ifit.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PointService {

    private final PointRepository pointRepository;
    private final UserRepository userRepository;
    //private final UserContextUtil userContextUtil;

    @Autowired
    public PointService(PointRepository pointRepository, UserRepository userRepository, UserContextUtil userContextUtil) {
        this.pointRepository = pointRepository;
        this.userRepository = userRepository;
        //this.userContextUtil = userContextUtil;
    }

    // 특정 사용자 ID로 포인트 조회
    public Integer getPointsByUserId(Long userId) {
        // 사용자 존재 여부 확인
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            // 사용자의 포인트 조회
            return pointRepository.findTotalPointsByUserId(userId);
        } else {
            throw new RuntimeException("사용자를 찾을 수 없습니다."); // 예외 처리
        }
    }


    // 로그인된 사용자의 포인트 조회 (userId 직접 전달하지 않음)
    /*public Integer getUserPoints() {
        // UserContextUtil을 통해 현재 로그인된 사용자 ID 가져오기
        Long userId = userContextUtil.getAuthenticatedUserId();

        // 사용자 존재 여부 확인
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            // 사용자의 포인트 조회
            return pointRepository.findTotalPointsByUserId(userId);
        } else {
            throw new RuntimeException("사용자를 찾을 수 없습니다."); // 예외 처리
        }
    }*/
}

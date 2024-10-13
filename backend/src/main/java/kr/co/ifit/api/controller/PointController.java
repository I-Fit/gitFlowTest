package kr.co.ifit.api.controller;

import kr.co.ifit.api.service.PointService;
import kr.co.ifit.common.util.UserContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/point")
public class PointController {

    private final PointService pointService;
    //private final UserContextUtil userContextUtil;

    @Autowired
    public PointController(PointService pointService, UserContextUtil userContextUtil) {
        this.pointService = pointService;
        //this.userContextUtil = userContextUtil;
    }

    @GetMapping("/{userId}")
    public Integer getUserPoints(@PathVariable Long userId) {
        return pointService.getPointsByUserId(userId);
    }



    /*@GetMapping("/amount")
    public Integer getUserPoints() {
        // UserContextUtil을 사용하여 인증된 사용자 ID 가져오기
        Long userId = userContextUtil.getAuthenticatedUserId();
        if (userId == null) {
            throw new RuntimeException("인증된 사용자를 찾을 수 없습니다.");
        }
        return pointService.getUserPoints();
    }*/
}

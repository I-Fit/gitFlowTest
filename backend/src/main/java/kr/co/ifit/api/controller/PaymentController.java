package kr.co.ifit.api.controller;

import kr.co.ifit.api.response.PaymentUserInfoDtoRes;
import kr.co.ifit.api.service.PaymentService;
import kr.co.ifit.common.util.UserContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PaymentController {

    private final PaymentService paymentService;
    private final UserContextUtil userContextUtil;

    @Autowired
    public PaymentController(PaymentService paymentService, UserContextUtil userContextUtil) {
        this.paymentService = paymentService;
        this.userContextUtil = userContextUtil;
    }

    @GetMapping("/payment/user-info")
    public ResponseEntity<PaymentUserInfoDtoRes> getUserInfo(@RequestParam int id) {
        Long userId = userContextUtil.getAuthenticatedUserId();
        if (userId == null) {
            //  인증되지 않은 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        PaymentUserInfoDtoRes userInfo = paymentService.getUserPaymentInfo(userId, id);
        return ResponseEntity.ok(userInfo);
    }
}

    /*@PostMapping("/process")
    public ResponseEntity<PaymentDtoRes> processPayment(@RequestBody @Valid PaymentDtoReq paymentDtoReq) {
        PaymentDtoRes result = paymentService.processPayment(paymentDtoReq);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
}*/
package kr.co.ifit.api.service;

import kr.co.ifit.api.response.ProfileDtoRes;
import kr.co.ifit.db.entity.Membership;
import kr.co.ifit.db.entity.Payment;
import kr.co.ifit.db.entity.Point;
import kr.co.ifit.db.entity.User;
import kr.co.ifit.db.repository.PaymentRepository;
import kr.co.ifit.db.repository.PointRepository;
import kr.co.ifit.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final PointRepository pointRepository;
    private final PaymentRepository paymentRepository;

    @Value("${app.upload.dir:/tmp/uploads}")
    private String uploadDir;

    public String saveProfileImage(String loginId, String base64Image) throws IOException {
        User user = userRepository.findByLoginId(loginId);
        if (user == null) {
                throw  new UsernameNotFoundException("유저를 찾을 수 없습니다.");
        }

        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        String fileName = loginId + "_profile.png";

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, imageBytes);

        String imageUrl = "/uploads/" + fileName;
        user.setProfileUrl(imageUrl);
        userRepository.save(user);

        return imageUrl;
    }

    public ProfileDtoRes getUserProfile(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
                throw new UsernameNotFoundException("사용자가 없습니다.");
        }

        Point point = pointRepository.findByUser(user);
        int points = (point != null) ? point.getAmount() : 0;

        Payment payment = paymentRepository.findByUser(user);
        Membership membership = (payment != null) ? payment.getMembership() : null;
        String membershipGrade = (membership != null) ? membership.getGrade() : null;

        return new ProfileDtoRes(
                user.getLoginId(),
                user.getUsername(),
                user.getEmail(),
                user.getProfileUrl(),
                points,
                membershipGrade
        );
    }
}
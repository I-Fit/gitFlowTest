package kr.co.ifit.api.service;

import kr.co.ifit.api.request.LikedGroupRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreatedGroupService {

    private final LikedGroupService likedGroupService;

    @Autowired
    public CreatedGroupService(LikedGroupService likedGroupService) {
        this.likedGroupService = likedGroupService;
    }

    public void toggleLike(Integer userId, Integer communityId, boolean isHeartFilled) {
        likedGroupService.toggleLike(new LikedGroupRequestDTO(userId, communityId, isHeartFilled));
    }
}

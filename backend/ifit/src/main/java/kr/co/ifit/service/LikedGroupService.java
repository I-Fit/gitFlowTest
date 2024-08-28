package kr.co.ifit.service;

import kr.co.ifit.domain.entity.Group;
import kr.co.ifit.domain.entity.LikedGroup;
import kr.co.ifit.domain.entity.User;
import kr.co.ifit.respository.GroupRepository;
import kr.co.ifit.respository.LikedGroupRepository;
import kr.co.ifit.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LikedGroupService {

    private final LikedGroupRepository likedGroupRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @Autowired
    public LikedGroupService(LikedGroupRepository likedGroupRepository, GroupRepository groupRepository, UserRepository userRepository) {
        this.likedGroupRepository = likedGroupRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    public void likedGroup(Integer userId, Integer communityId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Group group = groupRepository.findById(communityId).orElseThrow(() -> new RuntimeException("Group not found"));

        LikedGroup likedGroup = new LikedGroup();
        likedGroup.setUser(user);
        likedGroup.setGroup(group);
        likedGroup.setCreatedAt(LocalDateTime.now());
        likedGroup.setUpdatedAt(LocalDateTime.now());

        likedGroupRepository.save(likedGroup);
    }
}

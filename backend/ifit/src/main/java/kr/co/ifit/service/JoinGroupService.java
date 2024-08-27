package kr.co.ifit.service;

import kr.co.ifit.domain.entity.JoinGroup;
import kr.co.ifit.domain.entity.Group;
import kr.co.ifit.domain.entity.User;
import kr.co.ifit.respository.GroupRepository;
import kr.co.ifit.respository.JoinGroupRepository;
import kr.co.ifit.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

// 사용자가 모임에 참가하는 기능을 구현
@Service
public class JoinGroupService {

    private final JoinGroupRepository joinGroupRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    // 의존성 주입(생성자 주입)
    @Autowired
    public JoinGroupService(JoinGroupRepository joinGroupRepository, UserRepository userRepository, GroupRepository groupRepository) {
        this.joinGroupRepository = joinGroupRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    // userId, communityId를 받아서 해당 User, group에 조회한 후 새로운 Join 엔티티를 생성하고 저장
    public void joinGroup(Integer userId, Integer communityId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Group group = groupRepository.findById(communityId).orElseThrow(() -> new RuntimeException("Group not found"));

        JoinGroup joinGroup = new JoinGroup();
        joinGroup.setUser(user);
        joinGroup.setGroup(group);
        joinGroup.setJoinedAt(LocalDateTime.now());  // 현재 시간을 설정

        // 호출해서 데이터베이스에 저장
        joinGroupRepository.save(joinGroup);
    }
}

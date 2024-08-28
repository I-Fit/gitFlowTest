package kr.co.ifit.service;

import kr.co.ifit.domain.dto.GroupDTO;
import kr.co.ifit.domain.entity.Group;
import kr.co.ifit.domain.entity.User;
import kr.co.ifit.respository.GroupRepository;
import kr.co.ifit.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddGroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @Autowired
    public AddGroupService(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    //  메서드가 데이터베이스 상태를 변경할 수 있다
    @Transactional
    public Group createAddGroup(GroupDTO groupDTO) {
        //  userId로 User 객체를 조회 (사용자가 존재하는지)
        User user = userRepository.findById(groupDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        //  GroupDTO 객체에서 받은 데이터를 기반으로 새로운 Group 객체를 생성
        Group group = new Group(
                groupDTO.getTitle(),
                groupDTO.getTopboxContext(),
                groupDTO.getSport(),
                groupDTO.getLocation(),
                groupDTO.getPerson(),
                groupDTO.getDate(),
                user
        );
        // 생성한 Group 객체를 groupRepository를 통해 데이터베이스에 저장, 저장된 group 객체를 반환
        return groupRepository.save(group);
    }

    // 메서드가 읽기 전용 트랜잭션으로 실행됨을 명시
    @Transactional(readOnly = true)
    public List<Group> getGroupsByUserId(Integer userId) {
        // groupRepository를 사용하여 userId로 모임 리스트를 조회하고 반환
        return groupRepository.findByUserId(userId);
    }
}

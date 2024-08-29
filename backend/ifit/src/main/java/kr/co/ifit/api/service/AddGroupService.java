package kr.co.ifit.api.service;

import kr.co.ifit.api.request.AddGroupRequestDTO;
import kr.co.ifit.db.entity.Group;
import kr.co.ifit.db.entity.User;
import kr.co.ifit.db.repository.GroupRepository;
import kr.co.ifit.db.repository.UserRepository;
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
    public Group createAddGroup(AddGroupRequestDTO addGroupRequestDTO) {
        //  userId로 User 객체를 조회 (사용자가 존재하는지)
        User user = userRepository.findById(addGroupRequestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        //  GroupDTO 객체에서 받은 데이터를 기반으로 새로운 Group 객체를 생성
        Group group = new Group(
                addGroupRequestDTO.getTitle(),
                addGroupRequestDTO.getTopboxContext(),
                addGroupRequestDTO.getSport(),
                addGroupRequestDTO.getLocation(),
                addGroupRequestDTO.getPerson(),
                addGroupRequestDTO.getDate(),
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

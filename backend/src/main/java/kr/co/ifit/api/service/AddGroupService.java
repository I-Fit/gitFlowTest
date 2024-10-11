package kr.co.ifit.api.service;

import kr.co.ifit.api.request.AddGroupDtoReq;
import kr.co.ifit.db.entity.Group;
import kr.co.ifit.db.entity.User;
import kr.co.ifit.db.repository.GroupRepository;
import kr.co.ifit.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AddGroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    //  메서드가 데이터베이스 상태를 변경할 수 있다
    @Transactional
    public Group createAddGroup(AddGroupDtoReq addGroupDtoReq) {
        User user = userRepository.findById(addGroupDtoReq.getUserId()).orElseThrow(() ->
                new RuntimeException("사용자를 찾을 수 없습니다.")
        );

        LocalDateTime localDateTime = addGroupDtoReq.getDate().withSecond(0);

        //  GroupDTO 객체에서 받은 데이터를 기반으로 새로운 Group 객체를 생성
        Group group = new Group(
                addGroupDtoReq.getTitle(),
                addGroupDtoReq.getTopboxContent(),
                addGroupDtoReq.getSport(),
                addGroupDtoReq.getFullLocation(),
                addGroupDtoReq.getLocation(),
                addGroupDtoReq.getPerson(),
                localDateTime,
                user,
                addGroupDtoReq.isSaved()
        );
        return groupRepository.save(group);
    }

    // 메서드가 읽기 전용 트랜잭션으로 실행됨을 명시
    @Transactional(readOnly = true)
    public List<Group> getGroupsByUserId(User user) {
        // groupRepository를 사용하여 userId로 모임 리스트를 조회하고 반환
        return groupRepository.findByUser(user);
    }
}

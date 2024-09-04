package kr.co.ifit.api.service;

import kr.co.ifit.api.request.LikedGroupRequestDTO;
import kr.co.ifit.api.response.GroupResponseDTO;
import kr.co.ifit.db.entity.JoinedGroup;
import kr.co.ifit.db.entity.Group;
import kr.co.ifit.db.entity.User;
import kr.co.ifit.db.repository.GroupRepository;
import kr.co.ifit.db.repository.JoinedGroupRepository;
import kr.co.ifit.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// 사용자가 모임에 참가하는 기능을 구현
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class JoinedGroupService {

    private final JoinedGroupRepository joinedGroupRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final LikedGroupService likedGroupService;

    @Transactional(readOnly = true)
    //  특정 사용자가 참여한 모든 모임을 반환
    public List<GroupResponseDTO> getGroupsByUserId(Long userId) {
        //   userId로 사용자 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));
        //   사용자가 참여한 joinedGroup 엔티티를 조회하고, 엔티티에서 Group 객체를 추출하여 리스트로 반환
        return joinedGroupRepository.findByUser(user).stream().map(joinedGroup -> {
            Group group = joinedGroup.getGroup();
            return new GroupResponseDTO(
                    group.getCommunityId(),
                    group.getTitle(),
                    group.getTopboxContent(),
                    group.getSport(),
                    group.getLocation(),
                    group.getPerson(),
                    group.getPeopleParticipation(),
                    group.getDate()
            );
        }).toList();
    }

    // userId, communityId를 받아서 해당 User, group에 조회한 후 새로운 Join 엔티티를 생성하고 저장
    public void joinGroup(Long userId, Long communityId) {
        // userId로 사용자 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));
        // communityId로 모임 조회
        Group group = groupRepository.findById(communityId).orElseThrow(() -> new RuntimeException("모임을 찾을 수 없습니다."));

        if (joinedGroupRepository.existsByUserAndGroup(user, group)) {
            throw new RuntimeException("이미 참여 중인 모임입니다.");
        }

        JoinedGroup joinedGroup = new JoinedGroup();
        joinedGroup.setUser(user);
        joinedGroup.setGroup(group);
        joinedGroup.setJoinedAt(LocalDateTime.now());  // 현재 시간을 설정

        // 호출해서 데이터베이스에 저장
        joinedGroupRepository.save(joinedGroup);

        //  모임 참여 인원 수 증가 및 인원 수 초과 됐을 때 오류
        if (group.getPeopleParticipation() < group.getPerson()) {
            group.incrementPeopleParticipation();
            groupRepository.save(group);
        } else {
            throw new RuntimeException("모임이 가득 찼습니다.");
        }
    }

    //  참석한 모임 중 삭제를 했을 때
    public boolean deleteGroup(Long userId, Long communityId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        Group group = groupRepository.findById(communityId).orElseThrow(() -> new RuntimeException("모임을 찾을 수 없습니다."));

        JoinedGroup joinedGroup = joinedGroupRepository.findByUserAndGroup(user, group).orElseThrow(() -> new RuntimeException("참여 모임 내역에서 찾을 수 없습니다"));
        joinedGroupRepository.delete(joinedGroup);

        //  모임 참여 취소 후 모임 참여 인원 수 감소
        if (group.getPeopleParticipation() > 0) {
            group.decrementPeopleParticipation();
            groupRepository.save(group);
        }
        return true;
    }

    //  LikedGroupService에 있는 좋아요 추가, 삭제 기능 사용
    public void toggleLike(LikedGroupRequestDTO likedGroupRequestDTO) {
        if (likedGroupRequestDTO.isHeartFilled()) {
            likedGroupService.toggleLike(likedGroupRequestDTO);
        } else {
            likedGroupService.removeLike(likedGroupRequestDTO);
        }
    }

}

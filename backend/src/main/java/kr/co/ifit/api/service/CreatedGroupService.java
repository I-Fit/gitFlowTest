package kr.co.ifit.api.service;

import kr.co.ifit.api.request.LikedGroupDtoReq;
import kr.co.ifit.api.response.GroupDtoRes;
import kr.co.ifit.db.entity.Group;
import kr.co.ifit.db.entity.User;
import kr.co.ifit.db.repository.GroupRepository;
import kr.co.ifit.db.repository.JoinedGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CreatedGroupService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final LikedGroupService likedGroupService;
    private final JoinedGroupRepository joinedGroupRepository;

    //  GroupRepository에서 userId로 조회 후 GroupresponseDTO로 변환 후 groups 리스트 응답
    public List<GroupDtoRes> getCreatedGroupsByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("사용자를 찾을 수 없습니다."));
        List<Group> groups = groupRepository.findByUser(user);

        return groups.stream()
                .map(group -> new GroupDtoRes(
                        group.getCommunityId(),
                        group.getTitle(),
                        group.getTopboxContent(),
                        group.getSport(),
                        group.getLocation(),
                        group.getPerson(),
                        group.getPeopleParticipation(),
                        group.getDate()
                )).toList();
    }

    @Transactional
    //  내가 만든 모임에서 모임 삭제를 눌렀을 때 찾아서 없앤다
    public boolean deleteGroup(Long userId, Long communityId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<Group> groups = groupRepository.findByUser(user);

        Group groupDelete = groups.stream().filter(group -> group.getCommunityId().equals(communityId)).findFirst().orElse(null);
        if (groupDelete != null) {
            groupRepository.delete(groupDelete);
            return true;
        }
        return false;
    }

    public void toggleLike(LikedGroupDtoReq likedGroupDtoReq) {
        if (likedGroupDtoReq.isHeartFilled()) {
            likedGroupService.toggleLike(likedGroupDtoReq);
        } else {
            likedGroupService.removeLike(likedGroupDtoReq);
        }
    }
}

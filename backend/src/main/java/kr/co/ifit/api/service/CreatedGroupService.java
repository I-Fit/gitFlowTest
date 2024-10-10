package kr.co.ifit.api.service;

import kr.co.ifit.api.request.CreatedGroupDtoReq;
import kr.co.ifit.api.request.FilterDtoReq;
import kr.co.ifit.api.request.LikedGroupDtoReq;
import kr.co.ifit.api.response.GroupDtoRes;
import kr.co.ifit.db.entity.Group;
import kr.co.ifit.db.entity.User;
import kr.co.ifit.db.repository.GroupRepository;
import kr.co.ifit.db.repository.JoinedGroupRepository;
import kr.co.ifit.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CreatedGroupService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final LikedGroupService likedGroupService;
    private final JoinedGroupRepository joinedGroupRepository;

    //  GroupRepository에서 userId로 조회 후 GroupresponseDTO로 변환 후 groups 리스트 응답
    public List<GroupDtoRes> getCreatedGroupsByUserId(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        List<Group> createdGroups = groupRepository.findByUser(user);
        return createdGroups.stream().map(GroupDtoRes::convertToDto).collect(Collectors.toList());
    }

    //  내가 만든 모임에서 정렬 번호에 대한 모임 리스트 반환
    public List<GroupDtoRes> getCreatedGroupsSorted(Long userId, int value) {
        List<Group> createdGroups = groupRepository.findByIdSorted(userId, value);
        return createdGroups.stream().map(GroupDtoRes::convertToDto).collect(Collectors.toList());
    }

    // 내가 만든 모임에서 검색 단어에 해당하는 모임 리스트 반환
    public List<GroupDtoRes> getCreatedGroupsSearch(Long userId, String searchTerm) {
        List<Group> createdGroups = groupRepository.findByUserIdAndAnyFieldContaining(userId, searchTerm);
        return createdGroups.stream().map(GroupDtoRes::convertToDto).collect(Collectors.toList());
    }

    @Transactional
    //  내가 만든 모임에서 모임 삭제를 눌렀을 때 찾아서 없앤다
    public boolean deleteCreatedGroup(Long userId, Long communityId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<Group> groups = groupRepository.findByUser(user);

        Group groupDelete = groups.stream().filter(group -> group.getCommunityId().equals(communityId)).findFirst().orElse(null);
        if (groupDelete != null) {
            groupRepository.delete(groupDelete);
            return true;
        }
        return false;
    }

    public void createdGroupToggleLike(LikedGroupDtoReq likedGroupDtoReq) {
        if (likedGroupDtoReq.isSaved()) {
            likedGroupService.toggleLike(likedGroupDtoReq);
        } else {
            likedGroupService.removeLike(likedGroupDtoReq);
        }
    }
}

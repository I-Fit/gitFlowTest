package kr.co.ifit.api.service;

import kr.co.ifit.api.request.LikedGroupDtoReq;
import kr.co.ifit.api.response.GroupDtoRes;
import kr.co.ifit.db.entity.Group;
import kr.co.ifit.db.entity.LikedGroup;
import kr.co.ifit.db.repository.GroupRepository;
import kr.co.ifit.db.repository.LikedGroupRepository;
import kr.co.ifit.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class LikedGroupService {

    private final LikedGroupRepository likedGroupRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    //  받아온 userId를 조회해서 Group 리스트를 GroupResponseDTO로 변환해서 반환
    @Transactional
    public List<GroupDtoRes> getLikedGroupsByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("사용자 없습니다"));
        List<LikedGroup> likedGroups = likedGroupRepository.findByUser(user);
        return likedGroups.stream()
                .map(likedGroup ->GroupDtoRes.convertToDto(likedGroup.getGroup())).toList();
    }

    //  좋아요 한 모임 정렬
    public List<GroupDtoRes> getLikedGroupsSorted(Long userId, int value) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        List<LikedGroup> likedGroups = likedGroupRepository.findLikedGroupsSorted(user, value);

        return likedGroups.stream().map(likedGroup ->GroupDtoRes.convertToDto(likedGroup.getGroup())).toList();
    }

    // 좋아요 한 모임 검색 단어
    public List<GroupDtoRes> getLikedGroupsSearch(Long userId, String searchTerm) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        List<LikedGroup> likedGroups = likedGroupRepository.findLikedGroupsByUserAndSearchTerm(user, searchTerm);

        return likedGroups.stream().map(likedGroup -> GroupDtoRes.convertToDto(likedGroup.getGroup())).toList();
    }

    //  좋아요 기능 추가 및 삭제
    @Transactional
    public void toggleLike(LikedGroupDtoReq dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("사용자가 없습니다."));
        Group group = groupRepository.findById(dto.getCommunityId()).orElseThrow(() -> new RuntimeException("모임이 없습니다."));

        if (likedGroupRepository.existsByUserAndGroup(user, group)) {
            throw new RuntimeException("이미 찜한 모임입니다.");
        }

        //  좋아요 추가
        LikedGroup likedGroup = new LikedGroup();
        likedGroup.setUser(user);
        likedGroup.setGroup(group);
        likedGroup.setCreatedAt(LocalDateTime.now());
        likedGroup.setUpdatedAt(LocalDateTime.now());

        likedGroupRepository.save(likedGroup);
//        dto.setSaved(true);
        group.setSaved(true);
        groupRepository.save(group);
    }
    @Transactional
    public void removeLike(LikedGroupDtoReq dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("사용자가 없습니다."));
        Group group = groupRepository.findById(dto.getCommunityId()).orElseThrow(() -> new RuntimeException("모임이 없습니다."));

        LikedGroup likedGroup = likedGroupRepository.findByUserAndGroup(user, group);
        if (likedGroup != null) {
            likedGroupRepository.delete(likedGroup);
//            dto.setSaved(false);
            group.setSaved(false);
            groupRepository.save(group);
        } else {
            throw new RuntimeException("찜한 모임이 없습니다.");
        }
    }
}

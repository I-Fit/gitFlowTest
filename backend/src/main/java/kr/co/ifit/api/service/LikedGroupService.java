package kr.co.ifit.api.service;

import kr.co.ifit.api.request.LikedGroupDtoReq;
import kr.co.ifit.api.response.GroupDtoRes;
import kr.co.ifit.db.entity.Group;
import kr.co.ifit.db.entity.LikedGroup;
import kr.co.ifit.db.entity.User;
import kr.co.ifit.db.repository.GroupRepository;
import kr.co.ifit.db.repository.LikedGroupRepository;
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
                .map(likedGroup -> {
                    Group group = likedGroup.getGroup();
                    return new GroupDtoRes(
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

    //  좋아요 기능 추가 및 삭제
    @Transactional
    public void toggleLike(LikedGroupDtoReq dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Group group = groupRepository.findById(dto.getCommunityId()).orElseThrow(() -> new RuntimeException("Group not found"));
        if (likedGroupRepository.existsByUserAndGroup(user, group)) {
            throw new RuntimeException("이미 찜한 모임입니다.");
        }

        if (dto.isHeartFilled()) {
            //  좋아요 추가
            LikedGroup likedGroup = new LikedGroup();
            likedGroup.setUser(user);
            likedGroup.setGroup(group);
            likedGroup.setCreatedAt(LocalDateTime.now());
            likedGroup.setUpdatedAt(LocalDateTime.now());

            likedGroupRepository.save(likedGroup);
        }
    }
    @Transactional
    public void removeLike(LikedGroupDtoReq dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Group group = groupRepository.findById(dto.getCommunityId()).orElseThrow(() -> new RuntimeException("Group not found"));

        LikedGroup likedGroup = likedGroupRepository.findByUserAndGroup(user, group);
        if (likedGroup != null) {
            likedGroupRepository.delete(likedGroup);
        }
    }

}

package kr.co.ifit.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.ifit.api.request.*;
import kr.co.ifit.api.response.AddGroupDtoRes;
import kr.co.ifit.api.response.FilterDtoRes;
import kr.co.ifit.api.response.GroupDtoRes;
import kr.co.ifit.api.service.*;
import kr.co.ifit.common.util.UserContextUtil;
import kr.co.ifit.db.entity.Group;
import kr.co.ifit.db.entity.User;
import kr.co.ifit.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class GroupController {

    private final AddGroupService addGroupService;
    private final HomeGroupService homeGroupService;
    private final JoinedGroupService joinedGroupService;
    private final CreatedGroupService createdGroupService;
    private final LikedGroupService likedGroupService;

    private final UserContextUtil userContextUtil;

    //    생성된 모임 데이터를 받아오고 모임 ID를 생성해서 보낸다
    @PostMapping("/create-group")
    public ResponseEntity<AddGroupDtoRes> createGroup(@RequestBody AddGroupDtoReq addGroupDtoReq) {
        try {
            // 로그인 Id로 사용자 식별 Id 조회
            Long userId = userContextUtil.getAuthenticatedUserId();
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new AddGroupDtoRes(null, "사용자를 찾을 수 없습니다."));
            }
            addGroupDtoReq.setUserId(userId);
            Group group = addGroupService.createAddGroup(addGroupDtoReq);

            AddGroupDtoRes responseDTO = new AddGroupDtoRes(
                    group.getCommunityId(),
                    "모임이 등록되었습니다."
            );
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            AddGroupDtoRes responseDTO = new AddGroupDtoRes(
                    null,
                    "모임 등록에 실패했습니다: " + e.getMessage()
            );
            return ResponseEntity.status(500).body(responseDTO);
        }
    }

    // Home 페이지에 보여지는 그룹 부분
    // 캐러셀
    @GetMapping("/carousel-groups")
    public ResponseEntity<List<GroupDtoRes>> getGroupsByImageId(@RequestParam int imageId) {
        List<GroupDtoRes> groups = homeGroupService.findGroupsByImageId(imageId);
        return ResponseEntity.ok(groups);
    }


    // 홈 페이지에 있는 필터 부분 - 운동 종목
    @GetMapping("/filter/exercises")
    public ResponseEntity<List<FilterDtoRes>> getAllExercises() {
        List<FilterDtoRes> exercises = homeGroupService.getAllExercises();
        return ResponseEntity.ok(exercises);
    }

    //  홈페이지에서 운동 필터 부분의 선택한 부분의 운동 포함 모임 리스트
    @GetMapping("/filter/exercises/sport")
    public ResponseEntity<List<GroupDtoRes>> getAllExercisesSport(@RequestParam("sport") String sport) {
        logger.info("연결 성공=========================================================================");

        List<GroupDtoRes> groups = homeGroupService.getGroupsBySport(sport);
        return ResponseEntity.ok(groups);
    }

    // 홈페이지에서 지역 필터 부분
    @GetMapping("/filter/location")
    public ResponseEntity<List<GroupDtoRes>> getAllLocation(@RequestParam("location") String location) {
        logger.info("=================================================================={}", location);
        List<GroupDtoRes> groups = homeGroupService.getAllLocations(location);
        return ResponseEntity.ok(groups);
    }

    //  홈페이지에 있는 필터 부분 - 날짜
    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);
    @GetMapping("/filter/date")
    public ResponseEntity<List<GroupDtoRes>> getAllDate(String date) {
        //  문자열을 LocalDate로 변환

        LocalDate dateTime = LocalDate.parse(date.substring(0,10));

        logger.info("연결 성공========================================================================= {}", date);
        List<GroupDtoRes> groups = homeGroupService.getGroupsByDate(dateTime);
        return ResponseEntity.ok(groups);
    }

    //  홈페이지에 있는 필터 부분 - 시간
    @GetMapping("/filter/time")
    public ResponseEntity<List<GroupDtoRes>> getAllTime(@RequestParam String time) {
        logger.info("연========================================================================= {}", time);
        LocalTime parsedTime = LocalTime.parse(time);
        List<GroupDtoRes> groups = homeGroupService.getGroupsByTime(parsedTime);
        return ResponseEntity.ok(groups);
    }
//
//    //  홈페이지에 있는 필터 부분 - 장소
//    @PostMapping("/filter/location")

    //  전달한 검색어로 모임을 검색하여 결과를 반환    - 지역으로 해야된다고 합니다
    @GetMapping("/search")
    public ResponseEntity<List<GroupDtoRes>> searchGroups(@RequestParam String searchTerm) {
        List<GroupDtoRes> groups = homeGroupService.searchGroups(searchTerm);
        return ResponseEntity.ok(groups);
    }

    //  정렬순
    @GetMapping("/sort")
    public ResponseEntity<List<GroupDtoRes>> getSortedGroups(@RequestParam int value) {
        List<GroupDtoRes> groups = homeGroupService.getSortedGroups(value);
        return ResponseEntity.ok(groups);
    }

    //    Home 페이지에 모든 모임 데이터를 보낸다
    @GetMapping("/group-list")
    public ResponseEntity<List<GroupDtoRes>> getGroupDetails() {
        List<GroupDtoRes> groups = homeGroupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }

    //  Home 페이지에서 모임 참석을 누르면 communityId를 서버에 보낸다 (로그인이 안된 상태의 사용자가 참석을 누르면 로그인 하라고 메시지 응답)
    @PostMapping("/join-group")
    public ResponseEntity<String> joinGroup(@RequestBody JoinedGroupDtoReq joinedGroupDtoReq) {
        try {
            Long userId = userContextUtil.getAuthenticatedUserId();
            if (userId == null) {
                //  인증되지 않은 경우
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            joinedGroupService.joinGroup(userId, joinedGroupDtoReq.getCommunityId());
            return ResponseEntity.ok("모임 참여 완료");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/home/like-group", method = {RequestMethod.POST, RequestMethod.DELETE})
    public ResponseEntity<String> toggleLike(@RequestBody LikedGroupDtoReq likedGroupDtoReq) {
        try {
            logger.info("값 ========================================================================= {}", likedGroupDtoReq.getCommunityId());
            Long userId = userContextUtil.getAuthenticatedUserId();
            if (userId == null) {
                // 인증되지 않은 경우
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            likedGroupDtoReq.setUserId(userId);
            logger.info("isSaved 값 ========================================================================= {}", likedGroupDtoReq.isSaved());
            homeGroupService.homeGroupToggleLike(likedGroupDtoReq);

            return ResponseEntity.ok(likedGroupDtoReq.isSaved() ? "추가" : "취소");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // -------------------------------------------------------------------------------------------------------------------------------
    // createdGroup - 내가 만든 모임 내역
    // 사용자가 만든 모임 내역 부분
    @GetMapping("/created")
    //  userId로 만든 모임을 Group 엔티티에서 조회 후 응답
    public ResponseEntity<List<GroupDtoRes>> getCreatedGroups() {
        Long userId = userContextUtil.getAuthenticatedUserId();
        if (userId == null) {
            //  인증되지 않은 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        logger.info("성공========================================================================= {}", userId);
        List<GroupDtoRes> groups = createdGroupService.getCreatedGroupsByUserId(userId);
        return ResponseEntity.ok(groups);
    }

    //  내가 만든 모임에서 정렬
    @GetMapping("/created/sort")
    public ResponseEntity<List<GroupDtoRes>> getCreatedGroupsSort(@RequestParam int value) {
        Long userId = userContextUtil.getAuthenticatedUserId();
        if (userId == null) {
            //  인증되지 않은 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        //  사용자가 만든 모임 리스트를 정렬하여 반환
        List<GroupDtoRes> groups = createdGroupService.getCreatedGroupsSorted(userId, value);
        return ResponseEntity.ok(groups);
    }

    // 내가 만든 모임에서 검색 기능
    @GetMapping("/created/search")
    public ResponseEntity<List<GroupDtoRes>> searchCreatedGroups(@RequestParam String searchTerm) {
        Long userId = userContextUtil.getAuthenticatedUserId();
        if (userId == null) {
            //  인증되지 않은 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<GroupDtoRes> groups = createdGroupService.getCreatedGroupsSearch(userId, searchTerm);
        return ResponseEntity.ok(groups);
    }

    //  내가 만든 모임에서 모임 삭제 요청     -- delete는 param으로 일반적
    @DeleteMapping("/created/delete")
    public ResponseEntity<String> deleteCreatedGroup(@RequestParam Long communityId) {
        Long userId = userContextUtil.getAuthenticatedUserId();
        if (userId == null) {
            //  인증되지 않은 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean deleted = createdGroupService.deleteCreatedGroup(userId, communityId);
        if (deleted) {
            return ResponseEntity.ok("삭제 완료");
        } else {
            return ResponseEntity.status(400).body("모임을 찾을 수 없습니다.");
        }
    }
    //  좋아요 추가 및 삭제
    @RequestMapping(value = "/created/like", method = {RequestMethod.POST, RequestMethod.DELETE})
    public ResponseEntity<String> toggleCreatedGroupLike(@RequestBody LikedGroupDtoReq likedGroupDtoReq) {
        try {
            Long userId = userContextUtil.getAuthenticatedUserId();
            if (userId == null) {
                // 인증되지 않은 경우
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            likedGroupDtoReq.setUserId(userId);
            createdGroupService.createdGroupToggleLike(likedGroupDtoReq);

            return ResponseEntity.ok(likedGroupDtoReq.isSaved() ? "추가" : "취소");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    // --------------------------------------------------------------------------------------------------------------------------------------------
    // JoinedGroup - 참여한 모임 내역
    //  참여한 모임 내역에서 사용자가 참여한 모임 목록을 반환하고 참여한 모임 내역에 보여줌
    @GetMapping("/joined")
    //  userId를 쿼리 파라미터로 받아서 요청을 처리
    public ResponseEntity<List<GroupDtoRes>> getJoinedGroupsDetails() {
        try {
            Long userId = userContextUtil.getAuthenticatedUserId();
            if (userId == null) {
                //  인증되지 않은 경우
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            //  사용자가 참여한 모임 목록을 조회
            List<GroupDtoRes> groups = joinedGroupService.getGroupsByUserId(userId);
            //  조회된 group 반환
            return ResponseEntity.ok(groups);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Collections.emptyList());
        }
    }

    //  참여한 모임 내역에서 정렬순
    @GetMapping("/joined/sort")
    public ResponseEntity<List<GroupDtoRes>> getJoinedGroupsSorted(@RequestParam int value) {
        Long userId = userContextUtil.getAuthenticatedUserId();
        if (userId == null) {
            //  인증되지 않은 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<GroupDtoRes> groups = joinedGroupService.getJoinedGroupsSorted(userId, value);
        return ResponseEntity.ok(groups);
    }

    // 참여한 모임 내역에서 검색창
    @GetMapping("/joined/search")
    public ResponseEntity<List<GroupDtoRes>> searchJoinedGroups(@RequestParam String searchTerm) {
        Long userId = userContextUtil.getAuthenticatedUserId();
        if (userId == null) {
            //  인증되지 않은 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<GroupDtoRes> groups = joinedGroupService.getJoinedGroupsSearch(userId, searchTerm);
        return ResponseEntity.ok(groups);
    }

    // 참여한 모임 내역에서 사용자가 참석 취소를 누른 모임에 대한 코드
    @DeleteMapping("/joined/delete")
    public ResponseEntity<String> deleteJoinedGroups(@RequestParam Long communityId) {
        Long userId = userContextUtil.getAuthenticatedUserId();
        if (userId == null) {
            //  인증되지 않은 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean deleted = joinedGroupService.deleteJoinedGroup(userId, communityId);
        if (deleted) {
            return ResponseEntity.ok("삭제 완료");
        } else {
            return ResponseEntity.status(400).body("모임을 찾을 수 없습니다.");
        }
    }
    //  좋아요 추가 및 삭제
    @RequestMapping(value = "/joined/like", method = {RequestMethod.POST, RequestMethod.DELETE})
    public ResponseEntity<String> toggleJoinedGroupLike(@RequestBody LikedGroupDtoReq likedGroupDtoReq) {
        try {
            Long userId = userContextUtil.getAuthenticatedUserId();
            if (userId == null) {
                // 인증되지 않은 경우
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            likedGroupDtoReq.setUserId(userId);
            joinedGroupService.joinedGroupToggleLike(likedGroupDtoReq);

            return ResponseEntity.ok(likedGroupDtoReq.isSaved() ? "추가" : "취소");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------------------
    //  찜한 모임 내역
    //  사용자가 좋아요한 모든 모임 데이터를 반환
    @GetMapping("/liked")
    public ResponseEntity<List<GroupDtoRes>> getLikedGroups() {
        Long userId = userContextUtil.getAuthenticatedUserId();
        if (userId == null) {
            //  인증되지 않은 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<GroupDtoRes> likedGroups = likedGroupService.getLikedGroupsByUserId(userId);
        return ResponseEntity.ok(likedGroups);
    }

    // 좋아요한 모임에 정렬
    @GetMapping("/liked/sort")
    public ResponseEntity<List<GroupDtoRes>> getLikedGroupsSort(@RequestParam int value) {
        Long userId = userContextUtil.getAuthenticatedUserId();
        if (userId == null) {
            //  인증되지 않은 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<GroupDtoRes> likedGroups = likedGroupService.getLikedGroupsSorted(userId, value);
        return ResponseEntity.ok(likedGroups);
    }

    //  좋아요한 모임에 검색 단어
    @GetMapping("/liked/search")
    public ResponseEntity<List<GroupDtoRes>> searchLikedGroups(@RequestParam String searchTerm) {
        Long userId = userContextUtil.getAuthenticatedUserId();
        if (userId == null) {
            //  인증되지 않은 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<GroupDtoRes> likedGroups = likedGroupService.getLikedGroupsSearch(userId, searchTerm);
        return ResponseEntity.ok(likedGroups);
    }

    // 찜한 모임에서 찜 해제시 삭제 "/liked/nolike
    @DeleteMapping("/liked/nolike")
    public ResponseEntity<String> toggleLikedGroupLike(@RequestBody LikedGroupDtoReq likedGroupDtoReq) {
        Long userId = userContextUtil.getAuthenticatedUserId();
        if (userId == null) {
            // 인증되지 않은 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        likedGroupDtoReq.setUserId(userId);

        try {
            likedGroupService.removeLike(likedGroupDtoReq);
            return ResponseEntity.ok("찜 취소");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
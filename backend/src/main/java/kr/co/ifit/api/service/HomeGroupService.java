package kr.co.ifit.api.service;

import kr.co.ifit.api.request.FilterDtoReq;
import kr.co.ifit.api.request.LikedGroupDtoReq;
import kr.co.ifit.api.response.FilterDtoRes;
import kr.co.ifit.api.response.GroupDtoRes;
import kr.co.ifit.db.entity.Group;
import kr.co.ifit.db.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class HomeGroupService {

    private final GroupRepository groupRepository;
    private final LikedGroupService likedGroupService;


    //  Group 엔티티를 GroupResponseDTO 객체로 변환
    public List<GroupDtoRes> getAllGroups() {
        List<Group> groups = groupRepository.findAll();
        return groups.stream().map(GroupDtoRes::convertToDto).collect(Collectors.toList());     // 변환된 DTO 객체를 리스트로 수집
    }

    //  LikedGroupService에 좋아요 기능 로직을 가져다 씀
    public void homeGroupToggleLike(LikedGroupDtoReq likedGroupDtoReq) {
        if (likedGroupDtoReq.isSaved()) {
            likedGroupService.toggleLike(likedGroupDtoReq);
        } else {
            likedGroupService.removeLike(likedGroupDtoReq);
        }
    }

//    //  캐러셀
//    public List<GroupDtoRes> findGroupsByImageId(Long imageId) {
//        // 이미지 Id와 관련됨 모든 모임을 조회
//        List<Group> groups = groupRepository.findByImageId(imageId);
//
//        LocalDate today = LocalDate.now();
//        LocalTime startMorning = LocalTime.of(6, 0);
//        LocalTime endMorning = LocalTime.of(9, 0);
//
//        List<Group> filteredGroups = groups.stream()
//                .filter(group -> {
//                    LocalDateTime dateTime = group.getDate();
//
//                    if (imageId.equals(1L)) {// 날짜가 오늘 기준으로 임박한 모임
//                        return dateTime.toLocalDate().isAfter(today) || dateTime.toLocalDate().isEqual(today);
//
//                    } else if (imageId.equals(2L)) {//  주말인 경우의 모임 (토요일 또는 일요일)
//                        DayOfWeek dayOfWeek = dateTime.getDayOfWeek();
//                        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
//
//                    } else if (imageId.intValue() == 3) {//  오전 6시부터 9시 사이의 모임
//                        LocalTime time = dateTime.toLocalTime();
//                        return !time.isBefore(startMorning) && !time.isAfter(endMorning);
//
//                    }//  조건이 없으면 모든 모임 반환
//                    return true;
//                })
//                .sorted(Comparator.comparing(Group::getDate))   //  닐짜 기준으로 정렬
//                .toList();
//        return filteredGroups.stream()
//                .map(group -> new GroupDtoRes(
//                        group.getCommunityId(),
//                        group.getTitle(),
//                        group.getTopboxContent(),
//                        group.getSport(),
//                        group.getLocation(),
//                        group.getPerson(),
//                        group.getPeopleParticipation(),
//                        group.getDate())).collect(Collectors.toList());
//    }

    // 운동 종목 필터
    public List<FilterDtoRes> getAllExercises() {
        List<String> sports = groupRepository.findAllExercises();
        // 모든 Group 객체를 가져온 후 sport 필드만 추출하고 중복을 제거한다.
        List<FilterDtoRes> responses = new ArrayList<>();

        int key = 1;
        for (String sport : sports) {
            FilterDtoRes dto = new FilterDtoRes(key, sport);
            dto.setSport(sport);
            dto.setKey(key);
            responses.add(dto);
            key++;
        }
        return responses;
    }

    //  지역 필터
    public List<GroupDtoRes> getAllLocations(String location) {
        List<Group> locations = groupRepository.findByLocation(location);
        return locations.stream().map(GroupDtoRes::convertToDto).collect(Collectors.toList());
    }

    // select 된 운동 종목의 해당하는 모임 리스트 반환
    public List<GroupDtoRes> getGroupsBySport(String sport) {
        List<Group> groups = groupRepository.findBySport(sport);
        return groups.stream().map(GroupDtoRes::convertToDto).collect(Collectors.toList());
    }

    //  선택된 날짜에 해당하는 모임 리스트 반환
    public List<GroupDtoRes> getGroupsByDate(LocalDate dateTime) {
        LocalDateTime startOfDay = dateTime.atStartOfDay();
        LocalDateTime nextDate = dateTime.plusDays(1).atStartOfDay();

        List<Group> groups = groupRepository.findGroupsByDate(startOfDay, nextDate);
        return groups.stream().map(GroupDtoRes::convertToDto).collect(Collectors.toList());
    }

    //  선택된 시간에 해당하는 모임 리스트 반환
    public List<GroupDtoRes> getGroupsByTime(LocalTime parsedTime) {
        int hour = parsedTime.getHour();
        int minute = parsedTime.getMinute();
        List<Group> groups = groupRepository.findGroupsByTime(hour, minute);
        return groups.stream().map(GroupDtoRes::convertToDto).collect(Collectors.toList());
    }


    // 서버로 넘어온 검색어가 포함 되어 있는 모임 리스트 반환
    public List<GroupDtoRes> searchGroups(String searchTerm) {
        List<Group> groups = groupRepository.findByAnyFieldContaining(searchTerm);
        return groups.stream().map(GroupDtoRes::convertToDto).collect(Collectors.toList());
    }

    // 정렬순 에서 선택한 정렬의 옵션에 포함 되는 모임 리스트 반환
    public List<GroupDtoRes> getSortedGroups(int sortOption) {
        List<Group> groups = groupRepository.findAllSorted(sortOption);
        return groups.stream().map(GroupDtoRes::convertToDto).collect(Collectors.toList());
    }

}

//package kr.co.ifit.respository;
//
//import kr.co.ifit.domain.entity.AddGroup;
//import org.springframework.stereotype.Repository;
//
//import java.util.*;
//
//// 메모리에서 구현하고 있?
//@Repository
//public class MemoryGroupRepository {
//
//    private static final Map<Integer, AddGroup> group = new HashMap<>();
//    //    sequence : 키 값을 생성해줌
//    private static int sequence = 0;
//
//    public AddGroup save(AddGroup addGroup) {
//        addGroup.setCommunityId(++sequence);
//        group.put(addGroup.getCommunityId(), addGroup);
//        return addGroup;
//    }
//
//    public Optional<AddGroup> findById(Integer communityId) {
//        return Optional.ofNullable(group.get(communityId));
//    }
//
//    public List<AddGroup> findAll() {
//        return new ArrayList<>(group.values());
//    }
//
//    public void clearStore() {
//        group.clear();
//    }
//}
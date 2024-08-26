package kr.co.ifit.respository;

import kr.co.ifit.domain.entity.AddGroup;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// 모임에 대한 CRUD를 정의
@Repository
public interface GroupRespository {
    //    모임을 저장
    AddGroup save(AddGroup addGroup);

    //   communityId에 해당하는 모임을 찾는다
    Optional<AddGroup> findById(Integer communityId);


    // 저장된 모든 모임의 리스트를 반환
    List<AddGroup> findAll();
}

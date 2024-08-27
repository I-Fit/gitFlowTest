package kr.co.ifit.service;

import kr.co.ifit.domain.entity.Group;
import kr.co.ifit.respository.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeGroupService {

    private final GroupRepository groupRepository;

    public HomeGroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }
}

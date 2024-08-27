package kr.co.ifit.service;

import kr.co.ifit.domain.dto.GroupDTO;
import kr.co.ifit.domain.entity.Group;
import kr.co.ifit.domain.entity.User;
import kr.co.ifit.respository.GroupRepository;
import kr.co.ifit.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddGroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @Autowired
    public AddGroupService(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Group createAddGroup(GroupDTO groupDTO) {
        User user = userRepository.findById(groupDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Group group = new Group(
                groupDTO.getTitle(),
                groupDTO.getTopboxContext(),
                groupDTO.getSport(),
                groupDTO.getLocation(),
                groupDTO.getPerson(),
                groupDTO.getDate(),
                user
        );

        return groupRepository.save(group);
    }
}

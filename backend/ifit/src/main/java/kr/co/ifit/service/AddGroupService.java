package kr.co.ifit.service;

import kr.co.ifit.domain.dto.AddGroupDTO;
import kr.co.ifit.domain.entity.AddGroup;
import kr.co.ifit.domain.entity.User;
import kr.co.ifit.respository.GroupRespository;
import kr.co.ifit.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddGroupService {

    private final GroupRespository groupRespository;
    private final UserRepository userRepository;

    @Autowired
    public AddGroupService(GroupRespository groupRespository, UserRepository userRepository) {
        this.groupRespository = groupRespository;
        this.userRepository = userRepository;
    }

    @Transactional
    public AddGroup createAddGroup(AddGroupDTO addGroupDTO) {
        User user = userRepository.findById(addGroupDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        AddGroup addGroup = new AddGroup(
                addGroupDTO.getTitle(),
                addGroupDTO.getTopboxContext(),
                addGroupDTO.getSport(),
                addGroupDTO.getLocation(),
                addGroupDTO.getPerson(),
                addGroupDTO.getDate(),
                user
        );

        return groupRespository.save(addGroup);
    }
}

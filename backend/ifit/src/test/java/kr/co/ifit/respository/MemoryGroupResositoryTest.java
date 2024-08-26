package kr.co.ifit.respository;

import kr.co.ifit.domain.entity.AddGroup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class MemoryGroupResositoryTest {

    MemoryGroupRespository respository = new MemoryGroupRespository();

    @AfterEach
    public void afterEach() {
        respository.clearStore();
    }

    @Test
    public void test() {
        AddGroup addGroup = new AddGroup();
        addGroup.setTitle("같이 운동하실?");
        addGroup.setSport("수영");
        addGroup.setLocation("강남");
        addGroup.setPerson(12);
        addGroup.setTopboxContent("내일 운동하실 분 구함");
        addGroup.setUserId(2);

        respository.save(addGroup);

        AddGroup addGroup1 = respository.findById(addGroup.getCommunityId()).get();
        Assertions.assertEquals(addGroup, addGroup1);
    }

    @Test
    public void findByName() {
        AddGroup addGroup1 = new AddGroup();
        addGroup1.setTitle("같이 운동하실?");
        addGroup1.setSport("수영");
        addGroup1.setLocation("강남");
        addGroup1.setPerson(12);
        addGroup1.setTopboxContent("내일 운동하실 분 구함");
        addGroup1.setUserId(2);
        respository.save(addGroup1);

        AddGroup addGroup2 = new AddGroup();
        addGroup2.setTitle("같이 운동하실?");
        addGroup2.setSport("수영");
        addGroup2.setLocation("강남");
        addGroup2.setPerson(12);
        addGroup2.setTopboxContent("내일 운동하실 분 구함");
        addGroup2.setUserId(2);
        respository.save(addGroup2);

        AddGroup result = respository.findById(2).get();
        Assertions.assertEquals(addGroup2, result);
    }

    @Test
    public void findAll() {
        AddGroup addGroup1 = new AddGroup();
        addGroup1.setTitle("1231");
        respository.save(addGroup1);
        AddGroup addGroup2 = new AddGroup();
        addGroup2.setTitle("1232");
        respository.save(addGroup2);

        List<AddGroup> result = respository.findAll();
        Assertions.assertEquals(2, result.size());
    }
}
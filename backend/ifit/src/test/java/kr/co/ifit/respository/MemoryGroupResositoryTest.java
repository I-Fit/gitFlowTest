package kr.co.ifit.respository;

import kr.co.ifit.domain.entity.Group;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class MemoryGroupResositoryTest {

    MemoryGroupRepository respository = new MemoryGroupRepository();

    @AfterEach
    public void afterEach() {
        respository.clearStore();
    }

    @Test
    public void test() {
        Group group = new Group();
        group.setTitle("같이 운동하실?");
        group.setSport("수영");
        group.setLocation("강남");
        group.setPerson(12);
        group.setTopboxContent("내일 운동하실 분 구함");
        group.setUserId(2);

        respository.save(group);

        Group group1 = respository.findById(group.getCommunityId()).get();
        Assertions.assertEquals(group, group1);
    }

    @Test
    public void findByName() {
        Group group1 = new Group();
        group1.setTitle("같이 운동하실?");
        group1.setSport("수영");
        group1.setLocation("강남");
        group1.setPerson(12);
        group1.setTopboxContent("내일 운동하실 분 구함");
        group1.setUserId(2);
        respository.save(group1);

        Group group2 = new Group();
        group2.setTitle("같이 운동하실?");
        group2.setSport("수영");
        group2.setLocation("강남");
        group2.setPerson(12);
        group2.setTopboxContent("내일 운동하실 분 구함");
        group2.setUserId(2);
        respository.save(group2);

        Group result = respository.findById(2).get();
        Assertions.assertEquals(group2, result);
    }

    @Test
    public void findAll() {
        Group group1 = new Group();
        group1.setTitle("1231");
        respository.save(group1);
        Group group2 = new Group();
        group2.setTitle("1232");
        respository.save(group2);

        List<Group> result = respository.findAll();
        Assertions.assertEquals(2, result.size());
    }
}
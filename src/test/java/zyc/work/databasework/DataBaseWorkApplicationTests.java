package zyc.work.databasework;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import zyc.work.databasework.mapper.UserMapper;

@SpringBootTest
class DataBaseWorkApplicationTests {

    @Autowired
    public UserMapper userMapper;
    @Test
    void contextLoads() {
    }

    @Test
    void localTimeTest(){
        System.out.println(userMapper.arrivalTime("1965345c-c3d8-11ec-b70d-005056c00001","e6aeeac1-c3d3-11ec-b70d-005056c00001"));
    }
}

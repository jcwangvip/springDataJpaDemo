package com.jcwang.spring.springdata.demo.domin.repository;

import com.jcwang.spring.springdata.demo.domin.Dept;
import com.jcwang.spring.springdata.demo.domin.User;
import com.jcwang.spring.springdata.exception.DaoException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 类描述
 *
 * @author jiancheng
 * @date 2020-6-30
 */
@Slf4j
@DataJpaTest
@ComponentScan
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeptRepository deptRepository;

    String deptCode = "manpower";

    @BeforeEach
    void setUp() {
        saveUser();
    }

    @Test
    void deleteAll() {
        List<User> all = userRepository.findByJoinDeptCode(deptCode);
        userRepository.deleteAll(all);
        assertFalse(CollectionUtils.isEmpty(all));
        List<User> findAll = userRepository.findByJoinDeptCode(deptCode);
        assertTrue(CollectionUtils.isEmpty(findAll));
    }


    @Test
    void delete() {
        User user = userRepository.findById(3L).orElseThrow(new DaoException("查询用户失败"));
        userRepository.delete(user);
        Optional<User> deleteUserFind = userRepository.findById(3L);
        assertFalse(deleteUserFind.isPresent());
        Optional<Dept> deleteDeptFind = deptRepository.findById(2L);
        assertTrue(deleteDeptFind.isPresent());
    }


    @Test
    void update() {
        String userName = "update";

        User user = userRepository.findById(3L).orElseThrow(new DaoException("查询用户失败"));
        user.setUserName(userName);
        User findUser = userRepository.findById(3L).orElseThrow(new DaoException("查询用户失败"));
        assertEquals(userName, findUser.getUserName());
        Dept dept = deptRepository.findByCode(user.getJoinDeptCode()).orElseThrow(new DaoException("没有找到部门信息"));
        dept.setName(userName);
        Dept findUpdateAfterDept = deptRepository.findByCode(user.getJoinDeptCode()).orElseThrow(new DaoException("没有找到部门信息"));
        assertEquals(findUpdateAfterDept.getName(), userName);
    }


  /*  @Transactional(rollbackOn = Exception.class)
    @Test
    void saveDept() {
        Dept dept = new Dept(1L, "service", "服务部门");
        deptRepository.save(dept);
    }
*/
    /*@Transactional(rollbackOn = {Exception.class, Throwable.class})
    @Test
    void saveUser() {
        User user = new User(1L, "zhangsan", "张三");
        Dept dept = new Dept(1L, "service", "服务部门");
        user.setDept(dept);
        User save = userRepository.save(user);
        assertNotNull(save);
        log.info("成功保存的user = {}", save.toString());

        Optional<User> byId = userRepository.findById(1L);
        log.info("加上部门后，成功保存的user = {}", byId.toString());
        Optional<Dept> deptOptional = deptRepository.findById(1L);
        assertTrue(deptOptional.isPresent());
    }*/

    @Transactional(rollbackOn = {Exception.class})
    void saveUser() {
        Dept dept = new Dept(2L, deptCode, "人力资源");
        Optional<Dept> deptOptional = deptRepository.findByCode(deptCode);
        if (deptOptional.isPresent()) {
            dept = deptOptional.get();
        }
        User user = new User(3L, "wangwu", "王五", dept);
        User user1 = new User(4L, "zhaosi", "赵四", dept);
//        java.lang.UnsupportedOperationException
//        List<User> saveUsers = Arrays.asList(user, user1);
        List<User> saveUsers = new ArrayList<>();
        saveUsers.add(user);
        saveUsers.add(user1);
        List<User> users = userRepository.findByIdIn(saveUsers.stream().map(User::getId).collect(Collectors.toList()));

        List<User> existUsers = saveUsers.stream()
                .filter(saveUser -> users.stream()
                        .anyMatch(findUser -> findUser.getId().compareTo(saveUser.getId()) == 0))
                .collect(Collectors.toList());
        saveUsers.removeAll(existUsers);
        if (CollectionUtils.isEmpty(saveUsers)) {
            log.info("没有需要保存的用户");
        }
        userRepository.saveAll(saveUsers);
    }

}
package com.jcwang.spring.springdata.demo.domin;

import com.jcwang.spring.springdata.demo.domin.repository.DeptRepository;
import com.jcwang.spring.springdata.demo.domin.repository.UserRepository;
import com.jcwang.spring.springdata.exception.DaoException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
class DeptRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeptRepository deptRepository;

    private Dept dept;

    @BeforeEach
    void setUp() {
        List<User> users = new ArrayList<>();
        User user = new User(1L, "zhangsan", "张三");
        User user1 = new User(2L, "lisi", "李四");
        users.add(user);
        users.add(user1);
        dept = new Dept(1L, "service", "服务部门", users);
    }


    @Test
    void deleteDept() {
//        saveUser();
        User user = userRepository.findById(1L).orElseThrow(new DaoException("查询用户失败"));
        user.setDeleteDept();
        userRepository.delete(user);
        Optional<User> deleteUserFind = userRepository.findById(1L);
        assertFalse(deleteUserFind.isPresent());
        Optional<Dept> deleteDeptFind = deptRepository.findById(1L);
        assertFalse(deleteDeptFind.isPresent());
    }

    @Transactional(rollbackOn = Exception.class)
    @Test
    void deleteUser() {
        Dept dept = deptRepository.findById(1L).orElseThrow(new DaoException("查询部门信息失败"));
        List<User> users = dept.getUsers();
        users.stream()
                .filter(user -> user.getUserCode().equals("zhangsan"))
//                .filter(user -> user.getUserCode().equals("lisi"))
                .findAny().ifPresent(users::remove);
        deptRepository.save(dept);
    }

    @Transactional(rollbackOn = Exception.class)
    @Test
    void updateUser() {
        Dept dept = deptRepository.findById(1L).orElseThrow(new DaoException("查询部门信息失败"));
        String name = "update";
        dept.setName(name);
        dept.getUsers().stream()
                .filter(user -> user.getUserCode().equals("zhangsan"))
                .findAny().ifPresent(user -> user.setUserName(name));
        Optional<Dept> deptOptional = deptRepository.findById(1L);
        assertTrue(deptOptional.isPresent());
        Dept findDept = deptOptional.get();
        assertEquals(name, findDept.getName());
        dept.getUsers().stream()
                .filter(user -> user.getUserCode().equals("zhangsan"))
                .findAny().ifPresent(user -> assertEquals(name, user.getUserName()));
        log.info("修改完的部门信息 {} ", dept.toString());
    }

    @Test
    void updateDept() {
        Dept dept = deptRepository.findById(1L).orElseThrow(new DaoException("查询部门信息失败"));
        String name = "update";
        dept.setName(name);
        Optional<Dept> deptOptional = deptRepository.findById(1L);
        assertTrue(deptOptional.isPresent());
        assertEquals(name, deptOptional.get().getName());
        log.info("修改完的部门信息 {} ", dept.toString());
    }

    @Transactional(rollbackOn = {Exception.class, Throwable.class})
    @Test
    void findAllDept() {
        List<Dept> deptList = deptRepository.findAll();
        log.info("查询说的的部门信息{}", deptList.toString());
    }

    @Transactional(rollbackOn = {Exception.class, Throwable.class})
    @Test
    void saveDept() {
        /*List<User> users = new ArrayList<>();
        User user = new User(1L, "zhangsan", "张三");
        User lisiUser = new User(2L, "lisi", "李四");
        users.add(user);
        users.add(lisiUser);
        Dept dept = new Dept(1L, "service", "服务部门", users);*/
        deptRepository.save(dept);
    }


}
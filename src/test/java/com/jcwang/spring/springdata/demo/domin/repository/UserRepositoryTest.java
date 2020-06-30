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
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeptRepository deptRepository;

    @BeforeEach
    void setUp() {

    }

    @Test
    void deleteAll() {
        List<User> all = userRepository.findAll();
        all.forEach(User::setDeleteDept);
        userRepository.deleteAll(all);
        deptRepository.deleteAll(deptRepository.findAll());
    }

    @Test
    void saveDelete() {
        List<User> users = userRepository.findByJoinDeptCode("service");
        users.stream()
                .filter(user -> user.getUserCode().equals("zhangsan"))
                .findAny().ifPresent(users::remove);
        userRepository.saveAll(users);
    }

    @Test
    void delete() {
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
    void updateDept() {
//        saveUser();
        User user = userRepository.findById(1L).orElseThrow(new DaoException("查询用户失败"));
        String userName = "update1";
        user.updateDeptName(userName);
        Optional<Dept> deleteDeptFind = deptRepository.findById(1L);
        assertTrue(deleteDeptFind.isPresent());
    }

    @Test
    void update() {
//        saveUser();
        User user = userRepository.findById(1L).orElseThrow(new DaoException("查询用户失败"));
        String userName = "update";
        user.setUserName(userName);
        User findUser = userRepository.findById(1L).orElseThrow(new DaoException("查询用户失败"));
        assertEquals(userName, findUser.getUserName());
        Optional<Dept> deleteDeptFind = deptRepository.findById(1L);
        assertFalse(deleteDeptFind.isPresent());
    }


    @Transactional(rollbackOn = Exception.class)
    @Test
    void saveDept() {
        Dept dept = new Dept(1L, "service", "服务部门");
        deptRepository.save(dept);
    }

    @Transactional(rollbackOn = {Exception.class, Throwable.class})
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
    }

    @Transactional(rollbackOn = {Exception.class, Throwable.class})
    @Test
    void errorSaveUser() {
        // 级联保存的时候级联方新增的数据
        Dept dept = new Dept(1L, "service", "服务部门");
        List<User> users = new ArrayList<>();
        User user = new User(1L, "zhangsan", "张三", dept);
        User lisiUser = new User(2L, "lisi", "李四", dept);
        users.add(user);
        users.add(lisiUser);
        userRepository.saveAll(users);
//        assertThrows(Exception.class, () -> userRepository.save(user));
    }

}
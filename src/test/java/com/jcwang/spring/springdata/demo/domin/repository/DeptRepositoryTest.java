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
class DeptRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeptRepository deptRepository;

    String deptCode = "service";

    @BeforeEach
    void setUp() {
        saveDept();
    }

    @Transactional(rollbackOn = Exception.class)
    void saveDept() {
        List<User> saveUsers = new ArrayList<>();
        User user = new User(1L, "zhangsan", "张三");
        User user1 = new User(2L, "lisi", "李四");
        saveUsers.add(user);
        saveUsers.add(user1);
        Dept saveDept = new Dept(1L, deptCode, "服务部门", saveUsers);
        Optional<Dept> deptOptional = deptRepository.findByCode(deptCode);
        if (!deptOptional.isPresent()) {
            deptRepository.save(saveDept);
            return;
        }
        Dept dept = deptOptional.get();
        List<User> findUsers = dept.getUsers();
        List<User> existUsers = saveUsers.stream()
                .filter(saveUser -> findUsers.stream()
                        .anyMatch(findUser -> findUser.getId().compareTo(saveUser.getId()) == 0))
                .collect(Collectors.toList());
        saveUsers.removeAll(existUsers);
        if (CollectionUtils.isEmpty(saveUsers)) {
            log.info("没有需要保存的用户");
        }
        findUsers.addAll(saveUsers);
        deptRepository.save(dept);
    }

    @Test
    void deleteDept() {
        Dept dept = deptRepository.findByCode(deptCode).orElseThrow(new DaoException("查询部门信息失败"));
        deptRepository.delete(dept);
        List<User> users = userRepository.findByJoinDeptCode(deptCode);
        assertTrue(CollectionUtils.isEmpty(users));
        Optional<Dept> deleteDeptFind = deptRepository.findByCode(deptCode);
        assertFalse(deleteDeptFind.isPresent());
    }

    @Transactional(rollbackOn = Exception.class)
    @Test
    void deleteUser() {
        Dept dept = deptRepository.findByCode(deptCode).orElseThrow(new DaoException("查询部门信息失败"));
        List<User> users = dept.getUsers();
        users.stream()
                .filter(user -> user.getUserCode().equals("zhangsan"))
                .findAny().ifPresent(users::remove);
        deptRepository.save(dept);
    }

    @Transactional(rollbackOn = Exception.class)
    @Test
    void updateUser() {
        Dept dept = deptRepository.findByCode(deptCode).orElseThrow(new DaoException("查询部门信息失败"));
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
    void findDept() {
        Optional<Dept> deptOptional = deptRepository.findByCode(deptCode);
        assertTrue(deptOptional.isPresent());
    }


}
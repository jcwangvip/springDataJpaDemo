package com.jcwang.spring.springdata.demo.domin.repository;

import com.jcwang.spring.springdata.demo.domin.Role;
import com.jcwang.spring.springdata.demo.domin.User;
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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;

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


    @BeforeEach
    void setUp() {
        saveUser();
    }
/*
    @Test
    void deleteAll() {
        List<Person> all = personRepository.findByJoinDeptCode(deptCode);
        personRepository.deleteAll(all);
        assertFalse(CollectionUtils.isEmpty(all));
        List<Person> findAll = personRepository.findByJoinDeptCode(deptCode);
        assertTrue(CollectionUtils.isEmpty(findAll));
    }


    @Test
    void delete() {
        Person person = personRepository.findById(3L).orElseThrow(new DaoException("查询用户失败"));
        personRepository.delete(person);
        Optional<Person> deleteUserFind = personRepository.findById(3L);
        assertFalse(deleteUserFind.isPresent());
        Optional<Dept> deleteDeptFind = deptRepository.findById(2L);
        assertTrue(deleteDeptFind.isPresent());
    }


    @Test
    void update() {
        String userName = "update";

        Person person = personRepository.findById(3L).orElseThrow(new DaoException("查询用户失败"));
        person.setUserName(userName);
        Person findPerson = personRepository.findById(3L).orElseThrow(new DaoException("查询用户失败"));
        assertEquals(userName, findPerson.getUserName());
        Dept dept = deptRepository.findByCode(person.getJoinDeptCode()).orElseThrow(new DaoException("没有找到部门信息"));
        dept.setName(userName);
        Dept findUpdateAfterDept = deptRepository.findByCode(person.getJoinDeptCode()).orElseThrow(new DaoException("没有找到部门信息"));
        assertEquals(findUpdateAfterDept.getName(), userName);
    }*/


    @Test
    void find() {
        List<User> users = userRepository.findAll();
        assertFalse(CollectionUtils.isEmpty(users));
    }

    @Transactional(rollbackOn = {Exception.class})
    void saveUser() {
        Role role = new Role(1L, "admin", "admin");

        List<User> saveUsers = new ArrayList<>();
        User person = new User(1L, "wangwu", "王五");
        User person1 = new User(2L, "zhaosi", "赵四");
        saveUsers.add(person);
        saveUsers.add(person1);
        List<String> userCodes = saveUsers.stream().map(User::getUserCode).collect(Collectors.toList());
        List<User> users = userRepository.findByUserCodeIn(userCodes);
        List<User> existPeople = saveUsers.stream()
                .filter(savePerson -> users.stream()
                        .anyMatch(findPerson -> findPerson.getId().compareTo(savePerson.getId()) == 0))
                .collect(Collectors.toList());
        saveUsers.removeAll(existPeople);

        users.addAll(saveUsers);
        if (CollectionUtils.isEmpty(users)) {
            log.info("没有需要保存的用户");
        }
        userRepository.saveAll(users);
    }

}
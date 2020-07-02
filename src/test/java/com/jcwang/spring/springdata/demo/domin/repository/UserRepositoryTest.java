package com.jcwang.spring.springdata.demo.domin.repository;

import com.jcwang.spring.springdata.demo.domin.Role;
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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    */

    @Test
    void updateRole() {
        String findUserCode = "wangwu";
        String updateUserName = "王五修改";
        String roleCode = "admin";

        User user = userRepository.findByUserCode(findUserCode).orElseThrow(new DaoException("查询用户失败"));
        List<Role> roles = user.getRoles();
        roles.stream()
                .filter(role -> role.getRoleCode().equals(roleCode))
                .findAny().ifPresent(role -> role.setRoleName(updateUserName));
        User updateAfterFindUser = userRepository.findByUserCode(findUserCode).orElseThrow(new DaoException("查询用户失败"));
        updateAfterFindUser.getRoles()
                .stream()
                .filter(role -> role.getRoleCode().equals(roleCode))
                .findAny().ifPresent(role -> assertEquals(updateUserName, role.getRoleName()));
        // TODO 这里还需要研究一下，为什么不增加吗，领域驱动开发是不是就有问题
//        assertEquals(user.getVersion() + 1, updateAfterFindUser.getVersion());
    }


    @Test
    void updateUser() {
        String findUserCode = "wangwu";
        String updateUserName = "王五修改";

        User user = userRepository.findByUserCode(findUserCode).orElseThrow(new DaoException("查询用户失败"));
        user.setUserName(updateUserName);
        User updateAfterFindUser = userRepository.findByUserCode(findUserCode).orElseThrow(new DaoException("查询用户失败"));
        assertEquals(updateUserName, updateAfterFindUser.getUserName());
    }


    @Test
    void find() {
        List<User> users = userRepository.findAll();
        assertFalse(CollectionUtils.isEmpty(users));
    }

    @Transactional(rollbackOn = Exception.class)
    void saveUser() {
        Role role = new Role(1L, "admin", "admin");

        List<User> saveUsers = new ArrayList<>();
        User person = new User(1L, "wangwu", "王五", role);
        User person1 = new User(2L, "zhaosi", "赵四", role);
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
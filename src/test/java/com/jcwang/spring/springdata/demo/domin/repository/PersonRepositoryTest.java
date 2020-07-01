package com.jcwang.spring.springdata.demo.domin.repository;

import com.jcwang.spring.springdata.demo.domin.Dept;
import com.jcwang.spring.springdata.demo.domin.Person;
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
class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private DeptRepository deptRepository;

    String deptCode = "manpower";

    @BeforeEach
    void setUp() {
        saveUser();
    }

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
        Person user = new Person(1L, "zhangsan", "张三");
        Dept dept = new Dept(1L, "service", "服务部门");
        user.setDept(dept);
        Person save = personRepository.save(user);
        assertNotNull(save);
        log.info("成功保存的user = {}", save.toString());

        Optional<Person> byId = personRepository.findById(1L);
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
        Person person = new Person(3L, "wangwu", "王五", dept);
        Person person1 = new Person(4L, "zhaosi", "赵四", dept);
//        java.lang.UnsupportedOperationException
//        List<Person> savePeople = Arrays.asList(person, person1);
        List<Person> savePeople = new ArrayList<>();
        savePeople.add(person);
        savePeople.add(person1);
        List<Person> people = personRepository.findByIdIn(savePeople.stream().map(Person::getId).collect(Collectors.toList()));

        List<Person> existPeople = savePeople.stream()
                .filter(savePerson -> people.stream()
                        .anyMatch(findPerson -> findPerson.getId().compareTo(savePerson.getId()) == 0))
                .collect(Collectors.toList());
        savePeople.removeAll(existPeople);
        if (CollectionUtils.isEmpty(savePeople)) {
            log.info("没有需要保存的用户");
        }
        personRepository.saveAll(savePeople);
    }

}
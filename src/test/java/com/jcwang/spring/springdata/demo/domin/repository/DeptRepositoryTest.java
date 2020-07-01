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
class DeptRepositoryTest {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private DeptRepository deptRepository;

    String deptCode = "service";

    @BeforeEach
    void setUp() {
        saveDept();
    }

    @Transactional(rollbackOn = Exception.class)
    void saveDept() {
        List<Person> savePeople = new ArrayList<>();
        Person person = new Person(1L, "zhangsan", "张三");
        Person person1 = new Person(2L, "lisi", "李四");
        savePeople.add(person);
        savePeople.add(person1);
        Dept saveDept = new Dept(1L, deptCode, "服务部门", savePeople);
        Optional<Dept> deptOptional = deptRepository.findByCode(deptCode);
        if (!deptOptional.isPresent()) {
            deptRepository.save(saveDept);
            return;
        }
        Dept dept = deptOptional.get();
        List<Person> findPeople = dept.getPerson();
        List<Person> existPeople = savePeople.stream()
                .filter(savePerson -> findPeople.stream()
                        .anyMatch(findPerson -> findPerson.getId().compareTo(savePerson.getId()) == 0))
                .collect(Collectors.toList());
        savePeople.removeAll(existPeople);
        if (CollectionUtils.isEmpty(savePeople)) {
            log.info("没有需要保存的用户");
        }
        findPeople.addAll(savePeople);
        deptRepository.save(dept);
    }

    @Test
    void deleteDept() {
        Dept dept = deptRepository.findByCode(deptCode).orElseThrow(new DaoException("查询部门信息失败"));
        deptRepository.delete(dept);
        Optional<Dept> deleteDeptFind = deptRepository.findByCode(deptCode);
        assertFalse(deleteDeptFind.isPresent());
        List<Person> people = personRepository.findByJoinDeptCode(deptCode);
        assertTrue(CollectionUtils.isEmpty(people));
    }

    @Transactional(rollbackOn = Exception.class)
    @Test
    void deleteUser() {
        Dept dept = deptRepository.findByCode(deptCode).orElseThrow(new DaoException("查询部门信息失败"));
        List<Person> people = dept.getPerson();
        people.stream()
                .filter(person -> person.getUserCode().equals("zhangsan"))
                .findAny().ifPresent(people::remove);
        deptRepository.save(dept);
    }

    @Transactional(rollbackOn = Exception.class)
    @Test
    void updateUser() {
        Dept dept = deptRepository.findByCode(deptCode).orElseThrow(new DaoException("查询部门信息失败"));
        String name = "update";
        dept.setName(name);
        dept.getPerson().stream()
                .filter(person -> person.getUserCode().equals("zhangsan"))
                .findAny().ifPresent(person -> person.setUserName(name));
        Optional<Dept> deptOptional = deptRepository.findById(1L);
        assertTrue(deptOptional.isPresent());
        Dept findDept = deptOptional.get();
        assertEquals(name, findDept.getName());
        dept.getPerson().stream()
                .filter(person -> person.getUserCode().equals("zhangsan"))
                .findAny().ifPresent(person -> assertEquals(name, person.getUserName()));
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
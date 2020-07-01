package com.jcwang.spring.springdata.demo.domin.repository;

import com.jcwang.spring.springdata.demo.domin.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 类描述
 *
 * @author jiancheng
 * @date 2020-6-30
 */
public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findByJoinDeptCode(String joinDeptCode);

    List<Person> findByIdIn(Collection ids);

    Optional<Person> findByUserCode(String userCode);

}

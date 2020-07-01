package com.jcwang.spring.springdata.demo.domin.repository;

import com.jcwang.spring.springdata.demo.domin.Person;
import com.jcwang.spring.springdata.demo.domin.User;
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
public interface UserRepository extends JpaRepository<User, Long> {


    List<User> findByIdIn(Collection ids);

    Optional<User> findByUserCode(String userCode);

    List<User> findByUserCodeIn(List<String> userCodes);
}

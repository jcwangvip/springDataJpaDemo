package com.jcwang.spring.springdata.demo.domin.repository;

import com.jcwang.spring.springdata.demo.domin.Dept;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 类描述
 *
 * @author jiancheng
 * @date 2020-6-30
 */
public interface DeptRepository extends JpaRepository<Dept, Long> {
}

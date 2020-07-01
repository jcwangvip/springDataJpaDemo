package com.jcwang.spring.springdata.demo.domin;


import lombok.Data;

import javax.persistence.*;

/**
 * 类描述
 *
 * @author jiancheng
 * @date 2020-6-30
 */
@Data
@Entity
@Table
public class User {

    @Id
    private Long id;
    private String userCode;
    private String userName;
    private String joinDeptCode;
    @Version
    private Long version;


    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
//    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "joinDeptCode", referencedColumnName = "code", insertable = false, updatable = false)
//    @JoinColumn(name = "joinDeptCode1", referencedColumnName = "code")
    private Dept dept;

    public void setDept(Dept dept) {
        this.joinDeptCode = dept.getCode();
        this.dept = dept;
    }

    public User() {
    }

    public User(Long id, String userCode, String userName, Dept dept) {
        this.id = id;
        this.userCode = userCode;
        this.userName = userName;
        this.dept = dept;
        this.joinDeptCode = dept.getCode();
    }

    public User(Long id, String userCode, String userName) {
        this.id = id;
        this.userCode = userCode;
        this.userName = userName;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userCode='" + userCode + '\'' +
                ", userName='" + userName + '\'' +
                ", joinDeptCode='" + joinDeptCode + '\'' +
                '}';
    }

    public void updateDeptName(String name) {
        dept.setName(name);
    }

    public void setDeleteDept() {
        this.dept = null;
    }
}

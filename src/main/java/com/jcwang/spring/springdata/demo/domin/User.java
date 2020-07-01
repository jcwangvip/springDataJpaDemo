package com.jcwang.spring.springdata.demo.domin;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 类描述
 *
 * @author jiancheng
 * @date 2020-7-1
 */
@Getter
@Setter
@Entity
@Table
public class User {

    @Id
    private Long id;
    private String userCode;
    private String userName;
    private String joinRoleCode;
    @Version
    private Long version;

//    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "joinRoleCode", referencedColumnName = "roleCode", insertable = false, updatable = false)
//    private List<Role> roles;


    public User() {
    }

    public User(Long id, String userCode, String userName) {
        this.id = id;
        this.userCode = userCode;
        this.userName = userName;
    }
}

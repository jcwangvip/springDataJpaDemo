package com.jcwang.spring.springdata.demo.domin;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
public class User implements Serializable {

    @Id
    private Long id;
    private String userCode;
    private String userName;
    //    private String joinRoleCode;
    @Version
    private Long version;

    //    @ManyToMany(targetEntity = Role.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "joinRoleCode", referencedColumnName = "roleCode")
    //使用@JoinTable注解添加中间表
    //其中name属性设置中间表的表名
    //joinCloums属性在中间表中添加的列
    //JoinColumns属性：
    //				@joinColumn属性设置中间表中的列名
    //						referencedColumnName属性指向被映射表的主键（可以没有该属性）
    //@inverseJoinColumns另外一张表在中间表中的列
    @JoinTable(
            name = "user_role",
//            name = "role_users",
//            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            joinColumns = {@JoinColumn(name = "user_code", referencedColumnName = "userCode")},
//            joinColumns = {@JoinColumn(name = "user_id")},
//            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
            inverseJoinColumns = {@JoinColumn(name = "role_code", referencedColumnName = "roleCode")})
//            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Role> roles;


    public User() {
    }

    public User(Long id, String userCode, String userName) {
        this.id = id;
        this.userCode = userCode;
        this.userName = userName;
    }

    public User(Long id, String userCode, String userName, Role role) {
        this.id = id;
        this.userCode = userCode;
        this.userName = userName;
        if (null == roles) {
            roles = new ArrayList<>();
            roles.add(role);
        }
    }

    public User(Long id, String userCode, String userName, List<Role> roles) {
        this.id = id;
        this.userCode = userCode;
        this.userName = userName;
        this.roles = roles;
    }
}

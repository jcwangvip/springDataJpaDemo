package com.jcwang.spring.springdata.demo.domin;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
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
public class Role implements Serializable {


    @Id
    private Long id;
    private String roleCode;
    private String roleName;
    @Version
    private Long version;

    @ManyToMany(targetEntity = User.class, cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "role_code", referencedColumnName = "roleCode")},
            inverseJoinColumns = {@JoinColumn(name = "user_code", referencedColumnName = "userCode")})
    private List<User> users;

    public Role() {
    }

    public Role(Long id, String roleCode, String roleName) {
        this.id = id;
        this.roleCode = roleCode;
        this.roleName = roleName;
    }
}

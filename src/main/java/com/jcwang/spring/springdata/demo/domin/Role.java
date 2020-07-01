package com.jcwang.spring.springdata.demo.domin;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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
public class Role {


    @Id
    private Long id;
    private String roleCode;
    private String roleName;
    @Version
    private Long version;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
//    @JoinColumn(name = "roleCode", referencedColumnName = "joinRoleCode")
    private List<User> users;

    public Role(Long id, String roleCode, String roleName) {
        this.id = id;
        this.roleCode = roleCode;
        this.roleName = roleName;
    }
}

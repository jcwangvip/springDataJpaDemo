package com.jcwang.spring.springdata.demo.domin;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 类描述
 *
 * @author jiancheng
 * @date 2020-6-30
 */
@Data
@Entity
@Table
public class Dept implements Serializable {

    @Id
    private Long id;
    private String code;
    private String name;
    @Version
    private Long version;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "joinDeptCode")
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "joinDeptCode")
//    @JoinColumn(name = "code", referencedColumnName = "joinDeptCode", insertable = false, updatable = false)
//    @JoinColumn(name = "code", referencedColumnName = "joinDeptCode")
//    @JoinColumn(name = "code")
//    @JoinColumn(name = "joinDeptCode", referencedColumnName = "code", insertable = false, updatable = false)
    private List<User> users;


    public Dept() {
    }

    public Dept(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    Dept(Long id, String code, String name, List<User> users) {
        this.id = id;
        this.code = code;
        this.name = name;
        users.forEach(user -> user.setJoinDeptCode(code));
        this.users = users;
    }
}

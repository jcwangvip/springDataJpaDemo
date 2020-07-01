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
 * @date 2020-6-30
 */
@Getter
@Setter
@Entity
@Table
public class Dept implements Serializable {

    @Id
    private Long id;
    private String code;
    private String name;
    @Version
    private Long version;

    //    会把user一方的join列set为null
    //    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    加上orphanRemoval = true会把2张表全部删除，什么操作啊?因为2张关联表的级联操作都是All导致依赖删除,所以会造成删除一行,清空2张表的情况.慎重使用CascadeType.ALL
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
//    mappedBy = "joinDeptCode" 会生成中间表,不能和JoinColumn一起使用,因为joinColumn 是把关系维护到user表,不会生成第三张表
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "joinDeptCode")
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "joinDeptCode")
//        在一的一方中加入多的集合时,joinColumn中的name代表的是多的一方的管理字段(user表中的字段),和在多的一方维护相反,PS:其实也不是相反,name就是代表当前实体上的关联字段
//    @JoinColumn(name = "code")
//    没有指定user管理dept具体的字段,会默认和id关联,造成的错误Caused by: java.sql.SQLException: Referencing column 'join_dept_code' and referenced column 'id' in foreign key constraint 'FKikk93ikdxe5fjfr66dkox495' are incompatible.
//    @JoinColumn(name = "joinDeptCode")
    @JoinColumn(name = "joinDeptCode", referencedColumnName = "code")
//    @JoinColumn(name = "joinDeptCode", referencedColumnName = "code", insertable = false, updatable = false)
    private List<User> users;


    public Dept() {
    }

    public Dept(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public Dept(Long id, String code, String name, List<User> users) {
        this.id = id;
        this.code = code;
        this.name = name;
        users.forEach(user -> user.setJoinDeptCode(code));
        this.users = users;
    }
}

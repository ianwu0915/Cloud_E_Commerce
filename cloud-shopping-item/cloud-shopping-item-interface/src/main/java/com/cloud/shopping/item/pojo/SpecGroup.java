package com.cloud.shopping.item.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;
import javax.persistence.Transient;

@Table(name = "tb_spec_group")
@Data
public class SpecGroup {

    @Id
    @KeySql(useGeneratedKeys=true)
    private Long id;

    private Long cid;

    private String name;

    @Transient
    private List<SpecParam> params; // all the specification parameters in this group
}

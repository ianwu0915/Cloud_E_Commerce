package com.cloud.shopping.user.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import tk.mybatis.mapper.annotation.KeySql;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import java.util.Date;

@Data
@Table(name = "tb_user")
public class User {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    @NotEmpty(message = "Username cannot be empty")
    @Length(min=4,max=32,message = "Username length must be between 4 and 32")
    private String username;

    @Length(min=6,max=30,message = "Password length must be between 6 and 30")
    @JsonIgnore
    private String password;

    @Pattern(regexp = "^(13[0-9])|(14[579])|(15([0-3]|[5-9]))|(16[6])|(17[0135678])|(18[0-9])|(19[89])\\d{8}$",message = "手机号不正确")
    private String phone;

    private Date created;

    @JsonIgnore
    private String salt;
}
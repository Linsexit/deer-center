package com.xiaolu.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 林小鹿
 * @version 1.0
 * @create 2023/9/28 20:49
 * @Description 用户登录请求体
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 4869168056977984521L;

    private String userAccount;

    private String userPassword;

}

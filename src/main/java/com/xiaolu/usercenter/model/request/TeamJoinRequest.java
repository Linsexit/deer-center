package com.xiaolu.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 林小鹿
 * @version 1.0
 * @create 2023/9/28 20:49
 * @Description 用户登录请求体
 */
@Data
public class TeamJoinRequest implements Serializable {

    private static final long serialVersionUID = 6109047970680008363L;

    /**
     * id
     */
    private Long teamId;


    /**
     * 密码
     */
    private String password;


}

package com.xiaolu.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 林小鹿
 * @version 1.0
 * @create 2023/9/28 20:49
 * @Description 用户添加队伍请求体
 */
@Data
public class TeamAddRequest implements Serializable {

    private static final long serialVersionUID = 6109047970680008363L;

    /**
     * 队伍名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 0 - 公开，1 - 私有 2 - 加密
     */
    private Integer status;

    /**
     * 密码
     */
    private String password;


}

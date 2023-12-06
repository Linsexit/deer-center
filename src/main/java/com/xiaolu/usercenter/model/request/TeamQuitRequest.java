package com.xiaolu.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 林小鹿
 * @version 1.0
 * @create 2023/9/28 20:49
 * @Description 用户退出队伍请求体
 */
@Data
public class TeamQuitRequest implements Serializable {

    private static final long serialVersionUID = 6109047970680018363L;

    /**
     * id
     */
    private Long teamId;




}

package com.xiaolu.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 林小鹿
 * @version 1.0
 * @create 2023/11/24 13:46
 * @Description 通用删除请求参数
 */
@Data
public class DeleteRequest implements Serializable {

    private static final long serialVersionUID = -7895624954610761553L;

    private long id;

}

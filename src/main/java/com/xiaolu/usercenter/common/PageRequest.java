package com.xiaolu.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 林小鹿
 * @version 1.0
 * @create 2023/11/24 13:46
 * @Description 通用分页请求参数
 */
@Data
public class PageRequest implements Serializable {

    private static final long serialVersionUID = -7895624954610761553L;

    /**
     * 页面大小
     */
    protected int pageSize = 10;

    /**
     * 当前页码
     */
    protected  int pageNum = 1;

}

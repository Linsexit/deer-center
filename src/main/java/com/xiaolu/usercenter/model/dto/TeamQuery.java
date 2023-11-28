package com.xiaolu.usercenter.model.dto;


import com.xiaolu.usercenter.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author 林小鹿
 * @version 1.0
 * @create 2023/11/24 11:12
 * @Description 队伍查询 包装 / 封装类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TeamQuery extends PageRequest {
    /**
     * id
     */
    private Long id;

    /**
     * 搜索关键词（同时对队伍名称和描述搜索）
     */
    private String searchText;

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
     * 用户id
     */
    private Long userId;

    /**
     * 0 - 公开，1 - 私有 2 - 加密
     */
    private Integer status;

}

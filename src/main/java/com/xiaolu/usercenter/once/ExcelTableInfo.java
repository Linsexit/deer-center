package com.xiaolu.usercenter.once;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * Excel表格用户信息对象
 */
@Data
public class ExcelTableInfo {

    /**
     * 强制读取第三个 这里不建议 index 和 name 同时用，要么一个对象只用index，要么一个对象只用name去匹配
     */
    // @ExcelProperty(index = 0)
    @ExcelProperty("小鹿编号")
    private String deerCode;

    /**
     * 用户昵称
     */
    @ExcelProperty("成员昵称")
    private String username;

}
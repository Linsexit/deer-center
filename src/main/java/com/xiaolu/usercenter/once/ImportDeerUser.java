package com.xiaolu.usercenter.once;

import com.alibaba.excel.EasyExcel;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 林小鹿
 * @version 1.0
 * @create 2023/11/6 13:41
 * @Description 将Excel数据导入到数据库
 */
public class ImportDeerUser {

    public static void main(String[] args) {

        String fileName = "E:\\code\\item\\user-center\\src\\main\\resources\\testExcel.xlsx";

        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 同步读取会自动finish
        List<ExcelTableInfo> userInfoList =
                EasyExcel.read(fileName).head(ExcelTableInfo.class).sheet().doReadSync();
        // userInfoList.forEach(System.out::println);
        // 去重操作
        Map<String, List<ExcelTableInfo>> listMap =
                userInfoList.stream()
                        // 判断getUsername非空才返回
                        .filter(userInfo -> StringUtils.isNoneEmpty(userInfo.getUsername()))
                        // 根据getUsername分组
                        .collect(Collectors.groupingBy(ExcelTableInfo::getUsername));
        System.out.println("不重复昵称数 = " + listMap.keySet().size());

    }
}

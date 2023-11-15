package com.xiaolu.usercenter.once;

import com.alibaba.excel.EasyExcel;
import java.util.List;

/**
 * @author 林小鹿
 * @version 1.0
 * @create 2023/11/6 11:54
 * @Description 批量处理Excel数据
 */
public class ImportExcel {

    public static void main(String[] args) {
        String fileName = "E:\\code\\item\\user-center\\src\\main\\resources\\testExcel.xlsx";
        // readByListener(fileName);

        synchronousRead(fileName);
    }

    /**
     * 方式一 : 最简单的监听器读
     * 单独抽离处理逻辑，代码清晰易于维护；一条一条处理，适用于数据量大的场景。
     */
    public static void readByListener(String fileName) {
        // 写法1：JDK8+ ,不用额外写一个DemoDataListener
        // since: 3.0.0-beta1

        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        // 这里每次会读取3000条数据 然后返回过来 直接调用使用数据就行
        EasyExcel.read(fileName, ExcelTableInfo.class, new TableListener()).sheet().doRead();

    }

    /**
     * 方式二 : 同步读
     * 同步的返回，不推荐使用，如果数据量大会把数据放到内存里面
     * 无需创建监听器，一次性获取完整数据。方便简单，但是数据量大时会有等待时常，也可能内存溢出。
     */
    public static void synchronousRead(String fileName) {
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 同步读取会自动finish
        List<ExcelTableInfo> totalDataList =
                EasyExcel.read(fileName).head(ExcelTableInfo.class).sheet().doReadSync();
        totalDataList.forEach(System.out::println);

    }
}

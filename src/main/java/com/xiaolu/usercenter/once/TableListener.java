package com.xiaolu.usercenter.once;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.extern.slf4j.Slf4j;

/**
 * 监听器
 * 有个很重要的点 ExcelTableInfoListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
 */
@Slf4j
public class TableListener implements ReadListener<ExcelTableInfo> {

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context 上下文
     */
    @Override
    public void invoke(ExcelTableInfo data, AnalysisContext context) {
        System.out.println(data);
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        System.out.println("已解析完成");
    }
}
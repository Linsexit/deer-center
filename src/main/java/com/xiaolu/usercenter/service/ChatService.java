package com.xiaolu.usercenter.service;

import com.xiaolu.usercenter.model.domain.Chat;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolu.usercenter.model.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 16385
* @description 针对表【chat(用户)】的数据库操作Service
* @createDate 2023-10-17 21:41:54
*/
public interface ChatService extends IService<Chat> {

    /**
     * 查询历史记录
     * @param request
     * @param page
     * @return
     */
    List<Chat> searchChats(HttpServletRequest request, int page);

    /**
     * 调用chatGpt接口并存储结果
     * @param request
     * @param quest
     * @return
     */
    Chat addChat(HttpServletRequest request, String quest);
}

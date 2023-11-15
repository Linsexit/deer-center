package com.xiaolu.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolu.usercenter.common.ErrorCode;
import com.xiaolu.usercenter.exception.BusinessException;
import com.xiaolu.usercenter.model.domain.Chat;
import com.xiaolu.usercenter.model.domain.User;
import com.xiaolu.usercenter.service.ChatService;
import com.xiaolu.usercenter.mapper.ChatMapper;
import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.xiaolu.usercenter.contant.UserConstant.USER_LOGIN_STATE;

/**
* @author 16385
* @description 针对表【chat(用户)】的数据库操作Service实现
* @createDate 2023-10-17 21:41:54
*/
@Slf4j
@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat>
    implements ChatService{

    @Resource
    private YuCongMingClient client;

    @Value("${deer.chatType}")
    private Long chatType;

    private final int pageSize = 10;



    @Override
    public List<Chat> searchChats(HttpServletRequest request, int page) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN) ;
        }
        // 拿到总页数
        Long totalPage = currentUser.getTotalPage();

        if (page > totalPage) {
            throw new BusinessException(ErrorCode.CHAT_OUT_OF_PAGE);
        }

        LambdaQueryWrapper<Chat> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Chat::getUserid, currentUser.getId())
                .orderByAsc(Chat::getCreateTime);

        Page<Chat> chatPage = new Page<>(page , pageSize);
        page(chatPage, queryWrapper);
        totalPage = chatPage.getPages();
        // 更新总页数
        currentUser.setTotalPage(totalPage);
        request.getSession().setAttribute(USER_LOGIN_STATE, currentUser);

        log.info("总页数： "+ totalPage);
        log.info("总记录数： "+ chatPage.getTotal());

        List<Chat> chats = chatPage.getRecords();

        return chats.stream().peek(chat -> chat.setUserid(null)).collect(Collectors.toList());
    }

    @Override
    public Chat addChat(HttpServletRequest request, String quest) {

        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN) ;
        }

        if (StringUtils.isAnyBlank(quest)) {
            throw new BusinessException(ErrorCode.CHAT_REQUEST_ERROR, "内容不能为空");
        }
        // 构建请求参数
        DevChatRequest devChatRequest = new DevChatRequest();
        devChatRequest.setModelId(chatType);
        log.info("userID: " + currentUser.getId() + "quest: " + quest);
        devChatRequest.setMessage(quest);

        // 获取响应结果
        BaseResponse<DevChatResponse> response = client.doChat(devChatRequest);
        Chat chat = new Chat();
        chat.setUserid(currentUser.getId());
        chat.setContent(response.getData().getContent());

        if (!save(chat)) {
            throw new BusinessException(ErrorCode.CHAT_REQUEST_ERROR, "AI信息存储失败");
        }
        long count = count(new LambdaQueryWrapper<Chat>().eq(Chat::getUserid, currentUser.getId()));
        if(count % pageSize == 0) {
            currentUser.setTotalPage(999L);
        }
        request.getSession().setAttribute(USER_LOGIN_STATE, currentUser);

        log.info("AI: " + response.getData());
        chat.setUserid(null);

        return chat;
    }
}





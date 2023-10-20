package com.xiaolu.usercenter;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolu.usercenter.model.domain.Chat;
import com.xiaolu.usercenter.service.ChatService;
import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class UserCenterApplicationTests {

	@Resource
	private ChatService chatService;

	@Resource
	private YuCongMingClient client;

	@Value("${deer.chatType}")
	private Long chatType;

	@Test
	void contextLoads() {
	}

	@Test
	public void selectPage(){
		int page = 4;
		int pageSize = 3;
		LambdaQueryWrapper<Chat> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Chat::getUserid, 1714504346304245762L);

		Page<Chat> chatPage = new Page<>(page , pageSize);
		chatService.page(chatPage, queryWrapper);

		System.out.println("总页数： "+ chatPage.getPages());
		System.out.println("总记录数： "+ chatPage.getTotal());
		chatPage.getRecords().forEach(System.out::println);

	}

	@Test
	public void chatgpt() {
		// 构建请求参数
		DevChatRequest devChatRequest = new DevChatRequest();
		devChatRequest.setModelId(chatType);
		devChatRequest.setMessage("hello");

		// 获取响应结果
		BaseResponse<DevChatResponse> response = client.doChat(devChatRequest);

		System.out.println(response.getData().getContent());
	}

}

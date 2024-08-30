package com.apple.chat.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin
@RequestMapping("/chatroom/*")
public class ChatController {

	@Value("${sendbird.appId}")
	private String appId;
	
	@GetMapping("/chatroom")
	@ResponseBody
	public Map<String, String> chatroom() throws Exception{
		Map<String, String> chatMap = new HashMap<>();
		
		chatMap.put("appId", appId);
		chatMap.put("userId", "sampleUser");	//원래는 파라미터로 User의 userID 값을 얻어와야 한다. 임시로 String 저장
		chatMap.put("nickname", "octopus");		//원래 User의 nickname을 얻어와야 한다.
		
		return chatMap;
	}
	
	@GetMapping("/chatroomList")
	public String chatroomList() {
		return "chatroom/chatroomList";
	}
	
}

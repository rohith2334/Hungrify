package com.app.hungrify.main.template;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {
    private final ChatModel chatModel;

    public String sendPrompt(String message){
        return chatModel.call(message);
    }

}

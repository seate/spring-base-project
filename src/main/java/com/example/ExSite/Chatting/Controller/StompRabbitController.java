package com.example.ExSite.Chatting.Controller;

import com.example.ExSite.Chatting.dto.ChatDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Log4j2
public class StompRabbitController {
    private final RabbitTemplate template;

    private final static String CHAT_EXCHANGE_NAME = "chat.exchange";


    @MessageMapping("chat.enter.{chatRoomId}")
    public void enter(ChatDTO chat, @DestinationVariable String chatRoomId) {
        //chat.setRegDate(LocalDateTime.now());
        chat.setMessage("입장하셨습니다.");
        chat.setChatRoomId(Long.parseLong(chatRoomId));

        template.convertAndSend(CHAT_EXCHANGE_NAME, "room." + chatRoomId, chat); //exchange
        //template.convertAndSend("room." + chatRoomId, chat); //queue
        //template.convertAndSend("amq.topic", "room." + chatRoomId, chat); //topic
    }

    @MessageMapping("chat.message.{chatRoomId}")
    public void send(ChatDTO chat, @DestinationVariable String chatRoomId) {
        //chat.setRegDate(LocalDateTime.now());
        chat.setChatRoomId(Long.parseLong(chatRoomId));

        template.convertAndSend(CHAT_EXCHANGE_NAME, "room." + chatRoomId, chat);
        //template.convertAndSend("room." + chatRoomId, chat); //queue
        //template.convertAndSend("amq.topic", "room." + chatRoomId, chat); //topic
    }


}

package com.example.ExSite.Chatting.Controller;

import com.example.ExSite.Chatting.domain.Chat;
import com.example.ExSite.Member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ChatController {

    private final RabbitTemplate template;
    private final MemberService memberService;

    private final static String CHAT_EXCHANGE_NAME = "chat.exchange";

    @MessageMapping("chat.enter.{chatRoomId}")
    public void enter(Chat chat, @DestinationVariable String chatRoomId, java.security.Principal principal) {
        chat.setRegDate(LocalDateTime.now());
        chat.setMessage("입장하셨습니다.");
        chat.setChatRoomId(Long.parseLong(chatRoomId));

        chat.setMemberId(memberService.findIdByToken(principal));


        template.convertAndSend(CHAT_EXCHANGE_NAME, "room." + chatRoomId, chat); //exchange
        //template.convertAndSend("room." + chatRoomId, chat); //queue
        //template.convertAndSend("amq.topic", "room." + chatRoomId, chat); //topic
    }

    @MessageMapping("chat.message.{chatRoomId}")
    public void send(Chat chat, @DestinationVariable String chatRoomId, java.security.Principal principal) {
        chat.setRegDate(LocalDateTime.now());
        chat.setChatRoomId(Long.parseLong(chatRoomId));

        chat.setMemberId(memberService.findIdByToken(principal));

        template.convertAndSend(CHAT_EXCHANGE_NAME, "room." + chatRoomId, chat);
        //template.convertAndSend("room." + chatRoomId, chat); //queue
        //template.convertAndSend("amq.topic", "room." + chatRoomId, chat); //topic
    }
}

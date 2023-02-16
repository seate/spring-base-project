package com.example.ExSite.Chatting.Controller;

import com.example.ExSite.Chatting.Service.ChatService;
import com.example.ExSite.Chatting.domain.Chat;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class Receiver {

    private final ChatService chatService;
    private CountDownLatch latch = new CountDownLatch(1);

    public Receiver(ChatService chatService) {
        this.chatService = chatService;
    }

    public void receiveMessage(Chat chat) {
        chatService.saveChatting(chat);
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    /*//receive()는 단순히 큐에 들어온 메세지를 소비만 한다. (현재는 디버그용도)
    private final static String CHAT_QUEUE_NAME = "chat.queue";
    @RabbitListener(queues = CHAT_QUEUE_NAME)
    public void receive(final Message message) {
        //System.out.println("received: " + chat.getMessage());
        System.out.println(message);
    }*/
}

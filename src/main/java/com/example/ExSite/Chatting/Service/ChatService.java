package com.example.ExSite.Chatting.Service;

import com.example.ExSite.Chatting.Repository.ChatRepository;
import com.example.ExSite.Chatting.domain.Chat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ChatService {
    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }



    //CREATE

    public void saveChatting(Chat chat) {
        chatRepository.saveChat(chat);
    }

    //DELETE

    public void deleteByChatRoomId(long chatRoomId) {
        chatRepository.deleteChatsByChatRoomId(chatRoomId);
    }

    //READ

    public List<Chat> findByChatRoomId(long chatRoomId) {
        return chatRepository.findByChatRoomId(chatRoomId);
    }


    //UPDATE X
}

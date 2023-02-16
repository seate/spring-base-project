package com.example.ExSite.Chatting.Repository;


import com.example.ExSite.Chatting.domain.Chat;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ChatRepository {
    private final EntityManager em;

    public ChatRepository(EntityManager em) {
        this.em = em;
    }

    //CREATE
    public void saveChat(Chat chat) {
        em.persist(chat);
    }

    //DELETE
    /*public void deleteChat(Chat chat) {
        findById(chat.getId()).ifPresent(chat1 -> em.remove(chat1));
    }*/

    public void deleteChatsByChatRoomId(long chatRoomId) {
        em.createQuery("delete from Chat where chatRoomId = :chatRoomId")
                .setParameter("chatRoomId", chatRoomId);
    }


    //READ
    /*public Optional<Chat> findById(long chatId) {
        Chat chat = em.find(Chat.class, chatId);
        return Optional.ofNullable(chat);
    }*/

    public List<Chat> findByChatRoomId(long chatRoomId) {
        return em.createQuery("select c from Chat c where c.chatRoomId = :chatRoomId", Chat.class)
                .setParameter("chatRoomId", chatRoomId)
                .getResultList();
    }

    //UPDATE X
}

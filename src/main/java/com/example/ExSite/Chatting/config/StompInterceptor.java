package com.example.ExSite.Chatting.config;

import com.example.ExSite.Member.domain.GeneralMember;
import com.example.ExSite.Member.domain.Member;
import com.example.ExSite.Study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StompInterceptor implements ChannelInterceptor {

    private final StudyService studyService;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.SEND.equals(accessor.getCommand()) || StompCommand.SUBSCRIBE.equals(accessor.getCommand())){
            if (!validate(accessor)){
                //TODO 거부 로직 짜야함
                throw new RuntimeException("이 study에 포함되지 않은 member입니다.");
            }
        }

        return ChannelInterceptor.super.preSend(message, channel);
    }

    public boolean validate(StompHeaderAccessor accessor) {
        String destination = accessor.getDestination();
        long studyId = Long.parseLong(destination.substring(destination.lastIndexOf('.') + 1, destination.length()));

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) accessor.getUser();
        Member member = ((GeneralMember) token.getPrincipal()).getMember();

        return studyService.isContained(studyId, member.getId());
    }
}

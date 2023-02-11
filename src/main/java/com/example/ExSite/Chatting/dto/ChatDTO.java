package com.example.ExSite.Chatting.dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ChatDTO {
    private Long id;
    private Long chatRoomId;
    private Long memberId;

    private String nickname;
    private String message;
    private String region;

    /*@JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDateTime regDate;*/

}

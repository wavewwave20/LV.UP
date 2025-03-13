package com.alltogether.lvupbackend.smalltalk.user.websocket.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WebSocketMessageEvent {
    private final Integer userId;
    private final String message;
}
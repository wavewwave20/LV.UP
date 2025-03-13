package com.alltogether.lvupbackend.smalltalk.user.websocket.event;

public class WebSocketDisconnectEvent {
    private final Integer userId;

    public WebSocketDisconnectEvent(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }
} 
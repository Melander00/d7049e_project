package com.example.brainslop.core.messages;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Owns event dispatch via the publish/subscribe pattern.
 * Senders publish messages without knowing who receives them.
 * Receivers subscribe to message types without knowing who sends them.
 * MessageManager is the only connection between the two.
 */

public class MessageManager {

    private final Map<MessageType, List<MessageListener>> listeners;

    public MessageManager() {
        this.listeners = new EnumMap<>(MessageType.class);
        for (MessageType type : MessageType.values()) {
            this.listeners.put(type, new ArrayList<>());
        }
    }

    public void subscribe(MessageType type, MessageListener listener) {
        listeners.get(type).add(listener);
    }

    public void unsubscribe(MessageType type, MessageListener listener) {
        listeners.get(type).remove(listener);
    }

    public void sendMessage(Message message) {

//        System.out.println(message);

        for (MessageListener listener : listeners.get(message.getType())) {
            listener.onMessage(message);

        }
    }

}

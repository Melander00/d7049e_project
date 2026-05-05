package com.example.brainslop.core.messages;
/**
 * Marker interface for all engine messages.
 * Implement this to define a new message type.
 * MessageManager uses getType() to route messages to the correct subscribers.
 */
public interface Message {
    MessageType getType();
}

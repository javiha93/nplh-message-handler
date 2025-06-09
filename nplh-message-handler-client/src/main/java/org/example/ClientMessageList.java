package org.example;

import lombok.NoArgsConstructor;
import org.example.domain.host.ClientMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
public class ClientMessageList {
    static final Logger logger = LoggerFactory.getLogger(ClientMessageList.class);
    List<ClientMessage> messageList = new ArrayList<>();

    public void add(ClientMessage clientMessage) {
        messageList.removeIf(addedClientMessage -> Objects.equals(addedClientMessage.controlId, clientMessage.controlId));
        this.messageList.add(clientMessage);
    }

    public ClientMessage getMessageByControlId(String messageControlID) {
        for (ClientMessage message : messageList) {
            if (message.getMessage().contains(messageControlID)) {
                return message;
            }
        }
        logger.error("Not found any message with the following control Id {}", messageControlID);
        return null;
    }
}


package com.example.watchex.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;

import java.io.Serializable;

@Getter
@Setter
public class MessageEntity implements Serializable {
    private static final long serialVersionUID = 5498689480625016163L;
    private String field;
    private int code;
    private String message;
    private Object data;
    private MessageType type;
    private HttpHeaders httpHeaders;

    public MessageEntity(int code,String message) {
        this.code = code;
        this.message = message;
        if(code == 400){
            this.type = MessageType.ERROR;
        }
    }

    public MessageEntity(int code,Object data) {
        this.code = code;
        this.data = data;
        if(code == 200) {
            this.type = MessageType.SUCCESS;
        } else {
            this.type = MessageType.ERROR;
        }
    }

    public MessageEntity(String field, String message, MessageType type) {
        this.field = field;
        this.message = message;
        this.type = type;
    }

    public MessageEntity(String field, int code, String message, MessageType type) {
        this.field = field;
        this.code = code;
        this.message = message;
        this.type = type;
    }

    public MessageEntity(String field, String message, HttpHeaders httpHeaders, MessageType type) {
        this.field = field;
        this.message = message;
        this.type = type;
        this.httpHeaders = httpHeaders;
    }

}

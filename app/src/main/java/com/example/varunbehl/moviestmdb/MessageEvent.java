package com.example.varunbehl.moviestmdb;

/**
 * Created by varunbehl on 24/02/17.
 */

public class MessageEvent {
    private int request;

    public MessageEvent(int request) {
        this.request = request;
    }

    public int getRequest() {
        return request;
    }

    public void setRequest(int request) {
        this.request = request;
    }
}

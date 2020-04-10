package com.zch.eventbusapp;

public class StickyMessageEvent {

    public final String message;

    public StickyMessageEvent(String message) {
        this.message=message;
    }
}

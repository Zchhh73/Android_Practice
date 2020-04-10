package com.zch.eventbusapp;

public class MainOrderedEvent {
    public final String threadInfo;

    public MainOrderedEvent(String threadInfo) {
        this.threadInfo=threadInfo;
    }
}

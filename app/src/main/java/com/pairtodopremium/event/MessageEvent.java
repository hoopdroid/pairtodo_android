package com.pairtodopremium.event;

public class MessageEvent implements IEvent {
  private String message;

  public MessageEvent(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}

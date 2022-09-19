package com.higo1974j.aos.checkout.view;

public interface BaseView {

  public void display();

  public void submit();

  public String getTitle();

  public default boolean canHandle(String title) {
    if (title == null || title.isEmpty()) {
      return false;
    } else {
      return title.startsWith(getTitle());
    }
  }

  public default boolean isTerminated() {
    return false;
  }

  public default void execute() {
    ;
  }
}

package com.higo1974j.aos.checkout.view;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import com.higo1974j.utils.SeleniumWrapper;

public class Bag implements BaseView {

  private static final String TITLE = "バッグの内容を確認してください。";
  
  private SeleniumWrapper wrapper;

  public Bag(SeleniumWrapper wrapper) {
    this.wrapper = wrapper;
  }
  public void display() {
    wrapper.waitH1(TITLE);
  }
  public String getTitle() {
    return TITLE;
  }

  public boolean canHandle(String title) {
    if (title == null || title.isEmpty()) {
      return false;
    } else {
      return title.startsWith(getTitle());
    }
  }

  public void submit() {
    WebElement checkoutButton = wrapper.findElementAndTimer(By.id("shoppingCart.actions.checkout"));
    if (checkoutButton != null) {
      wrapper.waitClickableAndClick(checkoutButton, 3);
    }
  }
  
  public void execute() {
    display();
    submit();
  }
}

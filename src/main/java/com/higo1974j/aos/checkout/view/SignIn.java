package com.higo1974j.aos.checkout.view;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.higo1974j.aos.checkout.Config;
import com.higo1974j.utils.SeleniumWrapper;

public class SignIn implements BaseView {
  private static final String TITLE = "サインインで購入がよりスムーズに。";

  private SeleniumWrapper wrapper;

  private static String applePassword = Config.getInstance().getProperty("apple.password");

  public SignIn(SeleniumWrapper wrapper) {
    this.wrapper = wrapper;
  }
  public void display() {
    wrapper.waitH1(TITLE);
  }
  public String getTitle() {
    return TITLE;
  }

  public void setUserIdAndPassword(String userId, String password) {
    WebElement iframe = wrapper.findElementAndTimer(By.id("aid-auth-widget-iFrame"));
    if (iframe != null) {
      wrapper.switchToFrame(iframe);
      wrapper.waitVisibility(By.id("account_name_text_field"), 10);
      wrapper.waitVisibility(By.id("password_text_field"), 10);
      wrapper.waitClickable(By.id("sign-in"), 10);
      //WebElement userId = driver.findElement(By.id("account_name_text_field"));
      //userId.sendKeys(APPLE_ID);
      WebElement passwordElement = wrapper.findElementAndTimer(By.id("password_text_field"));
      passwordElement.sendKeys(password);
    }
  }

  public void submit() {
    WebElement button = wrapper.findElementAndTimer(By.id("sign-in"));
    wrapper.waitClickableAndClick(button, 5);
    wrapper.switchToParentFrame();
  }

  public void execute() {
    display();
    setUserIdAndPassword(null, applePassword);
    submit(); 
  }
}

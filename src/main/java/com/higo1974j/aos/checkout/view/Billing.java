package com.higo1974j.aos.checkout.view;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.higo1974j.aos.checkout.Config;
import com.higo1974j.utils.SeleniumWrapper;
import lombok.extern.slf4j.Slf4j;

//https://secure2.store.apple.com/jp/shop/checkout?_s=Billing
//ご希望の支払い方法は？
@Slf4j
public class Billing implements BaseView {
  private static final String TITLE = "ご希望の支払い方法は？";

  private String securityCode = Config.getInstance().getProperty("security.code");

  private SeleniumWrapper wrapper;

  public Billing(SeleniumWrapper wrapper) {
    this.wrapper = wrapper;
  }
  public void display() {
    wrapper.waitH1(TITLE);
  }

  public String getTitle() {
    return TITLE;
  }

  public void selectPayment() {
    // 一般的な支払い方法の１番上
    //<input
    // class="form-selector-input"
    // name="checkout.billing.billingOptions.selectBillingOption"
    // id="checkout.billing.billingoptions.saved_card"
    // type="radio"
    // aria-labelledby="checkout.billing.billingoptions.saved_card_label"
    // data-autom="checkout-billingOptions-SAVED_CARD"
    // aria-describedby="checkout-billingOptions-list-savedCard
    //<label
    //for="checkout.billing.billingoptions.saved_card"
    //class="form-selector-label"
    //id="checkout.billing.billingoptions.saved_card_label">

    WebElement radio = wrapper.findElementAndTimer("input", "data-autom", "checkout-billingOptions-SAVED_CARD");
    WebElement label = wrapper.findElementAndTimer("label", "for", radio.getAttribute("id"));
    WebElement cvv = null;
    if (label.isSelected()) {
      try {
        cvv = wrapper.findElementStartsWith("input", "id", "checkout.billing.billingOptions.selectedBillingOptions.savedCard.cardInputs.cardInput", 1); 
      } catch (RuntimeException ex) {
        ;
      }
    } else {
      wrapper.waitClickableAndClick(label, 3);
      wrapper.waitVisibility(By.className("form-tooltip-after"), 3);
      cvv = wrapper.findElementStartsWithAndTimer("input", "id", "checkout.billing.billingOptions.selectedBillingOptions.savedCard.cardInputs.cardInput"); 
    }
    // セキュリティコード
    //<input id="checkout.billing.billingOptions.selectedBillingOptions.savedCard.cardInputs.cardInput-0.securityCode"
    if (cvv != null) {
      String value = cvv.getAttribute("value");
      log.debug("value={}", value);
      if (value == null || value.isEmpty()) {
        cvv.clear();
        cvv.sendKeys(Keys.CONTROL + "a");
        cvv.sendKeys(Keys.DELETE);
        cvv.sendKeys(securityCode);
        //cvv.sendKeys("999");
      }
    }
  }

  public void submit() {
    WebElement confirmButton = wrapper.findElementAndTimer(By.id("rs-checkout-continue-button-bottom"));
    if (confirmButton != null) {
      wrapper.waitClickableAndClick(confirmButton, 10);
    }
  }

  public void execute() {
    log.info("Billing start");
    display();
    selectPayment();
    submit();
    log.info("Billing end"); 
  }
}

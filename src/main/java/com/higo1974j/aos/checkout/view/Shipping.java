package com.higo1974j.aos.checkout.view;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import com.higo1974j.utils.SeleniumWrapper;
import lombok.extern.slf4j.Slf4j;

//ご注文の商品の配送先を教えてください。
//https://secure2.store.apple.com/jp/shop/checkout?_s=Shipping
@Slf4j
public class Shipping implements BaseView {
  private static final String TITLE = "ご注文の商品の配送先を教えてください。";

  private static final int WAIT_SEC = 10;

  private SeleniumWrapper wrapper;

  public Shipping(SeleniumWrapper wrapper) {
    this.wrapper = wrapper;
  }
  public void display() {
    wrapper.waitH1(TITLE);
  }
  public String getTitle() {
    return TITLE;
  }
  public void setMobilePhoneNumber(String mobilePhoneNumber) {
    //<input
    // id="checkout.shipping.addressContactPhone.address.mobilePhone"
    // name="mobilePhone"
    // type="tel" class="form-textbox-input"
    // aria-labelledby="checkout.shipping.addressContactPhone.address.mobilePhone_label"
    // aria-describedby="checkout.shipping.addressContactPhone.address.mobilePhone_error "
    // aria-invalid="false" maxlength="14" required=""
    // aria-required="true" autocomplete="tel-local"
    // data-autom="form-field-mobilePhone" value="">
    WebElement mobilePhoneNumberElement = wrapper.findElementStartsWithAndTimer("input", "data-autom", "form-field-mobilePhone");
    if (mobilePhoneNumberElement != null) {
      String value = mobilePhoneNumberElement.getAttribute("value");
      log.debug("value={}", value);
      if (value == null || value.isEmpty()) {
        mobilePhoneNumberElement.clear();
        mobilePhoneNumberElement.sendKeys(Keys.CONTROL + "a");
        mobilePhoneNumberElement.sendKeys(Keys.DELETE);
        mobilePhoneNumberElement.sendKeys(mobilePhoneNumber);
      }
    }
  }

  //<button id="rs-checkout-continue-button-bottom"
  // type="button" class="form-button"
  // data-analytics-title="false"
  // data-autom="shipping-continue-button">
  public void submit() {
    WebElement addressVerificationButton = wrapper.findElementAndTimer("button", "data-autom", "shipping-continue-button");
    wrapper.waitClickableAndClick(addressVerificationButton, WAIT_SEC);
  }

  public void execute() {
    log.info("Shipping start");
    display();
    setMobilePhoneNumber("09064763189");
    submit();
    log.info("Shipping end");
  }
}

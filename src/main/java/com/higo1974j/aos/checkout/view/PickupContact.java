package com.higo1974j.aos.checkout.view;

import org.openqa.selenium.WebElement;

import com.higo1974j.aos.checkout.Config;
import com.higo1974j.utils.SeleniumWrapper;
import lombok.extern.slf4j.Slf4j;

//受け取りに関する情報を入力してください。
//https://secure2.store.apple.com/jp/shop/checkout?_s=PickupContact
@Slf4j
public class PickupContact implements BaseView {

  private static final String TITLE = "受け取りに関する情報を入力してください。";
  private SeleniumWrapper wrapper;

  private String mobileTelephoneNUmber = Config.getInstance().getProperty("mobile.telephone.number");
  
  public PickupContact(SeleniumWrapper wrapper) {
    this.wrapper = wrapper;
  }

  public void display() {
    wrapper.waitH1(TITLE);
  }
  public String getTitle() {
    return TITLE;
  }
  
  public void setMobilePhoneNumber(String mobilePhoneNumber) {
    // <input
    // id="checkout.pickupContact.selfPickupContact.selfContact.address.mobilePhone"
    // name="mobilePhone" type="tel" class="form-textbox-input"
    // aria-labelledby="checkout.pickupContact.selfPickupContact.selfContact.address.mobilePhone_label"
    // aria-describedby="checkout.pickupContact.selfPickupContact.selfContact.address.mobilePhone_error "
    // aria-invalid="true" maxlength="14" required="" aria-required="true"
    // autocomplete="tel-local"
    //data-autom="form-field-mobilePhone" value="">
    WebElement mobilePhoneNumberElement = wrapper.findElementStartsWithAndTimer("input", "data-autom", "form-field-mobilePhone");
    if (mobilePhoneNumberElement != null) {
      mobilePhoneNumberElement.clear();
      mobilePhoneNumberElement.sendKeys(mobilePhoneNumber);
    } 
  }

  // rs-checkout-continue-button-bottom click
  //<button
  // id="rs-checkout-continue-button-bottom"
  // type="button"
  // class="form-button"
  // aria-describedby=""
  // data-analytics-title="false"
  // data-autom="continue-button-label">
  // <span><span>続ける</span></span>
  //</button>
  public void submit() {
    // 続けるクリック
    WebElement continueButton = wrapper.findElementStartsWithAndTimer("button", "data-autom", "continue-button-label");
    if (continueButton != null) {
      wrapper.waitClickableAndClick(continueButton, 10);
    }
  }
  
  public void execute() {
    log.info("Pickup start");
    display();
    setMobilePhoneNumber(mobileTelephoneNUmber);
    submit();
    log.info("Pickup end");
  }
}

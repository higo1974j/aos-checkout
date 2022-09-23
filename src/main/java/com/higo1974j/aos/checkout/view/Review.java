package com.higo1974j.aos.checkout.view;

import org.openqa.selenium.WebElement;
import com.higo1974j.utils.SeleniumWrapper;
import lombok.extern.slf4j.Slf4j;

//ご注文の準備は整いましたか？
//すべてが正しいことを確認しましょう。
@Slf4j
public class Review implements BaseView {
  private static final String TITLE = "ご注文の準備は整いましたか？";
  private SeleniumWrapper wrapper;

  private boolean terminated = false;

  public Review(SeleniumWrapper wrapper) {
    this.wrapper = wrapper;
  }
  public void display() {
    wrapper.waitH1(TITLE);
  } 
  public String getTitle() {
    return TITLE;
  }

  //<button
  // id="rs-checkout-continue-button-bottom" type="button"
  // class="form-button" aria-describedby="rs-checkout-continuedisclaimer-bottom"
  // data-analytics-title="false" data-autom="continue-button-label"><span><span>注文する</span></span></button>
  public void submit() {
    WebElement continueButton = wrapper.findElementStartsWithAndTimer("button", "data-autom", "continue-button-label");
    if (continueButton != null) {
      wrapper.waitClickableAndClick(continueButton, 10);
      terminated = true;
    }
  }

  //<button id="checkout.review.fulfillmentReview.reviewGroup-1.edit"
  //class="as-buttonlink more rs-review-change"
  // type="button"
  // role="link"
  // aria-labelledby="checkout.review.fulfillmentReview.reviewGroup-1.edit checkout.review.fulfillmentReview.reviewGroup-1-shipquote"
  // data-autom="changeShippingMethod">ストアを変更</button>
  public void changeShippingMethod() {
    WebElement changeDeliveryButton = wrapper.findElementAndTimer("button", "data-autom", "changeShippingMethod"); 
    if (changeDeliveryButton != null) {
      wrapper.waitClickableAndClick(changeDeliveryButton, 5);
    }
  }

  public void execute() {
    log.info("Review start");
    display();
    submit();
    //terminated = true;
    log.info("Review end");
  }

  public boolean isTerminated() {
    return terminated;
  }
}

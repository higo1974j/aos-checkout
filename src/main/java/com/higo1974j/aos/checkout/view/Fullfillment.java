package com.higo1974j.aos.checkout.view;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import com.higo1974j.utils.SeleniumWrapper;
import lombok.extern.slf4j.Slf4j;

//ご希望の受け取り方法は？
//https://secure2.store.apple.com/jp/shop/checkout?_s=Fulfillment
@Slf4j
public class Fullfillment implements BaseView  {

  private static final String TITLE = "ご希望の受け取り方法は？";

  private SeleniumWrapper wrapper;

  private static final int WAIT_SEC = 10;

  private static final int WAIT_RETRY = 3;

  public Fullfillment(SeleniumWrapper wrapper) {
    this.wrapper = wrapper;
  }

  public void display() {
    wrapper.waitH1(TITLE);
  }

  public String getTitle() {
    return TITLE;
  }

  // ご希望の受け取り方法は
  // 配送を希望するをクリック
  public void selectDelivery() {
    WebElement labelHopeDelivery = wrapper.findElementAndTimer("label", "for", "fulfillmentOptionButtonGroup0");
    if (labelHopeDelivery != null) {
      wrapper.waitClickableAndClick(labelHopeDelivery, WAIT_SEC);

      // うまく動かない
      // Appleの配送ポリシーを見る
      //<a href="https://www.apple.com/jp/shop/help/shipping_delivery"
      // data-slot-name="globalNav"
      // data-feature-name="Astro Link"
      // data-display-name="AOS: help/shipping_delivery"
      // data-prop37="AOS:Checkout/Fulfillment||Shipping|policy selected"
      // target="_blank">Appleの配送ポリシーを見る
      wrapper.findElementAndTimer("a", "href", "https://www.apple.com/jp/shop/help/shipping_delivery");
      //wrapper.waitClickable(policyElement, WAIT_SEC);

    }
  }

  // ご希望の受け取り方法は
  // お客様がご自身で受け取るを選択
  public void  selectPickup() {
    WebElement labelHopeDelivery = wrapper.findElementStartsWithAndTimer("label", "for", "fulfillmentOptionButtonGroup1");
    if (labelHopeDelivery != null) {
      wrapper.waitClickableAndClick(labelHopeDelivery, WAIT_RETRY);
    }
  }


  //付近のストアを表示：をクリック
  public void selectLocalStore() {
    try {
      WebElement localStore = wrapper.findElementStartsWithAndTimer("button", "data-autom", "fulfillment-pickup-store-search-button"); 
      if (localStore != null) {
        wrapper.waitClickableAndClick(localStore, WAIT_SEC);
      }
    } catch (Exception ex) {
      ;
    }
  }

  public void waitShowingExtendedStore() {
    WebElement displayMoreShops = wrapper.findElementStartsWithAndTimer("button", "data-autom", "show-more-stores-button");
    wrapper.waitClickable(displayMoreShops, WAIT_RETRY);
  }



  //適応クリック
  public void setZipAndClick(String zipCode) {
    //<button type="button" class="form-textbox-button" id="checkout.fulfillment.pickupTab.pickup.storeLocator.search" data-autom="checkout-cityState-適用">
    WebElement applyButton = wrapper.findElementStartsWithAndTimer("button", "data-autom", "checkout-cityState"); 
    //<input id="checkout.fulfillment.pickupTab.pickup.storeLocator.searchInput" type="text" class="form-textbox-input form-textbox-entered" aria-labelledby="checkout.fulfillment.pickupTab.pickup.storeLocator.searchInput_label" aria-describedby="checkout.fulfillment.pickupTab.pickup.storeLocator.searchInput_error " aria-invalid="false" required="" aria-required="true" autocomplete="off" data-autom="bag-storelocator-input" value="162-0066">
    WebElement inputZip = wrapper.findElementStartsWithAndTimer("input", "data-autom", "bag-storelocator-input");
    if (inputZip != null) {
      inputZip.clear();
      inputZip.sendKeys(zipCode);
    }
    if (applyButton != null) {
      applyButton.click();
    }
  }

  public void waitApplying() {
    // invisible appy button 
    WebElement applyButton = wrapper.findElementStartsWithAndTimer("button", "data-autom", "checkout-cityState");
    wrapper.waitInvisibility(applyButton, WAIT_SEC);

    // visible zip-code
    WebElement localStore = wrapper.findElementStartsWithAndTimer("button", "data-autom", "fulfillment-pickup-store-search-button"); 
    wrapper.waitVisibility(localStore, WAIT_SEC);
  }


  public boolean getYourOrderLabelClickOneToThree() {
    {
      // select bottom label
      WebElement placeLabel = null;
      WebElement placeButton = null;

      for (int i=1; i<=2; i++) {
        placeButton = wrapper.findElementAndTimer("input", "class", "form-selector-input", i);
        placeLabel = wrapper.findElementAndTimer("label", "class", "form-selector-label", i);
        if (placeLabel == null) {
          continue;
        }

        String text = placeLabel.getText().replaceAll("(\n|\r)", " ");
        //if (placeButton.isEnabled()) {
          log.info("{},{}", placeButton.isEnabled(), text);
        //}
        try {
          if (placeButton.isEnabled() && placeLabel.isEnabled()) {
            wrapper.waitClickableAndClick(placeLabel, 10);
            return true;
          }
        } catch (Exception e) {
          ;
        }
      }
      return false;
    }
  }


  public void getYourOrderExtendMap() {
    //<button type="button" class="as-buttonlink rt-storelocator-map-expand-button">マップを広げる
    //<span class="icon icon-after icon-chevrondown" aria-hidden="true"></span></button>
    WebElement expandMap = wrapper.findElementStartsWithAndTimer("button", "class", "as-buttonlink rt-storelocator-map-expand-button");
    if (expandMap != null) {
      wrapper.waitClickableAndClick(expandMap, 10);
    }
  }

  public void selectTimeSlot() {
    //<select
    // id="checkout.fulfillment.pickupTab.pickup.timeSlot.dateTimeSlots.timeSlotValue"
    // class="form-dropdown-select form-dropdown-selectnone"
    // aria-labelledby="checkout.fulfillment.pickupTab.pickup.timeSlot.dateTimeSlots.timeSlotValue_label"
    // aria-describedby="checkout.fulfillment.pickupTab.pickup.timeSlot.dateTimeSlots.timeSlotValue_error"
    // data-autom="pickup-availablewindow-dropdown"><option value="">ご利用可能な時間帯</option><option value="3-16:45-17:00">16:45 – 17:00</option><option value="3-17:15-17:30">17:15 – 17:30</option><option value="3-17:30-17:45">17:30 – 17:45</option><option value="3-17:45-18:00">17:45 – 18:00</option><option value="3-18:00-18:15">18:00 – 18:15</option><option value="3-18:15-18:30">18:15 – 18:30</option><option value="3-18:45-19:00">18:45 – 19:00</option><option value="3-19:00-19:15">19:00 – 19:15</option><option value="3-19:15-19:30">19:15 – 19:30</option><option value="3-19:30-19:45">19:30 – 19:45</option><option value="3-19:45-20:00">19:45 – 20:00</option></select>
    WebElement selectElement =  wrapper.findElementAndTimer("select", "data-autom", "pickup-availablewindow-dropdown");
    wrapper.waitVisibility(selectElement, WAIT_SEC);
    Select selectObject = new Select(selectElement);
    selectObject.selectByIndex(selectObject.getOptions().size()-1);

    //<input
    // type="radio"
    // id="bartPickupDateSelector3"
    // name="bartPickupDateSelectorButtonGroup"
    // class="form-selector-input as-buttongroup-radio"
    //value="3" checked=""> 
  }

  //<button
  // id="rs-checkout-continue-button-bottom"
  // type="button" class="form-button"
  // data-analytics-title="false"
  // data-autom="fulfillment-continue-button"
  public void submit() {
    WebElement continueButton = wrapper.findElementStartsWithAndTimer("button", "data-autom", "fulfillment-continue-button");
    if (continueButton != null) {
      wrapper.waitClickableAndClick(continueButton, 10);
    }
  }

  public void execute() {
    log.info("Fullfillment start");
    display();
    selectDelivery();
    submit();
    log.info("Fullfillment end");
  }
}

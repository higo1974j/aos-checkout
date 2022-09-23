package com.higo1974j.aos.checkout.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import com.higo1974j.aos.checkout.Config;
import com.higo1974j.aos.checkout.view.Bag;
import com.higo1974j.aos.checkout.view.BaseView;
import com.higo1974j.aos.checkout.view.Billing;
import com.higo1974j.aos.checkout.view.Fullfillment;
import com.higo1974j.aos.checkout.view.PickupContact;
import com.higo1974j.aos.checkout.view.Review;
import com.higo1974j.aos.checkout.view.Shipping;
import com.higo1974j.aos.checkout.view.SignIn;
import com.higo1974j.utils.SeleniumWrapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckoutController {
  private SeleniumWrapper wrapper;

  private Bag bag;

  private SignIn signIn;

  private PickupContact picupContact;

  private Review review;

  private Fullfillment fullfillment;

  private Billing billing;

  private Shipping shipping;

  private static final int MAX_EXCEPTION_COUNT = 1;

  private static final int MAX_RESET_COUNT = 2;

  private ViewHelper helper = null;

  private String currentH1 = null;

  protected void preparePurchase() {
    try {
      // バッグへ
      wrapper.get("https://www.apple.com/jp/shop/bag");

      //wrapper.waitH1("バッグの内容を確認してください。");
      bag.execute();

      //wrapper.waitH1( "サインインで購入がよりスムーズに。");
      signIn.execute();

      setFullfillment();

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  protected boolean resetFullfillment() {
    try {
      wrapper.get("https://secure2.store.apple.com/jp/shop/checkout?_s=Fulfillment-init");
      setFullfillment();
      return true;
    } catch (Exception ex) {
      log.error("Reset failed. Try to reset again", ex);
    }
    return false;
  }

  private void setFullfillment() {
    //wrapper.waitH1( "ご希望の受け取り方法は？");
    fullfillment.execute();

    //wrapper.waitH1("ご注文の商品の配送先を教えてください。");
    shipping.execute();

    try {
      //wrapper.waitH1("ご希望の支払い方法は？");
      billing.execute();
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    // ご注文の準備は整いましたか？
    //wrapper.waitH1("ご注文の準備は整いましたか？");
    review.display();
    review.changeShippingMethod();
  }

  protected boolean fullfillmentLoop() {

    boolean preException = false;
    int exceptionCount = 0;
    int failResetCount = 0;
    while (true) {
      String storeZipCode = Config.getInstance().getProperty("store.zip.code");
      try {
        //"ご希望の受け取り方法は？"
        fullfillment.display();
        fullfillment.selectPickup();
        fullfillment.selectLocalStore();
        fullfillment.waitShowingExtendedStore();
        fullfillment.setZipAndClick(storeZipCode);
        boolean click = fullfillment.getYourOrderLabelClickOneToThree();
        preException = false;
        if (click) {
          fullfillment.selectTimeSlot();
          currentH1 = helper.getH1(); 
          log.info("h1={}", currentH1);
          fullfillment.submit();
          return true;
        }
      } catch (Exception ex) {
        log.error("loop error", ex);
        if (preException == true) {
          exceptionCount++;
        }
        preException = true;
        if (exceptionCount > MAX_EXCEPTION_COUNT) {
          log.info("Reset fullfillment!!");
          if (resetFullfillment()) {
            exceptionCount = 0;
            preException = false;
            log.info("Resetting is done");
          } else {
            failResetCount++;
          }
          if (failResetCount >= MAX_RESET_COUNT) {
            exceptionCount = 0;
            preException = false;
            failResetCount = 0;
            return false;
          }
        }
      }
      try {
        Thread.sleep(5*1000);
      } catch(Exception ex) {
        ;
      }
    }
  }

  protected void purchase() {
    for (int i=0; i < 20; i++) {
      boolean result = helper.monitoringH1(currentH1, 10, 100);
      if (result) {
        String newH1 = helper.getH1();
        log.info("h1={}", newH1);
        for (BaseView view : Arrays.asList(picupContact, review, billing)) {
          log.info("view={}", view);
          log.info("view.canHandle={}", view.canHandle(newH1));
          if (view.canHandle(newH1)) {
            currentH1 = newH1;
            view.execute();
            break;
          }
        }
        if (review.isTerminated()) {
          return;
        }
      }
    }
    throw new RuntimeException("max retry");
  }


  public void init(SeleniumWrapper wrapper) {
    this.wrapper = wrapper;
    bag = new Bag(wrapper);
    picupContact = new PickupContact(wrapper);
    review = new Review(wrapper);
    fullfillment = new Fullfillment(wrapper);
    billing = new Billing(wrapper);
    shipping = new Shipping(wrapper);
    signIn = new SignIn(wrapper);
    helper = new ViewHelper(wrapper);
  }

  protected void moveFile(File src) throws IOException{
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    File emptyFile = new File("capture-" + sdf.format(new Date()) + ".png");
    Path dstPath = Paths.get(emptyFile.toURI());
    Path srcPath = Paths.get(src.toURI());
    Files.move(srcPath, dstPath, StandardCopyOption.REPLACE_EXISTING);

  }
  public boolean execute() {
    preparePurchase();
    boolean purchase = false;
    while (!purchase) {
      boolean loopResult = fullfillmentLoop();
      if (!loopResult) {
        return false;
      }
      try {
        purchase();
        purchase = true;
        {
          File tempFile = wrapper.getScreenShopAsFile();
          Thread.sleep(10 * 1000);
          moveFile(tempFile);
        }

        {
          File tempFile = wrapper.getScreenShopAsFile();
          Thread.sleep(20 * 1000);
          moveFile(tempFile);
        }

      } catch (Exception ex) {
        log.error("purchase error", ex);
      }
    }
    return true;
  }

  public void terminate() {
    wrapper.quit();
  }

}

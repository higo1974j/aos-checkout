package com.higo1974j.aos.checkout.controller;

import org.openqa.selenium.By;
import com.higo1974j.utils.SeleniumWrapper;

public class ViewHelper {
  private SeleniumWrapper wrapper;

  public ViewHelper(SeleniumWrapper wrapper) {
    this.wrapper = wrapper;
  }

  public boolean monitoringH1(String h1, int maxCount, int termMills) {
    for (int i=0; i< maxCount; i++) {
      String tempH1 = wrapper.findElement(By.tagName("h1")).getText();
      if (tempH1!= null && !tempH1.equals(h1)) {
        return true;
      }
      try {
        Thread.sleep(termMills);
      } catch (InterruptedException ex) {
        ;
      }
    }
    return false;
  }

  public String getH1() {
    return wrapper.findElement(By.tagName("h1")).getText();
  }
}

package com.higo1974j.utils;

import java.io.File;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumWrapper {

  private static final int WAIT_RETRY = 5;

  private static final int WAIT_VIS_RETRY = 3;

  private static final int WAIT_INVIS_RETRY = 3;

  private static final int WAIT_CLICK_RETRY = 3;

  private WebDriver driver;

  public SeleniumWrapper(WebDriver webDriver) {
    this.driver = webDriver;
  }

  public WebElement findElement(By locator) {
    return driver.findElement(locator);
  }

  public WebElement findElement(String tag, String attrName, String attrValue) {
    return findElement(tag, attrName, attrValue, 1);
  }

  public WebElement findElement(String tag, String attrName, String attrValue, int searchIndex) {
    for (int retry=0; retry < WAIT_RETRY; retry++) {
      try {
        int index = 1;
        for (WebElement element : driver.findElements(By.tagName(tag))) {
          String value = element.getAttribute(attrName);
          //log.debug("{},{},{}", tag, attrName, value);
          if (value != null && value.equals(attrValue)) {
            if (index == searchIndex) {
              return element;
            }
            index++;
          }
        }
      } catch(Exception ex) {
        ;
      }
    } 
    return null;
  }

  public WebElement findElementAndTimer(By locator) {
    (new WebDriverWait(driver, Duration.ofSeconds(1))).until(new ExpectedCondition<WebElement>() {
      public WebElement apply(WebDriver driver) {
        return driver.findElement(locator);
      }
    });
    return driver.findElement(locator);
  }

  public WebElement findElementAndTimer(String tag, String attrName, String attrValue) {
    return findElementAndTimer(tag, attrName, attrValue, 1);
  }

  public WebElement findElementAndTimer(String tag, String attrName, String attrValue, int index) {
    (new WebDriverWait(driver, Duration.ofSeconds(1))).until(new ExpectedCondition<WebElement>() {
      public WebElement apply(WebDriver driver) {
        return findElement(tag, attrName, attrValue, index);
      }
    });
    return findElement(tag, attrName, attrValue, index);
  }

  public WebElement findElementStartsWith(String tag, String attrName, String attrValue) {
    return findElementStartsWith(tag, attrName, attrValue, 1);
  }

  public WebElement findElementStartsWith(String tag, String attrName, String attrValue, int searchIndex) {
    for (int retry=0; retry < WAIT_RETRY; retry++) {
      try {
        int index = 1;
        for (WebElement element : driver.findElements(By.tagName(tag))) {
          String value = element.getAttribute(attrName);
          if (value != null && value.startsWith(attrValue)) {
            if (index == searchIndex) {
              return element;
            }
            index++;
          }
        }
      } catch(Exception ex) {
        ;
      }
    }
    return null;
  }

  public WebElement findElementStartsWithAndTimer(String tag, String attrName, String attrValue) {
    return findElementStartsWithAndTimer(tag, attrName, attrValue, 1);
  }

  public WebElement findElementStartsWithAndTimer(String tag, String attrName, String attrValue, int index) {
    (new WebDriverWait(driver, Duration.ofSeconds(1))).until(new ExpectedCondition<WebElement>() {
      public WebElement apply(WebDriver driver) {
        return findElementStartsWith(tag, attrName, attrValue, index);
      }
    });
    return findElementStartsWith(tag, attrName, attrValue, index);
  }

  public void waitClickable(By locator, int waitSec) {
    for (int i=1; i <= WAIT_CLICK_RETRY; i++) {
      try {
        WebDriverWait wait =  new WebDriverWait(driver, Duration.ofSeconds(waitSec));
        wait.until(ExpectedConditions.elementToBeClickable(locator));
      } catch (RuntimeException ex) {
        if (i == WAIT_CLICK_RETRY) {
          throw ex;
        }
      }
    }
  }

  public void waitClickable(WebElement element, int waitSec) {
    for (int i=1; i <= WAIT_CLICK_RETRY; i++) {
      try {
        WebDriverWait wait =  new WebDriverWait(driver, Duration.ofSeconds(waitSec));
        wait.until(ExpectedConditions.elementToBeClickable(element));
      } catch (RuntimeException ex) {
        if (i == WAIT_CLICK_RETRY) {
          throw ex;
        }
      }
    }
  }


  public void waitClickableAndClick(WebElement element, int waitSec) {
    waitClickable(element, waitSec);
    element.click();
  }

  public void waitVisibility(WebElement element, int waitSec) {
    for (int i=1; i <= WAIT_VIS_RETRY; i++) {
      try {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitSec));
        wait.until(ExpectedConditions.visibilityOf(element));
      } catch (RuntimeException ex) {
        if (i == WAIT_VIS_RETRY) {
          throw ex;
        }
      }
    }
  }

  public void waitVisibility(By locator, int waitSec) {
    for (int i=1; i <= WAIT_VIS_RETRY; i++) {
      try {
        WebDriverWait wait =  new WebDriverWait(driver, Duration.ofSeconds(waitSec));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
      } catch (Exception ex) {
        if (i == WAIT_VIS_RETRY) {
          throw ex;
        }
      }
    }
  }

  public void waitInvisibility(By locator, int waitSec) {
    for (int i=1; i <= WAIT_INVIS_RETRY; i++) {
      try {
        WebDriverWait wait =  new WebDriverWait(driver, Duration.ofSeconds(waitSec));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
      } catch (Exception ex) {
        if (i == WAIT_INVIS_RETRY) {
          throw ex;
        }
      }
    }
  }

  public void waitInvisibility(WebElement element, int waitSec) {
    for (int i=1; i <= WAIT_INVIS_RETRY; i++) {
      try {
        WebDriverWait wait =  new WebDriverWait(driver, Duration.ofSeconds(waitSec));
        wait.until(ExpectedConditions.invisibilityOf(element));
      } catch (Exception ex) {
        if (i == WAIT_INVIS_RETRY) {
          throw ex;
        }
      }
    }
  }

  public void waitH1(String value) {
    (new WebDriverWait(driver, Duration.ofSeconds(10))).until(new ExpectedCondition<Boolean>() {
      public Boolean apply(WebDriver d) {
        WebElement h1 = d.findElement(By.tagName("h1"));
        return h1.getText().startsWith(value);
      }
    });
  }

  public void switchToFrame(WebElement frame) {
    driver.switchTo().frame(frame);
  }

  public void switchToParentFrame() {
    driver.switchTo().parentFrame();
  }

  public void get(String url) {
    driver.get(url);
  }

  public void quit() {
    driver.quit();
  }

  public File getScreenShopAsFile() {
    return ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
  }
}

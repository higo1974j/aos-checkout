package com.higo1974j.aos.checkout;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.higo1974j.aos.checkout.controller.CheckoutController;
import com.higo1974j.utils.SeleniumWrapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckoutExecutor {

  private Config config = Config.getInstance(); 

  public void init(String filename) throws IOException {
    config.load(filename);
  }

  public void execute() {
    boolean result = false;
    CheckoutController controller = null;
    while (!result) {
      try {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--user-data-dir=" + config.getProperty("user.data.dir"));
        options.addArguments("--profile-directory=apple");
        System.setProperty("webdriver.chrome.driver", config.getProperty("chrome.driver"));
        WebDriver driver = new ChromeDriver(options);
        SeleniumWrapper wrapper = new SeleniumWrapper(driver);
        controller = new CheckoutController();
        controller.init(wrapper);

        result = controller.execute();
      } catch (Exception ex) {
        log.error("error occured. reset all", ex);
        return;
      } finally {
        if (controller != null) {
          controller.terminate();
        }

      }
    }
  }

  public void setupLoop(WebDriver driver) {
    try {
      Thread.sleep(3600 * 1000);
    } catch (Exception ex) {

    }
  }


  public static void main(String[] args) throws Exception {
    CheckoutExecutor coItem = new CheckoutExecutor();
    coItem.init(args[0]);
    coItem.execute();
  }
}

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import support.EventHandler;
//import support.EventHandler;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class DriverManager {
  public static WebDriver getDriver(String browser) {

    switch (browser) {
      case "firefox":
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(FirefoxDriver.MARIONETTE, false);
        System.setProperty("webdriver.gecko.driver",
                //new File(DriverManager.class.getResource("/geckodriver.exe").getFile()).getPath());
                System.getProperty("user.dir")+"//drivers//geckodriver.exe");
        return new FirefoxDriver();
      case "ie":
      case "internet explorer":
        System.setProperty("webdriver.ie.driver",
                System.getProperty("user.dir")+"//drivers//IEDriverServer.exe");
        return new InternetExplorerDriver();
      case "chrome":
      default:
        System.setProperty("webdriver.chrome.driver",
//                new File(DriverManager.class.getResource("/chromedriver.exe").getFile()).getPath());
                System.getProperty("user.dir")+"//drivers//chromedriver.exe");
        return new ChromeDriver();

    }
  }
    public static EventFiringWebDriver getConfiguredDriver(String browser) {
    EventFiringWebDriver driver = new EventFiringWebDriver(getDriver(browser));
    driver.register(new EventHandler());

    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
    driver.manage().window().maximize();
    driver.manage().deleteAllCookies();

    return driver;
  }

}

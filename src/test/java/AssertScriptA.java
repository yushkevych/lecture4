import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class AssertScriptA {
  private static String productName;
  private static String productQty;
  private static String productPrice;

  private static WebDriver driver;
  private static WebDriverWait wait;
  private static Actions actions;

  @DataProvider(name = "Authentication")

  public static Object[][] credentials() {

    return new Object[][] { { "webinar.test@gmail.com", "Xcg7299bnSmMuRLp9ITw" }};

  }

  @BeforeClass
  @Parameters ("browser")
    public void setup(String browser){
    driver = DriverManager.getConfiguredDriver(browser);
    wait = new WebDriverWait(driver, 10);
    actions = new Actions(driver);

  }


//  @Parameters({ "sUsername", "sPassword" })
  @Test(dataProvider = "Authentication")
  public void addProduct(String sUsername, String sPassword) {

    enterIntoAdminPanel(sUsername, sPassword);
    addProductOpenPanel();
    fillProductInformationAndActivateIt();


  }

  @Test (dependsOnMethods ="addProduct")
  public void checkProduct() {

    openSiteAllProductsAndAddedProductPage();
    checkProductNamePriceQuantity();
  }

  @AfterClass
  public void releaseDriver(){
    if (driver!= null) {
      driver.quit();
    }
  }

  private static void checkProductNamePriceQuantity(){
    WebElement pageProductName = driver.findElement(By.cssSelector("h1.h1"));

    WebElement pageProductPrice = driver.findElement(By.cssSelector("div.current-price"));

    WebElement pageProductQty = driver.findElement(By.id("product-details"));

    Assert.assertEquals(pageProductName
            .getText().toLowerCase(),productName.toLowerCase(),"The product name on the page does not match its real name");
    System.out.println(pageProductPrice.getText().replace(",","."));
    System.out.println(productPrice);
    Assert.assertTrue(pageProductPrice
            .getText().toLowerCase().replace(",",".").contains(productPrice),"The product price on the page does not match its real price");
    Assert.assertTrue(pageProductQty
            .getText().toLowerCase().contains(productQty),"The product quantity  on the page does not match its real quantity");
  }

  private static void openSiteAllProductsAndAddedProductPage(){

    driver.navigate().to("http://prestashop-automation.qatestlab.com.ua/");
    driver.findElement(By.cssSelector("a[class*='all-product-link']")).click();
    Assert.assertEquals(driver.findElement(By.linkText(productName))
            .getAttribute("text"),productName,"The product "+productName +" is absent");
    WebElement showMyProduct = driver.findElement(By.linkText(productName));
    showMyProduct.click();
    Assert.assertEquals(driver.getTitle(),productName,"The page "+productName +" is not opened");
  }

  private static void fillProductInformationAndActivateIt(){
    WebElement nameProduct = driver.findElement(By.id("form_step1_name_1"));
    WebElement qtyProduct = driver.findElement(By.id("form_step1_qty_0_shortcut"));
    WebElement priceProduct = driver.findElement(By.id("form_step1_price_shortcut"));
    WebElement activeSwitch = driver.findElement(By.cssSelector("div.switch-input"));
    WebElement productSave = driver.findElement(By.id("submit"));
    productName = randomName("TestProduct");
    productQty = Integer.toString(price(1, 100));
    productPrice = Double.toString(price(10, 10000)/100.0);
    nameProduct.sendKeys(productName);
    qtyProduct.sendKeys(productQty);
    priceProduct.clear();
    priceProduct.sendKeys(productPrice);
    activeSwitch.click();
    wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("div.switch-input.-checked"))));
    productSave.click();
    wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("growls")))); //Check popup message
  }

  private static void addProductOpenPanel() {
    WebElement navCatalog = driver.findElement(By.id("subtab-AdminCatalog"));
    WebElement menuTovar = driver.findElement(By.id("subtab-AdminProducts"));

    actions.moveToElement(navCatalog).pause(Duration.ofSeconds(2))
            .click(menuTovar).build().perform();
    WebElement addProduct = driver.findElement(By.id("page-header-desc-configuration-add"));
    addProduct.click();
    wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("form_step1_name_1"))));

    Assert.assertTrue(driver.findElements(By.id("form_step1_name_1")).size() > 0,
            "The page add product is not loaded");

  }

  private static void enterIntoAdminPanel(String sUsername, String sPassword) {

    driver.navigate().to("http://prestashop-automation.qatestlab.com.ua/admin147ajyvk0/");

    Assert.assertTrue(driver.findElements(By.id("email")).size() > 0, "No login field found");
    Assert.assertTrue(driver.findElements(By.id("passwd")).size() > 0, "No password field found");

    WebElement loginEmail = driver.findElement(By.id("email"));
    WebElement loginPassword = driver.findElement(By.id("passwd"));
    WebElement submitButton = driver.findElement(By.name("submitLogin"));

    loginEmail.sendKeys(sUsername);
    loginPassword.sendKeys(sPassword);
    submitButton.click();
    wait.until(ExpectedConditions.titleContains("Dashboard"));

  }

  private static String randomName(String nameStart) {
    Date dateCat = new Date();
    long dateCatlong = dateCat.getTime();
    return nameStart + "_" + dateCatlong;

  }

  private static int price(int minPrice, int maxPrice) {
    return minPrice + (int) (Math.random() * ((maxPrice - minPrice) + 1));
  }

}
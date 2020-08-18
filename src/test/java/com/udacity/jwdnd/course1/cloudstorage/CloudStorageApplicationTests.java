package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    public String baseURL;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
        baseURL = baseURL = "http://localhost:" + port;
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testGetLoginPage() {
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    public void testUnauthorizedUserOnlyViewLoginAndSignUpPages() {
        driver.get(baseURL + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
        driver.get(baseURL + "/signup");
        Assertions.assertEquals("Sign Up", driver.getTitle());
        driver.get(baseURL + "/home");
        Assertions.assertNotEquals("Home", driver.getTitle());
        driver.get(baseURL + "/result");
        Assertions.assertNotEquals("Result", driver.getTitle());
    }


    public void signup(String username,String password){

        driver.get(baseURL + "/signup");
        SignupPage signupPage = new SignupPage(driver);
        signupPage.signup("Rıdvan", "Yazıcı", username, password);
    }
    public void login(String username,String password){
        driver.get(baseURL + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);

    }

    @Test
    public void testUserSignupLoginAndLogoutAndHomePageNoLongerAccessible() throws InterruptedException {

        String username = "ridvan";
        String password = "password";

        signup(username,password);
        login(username,password);

        Assertions.assertEquals("Home", driver.getTitle());

        HomePage homePage = new HomePage(driver);
        homePage.logout();
        Assertions.assertNotEquals("Home", driver.getTitle());

    }

    @Test
    public void testNewNoteCreatedAndDisplayed(){
        String username = "ridvan";
        String password = "password";

        String newNoteTitle = "New Note Title";
        String newNoteDescription = "New Note Description";

        signup(username,password);
        login(username,password);
        Assertions.assertEquals("Home", driver.getTitle());

        HomePage homePage = new HomePage(driver);
        homePage.chooseNoteTab();
        homePage.openNewNoteModal();

        homePage.submitNote(newNoteTitle,newNoteDescription);
        Assertions.assertEquals(baseURL + "/result?success", driver.getCurrentUrl());

        ResultPage resultPage = new ResultPage(driver);
        resultPage.successReturnHome();

        Assertions.assertEquals("Home", driver.getTitle());
        homePage.chooseNoteTab();
        Assertions.assertEquals(newNoteTitle, homePage.getNotTitle()) ;
        homePage.openDeleteNoteModal();
        homePage.submitDeleteNote();

    }

    @Test
    public void testCurrentNoteEditedAndEditedVersionDisplayed(){
        String username = "ridvan";
        String password = "password";

        String newNoteTitle = "New Note Title";
        String newNoteDescription = "New Note Description";
        String editedNoteTitle ="Edited Note Title";
        String editedNoteDescription ="Edited Note Description";

        signup(username,password);
        login(username,password);
        HomePage homePage = new HomePage(driver);
        homePage.chooseNoteTab();
        homePage.openNewNoteModal();

        homePage.submitNote(newNoteTitle,newNoteDescription);

        ResultPage resultPage = new ResultPage(driver);
        resultPage.successReturnHome();
        homePage.chooseNoteTab();
        homePage.openEditNoteModal();
        homePage.submitNote(editedNoteTitle,editedNoteDescription);
        resultPage.successReturnHome();
        homePage.chooseNoteTab();
        Assertions.assertEquals(editedNoteTitle, homePage.getNotTitle()) ;
        homePage.openDeleteNoteModal();
        homePage.submitDeleteNote();

    }

    @Test
    public void testNoteDeletedAndNoLongerDisplayed(){
        String username = "ridvan";
        String password = "password";

        String newNoteTitle = "New Note Title";
        String newNoteDescription = "New Note Description";

        signup(username,password);
        login(username,password);
        HomePage homePage = new HomePage(driver);
        homePage.chooseNoteTab();
        homePage.openNewNoteModal();

        homePage.submitNote(newNoteTitle,newNoteDescription);

        ResultPage resultPage = new ResultPage(driver);
        resultPage.successReturnHome();
        homePage.chooseNoteTab();
        homePage.openDeleteNoteModal();
        homePage.submitDeleteNote();
        Assertions.assertEquals(baseURL + "/result?success", driver.getCurrentUrl());
        resultPage.successReturnHome();
        Assertions.assertEquals("Home", driver.getTitle());
        homePage.chooseNoteTab();
        Assertions.assertTrue(homePage.getNotes().isEmpty());
    }

    @Test
    public void testNewCredentialCreatedAndDisplayedAndPasswordEncrypted(){
        String username = "ridvan";
        String password = "password";

        String newCredentialUrl = "https://www.java.com/";
        String newCredentialUsername ="jamesgosling";
        String newCredentialPassword = "javaisgreat";


        signup(username,password);
        login(username,password);
        Assertions.assertEquals("Home", driver.getTitle());

        HomePage homePage = new HomePage(driver);
        homePage.chooseCredentialTab();
        homePage.openNewCredentialModal();

        homePage.submitCredential(newCredentialUrl,newCredentialUsername,newCredentialPassword);
        Assertions.assertEquals(baseURL + "/result?success", driver.getCurrentUrl());

        ResultPage resultPage = new ResultPage(driver);
        resultPage.successReturnHome();

        Assertions.assertEquals("Home", driver.getTitle());
        homePage.chooseCredentialTab();
        Assertions.assertEquals(newCredentialUrl, homePage.getCredentialUrl()) ;
        Assertions.assertNotEquals(newCredentialPassword, homePage.getCredentialPassword()) ;
        homePage.openDeleteCredentialModal();
        homePage.submitDeleteCredential();

    }

    @Test
    public void testCurrentCredentialEditedAndWhenEditingUnencryptedPasswordDisplayedAndChangesDisplayed(){
        String username = "ridvan";
        String password = "password";

        String newCredentialUrl = "https://www.java.com/";
        String newCredentialUsername ="jamesgosling";
        String newCredentialPassword = "javaisgreat";

        String editedCredentialUrl = "https://www.oracle.com/";
        String editedCredentialUsername = "joshuabloch";
        String editedCredentialPassword = "javaiseffective";

        signup(username,password);
        login(username,password);
        Assertions.assertEquals("Home", driver.getTitle());

        HomePage homePage = new HomePage(driver);
        homePage.chooseCredentialTab();
        homePage.openNewCredentialModal();

        homePage.submitCredential(newCredentialUrl,newCredentialUsername,newCredentialPassword);
        ResultPage resultPage = new ResultPage(driver);
        resultPage.successReturnHome();
        homePage.chooseCredentialTab();
        String newCredentialPasswordEncrypted = homePage.getCredentialPassword();

        homePage.chooseCredentialTab();
        homePage.openEditCredentialModal();
        homePage.submitCredential(editedCredentialUrl,editedCredentialPassword,editedCredentialUsername);
        resultPage.successReturnHome();
        homePage.chooseCredentialTab();
        Assertions.assertEquals(editedCredentialUrl, homePage.getCredentialUrl()) ;
        Assertions.assertNotEquals(newCredentialPasswordEncrypted, homePage.getCredentialPassword()) ;
        homePage.openDeleteCredentialModal();
        homePage.submitDeleteCredential();
    }

    @Test
    public void testCredentialDeletedAndNoLongerDisplayed(){
        String username = "ridvan";
        String password = "password";

        String newCredentialUrl = "https://www.java.com/";
        String newCredentialUsername ="jamesgosling";
        String newCredentialPassword = "javaisgreat";


        signup(username,password);
        login(username,password);

        HomePage homePage = new HomePage(driver);
        homePage.chooseCredentialTab();
        homePage.openNewCredentialModal();

        homePage.submitCredential(newCredentialUrl,newCredentialUsername,newCredentialPassword);
        Assertions.assertEquals(baseURL + "/result?success", driver.getCurrentUrl());

        ResultPage resultPage = new ResultPage(driver);
        resultPage.successReturnHome();
        homePage.chooseCredentialTab();
        homePage.openDeleteCredentialModal();
        homePage.submitDeleteCredential();
        Assertions.assertEquals(baseURL + "/result?success", driver.getCurrentUrl());
        resultPage.successReturnHome();
        homePage.chooseCredentialTab();
        Assertions.assertTrue(homePage.getCredentials().isEmpty());
    }

}

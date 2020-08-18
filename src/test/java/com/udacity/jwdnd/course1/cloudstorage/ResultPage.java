package com.udacity.jwdnd.course1.cloudstorage;

import com.google.gson.internal.$Gson$Preconditions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ResultPage {

    private WebDriver driver = null;

    @FindBy(id = "successReturnHomeLink")
    private WebElement successReturnHomeLink;

    public ResultPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver,this);

    }

    public void successReturnHome(){
        ((JavascriptExecutor)driver).executeScript(
                "arguments[0].click();", successReturnHomeLink);
    }

}

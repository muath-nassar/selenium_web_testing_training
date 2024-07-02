package webpages;

import enums.Gender;
import enums.Hobby;
import org.openqa.selenium.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

public class StudentFormWebpage {
    public static String DATE_PATTERN = "dd MMM yyyy";
    public static String URL = "https://demoqa.com/automation-practice-form";
    private JavascriptExecutor executor;

    private class IDs {
        private static final String FIRST_NAME = "firstName";
        private static final String LAST_NAME = "lastName";
        private static final String EMAIL = "userEmail";
        private static final String GENDER_MALE = "gender-radio-1";
        private static final String GENDER_FEMALE = "gender-radio-2";
        private static final String GENDER_OTHER = "gender-radio-3";
        private static final String Mobile = "userNumber";
        private static final String DateOfBirth = "dateOfBirthInput";
        private static final String STATE = "state";
        private static final String STATE_INPUT = "react-select-3-input";
        private static final String CITY = "city";
        private static final String CITY_INPUT = "react-select-4-input";
        private static final String FILE = "uploadPicture";
        private static final String SUBJECT = "subjectsInput";
        private static final String HOBBY_SPORTS = "hobbies-checkbox-1";
        private static final String HOBBY_READING = "hobbies-checkbox-2";
        private static final String HOBBY_MUSIC = "hobbies-checkbox-3";
        private static final String SUBMIT = "submit";
    }

    private WebDriver driver;

    private void click(WebElement webElement) {
        executor.executeScript("arguments[0].click();", webElement);
    }

    public StudentFormWebpage(WebDriver driver) {
        this.driver = driver;
        this.executor = (JavascriptExecutor) driver;
    }

    public void open() {
        driver.get(URL);
    }

    private WebElement getElementById(String id) {
        return driver.findElement(By.id(id));
    }

    public void setFirstName(String firstName) {
        getElementById(IDs.FIRST_NAME).sendKeys(firstName);
    }

    public void setLastName(String lastName) {
        getElementById(IDs.LAST_NAME).sendKeys(lastName);
    }

    public void setEmail(String email) {
        getElementById(IDs.EMAIL).sendKeys(email);
    }

    public void setGender(Gender gender) {

        switch (gender) {
            case MALE -> click(getElementById(IDs.GENDER_MALE));
            case FEMALE -> click(getElementById(IDs.GENDER_FEMALE));
            case OTHER -> click(getElementById(IDs.GENDER_OTHER));
        }
    }

    public void setMobile(String mobile) {
        getElementById(IDs.Mobile).sendKeys(mobile);
    }

    public void setHobby(Hobby hobby) {
        switch (hobby) {
            case SPORTS -> click(getElementById(IDs.HOBBY_SPORTS));
            case READING -> click(getElementById(IDs.HOBBY_READING));
            case MUSIC -> click(getElementById(IDs.HOBBY_MUSIC));
        }
    }

    public void setSubjects(String subject) throws InterruptedException {
        WebElement textBox = getElementById(IDs.SUBJECT);
        textBox.sendKeys(subject);
        Thread.sleep(1000);
        textBox.sendKeys(Keys.RETURN);
        Thread.sleep(1000);


    }

    public void setDOB(int day, int month, int year) {
        LocalDate localDate = LocalDate.of(year, month, day);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        System.out.println(localDate.format(formatter));
        WebElement dob = getElementById(IDs.DateOfBirth);
        dob.clear();
        dob.sendKeys(localDate.format(formatter) + Keys.RETURN);
        System.out.println("Updated Input Value: " + dob.getAttribute("value"));
    }

    public void setAddress(String state, String city) throws InterruptedException {
        WebElement stateContainer = getElementById(IDs.STATE);
        WebElement cityContainer = getElementById(IDs.CITY);
        WebElement stateInput = getElementById(IDs.STATE_INPUT);
        WebElement cityInput = getElementById(IDs.CITY_INPUT);
        click(stateContainer);
        stateInput.sendKeys(state);
        stateInput.sendKeys(Keys.RETURN);
        click(cityContainer);
        Thread.sleep(3000);
        cityInput.sendKeys(city);
        cityInput.sendKeys(Keys.RETURN);
    }

    public void setFile(String path) {
        getElementById(IDs.FILE).sendKeys(path);
    }

    public void submit() {
        click(getElementById(IDs.SUBMIT));
    }

    public HashMap<String, String> getResults() {
        HashMap<String, String> result = new HashMap<>();
        //get the result div
        WebElement table = driver.findElement(
                By.cssSelector("table"));
        System.out.println(table.getTagName());
        List<WebElement> rows = table.findElements(By.cssSelector("tbody > tr"));
        for (WebElement row : rows) {
            String title = row.findElement(By.xpath("td[1]")).getText();
            String value = row.findElement(By.xpath("td[2]")).getText();
            result.put(title, value);
        }
        return result;
    }

}

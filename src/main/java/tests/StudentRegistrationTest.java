package tests;

import enums.Gender;
import enums.Hobby;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import webpages.StudentFormWebpage;

import java.io.File;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class StudentRegistrationTest {
    private WebDriver driver;
    StudentFormWebpage page;
    Random random;


    @BeforeClass
    private void setup() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        page = new StudentFormWebpage(driver);
        page.open();
        random = new Random();
        System.out.println("Test start");
    }

    @AfterClass
    private void tearDown() {
        driver.quit();
    }

    @Test
    void simpleRegistrationScenario() throws InterruptedException {
        String firstName, lastName, email, mobile, dateOfBirth, filePath, state, city;
        HashMap<String, String> result;
        Gender gender = Gender.MALE;
        List<Hobby> hobbies;
        List<String> subjects;
        int year, month, day;
        //Set Date
        firstName = "Ahmad";
        lastName = "Gamal";
        email = "example@mail.com";
        page.setFirstName(firstName);
        page.setLastName(lastName);
        page.setEmail(email);
        year = 1990;
        day = 6;
        month = 7;
        mobile = "0974121212";
        subjects = new ArrayList<>();
        subjects.add("Maths");
        subjects.add("English");
        hobbies = new ArrayList<>();
        hobbies.add(Hobby.READING);
        hobbies.add(Hobby.SPORTS);
        filePath = new File("img/logo.png").getAbsolutePath();
        state = "NCR";
        city = "Delhi";
        LocalDate localDate = LocalDate.of(year, month, day);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM,yyyy");
        dateOfBirth = formatter.format(localDate);
        page.setDOB(day, month, year);
        page.setGender(gender);
        page.setMobile(mobile);
        for (String subject : subjects) {
            page.setSubjects(subject);
        }
        for (Hobby hobby : hobbies) {
            page.setHobby(hobby);
        }
        page.setFile(filePath);
        page.setAddress(state, city);
        page.submit();
        result = page.getResults();
        validateForm(firstName, lastName, email, gender, mobile, dateOfBirth, subjects,
                hobbies, filePath, state, city, result);
    }

    public static void validateForm(
            String firstName, String lastName, String email, Gender gender, String mobile, String dateOfBirth,
            List<String> subjects, List<Hobby> hobbies, String filePath, String state,
            String city, HashMap<String, String> result) {
        //validate Student Name
        String studentName = firstName + " " + lastName;
        Assert.assertEquals(result.get("Student Name"), studentName);

        //validate email
        Assert.assertEquals(result.get("Student Email"), email);

        //validate Gender
        String genderInput = "";
        switch (gender) {
            case MALE -> genderInput = "Male";
            case FEMALE -> genderInput = "Female";
            case OTHER -> genderInput = "Other";
        }
        Assert.assertEquals(result.get("Gender"), genderInput);

        //validate Mobile
        Assert.assertEquals(result.get("Mobile"), mobile);

        //validate Date of Birth
//        Assert.assertEquals(result.get("Date of Birth"), dateOfBirth);

        //validate subjects
        List<String> subjectsResult =
                new ArrayList<>(Arrays.asList(result.get("Subjects").split(", ")));

        for (String subject : subjects) {
            Assert.assertTrue(subjectsResult.contains(subject));
            subjectsResult.remove(subject);
        }
        Assert.assertTrue(subjectsResult.isEmpty());

        //validate Hobbies
        List<String> hobbiesInput = new ArrayList<>();
        hobbies.forEach(hobby -> {
            switch (hobby) {
                case SPORTS -> hobbiesInput.add("Sports");
                case MUSIC -> hobbiesInput.add("Music");
                case READING -> hobbiesInput.add("Reading");
            }
        });
        List<String> hobbiesResult =
                new ArrayList<>(Arrays.asList(result.get("Hobbies").split(", ")));
        for (String hobby : hobbiesInput) {
            Assert.assertTrue(hobbiesResult.contains(hobby));
            hobbiesResult.remove(hobby);
        }
        Assert.assertTrue(hobbiesResult.isEmpty());

        //validate Picture
        String[] pictureData = filePath.split("/");
        String picture = pictureData[pictureData.length - 1];
        Assert.assertEquals(result.get("Picture"), picture);

        //validate State and City
        String stateResult, cityResult;
        String[] stateCity = result.get("State and City").split(" ");
        stateResult = stateCity[0];
        cityResult = stateCity[1];
        Assert.assertEquals(stateResult, state);
        Assert.assertEquals(cityResult, city);
    }
}

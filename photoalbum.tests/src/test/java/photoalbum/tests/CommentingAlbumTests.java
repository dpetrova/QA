package photoalbum.tests;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class CommentingAlbumTests {
	
	private WebDriver driver;
	private static final String BASE_URL = "http://localhost:8080/PhotoAlbum/";
	private static final String ADMIN_USERNAME = "admin";
	private static final String ADMIN_PASSWORD = "admin";
		
	
	@Before
	  public void setUp() {
	    this.driver = new FirefoxDriver();
	    this.driver.manage().window().maximize();
	    
//	    //login as admin	    
//        this.login(ADMIN_USERNAME, ADMIN_PASSWORD);
//        
//        //create new category        
//        String categoryName = "category" + UUID.randomUUID().toString();
//        this.createCategory(categoryName);
//        
//        //logout
//        this.logout();
//        
//        //register new unique user and login
//	    String registerUsername = "pesho" + UUID.randomUUID().toString();	    
//	    String registerPassword = "123456";	    
//        this.register(registerUsername, registerPassword);
//        this.login(registerUsername, registerPassword);
//        
//        //create new album        
//        String albumName = "album" + UUID.randomUUID().toString();
//        String description = "description" + UUID.randomUUID().toString();
//        this.createAlbum(albumName, categoryName, description);
//        
//        //navigate to the created album
//        this.navigateToAlbum(albumName);
	  }
	
	@Test
	  public void testComment_loggedUserValidText_expectCreateComment() {
		//login as admin	    
        this.login(ADMIN_USERNAME, ADMIN_PASSWORD);
        
        //create new category        
        String categoryName = "category" + UUID.randomUUID().toString();
        this.createCategory(categoryName);
        
        //logout
        this.logout();
        
        //register new unique user and login
	    String registerUsername = "pesho" + UUID.randomUUID().toString();	    
	    String registerPassword = "123456";	    
        this.register(registerUsername, registerPassword);
        this.login(registerUsername, registerPassword);
        
        //create new album        
        String albumName = "album" + UUID.randomUUID().toString();
        String description = "description" + UUID.randomUUID().toString();
        this.createAlbum(albumName, categoryName, description);
        
        //navigate to the created album
        this.navigateToAlbum(albumName);
        
        //navigate to the comment button
        this.navigateToCommentButton();
        
        //create comment
        this.createComment("test");
        
        WebElement commentBox = this.driver.findElement(By.xpath("/html/body/div/div[4]/ul/li[1]"));
        
        DateFormat dateFormat = new SimpleDateFormat("d-MMMM-yyyy");
        Date date = new Date();
        String currentDate = dateFormat.format(date);        
        
        String expected = String.format("Comment text: %s, User: %s, Created On: %s",
        										"test", registerUsername, currentDate);
        String actual = commentBox.getText();
        
        assertEquals(expected, actual);        
	  }
	
	
	@Test
	  public void testComment_withoutLoggedUser_expectErrorBox() {
		this.logout();
		
		//login as admin	    
        this.login(ADMIN_USERNAME, ADMIN_PASSWORD);
        
        //create new category        
        String categoryName = "category" + UUID.randomUUID().toString();
        this.createCategory(categoryName);
        
        //logout
        this.logout();
        
        //register new unique user and login
	    String registerUsername = "pesho" + UUID.randomUUID().toString();	    
	    String registerPassword = "123456";	    
        this.register(registerUsername, registerPassword);
        this.login(registerUsername, registerPassword);
        
        //create new album        
        String albumName = "album" + UUID.randomUUID().toString();
        String description = "description" + UUID.randomUUID().toString();
        this.createAlbum(albumName, categoryName, description);
        
        //logout
        this.logout();
        
        //navigate to the created album
        this.navigateToAlbum(albumName);
        
        //navigate to the comment button
        this.navigateToCommentButton();
        
        //create comment
        this.createComment("test");
        this.driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        
        WebElement errorBox = this.driver.findElement(By.xpath("/html/body/div/form/div/div[2]"));
		String expected = "You are not logged in";
		String actual = errorBox.getText().trim();
		
		assertEquals(expected, actual);
	  }
	
	@After
	  public void tearDown() {
	    this.driver.close();	    
	  }
	
	private void register(String username, String password)
    {
        this.driver.get(BASE_URL + "home/index");

        WebElement registerButton = this.driver.findElement(By.linkText("Register"));
        registerButton.click();

        WebElement usernameField = this.driver.findElement(By.id("inputUserName"));
        usernameField.click();
        usernameField.clear();
        usernameField.sendKeys(username);

        WebElement passwordField = this.driver.findElement(By.id("inputPassword"));
        passwordField.click();
        passwordField.clear();
        passwordField.sendKeys(password);

        WebElement submitButton = this.driver.findElement(By.name("register"));
        submitButton.click();
    }
	
	
	private void login(String username, String password)
    {
        this.driver.get(BASE_URL + "home/index");

        WebElement loginButton = this.driver.findElement(By.linkText("Log In"));
        loginButton.click();

        WebElement usernameField = this.driver.findElement(By.id("inputUserName"));
        usernameField.click();
        usernameField.clear();
        usernameField.sendKeys(username);

        WebElement passwordField = this.driver.findElement(By.id("inputPassword"));
        passwordField.click();
        passwordField.clear();
        passwordField.sendKeys(password);

        WebElement submitButton = this.driver.findElement(By.name("login"));
        submitButton.click();
    }
	
	private void logout()
    {
		//this.driver.get(BASE_URL + "home/index");
		//
		//WebElement loginButton = this.driver.findElement(By.linkText("Log Out"));
		//loginButton.click();
		
		this.driver.get(BASE_URL + "users/logout");  
    }
	
	private void createCategory(String name)
    {
        this.driver.get(BASE_URL + "categories/add");

        WebElement nameField = this.driver.findElement(By.name("name"));
        nameField.click();
        nameField.clear();
        nameField.sendKeys(name);        

        WebElement createButton = this.driver.findElement(By.name("create"));
        createButton.click();
    }
	
	private void createAlbum(String albumName, String categoryName, String description)
    {
        this.driver.get(BASE_URL + "albums/add");

        WebElement albumNameField = this.driver.findElement(By.name("name"));
        albumNameField.click();
        albumNameField.clear();
        albumNameField.sendKeys(albumName);
        
        WebElement categoryNameField = this.driver.findElement(By.name("categoryName"));
        categoryNameField.click();
        categoryNameField.clear();
        categoryNameField.sendKeys(categoryName);
        
        WebElement descriptionField = this.driver.findElement(By.name("description"));
        descriptionField.click();
        descriptionField.clear();
        descriptionField.sendKeys(description); 

        WebElement createButton = this.driver.findElement(By.name("create"));
        createButton.click();
    }
	
	private void navigateToAlbum(String name)
    {
        this.driver.get(BASE_URL + "albums/showall");
        
        List<WebElement> albumElements = this.driver.findElements(By.className("album-title"));
                
        for	(WebElement albumElement : albumElements){        	
        	if(albumElement.getText().equals(name)){
        		albumElement.click();
        		break;
        	}        	
//        	WebElement albumLink = albumElement.findElement(By.tagName("a"));
//        	if(albumLink.getText().equals(name)){
//        		albumLink.click();
//        		break;
//        	}        	
        }
    }
	
	private void navigateToCommentButton()
    {
		WebElement commentButton = this.driver.findElement(By.linkText("Comment"));
		commentButton.click();
    }
	
	private void createComment(String text)
    {
		WebElement textField = this.driver.findElement(By.name("comment"));
		textField.click();
		textField.clear();
		textField.sendKeys(text); 
		
		WebElement createButton = this.driver.findElement(By.name("create"));
		createButton.click();
    }

}

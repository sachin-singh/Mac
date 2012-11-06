import java.util.StringTokenizer;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;


public class Craiglist {
	
	public HtmlUnitDriver driver;
    public WebDriverBackedSelenium sel;
    public WebElement element;
    
    String link="";
    String craigListNumber="";
    String titleFull="";
    String subject="";
    String price="";
    String location="";
    String dateTime="";
    String context="";
    
    public void CraiglistMain(){
    	driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_3_6);
        driver.setJavascriptEnabled(true);
        sel = new WebDriverBackedSelenium(driver , "");
    }
    
    public String findCraiglistNumber(String url) {
    	StringTokenizer tok = new StringTokenizer(url, "/");
    	tok.nextElement();
    	tok.nextElement().toString();
    	tok.nextElement().toString();
    	tok.nextElement().toString();
    	String number = tok.nextElement().toString();
    	
    	StringTokenizer tok1 = new StringTokenizer(number, ".");
    	String digits = tok1.nextElement().toString();
    	return digits;
    	
    }
    
    public void findSubject(String fulltitle){
    	StringTokenizer token = new StringTokenizer(fulltitle, "-");
    	String subject = token.nextElement().toString();
    	String rest = token.nextElement().toString();
    	StringTokenizer token2= new StringTokenizer(rest,"(");
    	String price = token2.nextElement().toString().substring(1);
    	System.out.println(token2.nextElement().toString());
    }
    
    public void run(String url) {
    	
    	driver.get(url);
    	
    	link = driver.findElement(By.xpath("//p/a")).getAttribute("href");
    	craigListNumber = findCraiglistNumber(url);
    	titleFull = driver.findElement(By.cssSelector("h2")).getText().toString();
    	
    	
    }
    
    
    public static void main(String[] args){
    	Craiglist test = new Craiglist();
    	String text="1996 HONDA PASSPORT - $1 (27thave / camelback)";
    	test.findSubject(text);
    }
    
    

}

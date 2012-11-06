import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


public class Test2 {
	
	public static void main(String[] args){
		WebDriver driver = new FirefoxDriver();
		driver.get("http://harrisonabbott.nmfn.com/contact.htm");
		String mwh=driver.getWindowHandle();
		System.out.println(" mwh = "+mwh);
		driver.findElement(By.id("contactButton")).click();
		Set s=driver.getWindowHandles();
		Iterator ite=s.iterator();

		while(ite.hasNext())
		{
		    String popupHandle=ite.next().toString();
		    if(!popupHandle.contains(mwh))
		    {
		                driver.switchTo().window(popupHandle);
		                String popup=driver.getWindowHandle();
		                System.out.println("popup ="+popup);
		                System.out.println(driver.getCurrentUrl());
		                driver.close();
		                driver.switchTo().window(mwh);
		    }
		}
		
	}
}



import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.gargoylesoftware.htmlunit.BrowserVersion;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created with IntelliJ IDEA.
 * User: I3CLOGIC
 * Date: 10/18/12
 * Time: 1:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class CraiglistSeed {


    public HtmlUnitDriver driver;
    public WebDriverBackedSelenium sel;
    public WebElement element;
    
//    public WebDriver driver;
    String url=""; 
    String location="";
    int sn=1;
    String[] liststring; 
    ArrayList<String> list = new ArrayList<String>();

    public void initialize() {
            driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_3_6);
            driver.setJavascriptEnabled(true);
            sel = new WebDriverBackedSelenium(driver , "");
    }


    public void findSeed() throws Exception {
            driver.get("http://www.craigslist.org/about/sites");
            
            new WebDriverWait(driver, 60).until(ExpectedConditions.presenceOfElementLocated((By.xpath("//body[@id='index']/div[3]/div/div/div/div/ul/li/a"))));
            url = driver.findElement(By.xpath("//body[@id='index']/div[3]/div/div/div/div/ul/li/a")).getAttribute("href");
            location = driver.findElement(By.xpath("//body[@id='index']/div[3]/div/div/div/div/ul/li/a")).getText().toString();
            
            
//            System.out.println(url);
        	list.add(url + " ,"+location);
        	
	            for(int i=2;;i++){
	            	try{
	            		url = driver.findElement(By.xpath("//body[@id='index']/div[3]/div/div/div/div/ul/li["+i+"]/a")).getAttribute("href");
	            		location = driver.findElement(By.xpath("//body[@id='index']/div[3]/div/div/div/div/ul/li["+i+"]/a")).getText().toString();
//	            		System.out.println(url);
	            		list.add(url + " ,"+location);
	            	}
	            	catch(NoSuchElementException e){ 
	            		break;
	            	}
	            }
	            
	            for(int k=2;;k++){
	            	try{
			            	 url = driver.findElement(By.xpath("//body[@id='index']/div[3]/div/div/div/div/ul["+k+"]/li/a")).getAttribute("href");
			            	location = driver.findElement(By.xpath("//body[@id='index']/div[3]/div/div/div/div/ul["+k+"]/li/a")).getText().toString();
//			                 System.out.println(url);
			            	 list.add(url + " ,"+location);
			            for(int i=2;;i++)     {
			            	try{
			            		url = driver.findElement(By.xpath("//body[@id='index']/div[3]/div/div/div/div/ul["+k+"]/li["+i+"]/a")).getAttribute("href");
//			            		System.out.println(url);
			            		list.add(url + " ,"+location);
			            	}
			            	catch(NoSuchElementException e){ 
			            		break;
			            	}
			            }
	            	}
	            	catch(NoSuchElementException j){ 
	            		break;
	            	}
	            }
           for(int j=2;;j++){
        	   try{
			       url = driver.findElement(By.xpath("//body[@id='index']/div[3]/div/div/div/div["+j+"]/ul/li/a")).getAttribute("href");
			       location = driver.findElement(By.xpath("//body[@id='index']/div[3]/div/div/div/div["+j+"]/ul/li/a")).getText().toString();
//			       System.out.println(url);
			       list.add(url + " ,"+location);
			       for(int i=2;;i++){
			           	try{
			           		url = driver.findElement(By.xpath("//body[@id='index']/div[3]/div/div/div/div["+j+"]/ul/li["+i+"]/a")).getAttribute("href");
//			           		System.out.println(url); 
			           		list.add(url + " ,"+location);
			           	}
			           	catch(NoSuchElementException e){ 
			           		break;
			           	}
		           }
			       for(int k=2;;k++){
		            	try{
				            	 url = driver.findElement(By.xpath("//body[@id='index']/div[3]/div/div/div/div["+j+"]/ul["+k+"]/li/a")).getAttribute("href");
				            	 location = driver.findElement(By.xpath("//body[@id='index']/div[3]/div/div/div/div["+j+"]/ul["+k+"]/li/a")).getText().toString();
//				                 System.out.println(url);
				            	 list.add(url + " ,"+location);
				            for(int i=2;;i++)     {
				            	try{
				            		url = driver.findElement(By.xpath("//body[@id='index']/div[3]/div/div/div/div["+j+"]/ul["+k+"]/li["+i+"]/a")).getAttribute("href");
				            		location =  driver.findElement(By.xpath("//body[@id='index']/div[3]/div/div/div/div["+j+"]/ul["+k+"]/li["+i+"]/a")).getText().toString();
//				            		System.out.println(url);
				            		list.add(url + " ,"+location);
				            	}
				            	catch(NoSuchElementException e){ 
				            		break;
				            	}
				            }
		            	}
		            	catch(NoSuchElementException a){ 
		            		break;
		            	}
		            }
        	   }
        	   catch(NoSuchElementException b){
        		   System.out.println("======= Done! ======");
        		   break;
        	   }
           }
           
           
        HashSet hs = new HashSet();
   		hs.addAll(list);
   		list.clear();
   		list.addAll(hs);
   		writeExcel(list);

    }
    
    public String[] breakLink(String link) throws Exception{
    	StringTokenizer tok = new StringTokenizer(link, " ,");
    	String website = tok.nextElement().toString();
    	
    	String loc = tok.nextElement().toString();
    	String[] ret ={website, loc};
    	return  ret;
    	
    }
    
    public void writeExcel(ArrayList<String> list) throws Exception{
    	Label label;
    	WritableWorkbook workbook = Workbook.createWorkbook(new File("seedTable.xls"));
        WritableSheet sheet = workbook.createSheet("First Sheet", 0);
        for(int i=0;i<list.size();i++){
        	String[] newList = breakLink(list.get(i));
        	label = new Label(0, i, newList[0]+"/cto");
        	sheet.addCell(label);
        	label = new Label(1, i, newList[1]);
        	sheet.addCell(label);
        }
        
        workbook.write(); 
        workbook.close();
    }
    
    public static void main(String[] args){
    	Logger logger = Logger.getLogger ("");
        logger.setLevel(Level.OFF);
    	CraiglistSeed seed = new CraiglistSeed();
    	try{
    		seed.initialize();
    		seed.findSeed();
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	
    }



}

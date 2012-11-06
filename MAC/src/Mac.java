

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.gargoylesoftware.htmlunit.BrowserVersion;


public class Mac {
    public HtmlUnitDriver driver;
    //    	public WebDriver driver;
    public HtmlUnitDriver subdriver;
    //    	 public WebDriver subdriver;
    public WebDriverBackedSelenium sel;
    public WebElement element;
    ArrayList<String> name;
    String agentName="";
    String nameString="";
    String addressLine1="";
    String addressLine2="";
    String addressLine3="";
    String phone="";
    String email="";
    String emailLink="";
    String url ="";
    By by;
    String mainWindow="";
    static String initial="";

    Workbook format;
    WritableWorkbook workbook;
    WritableSheet sheet;
    WritableCell cell;
    Label label;
    int row =1;
    
    CsvWriter csvOutput;




    public Mac(){
    	System.out.println("Loading first driver");
        driver= new HtmlUnitDriver(BrowserVersion.FIREFOX_3_6);
        driver.setJavascriptEnabled(true);
//		driver = new FirefoxDriver();
        System.out.println("Loading Second driver");
        sel = new WebDriverBackedSelenium(driver, "");
        subdriver= new HtmlUnitDriver(BrowserVersion.FIREFOX_3_6);
        subdriver.setJavascriptEnabled(true); 
//		subdriver = new FirefoxDriver();

    }


    public void run() throws Exception{
    	System.out.println("Loading URL");
        driver.get("http://www.northwesternmutual.com/find-a-representative.aspx");
        System.out.println("Starting.........");
        mainWindow = driver.getWindowHandle();
        String[] alphabet = { "a","b", "c", "d", "e", "f", "g", "h", "i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
    	System.out.println("Enter initial: ");
    	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		initial = reader.readLine();
		
        for(int i=new Integer(initial);i<alphabet.length;i++){
        	row =1; 
        driver.findElement(By.id("txtFRNameValue")).clear();
        driver.findElement(By.id("txtFRNameValue")).sendKeys(alphabet[i]);
        driver.findElement(By.id("btnFRLocate")).click();
//        readCSV(alphabet[i]);



//            Thread.sleep(10000);
//            driver.findElement(By.linkText("2")).click();
        Thread.sleep(20000);
        crawlPage(alphabet[i]);
        while(isElementPresent(By.linkText("Next")))  {
        	Thread.sleep(5000);
            element = driver.findElement(By.linkText("Next"));
            element.click();
            System.out.println("Moving to next page");
            Thread.sleep(25000);
            crawlPage(alphabet[i]);
        }

        System.out.println("Agent with initial '"+alphabet[i]+"' are done!");
        workbook.write();
        workbook.close();
//        }
        }
        System.out.println("====== Done ! =====");
        subdriver.quit();
        driver.quit();
       
    }


    public void crawlPage(String fileinitial) {
    	System.out.println("Crawling Page: ");
    	try{
	
	        new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='FRResults']/div/div/a")));
	        element = driver.findElement(By.xpath("//div[@id='FRResults']/div/div/a"));
	        nameString =element.getText();
	        agentName = findName(nameString);
	        url=element.getAttribute("href");
	        findDetails2(fileinitial,agentName, url);
	        System.out.println("==========================");
	
	        for(int i=2;;i++){
	            by = By.xpath("//div[@id='FRResults']/div["+i+"]/div/a")   ;
	
	            if(isElementPresent(by))  {
	                element = driver.findElement(by);
	                nameString =element.getText();
	                agentName = findName(nameString);
	                url=element.getAttribute("href");
	                findDetails2(fileinitial, agentName, url);
	                System.out.println("==========================");
	            }
	            else{
	                break;
	            }

	        }
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    }

    public void findDetails(ArrayList<String> names, String url) throws Exception {
        try{
            subdriver.get(url);
            new WebDriverWait(subdriver, 60).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("span.street-address")));

            try{
                addressLine1= subdriver.findElement(By.cssSelector("span.street-address")).getText().toString();
            }
            catch(Exception e){
                addressLine1="NA";
            }
            try{
                addressLine2 = subdriver.findElement(By.cssSelector("span.locality")).getText().toString();
            }
            catch(Exception e){
                addressLine2="NA";
            }
            try{
                addressLine3 =subdriver.findElement(By.cssSelector("span.region")).getText().toString();
            }
            catch(Exception e){
                addressLine3="NA";
            }
            try{
                phone= subdriver.findElement(By.cssSelector("span.value")).getText().toString();
            }
            catch(Exception e){
                phone="NA";
            }

            try{
//                emailLink = subdriver.findElement(By.cssSelector("strong > a.email")).getAttribute("href");
                emailLink= subdriver.findElement(By.xpath("/html/body/div/div/div[2]/table/tbody/tr[2]/td/table/tbody/tr/td/div/a")).getAttribute("href");
                email=  findEmail(emailLink);
            }

            catch(Exception e){
                try{
                    emailLink= subdriver.findElement(By.xpath("/html/body/div/div/table/tbody/tr[2]/td/div/div[3]/a")).getAttribute("href");
                    email=  findEmail(emailLink);
                }
                catch(Exception j){
                    email="NA";
                }
            }
            System.out.println(subdriver.getCurrentUrl());
            System.out.println(addressLine1);
            System.out.println(addressLine2);
            System.out.println(addressLine3);
            System.out.println(phone);
            System.out.println(email);

//            writeCSV(names ,phone, addressLine1, addressLine2, addressLine3, url,  email );

        }
        catch(Exception e){
            System.out.println("Timeout while loadiing " + url);
        }





    }

    public void findDetails2(String fileinitial,String name, String url) throws Exception {
    	System.out.println("Finding Details: ");
        try{
            subdriver.get(url+"/contact.htm");
            new WebDriverWait(subdriver, 60).until(ExpectedConditions.presenceOfElementLocated(By.id("contactButton")));


            try{
                addressLine1= subdriver.findElement(By.cssSelector("li.address01.street-address")).getText();
            }
            catch(Exception e){
                addressLine1="NA";
            }
            try{
                addressLine2 = subdriver.findElement(By.cssSelector("span.locality")).getText().toString();
            }
            catch(Exception e){
                addressLine2="NA";
            }
            try{
                addressLine3 =subdriver.findElement(By.cssSelector("span.region")).getText().toString();
            }
            catch(Exception e){
                addressLine3="NA";
            }
            try{
                phone= subdriver.findElement(By.cssSelector("span.value")).getText().toString();
            }
            catch(Exception e){
                phone="NA";
            }
            try{
                email=  getEmail();
            }
            catch(Exception e){
                email="NA";
            }
            System.out.println(name);
            System.out.println(url);
            System.out.println(addressLine1);
            System.out.println(addressLine2+", "+addressLine3);
            System.out.println(phone);
            System.out.println(email);

            writeCSV(fileinitial, name ,phone, addressLine1, addressLine2, addressLine3, url,  email );

        }
        catch(Exception e){
            System.out.println("Timeout while loading " + url+"/contact.htm");
            writeCSV(fileinitial, name ,"Timeout", "Timeout", "Timeout", "Timeout", url, "Timeout" );
        }





    }


    public String getEmail() throws Exception{
        String mail="";
        String mwh=subdriver.getWindowHandle();
        subdriver.findElement(By.id("contactButton")).click();
        Set s=subdriver.getWindowHandles();
        Iterator ite=s.iterator();

        while(ite.hasNext())
        {
            String popupHandle=ite.next().toString();
            if(!popupHandle.contains(mwh))
            {
                subdriver.switchTo().window(popupHandle);
                mail= findEmail(subdriver.getCurrentUrl());
                subdriver.close();
                subdriver.switchTo().window(mwh);
            }
        }
        return mail;
    }

    public void writeExcel(String name, String phone, String add1, String add2, String add3, String url, String email) throws Exception{
        label = new Label(0 , row, name );
        sheet.addCell(label);
        label = new Label(1, row, phone);
        sheet.addCell(label);
        label= new Label(2, row, add1);
        sheet.addCell(label);
        label = new Label(3, row,add2 + ", " + add3 );
        sheet.addCell(label);
        label = new Label(4, row , url);
        sheet.addCell(label);
        label= new Label(5, row, email);
        sheet.addCell(label);
        System.out.println("Entry done in Row: "+row);

        
        row++;

    }
    
    public void setPage(String agentName){
    	boolean bool;
    	try{
    		for(int i=0;;i++){
    			if(sel.isTextPresent(agentName)){
    				break;
    			}
    			else{
    				
    	            element = driver.findElement(By.linkText("Next"));
    	            element.click();
    	            Thread.sleep(5000);
    			}
    		}
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    public void readCSV(String fileinitial) {
    	try{
    		CsvReader results = new CsvReader(fileinitial+"_output.csv");
    		results.readHeaders();
    		String agentName="";
    		while(results.readRecord()){
    			agentName=results.get("Name");
    		}
    		
    		setPage(agentName);
    		
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    public void writeCSV(String filename, String name, String phone, String add1, String add2, String add3, String url, String email){
    	boolean alreadyExists = new File(filename+"_output.csv").exists();
    	try{
    		CsvWriter csvOutput = new CsvWriter(new FileWriter(filename+"_output.csv", true), ',');
	    	
    		if (!alreadyExists)
			{
				csvOutput.write("Name");
				csvOutput.write("Phone Number");
				csvOutput.write("Agent Adress Line 1");
				csvOutput.write("Agent Address Line 2");
				csvOutput.write("Agent Website");
				csvOutput.write("Agent Email");				 			
				csvOutput.endRecord();
			}
    		
    		
    		csvOutput.write(name);
			csvOutput.write(phone);
			csvOutput.write(add1);
			csvOutput.write(add2 + ", " + add3);
			csvOutput.write(url);
			csvOutput.write(email);
			System.out.println("Entry done in "+row);
			csvOutput.endRecord();
			csvOutput.close();
			row++;
    	}
    	catch(Exception e){
    		e.printStackTrace();
    		System.out.println(name+" error during writing");
    	}
    }

    public void initialize(String name) throws Exception{
//    	System.out.println("Enter initial: ");
//    	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//		initial = reader.readLine();
        workbook = Workbook.createWorkbook(new File(name+"_output.xls"));
        sheet = workbook.createSheet("First Sheet", 0);
        label = new Label(0,0 ,"Agent Name");
        sheet.addCell(label);
        label = new Label(1, 0, "Agent Phone");
        sheet.addCell(label);
        label = new Label(2, 0, "Agent Adress Line 1");
        sheet.addCell(label);
        label  = new Label(3 , 0, "Agent Address Line 2");
        sheet.addCell(label);
        label = new Label(4, 0 , "Agent Website");
        sheet.addCell(label);
        label = new Label(5, 0 , "Agent Email");
        sheet.addCell(label);
    }
    
    public void initializeCSV(String name) {
    	boolean alreadyExists = new File(name+"_output.csv").exists();
		try{
			csvOutput = new CsvWriter(new FileWriter(name+"_output.csv", true), ',');
			if (!alreadyExists)
			{
				csvOutput.write("Name");
				csvOutput.write("Phone Number");
				csvOutput.write("Agent Adress Line 1");
				csvOutput.write("Agent Address Line 2");
				csvOutput.write("Agent Website");
				csvOutput.write("Agent Email");
				 			
				csvOutput.endRecord();
			}
		}
			catch(Exception e){
				System.out.println("Error: while initialization");
			}
			
    }

    public static void main(String[] args){
        Logger logger = Logger.getLogger ("");
        logger.setLevel(Level.OFF);
        Mac r= new Mac();
            try{

                r.run();
            }
            catch(Exception e){

            }

    }

    public String findName(String s){

        StringTokenizer st = new StringTokenizer(s, ",");
        String secondName = st.nextElement().toString();

        String spacedName = st.nextElement().toString();
        String firstName = spacedName.substring(1);

        String fullName = firstName+" "+secondName;

        return fullName;
    }

    public String findEmail(String url){
        StringTokenizer st = new StringTokenizer(url, "=");
        st.nextElement().toString();
        st.nextElement().toString();
        String elem = st.nextElement().toString();
        StringTokenizer stn = new StringTokenizer(elem, "&");
        String email = stn.nextElement().toString();
        return email+"@nmfn.com";
    }

    public boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
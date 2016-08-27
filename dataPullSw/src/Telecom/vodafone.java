package Telecom;

import gMailAPI.gMail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.api.services.drive.Drive;

import Generic.Generic;

public class vodafone extends Generic   {
	
	//Object declaration
	static String strloginurl="https://myvodafone.vodafone.in";
	static String m_LoginButton = "//a[text()='Login']";
	static String BillsAndPaymentLink = "//li/a[text()='Bills & Payment']";
	static String StatementAccountLink = "//li/a[text()='Statement of account']";
	static String StatementAccountPage = "//h1[text()='Statement of Account']";
	static String FromMonthDropDown = "//select[@id='prntFromMonth']";
	static String FromYearDropDown = "//select[@id='prntFromYear']";
	static String GetStatementButton = "//a[text()='Get Statement']";
	static String ToMonthDropDown = "//select[@id='prntToMonth']";
	static String ToYearDropDown = "//select[@id='prntToYear']";
	static String strMainMenuXpath="//ul[@class='mainMenu']";
	static String strLoginFrame="portlet2_iframe";
	static String strVodafoneLogin="//*[@id='ssousernameUI']";
	static String strDLButton = "//a[text()='Download']";
	static String strInvalidUserCredential="//strong[@class='errMsgClass' and text()='Invalid User Name / Mobile Number or Password. Please try again.']";
	static String objVodafoneLogOut="(//div[@class='loginBox']//a)[1]";
	static String strLogOutButton="//div[@class='loginBox']//a[text()='Logout']";
	static String newline = System.getProperty("line.separator");
	
	//Implicit Waits
	static int intMinWait=5;
	static int intMedWait=10;
	static int intMaxWait=1000;
	static String googleDrivePath;
	
	
	
	
	public static boolean dlVodafoneBill(FirefoxDriver driver,String strBaseDir, Logger log,String strUserName, String strFForderNumber, String seqNo){
		MDC.put("EventType","DataDownloadStart");
		MDC.put("EventData","Success");
		log.info("Data dwonload started");
			
		ImplicitWait(driver);
		WebDriverWait wait = new WebDriverWait(driver, intMaxWait);
		try{
			waitTillElementIsPresent(driver,strMainMenuXpath , intMaxWait);
			wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("portlet2_iframe"));
			waitTillElementIsPresent(driver, m_LoginButton, intMaxWait);
			ImplicitWait(driver);
			}catch(InterruptedException e1){
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return false;
			}
		
	    String arrMessage[] = readFile("./InputMessages/LoginMessage.txt").split("#newLine#");
	    String strMessage = "";
	    for(int intLoop=0;intLoop<arrMessage.length;intLoop++){
	    	if(strMessage==""){
	    		strMessage =arrMessage[intLoop];
	    		}else{
	        	   strMessage =strMessage+ newline+arrMessage[intLoop];
	        	   }
	    	}
	    showPopup(strMessage,driver,log, strUserName, strFForderNumber, seqNo);
			
		boolean blnFlag= true;
	    ImplicitWait(driver, intMinWait);
	    int intCount=0;
	   
	    ImplicitWait(driver,intMedWait);
	    
	    while(blnFlag){
	    	try{
	    		driver.switchTo().defaultContent();
	            driver.switchTo().frame(strLoginFrame);
	            if(driver.findElements(By.xpath(strInvalidUserCredential)).size()>0){
	            	showPopup("Invalid login crendentials entered. The page will be reloaded now.");
	  				driver.get(strloginurl);
	  				driver.manage().deleteAllCookies();
	  		        driver.switchTo().frame(strLoginFrame);
	  				intCount ++;
	  				if(intCount>=3){
	  					showPopup("Maximum number of allowed attempt over. The Utility will exit now.");
			  			driver.close();
			  			driver.quit();
			  			exit("all",log, strUserName,strFForderNumber,seqNo);
			  			System.exit(0);
			  			break;
			  			}
	  				}else if(exists(driver, BillsAndPaymentLink)){
	  					break;
	  					}
	            }catch(Exception e){
	            	// TODO Auto-generated catch block
	                   log.error("Something went wrong");
	                   e.printStackTrace();
	                   return false;
	                   }
	    	}
	    try{
	    	//	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("portlet2_iframe"));
	    	waitTillElementIsPresent(driver, BillsAndPaymentLink, intMaxWait);			
			String[] arrMsg = readFile("./InputMessages/FinFortSoftwareTakeover.txt").split("#newLine#");
	        String strMsg = "";
	        newline=System.getProperty("line.separator");
	        for(int intLoop = 0;intLoop<arrMsg.length;intLoop++){
	        	if(strMsg==""){
	        		strMsg =arrMsg[intLoop];
	        		}else{
	        			strMsg =strMsg + newline + arrMsg[intLoop];
	        			}
	        	}
	        
	        showPopup(strMsg,driver,log, strUserName, strFForderNumber, seqNo);
			Click(driver, BillsAndPaymentLink);
			MDC.put("EventType","DataDownload");
			MDC.put("EventData","Success");
			log.info("Bill and payment link clicked");
			
			waitTillElementIsPresent(driver, StatementAccountLink, intMaxWait);
			ImplicitWait(driver);
			log.info("Statement Account link clicked");
			Click(driver, StatementAccountLink);
			ImplicitWait(driver);
			driver.switchTo().frame("portlet2_iframe");
			
			waitTillElementIsPresent(driver, StatementAccountPage, intMaxWait);
			ImplicitWait(driver);
			String strCurrMonth = getMonthForInt(Calendar.getInstance().get(Calendar.MONTH));
			int strCurrYear = Calendar.getInstance().get(Calendar.YEAR);	
			SelectItemFromDropdownByText(driver, FromMonthDropDown, strCurrMonth);
			SelectItemFromDropdownByText(driver, FromYearDropDown,String.valueOf(strCurrYear - 1));
			SelectItemFromDropdownByText(driver, ToMonthDropDown, strCurrMonth);
			SelectItemFromDropdownByText(driver, ToYearDropDown,String.valueOf(strCurrYear));
			log.info("Month and year from calender selected");
	
			ImplicitWait(driver);
			Click(driver, GetStatementButton);
			log.info("Clicked get statement");
	
			ImplicitWait(driver);
			wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("portlet2_iframe"));
			waitTillElementIsPresent(driver,"//tr[@class='alignCenter billTotal']", intMaxWait);
			ImplicitWait(driver);
			
			WebElement DownloadButton = driver.findElement(By.xpath(strDLButton));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", DownloadButton);	
			waitTillElementIsPresent(driver, strDLButton, intMaxWait);
			Click(driver, strDLButton);
			File f = new File(strBaseDir+"/Statement_of_Account.pdf.part");
			Thread.sleep(intMaxWait*5);
			while (f.exists()) {
				ImplicitWait(driver);
				}
			
			MDC.put("EventType","DataDownloadComplete");
			MDC.put("EventData","Success");
			log.info("Data download completed for Vodafone");
			
			}catch(Exception e){
				MDC.put("EventType","DataDownload");
				MDC.put("EventData","Failure");
				log.error("Data download is failed for Vodafone: "+e.getMessage());
				return false;
				}
	    return true;
	    }
	
	public static boolean initiateVodafone(String strTo, String strFrom, String strCc, String strUserName, String subject, String SubjectPwd, String body, String bodyPwd, String strPasswordToZip, Logger log, String strFForderNumber,String strUserNAme,String strPhoneNumber,String seqNo) throws IOException, ParseException{
		
		JFrame jfrm =showPreLoader();
		MDC.put("EventType", "WebsiteLogin");
		MDC.put("EventData", "Success");
		log.info("Logging in to the Vodafone website");
				
		FirefoxDriver driver = initiate("Vodafone",strloginurl,log,strUserName,strFForderNumber,seqNo);
		String strBaseDir = Generic.getBase();
		log.info("Navigated to Vodafone url :"+strloginurl);		
		driver.manage().deleteAllCookies();
		JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("BasicInfo");
		jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("DriveConfig");
		String strDriveBaseDir = jObj.get("driveBaseDir").toString();		
		boolean dlUnsuccessful= false;
		try{
			log.info("Started dlVodafoneBill");
			closePreLoader(jfrm);
			if(!dlVodafoneBill(driver, strBaseDir,log, strUserName,  strFForderNumber,  seqNo)){
				dlUnsuccessful=true;
				vodafoneLogOut(driver, log, strUserName,  strFForderNumber,  seqNo);
				}
			}catch(Exception e){
				log.error("dlVodafoneBill: "+e.getMessage());
				dlUnsuccessful=true;
				vodafoneLogOut(driver, log, strUserName,  strFForderNumber,  seqNo);
				}
		
		if(!dlUnsuccessful){
			try{
				MDC.put("EventType", "ZipFileCreation");
			    log.info("initiated zipFiles");
				zipFiles(strBaseDir, "Vodafone", strPasswordToZip);
				MDC.put("EventData","Success");
				log.info("Mail ZIP Password: "+strPasswordToZip);
				}catch(Exception e){
					MDC.put("EventData","Failure"+e.getMessage());
					log.error("zipFiles: "+e.getMessage());
					dlUnsuccessful=true;
					vodafoneLogOut(driver, log, strUserName,  strFForderNumber,  seqNo);
					}
			}
			//send Mail to the Borrower Mail id	
		if(!dlUnsuccessful){
			try{
				MDC.put("EventType", "EmailDownloadedFiles");
				log.info("Initiated sendMail");
				gMail.sendMessage(strUserName, strTo, strFrom, strCc,subject, body, strBaseDir +"/","Vodafone.zip",log);
				MDC.put("EventData","Success");
				log.info("Mail sent successfully to "+strTo);
				}catch(Exception e){
					MDC.put("EventData","Failure");
					log.error("Fail to send Mail: "+e.getMessage());
					dlUnsuccessful=true;
					vodafoneLogOut(driver, log, strUserName,  strFForderNumber,  seqNo);
					}
			}
		
		//Send  Password SMS or Mail
		if(!dlUnsuccessful){
			try{
				MDC.put("EventType", "SendPassword");
				MDC.put("EventData","Success");
				jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("MailConfig");
				String strSenderId=jObj.get("passwordDeliveryMode").toString();
				if(strSenderId.equalsIgnoreCase("Email")){
					log.info("initiated Password Mail");
					gMailAPI.gMail.sendMessage(strUserName, strTo, strFrom,strCc, SubjectPwd, bodyPwd, "", "",log);
					MDC.put("EventData","Success");
					log.info("Password: "+strPasswordToZip+" Mail sent Successfully to "+strTo);
					System.out.println("Mail 2 Sent");
					}else if(strSenderId.equalsIgnoreCase("SMS")){
						log.info("initiated send SMS - password");
						SendMailZippedPassword( strPhoneNumber, strUserNAme, "Vodafone", strPasswordToZip);
						MDC.put("EventData","Success");
						log.info("Password: "+strPasswordToZip+" SMS sent successfully to"+strPhoneNumber);						
						System.out.println("SMS password  Sent");
						}else if(strSenderId.equalsIgnoreCase("BothEmailandSMS")){
							log.info("initiated sendMail - password");
							gMailAPI.gMail.sendMessage(strUserName, strTo, strFrom,strCc, SubjectPwd, bodyPwd, "", "",log);
							MDC.put("EventData","Success");
							log.info("Password: "+strPasswordToZip+" Mail sent Successfully to"+strTo);
							System.out.println("Mail 2 Sent");
							 
							log.info("initiated send SMS - password");
							SendMailZippedPassword(strPhoneNumber, strUserNAme, "Vodafone", strPasswordToZip);
							MDC.put("EventData","Success");
							log.info("Password: "+strPasswordToZip+" SMS sent successfully to"+strPhoneNumber);	
							System.out.println("SMS password  Sent");
							}
				}catch(Exception e){
					MDC.put("EventType", "SendPassword");
					MDC.put("EventData","Failure");
					log.error("sendMail - failed: "+e.getMessage());
					dlUnsuccessful=true;
					}
			}
		
		//Google drive Upload
		try{
			
			MDC.put("EventType", "GoogleDriveUpload");
			MDC.put("EventData", "Success");
			log.info("Upload to google drive initiated. Name- FFOrder: "+strFForderNumber);
			File fileToDelete = new File(strBaseDir + "/Vodafone.zip");
			deleteFolder(fileToDelete);
			Drive service = gMail.getDriveService();
			String strParentFolderID= null;
			ArrayList arrList = new ArrayList();
			String[] arrnew = strDriveBaseDir.toString().split("\\\\");

			for(int k=0;k<arrnew.length;k++){
				arrList.add(arrnew[k]);
			}
			arrList.add("FF Order# "+strFForderNumber);
			arrList.add("PullSequenceNumber: "+seqNo);
			arrList.add("Vodafone");
			String []arr = new String[arrList.size()];
			arrList.toArray(arr);
			String[] arrFolderID=gMail.createFoldersPath(arr).split("~");
			strParentFolderID=arrFolderID[0];
			String googleDrivePath=arrFolderID[1];
			if(strParentFolderID!=(null)){
				if(!uploadToDriveHierarchy(service, strBaseDir, strParentFolderID, log).equals("false")){
					dlUnsuccessful= false;
				}else{
					dlUnsuccessful= true;
				}
			}
			MDC.put("EventData", "Success");
			log.info("File Uploaded to goole drive Successfully");

		}catch(Exception e){
			MDC.put("EventType", "GoogleDriveUpload");
			MDC.put("EventData","Failure");
			log.error("Failed to upload into google drive :"+e.getMessage());
			dlUnsuccessful= true;
		}
		
		if(!dlUnsuccessful){
			try{
				String newline = System.getProperty("line.separator");
				String strMessage = "The system has successfully completed the downloads."+ newline+
					"The system will be logged out.";
				showPopup(strMessage);
				vodafoneLogOut(driver, log, strUserName,  strFForderNumber,  seqNo);
				}catch(Exception e){
					e.printStackTrace();
					dlUnsuccessful=true;
					log.error("Logout failed"+e.getMessage());
					}
			}
		
		//Capturing the screenshot
		if(dlUnsuccessful){
			try{
				MDC.put("EventType", "CaptureScreenShot");
				captureScreenshot(driver, strBaseDir+"./ErrorScreenshot/errVodfone.png");
				MDC.put("EventData","Success");
				log.info("Screenshot captured Successfully"+strBaseDir+"./ErrorScreenshot/errVodfone.png");
				driver.close();
				driver.quit();
				if(!exit("Vodafone",log,strUserName,strFForderNumber,seqNo)){
					showPopup("System can't delete the files beacause the file is in use");
					log.info("File couldn't deleted");
					SendCleanUpUnsuccessfulMail(strUserName, strFrom, strCc, strFForderNumber, seqNo, "ReliancePower",log);
					dlUnsuccessful= true;
				}
				log.info("Exit Vodafone");	

			}catch(Exception e){
				e.printStackTrace();
				MDC.put("EventType", "CaptureScreenShot");
				MDC.put("EventData","Failure");
				log.error("Failed to take screenshot");
				
				if(!exit("Vodafone",log,strUserName,strFForderNumber,seqNo)){
					showPopup("The clean exit was unsuccessful as the files to be deleted are in use. Please close all the files and clear the download folder.");
					log.info("File couldn't deleted");
					SendCleanUpUnsuccessfulMail(strUserName, strFrom, strCc, strFForderNumber, seqNo, "ReliancePower",log);
					dlUnsuccessful= true;
				}
				log.info("Exit Vodafone");	
			}
		
		}
		
		//Checking the status completed or Failed
		jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("BorrowerDetails");
		String strFFProductCode = jObj.get("FFProductCode").toString();
		if(!dlUnsuccessful){
			MDC.put("EventType","Datapull");
			MDC.put("EventData","Success");
			log.info("datapull for Vodafone is completed");
			Generic.writeToPostbackJson("./OutPut/Postback.json", strFForderNumber,strFFProductCode,"Vodafone",seqNo,googleDrivePath,"Success");
		}else{
			MDC.put("EventType","Datapull");
			MDC.put("EventData","Failure");
			log.info("datapull for Vodafone is Failed");
			Generic.writeToPostbackJson("./OutPut/Postback.json", strFForderNumber,strFFProductCode,"Vodafone",seqNo,googleDrivePath,"Success");
			}
		return dlUnsuccessful;
		}
	
	
	public static boolean vodafoneLogOut(FirefoxDriver driver, Logger log,String strUserName, String strFForderNumber, String seqNo){
		
		MDC.put("EventType", "WebsiteLogout");
		log.info("initiated logout");
		boolean bStatus=false;
		try{
			
				driver.switchTo().defaultContent();
				ClickUsingJS(driver, objVodafoneLogOut);
				ClickUsingJS(driver,strLogOutButton );
				MDC.put("EventData","Success");
				log.info("Clicked Logout button");
				log.info("logged out successfully from "+strloginurl);
				waitAftereLogout(driver);
				exit("Vodafone",log,strUserName,strFForderNumber,seqNo);
				driver.close();
				log.info("Exit Vodafone");
				bStatus=true;
		}catch(Exception e){
			MDC.put("EventType", "WebsiteLogout");
			MDC.put("EventData","Failure");
			log.error("Failed to logout  From "+strloginurl+"+e.getMessage()");
			e.printStackTrace();
			bStatus=false;
		}
		return bStatus;
		}

}

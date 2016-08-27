package Telecom;


import gMailAPI.gMail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.api.services.drive.Drive;
import com.thoughtworks.selenium.Wait;

import Generic.Generic;

public class BSNLLandline extends Generic {
	
	/*Xpaths of Objects*/
	static String strloginurl="http://selfcare.sdc.bsnl.co.in/selfcare/start.swe?SWECmd=Start&SWEHo=selfcare.sdc.bsnl.co.in";
	static String loginBtn="//a[text()='Login']";
	static String strServiceBtn="//a[contains(text(),'Service')]";
	static String strCheckMyBillsink="//a[text()='Check My Bills']";
	static String strInvalidPassword="//td[contains(text(),'The password you have entered is not correct. Please enter your password again.') and span[@class='error']]";
	
	static String strAccountNolink="//form[starts-with(@name,'SWEForm3')]//tr[@class='listRowOff']//a";
	static String logoutBtn="//a[text()='Log Out']";
	
	/*WebElements*/	
	static WebElement YrDropdown;
	//Implicit Waits
		static int intMinWait=5;
		static int intMedWait=10;
		static int intMaxWait=1000;
		static String newline = System.getProperty("line.separator");
		static String googleDrivePath;
		
		
public static boolean dlBSNLLandlineBill(FirefoxDriver driver,String strBaseDir, Logger log,String strUserName, String strFForderNumber, String seqNo){
	 boolean blnDlStatus= false;
     try{  
		    MDC.put("EventType","DataDownloadStart");
			MDC.put("EventData","Success");
			log.info("Data dwonload started");
				
			ImplicitWait(driver);
			//WebDriverWait wait = new WebDriverWait(driver, intMaxWait);
			driver.switchTo().frame("_sweclient").switchTo().frame("_swecontent").switchTo().frame("_sweview");
				
			waitTillElementIsPresent(driver,loginBtn , intMaxWait);	
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",driver.findElementByXPath(loginBtn));	
			
			/*lOGIN POPUP*/
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
		    
		    MDC.put("EventType","DataDownloadStart");
			MDC.put("EventData","Success");
			log.info("Data dwonload started");
				
			boolean blnFlag= true;
		    ImplicitWait(driver, intMinWait);
		    int intCount=0;
		   
		    ImplicitWait(driver,intMedWait);
		    
		    while(blnFlag){
		    	driver.switchTo().defaultContent();
				driver.switchTo().frame("_sweclient").switchTo().frame("_swecontent").switchTo().frame("_sweview");
		   try{
			   	ImplicitWait(driver,intMedWait);			   	
		    	if((driver.findElements(By.xpath(strInvalidPassword)).size()>0)){		    		
		    	
		    	showPopup("Invalid login crendentials entered. The page will be reloaded now.");
		    	
		  		driver.get(strloginurl);
		  		driver.manage().deleteAllCookies();
		  		        
		  		intCount ++;
		  		if(intCount>=3){
		  			showPopup("Maximum number of allowed attempt over. The Utility will exit now.");
				  	driver.close();
				  	driver.quit();
				  	exit("all",log, strUserName,strFForderNumber,seqNo);
				  	System.exit(0);
				  	break;
				  	}
		  		}else{
		  			driver.switchTo().defaultContent();
					driver.switchTo().frame("_sweclient").switchTo().frame("_swecontent").switchTo().frame("_sweview");
		  			if(exists(driver, loginBtn)==true){
		  				blnFlag=true;
		  				}else{
		  					driver.switchTo().defaultContent();
		  					driver.switchTo().frame("_sweclient").switchTo().frame("_swecontent").switchTo().frame("_sweview");
		  					if( exists(driver, strServiceBtn)==true){
		  						blnFlag=false;
		  						}else{
		  							log.info("Navigated to wrong link.");
		  							showPopup("Navigated to wrong link. The system will now redirect you to homepage.");
		  							driver.get(strloginurl);
		  							blnFlag=true;
		  							}
		  					}
		  			}
		    	}catch(Exception e){
		    		e.getMessage();
		    		}
		   }

		   
		    try{
		    	driver.switchTo().defaultContent();				
		    	driver.switchTo().frame("_sweclient").switchTo().frame("_swescrnbar");
		    	waitTillElementIsPresent(driver, strServiceBtn, intMaxWait);
		    	Thread.sleep(2000);
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
		        
		        MDC.put("EventType","DataDownloading");
				MDC.put("EventData","Success");
				log.info("Data dwonload started");
			
					
				Click(driver, strServiceBtn);
				log.info("Clicked on service tab");
				
				driver.switchTo().defaultContent();
				driver.switchTo().frame("_sweclient").switchTo().frame("_swecontent").switchTo().frame("_sweview");
				Click(driver, strCheckMyBillsink);
				log.info("Clicked on Check my bills link");
				
				driver.switchTo().defaultContent();
				driver.switchTo().frame("_sweclient").switchTo().frame("_swecontent").switchTo().frame("_sweview");
				
				Click(driver, "//form[starts-with(@name,'SWEForm3')]//tr[@class='listRowOff']//a");
				log.info("Clicked on Billing Account Number link");
				
				driver.switchTo().defaultContent();
				driver.switchTo().frame("_sweclient").switchTo().frame("_swecontent").switchTo().frame("_sweview");
				
				int stepCount=0;
		while(!(driver.findElementsByXPath("(//td[5]//img[@title='Next record set' and contains(@src,'images/rcnv_nxtset_0.gif')])[last()]").size()>0)){
			stepCount++;
			List<WebElement> lst=driver.findElementsByXPath("//table[@valign='top' and @bgcolor='#cccccc']//tr[@class='listRowOff']//a");	
				
			for(int i=1; i<=lst.size();i++){
				
				driver.switchTo().defaultContent();
				driver.switchTo().frame("_sweclient").switchTo().frame("_swecontent").switchTo().frame("_sweview");
				Click(driver, "//table[@valign='top' and @bgcolor='#cccccc']//tr[@class='listRowOff']["+i+"]//a");
				
				driver.switchTo().defaultContent();
				driver.switchTo().frame("_sweclient").switchTo().frame("_swecontent").switchTo().frame("_sweview");
				Click(driver, "//a[text()='Download']");
				
							
				driver.navigate().back();
				driver.navigate().back();
				
				if(driver.findElementsByXPath("//table[@valign='top' and @bgcolor='#cccccc']//tr[@class='listRowOff']["+i+"]//a").size()>0){
					
				}else{
					WebElement frm=driver.findElementByXPath("//iframe[starts-with(@id,'symbUrlIFrame')]");
					driver.switchTo().defaultContent();
					driver.switchTo().frame("_sweclient").switchTo().frame("_swecontent").switchTo().frame("_sweview").switchTo().frame(frm);
					if(driver.findElementsByXPath("//html//body[contains(text(),'File Could not be located, Pls contact Support center')]").size()>0){
					break;
					}
					}
				}if(exists(driver, "(//img[@title='Next record set' and contains(@src,'images/rcnv_nxtset_1.gif')])[last()]")){
					Click(driver, "(//img[@title='Next record set' and contains(@src,'images/rcnv_nxtset_1.gif')])[last()]");
					}else{
						break;
						}
				}
		blnDlStatus=true;
		Thread.sleep(2000);
		}catch(Exception e){
			MDC.put("EventType","DataDownload");
			MDC.put("EventData","Failure");
			log.error("Data download is failed for BSNLLandline: "+e.getMessage());
			blnDlStatus= false;
			System.out.println(blnDlStatus);
			}
		    }catch(Exception e){
		    	MDC.put("EventType","DataDownload");
				MDC.put("EventData","Failure");
				log.error("Data download is failed for BSNLLandline: "+e.getMessage());
				blnDlStatus= false;
				}
     return blnDlStatus;
     }
		
public static boolean initiateBSNLLandline(String strTo, String strFrom, String strCc, String strUserName, String subject, String SubjectPwd, String body, String bodyPwd, String strPasswordToZip, Logger log, String strFForderNumber,String strUserNAme,String strPhoneNumber,String seqNo) throws IOException, ParseException{
			
			JFrame jfrm =showPreLoader();
			MDC.put("EventType", "WebsiteLogin");
			MDC.put("EventData", "Success");
			log.info("Logging in to the BSNLLandline website");
					
			FirefoxDriver driver = initiate("BSNLLandline",strloginurl,log,strUserName,strFForderNumber,seqNo);
			String strBaseDir = Generic.getBase();
			log.info("Navigated to BSNLLandline url :"+strloginurl);		
			driver.manage().deleteAllCookies();
			JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("BasicInfo");
			jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("DriveConfig");
			String strDriveBaseDir = jObj.get("driveBaseDir").toString();		
			boolean dlUnsuccessful= false;
			
			try{
				log.info("Started dlBSNLLandlineBill");
				closePreLoader(jfrm);
				//waitTillElementIsPresent(driver, loginBtn, intMaxWait);
				if(!dlBSNLLandlineBill(driver, strBaseDir,log, strUserName,  strFForderNumber,  seqNo)){
					dlUnsuccessful=true;
					BSNLLandlineLogOut(driver, log, strUserName,  strFForderNumber,  seqNo);
					}
				}catch(Exception e){
					log.error("dlBSNLLandlineBill: "+e.getMessage());
					dlUnsuccessful=false;
					BSNLLandlineLogOut(driver, log, strUserNAme, strFForderNumber, seqNo);
					}
			
			if(!dlUnsuccessful){
				try{
					
					ImplicitWait(driver, 2000);
					MDC.put("EventType", "ZipFileCreation");
				    log.info("initiated zipFiles");
					zipFiles(strBaseDir, "BSNLLandline", strPasswordToZip);
					MDC.put("EventData","Success");
					log.info("Mail ZIP Password: "+strPasswordToZip);
					}catch(Exception e){
						MDC.put("EventData","Failure"+e.getMessage());
						log.error("zipFiles: "+e.getMessage());
						dlUnsuccessful=true;
						BSNLLandlineLogOut(driver, log, strUserName,  strFForderNumber,  seqNo);
						}
				}
				//send Mail to the Borrower Mail id	
			if(!dlUnsuccessful){
				try{
					MDC.put("EventType", "EmailDownloadedFiles");
					log.info("Initiated sendMail");
					gMail.sendMessage(strUserName, strTo, strFrom, strCc,subject, body, strBaseDir + "/","BSNLLandline.zip",log);
					MDC.put("EventData","Success");
					log.info("Mail sent successfully to "+strTo);
					
					}catch(Exception e){
						MDC.put("EventData","Failure");
						log.error("Fail to send Mail: "+e.getMessage());
						dlUnsuccessful=true;
						BSNLLandlineLogOut(driver, log, strUserName,  strFForderNumber,  seqNo);
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
							SendMailZippedPassword( strPhoneNumber, strUserNAme, "BSNLLandline", strPasswordToZip);
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
								SendMailZippedPassword(strPhoneNumber, strUserNAme, "BSNLLandline", strPasswordToZip);
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
		if(!dlUnsuccessful){
			try{
				
				MDC.put("EventType", "GoogleDriveUpload");
				MDC.put("EventData", "Success");
				log.info("Upload to google drive initiated. Name- FFOrder: "+strFForderNumber);
				File fileToDelete = new File(strBaseDir + "/BSNLLandline.zip");
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
				arrList.add("BSNLLandline");
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
		}
			
			if(!dlUnsuccessful){
				try{
					String newline = System.getProperty("line.separator");
					String strMessage = "The system has successfully completed the downloads."+ newline+
						"The system will be logged out.";
					showPopup(strMessage);
					BSNLLandlineLogOut(driver, log, strUserName,  strFForderNumber,  seqNo);
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
					captureScreenshot(driver, strBaseDir+"./ErrorScreenshot/errBSNLLandline.png");
					MDC.put("EventData","Success");
					log.info("Screenshot captured Successfully"+strBaseDir+"./ErrorScreenshot/errBSNLLandline.png");
					driver.close();
					driver.quit();
					if(!exit("BSNLLandline",log,strUserName,strFForderNumber,seqNo)){
						showPopup("System can't delete the files beacause the file is in use");
						log.info("File couldn't be deleted");
						SendCleanUpUnsuccessfulMail(strUserName, strFrom, strCc, strFForderNumber, seqNo, "BSNLLandline",log);
						dlUnsuccessful= true;
					}
					log.info("Exit BSNLLandline");	

				}catch(Exception e){
					e.printStackTrace();
					MDC.put("EventType", "CaptureScreenShot");
					MDC.put("EventData","Failure");
					log.error("Failed to take screenshot");
					
					if(!exit("BSNLLandline",log,strUserName,strFForderNumber,seqNo)){
						showPopup("The clean exit was unsuccessful as the files to be deleted are in use. Please close all the files and clear the download folder.");
						log.info("File couldn't deleted");
						SendCleanUpUnsuccessfulMail(strUserName, strFrom, strCc, strFForderNumber, seqNo, "ReliancePower",log);
						dlUnsuccessful= true;
					}
					log.info("Exit BSNLLandline");	
				}
			
			}
			
			//Checking the status completed or Failed
			jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("BorrowerDetails");
			String strFFProductCode = jObj.get("FFProductCode").toString();
			if(!dlUnsuccessful){
				MDC.put("EventType","Datapull");
				MDC.put("EventData","Success");
				log.info("datapull for BSNLLandline is completed");
				Generic.writeToPostbackJson("./OutPut/Postback.json", strFForderNumber,strFFProductCode,"BSNLLandline",seqNo,googleDrivePath,"Success");
			}else{
				MDC.put("EventType","Datapull");
				MDC.put("EventData","Failure");
				log.info("datapull for BSNLLandline is Failed");
				Generic.writeToPostbackJson("./OutPut/Postback.json", strFForderNumber,strFFProductCode,"BSNLLandline",seqNo,googleDrivePath,"Success");
				}
			return dlUnsuccessful;
			}
		
		
		public static boolean BSNLLandlineLogOut(FirefoxDriver driver, Logger log,String strUserName, String strFForderNumber, String seqNo){
			
			MDC.put("EventType", "WebsiteLogout");
			log.info("initiated logout");
			boolean bStatus=false;
			try{
				driver.switchTo().defaultContent();
				driver.switchTo().frame("_sweclient").switchTo().frame("Page");
				ClickUsingJS(driver, logoutBtn);
				MDC.put("EventData","Success");
				log.info("Clicked Logout button");
				log.info("logged out successfully from "+strloginurl);
				waitAftereLogout(driver);
				exit("BSNLLandline",log,strUserName,strFForderNumber,seqNo);
				driver.close();
				log.info("Exit BSNLLandline");
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

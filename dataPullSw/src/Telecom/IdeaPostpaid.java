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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.api.services.drive.Drive;

import Generic.Generic;

public class IdeaPostpaid  extends Generic {
	static String strloginurl="https://care.ideacellular.com/wps/portal/account/account-login";
	static String strTableLabel="//div[contains(text(),'Summary of my Idea Number')]";
	static String strMyBillsLink="//li[a[text()='My Postpaid']]//following-sibling::ul//a[text()='My Bills']";
	static String strBillDetailsLabel="//div[text()='Bill Details']";
	static String strBillsLink="//a[contains(text(),'Click here to view, download & email')]";
	static String strBillsTable="//td[contains(text(),'Bill Number')]";
	static String strInvalidData="//p[@class='errorBox' and @style='display: block;']";
	static String strWrongPasswrdMessage="(//div[@id='errorHolder']/label[@class='error'])[1]";
	static String strLoginBtn="//input[@value='Sign In']";
	static String strlogoutBtn="//div[@class='top_menu']//following-sibling::div//a[contains(text(),'Logout')]";
	
	//Implicit Waits
		static int intMinWait=5;
		static int intMedWait=10;
		static int intMaxWait=1000;
		static String newline = System.getProperty("line.separator");
		static String googleDrivePath;
		
		
public static boolean dlIdeaPostpaidBill(FirefoxDriver driver,String strBaseDir, Logger log,String strUserName, String strFForderNumber, String seqNo){
			MDC.put("EventType","DataDownloadStart");
			MDC.put("EventData","Success");
			log.info("Data dwonload started");
				
			ImplicitWait(driver);
			WebDriverWait wait = new WebDriverWait(driver, intMaxWait);
			try {
				waitTillElementIsPresent(driver,strLoginBtn , intMaxWait);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
		    		
		    		    if(driver.findElements(By.xpath(strInvalidData)).size()>0){
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
		  				}else if(driver.findElements(By.xpath(strTableLabel)).size()>0){
		  					blnFlag=false;
		  				}else if(driver.findElements(By.xpath(strLoginBtn)).size()>0){
		  					blnFlag=true;
		  				}else{
		  					MDC.put("EventType", "NavigatedToWrongLink");
							MDC.put("EventData","Failure");
						    log.info("Navigated to wrong link.");
						    showPopup("Navigated to wrong link. The system will now redirect you to homepage.");
						    driver.get(strloginurl);
						    blnFlag=true;
		  				}
		  					    		    
		            }catch(Exception e){
		            	// TODO Auto-generated catch block
		                  // log.error("Something went wrong");
		                   e.printStackTrace();
		                   blnFlag=false;
		                   }
		    	   	}
		    
		    try{
		    	//	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("portlet2_iframe"));
		    	waitTillElementIsPresent(driver, strTableLabel, intMaxWait);			
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
		        
		        ClickUsingJS(driver, strMyBillsLink);
				MDC.put("EventType","Click");
				MDC.put("EventData","Success");
				log.info("Clicked on My Bills Link");
				
				waitTillElementIsPresent(driver, strBillDetailsLabel, intMaxWait);
				
				ClickUsingJS(driver, strBillsLink);
				MDC.put("EventType","Click");
				MDC.put("EventData","Success");
				log.info("Clicked on View,Download link");
				SwitchToWindow(driver,log);
				ImplicitWait(driver, intMaxWait);
				waitTillElementIsPresent(driver, strBillsTable, intMaxWait);
				
				List<WebElement> lst=driver.findElementsByXPath("//table[@class='tableborder']//tr[contains(@class,'griddata')]");
				
				for(int i=2;i<=lst.size()+1;i++)
				{
					
				 ClickUsingJS(driver,"//tr["+i+"]//td//a[contains(text(),'View')]");
				
				}
				SwitchToParentWindow(driver,log);
				
				}catch(Exception e){
					MDC.put("EventType","DataDownload");
					MDC.put("EventData","Failure");
					log.error("Data download is failed for IdeaPostpaid: "+e.getMessage());
					return false;
					
					}
		    ImplicitWait(driver, 30000);
		    return true;
		    }
		
		public static boolean initiateIdeaPostpaid(String strTo, String strFrom, String strCc, String strUserName, String subject, String SubjectPwd, String body, String bodyPwd, String strPasswordToZip, Logger log, String strFForderNumber,String strUserNAme,String strPhoneNumber,String seqNo) throws IOException, ParseException{
			
			JFrame jfrm =showPreLoader();
			MDC.put("EventType", "WebsiteLogin");
			MDC.put("EventData", "Success");
			log.info("Logging in to the IdeaPostpaid website");
					
			FirefoxDriver driver = initiate("IdeaPostpaid",strloginurl,log,strUserName,strFForderNumber,seqNo);
			String strBaseDir = Generic.getBase();
			log.info("Navigated to IdeaPostpaid url :"+strloginurl);		
			driver.manage().deleteAllCookies();
			JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("BasicInfo");
			jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("DriveConfig");
			String strDriveBaseDir = jObj.get("driveBaseDir").toString();		
			boolean dlUnsuccessful= false;
			
			try{
				log.info("Started dlIdeaPostpaidBill");
				closePreLoader(jfrm);
				waitTillElementIsPresent(driver, strLoginBtn, intMaxWait);
				if(!dlIdeaPostpaidBill(driver, strBaseDir,log, strUserName,  strFForderNumber,  seqNo)){
					dlUnsuccessful=true;
					IdeaPostpaidLogOut(driver, log, strUserName,  strFForderNumber,  seqNo);
					}
				}catch(Exception e){
					log.error("dlIdeaPostpaidBill: "+e.getMessage());
					dlUnsuccessful=true;
					IdeaPostpaidLogOut(driver, log, strUserNAme, strFForderNumber, seqNo);
					}
			
			if(!dlUnsuccessful){
				try{
					
					Thread.sleep(60000);
					MDC.put("EventType", "ZipFileCreation");
				    log.info("initiated zipFiles");
					zipFiles(strBaseDir, "IdeaPostpaid", strPasswordToZip);
					MDC.put("EventData","Success");
					log.info("Mail ZIP Password: "+strPasswordToZip);
					}catch(Exception e){
						MDC.put("EventData","Failure"+e.getMessage());
						log.error("zipFiles: "+e.getMessage());
						dlUnsuccessful=true;
						IdeaPostpaidLogOut(driver, log, strUserName,  strFForderNumber,  seqNo);
						}
				}
				//send Mail to the Borrower Mail id	
			if(!dlUnsuccessful){
				try{
					MDC.put("EventType", "EmailDownloadedFiles");
					log.info("Initiated sendMail");
					gMail.sendMessage(strUserName, strTo, strFrom, strCc,subject, body, strBaseDir + "/","IdeaPostpaid.zip",log);
					MDC.put("EventData","Success");
					log.info("Mail sent successfully to "+strTo);
					
					}catch(Exception e){
						MDC.put("EventData","Failure");
						log.error("Fail to send Mail: "+e.getMessage());
						dlUnsuccessful=true;
						IdeaPostpaidLogOut(driver, log, strUserName,  strFForderNumber,  seqNo);
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
							SendMailZippedPassword( strPhoneNumber, strUserNAme, "IdeaPostpaid", strPasswordToZip);
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
								SendMailZippedPassword(strPhoneNumber, strUserNAme, "IdeaPostpaid", strPasswordToZip);
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
				File fileToDelete = new File(strBaseDir + "/IdeaPostpaid.zip");
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
				arrList.add("IdeaPostpaid");
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
					IdeaPostpaidLogOut(driver, log, strUserName,  strFForderNumber,  seqNo);
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
					captureScreenshot(driver, strBaseDir+"./ErrorScreenshot/errIdeaPostpaid.png");
					MDC.put("EventData","Success");
					log.info("Screenshot captured Successfully"+strBaseDir+"./ErrorScreenshot/errIdeaPostpaid.png");
					driver.close();
					driver.quit();
					if(!exit("IdeaPostpaid",log,strUserName,strFForderNumber,seqNo)){
						showPopup("System can't delete the files beacause the file is in use");
						log.info("File couldn't be deleted");
						SendCleanUpUnsuccessfulMail(strUserName, strFrom, strCc, strFForderNumber, seqNo, "IdeaPostpaid",log);
						dlUnsuccessful= true;
					}
					log.info("Exit IdeaPostpaid");	

				}catch(Exception e){
					e.printStackTrace();
					MDC.put("EventType", "CaptureScreenShot");
					MDC.put("EventData","Failure");
					log.error("Failed to take screenshot");
					
					if(!exit("IdeaPostpaid",log,strUserName,strFForderNumber,seqNo)){
						showPopup("The clean exit was unsuccessful as the files to be deleted are in use. Please close all the files and clear the download folder.");
						log.info("File couldn't deleted");
						SendCleanUpUnsuccessfulMail(strUserName, strFrom, strCc, strFForderNumber, seqNo, "ReliancePower",log);
						dlUnsuccessful= true;
					}
					log.info("Exit IdeaPostpaid");	
				}
			
			}
			
			//Checking the status completed or Failed
			jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("BorrowerDetails");
			String strFFProductCode = jObj.get("FFProductCode").toString();
			if(!dlUnsuccessful){
				MDC.put("EventType","Datapull");
				MDC.put("EventData","Success");
				log.info("datapull for IdeaPostpaid is completed");
				Generic.writeToPostbackJson("./OutPut/Postback.json", strFForderNumber,strFFProductCode,"IdeaPostpaid",seqNo,googleDrivePath,"Success");
			}else{
				MDC.put("EventType","Datapull");
				MDC.put("EventData","Failure");
				log.info("datapull for IdeaPostpaid is Failed");
				Generic.writeToPostbackJson("./OutPut/Postback.json", strFForderNumber,strFFProductCode,"IdeaPostpaid",seqNo,googleDrivePath,"Success");
				}
			return dlUnsuccessful;
			}
		
		
public static boolean IdeaPostpaidLogOut(FirefoxDriver driver, Logger log,String strUserName, String strFForderNumber, String seqNo){
			
			MDC.put("EventType", "WebsiteLogout");
			log.info("initiated logout");
			boolean bStatus=false;
			try{		
				
				ClickUsingJS(driver, strlogoutBtn);			
				MDC.put("EventData","Success");
				log.info("Clicked Logout button");
				log.info("logged out successfully from "+strloginurl);
				waitAftereLogout(driver);
				exit("IdeaPostpaid",log,strUserName,strFForderNumber,seqNo);
				driver.close();
				log.info("Exit IdeaPostpaid");
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

package govt_ElectricityBoard;

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

public class Best  extends Generic {
	static String strloginurl="https://www.bestundertaking.net/ConsumerLogin.aspx?ReturnURL=quickpayment.aspx";	
	static String strLoginButton = "//input[@value='Login']";
	static String strInvalidCredentialsMsg = "//span[contains(text(),'Either the consumer number or password is incorrect.')]";
	static String strPayOnlinePage = "//div[contains(text(),'Please Enter your Consumer Number in the box below')]";
	static String strBillInfoLink = "//td//a[text()='Bill Info']";
	static String strBillInfoPage = "//span[text()='Bill Information']";
	static String strLogoutBtn = "//a[text()='Logout']";
	
	//Implicit Waits
		static int intMinWait=5;
		static int intMedWait=10;
		static int intMaxWait=1000;
		static String newline = System.getProperty("line.separator");
		static String googleDrivePath;
		
		
public static boolean dlBestBill(FirefoxDriver driver,String strBaseDir, Logger log,String strUserName, String strFForderNumber, String seqNo){
			MDC.put("EventType","DataDownloadStart");
			MDC.put("EventData","Success");
			log.info("Data dwonload started");
				
			ImplicitWait(driver);
			WebDriverWait wait = new WebDriverWait(driver, intMaxWait);
			try {
				waitTillElementIsPresent(driver,strLoginButton , intMaxWait);
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
		    		
		    		    if(driver.findElements(By.xpath(strInvalidCredentialsMsg)).size()>0){
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
		  				}else if(driver.findElements(By.xpath(strBillInfoLink)).size()>0){
		  					blnFlag=false;
		  				}else if(driver.findElements(By.xpath(strLoginButton)).size()>0){
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
		    	waitTillElementIsPresent(driver, strBillInfoLink, intMaxWait);			
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
		        
		        ClickUsingJS(driver, strBillInfoLink);
				MDC.put("EventType","Click");
				MDC.put("EventData","Success");
				log.info("Clicked on My Bills Link");
				
				waitTillElementIsPresent(driver, strBillInfoPage, intMaxWait);
								
				List<WebElement> lst=driver.findElementsByXPath("//*[@id='ctl00_Contentplaceholder2_ddlMonth']//option");
				
				for(int i=2;i<=lst.size();i++)
				{
				 Click(driver,"//*[@id='ctl00_Contentplaceholder2_ddlMonth']");
				
 				 Click(driver,"//*[@id='ctl00_Contentplaceholder2_ddlMonth']//option["+i+"]");
				
				 ImplicitWait(driver, intMinWait);
				 ImplicitWait(driver, intMinWait);
				 SwitchToWindow(driver,log);
					
				//ClickUsingJS(driver,"//*[@id='LinkButton1']");
				// waitTillElementIsPresent(driver, "//*[@id='LinkButton1']", intMedWait);
				 captureScreenshot(driver, strBaseDir+"/"+i+".png");
				 ImplicitWait(driver, intMinWait);
				 SwitchToParentWindow(driver,log);
				
				}
				ImplicitWait(driver, 30000);
			    return true;
				
				}catch(Exception e){
					MDC.put("EventType","DataDownload");
					MDC.put("EventData","Failure");
					log.error("Data download is failed for Best: "+e.getMessage());
					return false;
					
					}
		    
		    }
		
		public static boolean initiateBest(String strTo, String strFrom, String strCc, String strUserName, String subject, String SubjectPwd, String body, String bodyPwd, String strPasswordToZip, Logger log, String strFForderNumber,String strUserNAme,String strPhoneNumber,String seqNo) throws IOException, ParseException{
			
			JFrame jfrm =showPreLoader();
			MDC.put("EventType", "WebsiteLogin");
			MDC.put("EventData", "Success");
			log.info("Logging in to the Best Undertaking website");
					
			FirefoxDriver driver = initiate("Best",strloginurl,log,strUserName,strFForderNumber,seqNo);
			String strBaseDir = Generic.getBase();
			log.info("Navigated to Best Undertaking url :"+strloginurl);		
			driver.manage().deleteAllCookies();
			JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("BasicInfo");
			jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("DriveConfig");
			String strDriveBaseDir = jObj.get("driveBaseDir").toString();		
			boolean dlUnsuccessful= false;
			
			try{
				log.info("Started dlBestBill");
				closePreLoader(jfrm);
				waitTillElementIsPresent(driver, strLoginButton, intMaxWait);
				if(!dlBestBill(driver, strBaseDir,log, strUserName,  strFForderNumber,  seqNo)){
					dlUnsuccessful=true;
					BestLogOut(driver, log, strUserName,  strFForderNumber,  seqNo);
					}
				}catch(Exception e){
					log.error("dlBestBill: "+e.getMessage());
					dlUnsuccessful=true;
					BestLogOut(driver, log, strUserNAme, strFForderNumber, seqNo);
					}
			
			if(!dlUnsuccessful){
				try{
					
					Thread.sleep(60000);
					MDC.put("EventType", "ZipFileCreation");
				    log.info("initiated zipFiles");
					zipFiles(strBaseDir, "Best", strPasswordToZip);
					MDC.put("EventData","Success");
					log.info("Mail ZIP Password: "+strPasswordToZip);
					}catch(Exception e){
						MDC.put("EventData","Failure"+e.getMessage());
						log.error("zipFiles: "+e.getMessage());
						dlUnsuccessful=true;
						BestLogOut(driver, log, strUserName,  strFForderNumber,  seqNo);
						}
				}
				//send Mail to the Borrower Mail id	
			if(!dlUnsuccessful){
				try{
					MDC.put("EventType", "EmailDownloadedFiles");
					log.info("Initiated sendMail");
					gMail.sendMessage(strUserName, strTo, strFrom, strCc,subject, body, strBaseDir + "/","Best.zip",log);
					MDC.put("EventData","Success");
					log.info("Mail sent successfully to "+strTo);
					
					}catch(Exception e){
						MDC.put("EventData","Failure");
						log.error("Fail to send Mail: "+e.getMessage());
						dlUnsuccessful=true;
						BestLogOut(driver, log, strUserName,  strFForderNumber,  seqNo);
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
							SendMailZippedPassword( strPhoneNumber, strUserNAme, "Best", strPasswordToZip);
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
								SendMailZippedPassword(strPhoneNumber, strUserNAme, "Best", strPasswordToZip);
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
				File fileToDelete = new File(strBaseDir + "/Best.zip");
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
				arrList.add("Best");
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
					BestLogOut(driver, log, strUserName,  strFForderNumber,  seqNo);
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
					captureScreenshot(driver, strBaseDir+"./ErrorScreenshot/errBest.png");
					MDC.put("EventData","Success");
					log.info("Screenshot captured Successfully"+strBaseDir+"./ErrorScreenshot/errBest.png");
					driver.close();
					driver.quit();
					if(!exit("Best",log,strUserName,strFForderNumber,seqNo)){
						showPopup("System can't delete the files beacause the file is in use");
						log.info("File couldn't be deleted");
						SendCleanUpUnsuccessfulMail(strUserName, strFrom, strCc, strFForderNumber, seqNo, "Best",log);
						dlUnsuccessful= true;
					}
					log.info("Exit Best");	

				}catch(Exception e){
					e.printStackTrace();
					MDC.put("EventType", "CaptureScreenShot");
					MDC.put("EventData","Failure");
					log.error("Failed to take screenshot");
					
					if(!exit("Best",log,strUserName,strFForderNumber,seqNo)){
						showPopup("The clean exit was unsuccessful as the files to be deleted are in use. Please close all the files and clear the download folder.");
						log.info("File couldn't deleted");
						SendCleanUpUnsuccessfulMail(strUserName, strFrom, strCc, strFForderNumber, seqNo, "Best",log);
						dlUnsuccessful= true;
					}
					log.info("Exit Best");	
				}
			
			}
			
			//Checking the status completed or Failed
			jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("BorrowerDetails");
			String strFFProductCode = jObj.get("FFProductCode").toString();
			if(!dlUnsuccessful){
				MDC.put("EventType","Datapull");
				MDC.put("EventData","Success");
				log.info("datapull for Best is completed");
				Generic.writeToPostbackJson("./OutPut/Postback.json", strFForderNumber,strFFProductCode,"Best",seqNo,googleDrivePath,"Success");
			}else{
				MDC.put("EventType","Datapull");
				MDC.put("EventData","Failure");
				log.info("datapull for Best is Failed");
				Generic.writeToPostbackJson("./OutPut/Postback.json", strFForderNumber,strFFProductCode,"Best",seqNo,googleDrivePath,"Success");
				}
			System.out.println(dlUnsuccessful);
			return dlUnsuccessful;
			}
		
		
public static boolean BestLogOut(FirefoxDriver driver, Logger log,String strUserName, String strFForderNumber, String seqNo){
			
			MDC.put("EventType", "WebsiteLogout");
			log.info("initiated logout");
			boolean bStatus=false;
			try{		
				
				ClickUsingJS(driver, strLogoutBtn);			
				MDC.put("EventData","Success");
				log.info("Clicked Logout button");
				log.info("logged out successfully from "+strloginurl);
				waitAftereLogout(driver);
				exit("Best",log,strUserName,strFForderNumber,seqNo);
				driver.close();
				log.info("Exit Best");
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



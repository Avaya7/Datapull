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

public class ReliancePostPaid  extends Generic {
	
	/*Xpaths of Objects*/
	static String strloginurl="http://myservices.relianceada.com/launchAMSS.do";
	static String popupCloseBtn=".//*[@id='dialog' and contains(@style,'display: block;')]/span[text()='x']";
	static String BillingSummaryTable="//div[div[text()='My Billing Summary']]/following-sibling::table[@id='poptable']";
	static String loginBtn="//a[@class='ovalbutton2']/span[text()='Login']";
	static String logoutBtn=".//*[@id='welcomeuser']/a/img[contains(@src,'logout.jpg')]";
	static String InvalidCredential="//div[@class='errorMessages']//li[text()='Login failed. Please check your username and password and try again..']";
	static String InvalidMsg="//div[@class='errorMessages']//li[text()='The system could not process your request at this moment. Please try again later.']";
	
	/*WebElements*/	
	static WebElement YrDropdown;
	//Implicit Waits
		static int intMinWait=5;
		static int intMedWait=10;
		static int intMaxWait=1000;
		static String newline = System.getProperty("line.separator");
		static String googleDrivePath;
		
		
public static boolean dlReliancePostpaidBill(FirefoxDriver driver,String strBaseDir, Logger log,String strUserName, String strFForderNumber, String seqNo){
	 boolean blnDlStatus= false;
     try{  
		    MDC.put("EventType","DataDownloadStart");
			MDC.put("EventData","Success");
			log.info("Data dwonload started");
				
			ImplicitWait(driver);
			WebDriverWait wait = new WebDriverWait(driver, intMaxWait);
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
		    	
		   try{
			   	ImplicitWait(driver,intMedWait);
			  
		    	if((driver.findElements(By.xpath(InvalidCredential)).size()>0 || driver.findElements(By.xpath(InvalidMsg)).size()>0)){		    		
		    	
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

		  		}else if(exists(driver, loginBtn)==true){
		  			blnFlag=true;
		  			}else if( exists(driver, BillingSummaryTable)==true){
		  				blnFlag=false;
		  				}else{
		  					log.info("Navigated to wrong link.");
		  					showPopup("Navigated to wrong link. The system will now redirect you to homepage.");
		  					driver.get(strloginurl);
		  					blnFlag=true;
		                }
		  			
		  		
		   }catch(Exception e){
			   e.getMessage();
			   }
		   }

		   
		    try{
		    	
		    		waitTillElementIsPresent(driver, BillingSummaryTable, intMaxWait);
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
		        
		        MDC.put("EventType","DataDownloadStart");
				MDC.put("EventData","Success");
				log.info("Data dwonload started");
				
				if(driver.findElementsByXPath(popupCloseBtn).size()>0){
					Click(driver, popupCloseBtn);
					log.info("Closed the Unexpected Alert popup");
				}
				String MyBillingLink="//a[@title='My Billing']";
				String ViewBillLink="//a[@title='View Bill']";
				String BillTable=".//*[@id='poptable']/tbody/tr[td]";
				
				Actions act= new Actions(driver);
				act.moveToElement(driver.findElement(By.xpath(MyBillingLink))).build().perform();
				act.moveToElement(driver.findElementByXPath(ViewBillLink)).build().perform();
				Click(driver, ViewBillLink);
				
				
				List <WebElement> lst=driver.findElementsByXPath(BillTable);
				log.info("Bill available for "+lst.size()+" month");
				
				for(int i =2;i<=lst.size()+1;i++ ){
					
					Click(driver, ".//*[@id='poptable']/tbody/tr["+i+"]//a[text()='Download Bills']");
					String RelationshipNo=driver.findElementByXPath(".//*[@id='poptable']/tbody/tr["+i+"]//td[2]").getText();
					String InvoiceNo=driver.findElementByXPath(".//*[@id='poptable']/tbody/tr["+i+"]//td[6]").getText();
					String BillingDate=driver.findElementByXPath(".//*[@id='poptable']/tbody/tr["+i+"]//td[5]").getText();
					//waitUntilFileDownloaded(strBaseDir+"/"+RelationshipNo+"_"+InvoiceNo+".pdf", log);
					
					 log.info("Clicked on the Download bill button");
					 log.info(RelationshipNo+"_"+InvoiceNo+".pdf file is download Billing on "+BillingDate);
					 log.info("Relationship No is:"+RelationshipNo+"and the Invoice no is "+InvoiceNo);
										 
				}
				blnDlStatus=true;
				Thread.sleep(2000);
		    }catch(Exception e){
					MDC.put("EventType","DataDownload");
					MDC.put("EventData","Failure");
					log.error("Data download is failed for ReliancePostpaid: "+e.getMessage());
					blnDlStatus= false;
					System.out.println(blnDlStatus);
					}
		    
     }catch(Exception e){
				MDC.put("EventType","DataDownload");
				MDC.put("EventData","Failure");
				log.error("Data download is failed for ReliancePostpaid: "+e.getMessage());
				blnDlStatus= false;
				}
     return blnDlStatus;
     }
		
public static boolean initiateReliancePostpaid(String strTo, String strFrom, String strCc, String strUserName, String subject, String SubjectPwd, String body, String bodyPwd, String strPasswordToZip, Logger log, String strFForderNumber,String strUserNAme,String strPhoneNumber,String seqNo) throws IOException, ParseException{
			
			JFrame jfrm =showPreLoader();
			MDC.put("EventType", "WebsiteLogin");
			MDC.put("EventData", "Success");
			log.info("Logging in to the ReliancePostpaid website");
					
			FirefoxDriver driver = initiate("ReliancePostpaid",strloginurl,log,strUserName,strFForderNumber,seqNo);
			String strBaseDir = Generic.getBase();
			log.info("Navigated to ReliancePostpaid url :"+strloginurl);		
			driver.manage().deleteAllCookies();
			JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("BasicInfo");
			jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("DriveConfig");
			String strDriveBaseDir = jObj.get("driveBaseDir").toString();		
			boolean dlUnsuccessful= false;
			
			try{
				log.info("Started dlReliancePostpaidBill");
				closePreLoader(jfrm);
				waitTillElementIsPresent(driver, loginBtn, intMaxWait);
				if(!dlReliancePostpaidBill(driver, strBaseDir,log, strUserName,  strFForderNumber,  seqNo)){
					dlUnsuccessful=true;
					ReliancePostpaidLogOut(driver, log, strUserName,  strFForderNumber,  seqNo);
					}
				}catch(Exception e){
					log.error("dlReliancePostpaidBill: "+e.getMessage());
					dlUnsuccessful=false;
					ReliancePostpaidLogOut(driver, log, strUserNAme, strFForderNumber, seqNo);
					}
			
			if(!dlUnsuccessful){
				try{
					
					ImplicitWait(driver, 2000);
					MDC.put("EventType", "ZipFileCreation");
				    log.info("initiated zipFiles");
					zipFiles(strBaseDir, "ReliancePostpaid", strPasswordToZip);
					MDC.put("EventData","Success");
					log.info("Mail ZIP Password: "+strPasswordToZip);
					}catch(Exception e){
						MDC.put("EventData","Failure"+e.getMessage());
						log.error("zipFiles: "+e.getMessage());
						dlUnsuccessful=true;
						ReliancePostpaidLogOut(driver, log, strUserName,  strFForderNumber,  seqNo);
						}
				}
				//send Mail to the Borrower Mail id	
			if(!dlUnsuccessful){
				try{
					MDC.put("EventType", "EmailDownloadedFiles");
					log.info("Initiated sendMail");
					gMail.sendMessage(strUserName, strTo, strFrom, strCc,subject, body, strBaseDir + "/","ReliancePostpaid.zip",log);
					MDC.put("EventData","Success");
					log.info("Mail sent successfully to "+strTo);
					
					}catch(Exception e){
						MDC.put("EventData","Failure");
						log.error("Fail to send Mail: "+e.getMessage());
						dlUnsuccessful=true;
						ReliancePostpaidLogOut(driver, log, strUserName,  strFForderNumber,  seqNo);
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
							SendMailZippedPassword( strPhoneNumber, strUserNAme, "ReliancePostpaid", strPasswordToZip);
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
								SendMailZippedPassword(strPhoneNumber, strUserNAme, "ReliancePostpaid", strPasswordToZip);
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
				File fileToDelete = new File(strBaseDir + "/ReliancePostpaid.zip");
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
				arrList.add("ReliancePostpaid");
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
					ReliancePostpaidLogOut(driver, log, strUserName,  strFForderNumber,  seqNo);
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
					captureScreenshot(driver, strBaseDir+"./ErrorScreenshot/errReliancePostpaid.png");
					MDC.put("EventData","Success");
					log.info("Screenshot captured Successfully"+strBaseDir+"./ErrorScreenshot/errReliancePostpaid.png");
					driver.close();
					driver.quit();
					if(!exit("ReliancePostpaid",log,strUserName,strFForderNumber,seqNo)){
						showPopup("System can't delete the files beacause the file is in use");
						log.info("File couldn't be deleted");
						SendCleanUpUnsuccessfulMail(strUserName, strFrom, strCc, strFForderNumber, seqNo, "ReliancePostpaid",log);
						dlUnsuccessful= true;
					}
					log.info("Exit ReliancePostpaid");	

				}catch(Exception e){
					e.printStackTrace();
					MDC.put("EventType", "CaptureScreenShot");
					MDC.put("EventData","Failure");
					log.error("Failed to take screenshot");
					
					if(!exit("ReliancePostpaid",log,strUserName,strFForderNumber,seqNo)){
						showPopup("The clean exit was unsuccessful as the files to be deleted are in use. Please close all the files and clear the download folder.");
						log.info("File couldn't deleted");
						SendCleanUpUnsuccessfulMail(strUserName, strFrom, strCc, strFForderNumber, seqNo, "ReliancePower",log);
						dlUnsuccessful= true;
					}
					log.info("Exit ReliancePostpaid");	
				}
			
			}
			
			//Checking the status completed or Failed
			jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("BorrowerDetails");
			String strFFProductCode = jObj.get("FFProductCode").toString();
			if(!dlUnsuccessful){
				MDC.put("EventType","Datapull");
				MDC.put("EventData","Success");
				log.info("datapull for ReliancePostpaid is completed");
				Generic.writeToPostbackJson("./OutPut/Postback.json", strFForderNumber,strFFProductCode,"ReliancePostpaid",seqNo,googleDrivePath,"Success");
			}else{
				MDC.put("EventType","Datapull");
				MDC.put("EventData","Failure");
				log.info("datapull for ReliancePostpaid is Failed");
				Generic.writeToPostbackJson("./OutPut/Postback.json", strFForderNumber,strFFProductCode,"ReliancePostpaid",seqNo,googleDrivePath,"Success");
				}
			return dlUnsuccessful;
			}
		
		
		public static boolean ReliancePostpaidLogOut(FirefoxDriver driver, Logger log,String strUserName, String strFForderNumber, String seqNo){
			
			MDC.put("EventType", "WebsiteLogout");
			log.info("initiated logout");
			boolean bStatus=false;
			try{
				ClickUsingJS(driver, logoutBtn);
				MDC.put("EventData","Success");
				log.info("Clicked Logout button");
				log.info("logged out successfully from "+strloginurl);
				waitAftereLogout(driver);
				exit("ReliancePostpaid",log,strUserName,strFForderNumber,seqNo);
				driver.close();
				log.info("Exit ReliancePostpaid");
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

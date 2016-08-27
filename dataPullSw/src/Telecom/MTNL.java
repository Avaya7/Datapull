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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.api.services.drive.Drive;

import Generic.Generic;

public class MTNL  extends Generic {
	
	/*Xpaths of Objects*/
	static String strloginurl="http://selfcare.mtnl.net.in/mumbai/Duplicate_BillReceipt.aspx";
	static String StrDuplicateBillHeading="//p[text()='Duplicate Receipt for Landline Bill Payment']";
	static String strSubmitBtn="//input[@id='ContentPlaceHolder1_btnsubmit']";
	static String strSavePDFBtn="//input[@id='btnpdf']";
	static String strmonthDDL="//select[@id='ContentPlaceHolder1_DropDownList1']";
	static String strYearDDL="//select[@id='ContentPlaceHolder1_DropDownList2']";
	static String strCancelBtn=".//*[@id='ContentPlaceHolder1_btncancel']";
	static String strDatanotFoundErr="//span[text()='Data is not found in our Database']";
	static String strCorrectMonThYrErr="//span[text()='Please select correct month and year']";
	static String strInvalidCred="//span[text()='Object reference not set to an instance of an object.']";
	/*WebElements*/	
	static WebElement YrDropdown;
	//Implicit Waits
		static int intMinWait=5;
		static int intMedWait=10;
		static int intMaxWait=1000;
		static String newline = System.getProperty("line.separator");
		static String googleDrivePath;
		
		
public static boolean dlmtnlBill(FirefoxDriver driver,String strBaseDir, Logger log,String strUserName, String strFForderNumber, String seqNo){
	 boolean blnDlStatus= false;
     try{  
		    MDC.put("EventType","DataDownloadStart");
			MDC.put("EventData","Success");
			log.info("Data dwonload started");
				
			ImplicitWait(driver);
			WebDriverWait wait = new WebDriverWait(driver, intMaxWait);
			waitTillElementIsPresent(driver,strSubmitBtn , intMaxWait);				
			
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
		    	if((driver.findElements(By.xpath(strInvalidCred)).size()>0)){
		    		
		    	
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
		  			 if(exists(driver, StrDuplicateBillHeading)==false){
						   log.info("Navigated to wrong link.");
		                   showPopup("Navigated to wrong link. The system will now redirect you to homepage.");
		                   driver.get(strloginurl);
		                   blnFlag=true;
					   }else{
						   blnFlag=false;
					   }
		  			
		  		}
		   }catch(Exception e){
			   e.getMessage();
			   if(exists(driver, StrDuplicateBillHeading)==false){
				   log.info("Navigated to wrong link.");
                   showPopup("Navigated to wrong link. The system will now redirect you to homepage.");
                   driver.get(strloginurl);
                   blnFlag=true;
			   }else{
				   blnFlag=false;
			   }
			   
		   }
		    	
		    }
		    try{
		    	boolean bls=true;
		    	while(bls){
		    	if(driver.findElementById("ContentPlaceHolder1_txttel").getAttribute("value").length()>0 && driver.findElementById("ContentPlaceHolder1_txttel").getAttribute("value").length()>0 ){
		    		//waitTillElementIsPresent(driver, strBillDetails, intMaxWait);		
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
				
		        YrDropdown=driver.findElementByXPath(strYearDDL);
		        Select YrSel=new Select(YrDropdown);
		        List<WebElement> Yli=YrSel.getOptions();
		        
		        for(int i=1;i<=Yli.size()-1;i++){
		        	YrDropdown=driver.findElementByXPath(strYearDDL);
		        	 YrSel=new Select(YrDropdown);
		        	 YrSel.selectByIndex(i);
		        	 Yli=YrSel.getOptions();
		        	 String Year=Yli.get(i).getText();
		        	 
				        
		        	for(int j=1;j<=12;j++){
		        				
		        				WebElement MonDropdown=driver.findElementByXPath(strmonthDDL);
		 				        Select MonSel=new Select(MonDropdown);
		 				        List<WebElement> Mli=MonSel.getOptions();
		 				        MonSel.selectByIndex(j);
		 				        String MonthName=Mli.get(j).getText();
				        		Click(driver, strSubmitBtn);
				        		
				        		
				        		if(driver.findElementsByXPath(strDatanotFoundErr).size()>0){
				        			
				        		}else if(driver.findElementsByXPath(strSavePDFBtn).size()>0){
				        			
					        		Click(driver, strSavePDFBtn);
					        		log.info("Clicked On the SavePDF button");
					        		String transactionId=driver.findElementByXPath("//td[span[text()='Transaction ID :']]/following-sibling::td//span").getText();
					        		waitUntilFileDownloaded(strBaseDir+"/"+transactionId+".pdf", log);
					        		
					        		log.info(transactionId+".pdf file is downloaded for Month : "+MonthName+" and Year: "+Year);
					        		driver.navigate().back();
					        		}else if(driver.findElementsByXPath(strCorrectMonThYrErr).size()>0){
					        			break;
					        		}
				        		}
		        	}			
					MDC.put("EventType","DataDownloadComplete");
					MDC.put("EventData","Success");
					log.info("Data download completed for MTNL");
				bls= false;	
		    	}else{
		    		showPopup("Please Enter Credentials To download the Bill Details");
		    		Thread.sleep(50000);
		    		bls=true;
		    	}
		    	
		    }
		    }catch(Exception e){
					MDC.put("EventType","DataDownload");
					MDC.put("EventData","Failure");
					log.error("Data download is failed for MTNL: "+e.getMessage());
					blnDlStatus= false;
					System.out.println(blnDlStatus);
					}
		    
		   
     }catch(Exception e){
				MDC.put("EventType","DataDownload");
				MDC.put("EventData","Failure");
				log.error("Data download is failed for MTNL: "+e.getMessage());
				blnDlStatus= false;
				}
     return blnDlStatus;
     }
		
		public static boolean initiatemtnl(String strTo, String strFrom, String strCc, String strUserName, String subject, String SubjectPwd, String body, String bodyPwd, String strPasswordToZip, Logger log, String strFForderNumber,String strUserNAme,String strPhoneNumber,String seqNo) throws IOException, ParseException{
			
			JFrame jfrm =showPreLoader();
			MDC.put("EventType", "WebsiteLogin");
			MDC.put("EventData", "Success");
			log.info("Logging in to the MTNL website");
					
			FirefoxDriver driver = initiate("MTNL",strloginurl,log,strUserName,strFForderNumber,seqNo);
			String strBaseDir = Generic.getBase();
			log.info("Navigated to MTNL url :"+strloginurl);		
			driver.manage().deleteAllCookies();
			JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("BasicInfo");
			jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("DriveConfig");
			String strDriveBaseDir = jObj.get("driveBaseDir").toString();		
			boolean dlUnsuccessful= false;
			
			try{
				log.info("Started dlmtnlBill");
				closePreLoader(jfrm);
				waitTillElementIsPresent(driver, strSubmitBtn, intMaxWait);
				if(!dlmtnlBill(driver, strBaseDir,log, strUserName,  strFForderNumber,  seqNo)){
					dlUnsuccessful=true;
					mtnlLogOut(driver, log, strUserName,  strFForderNumber,  seqNo);
					}
				}catch(Exception e){
					log.error("dlmtnlBill: "+e.getMessage());
					dlUnsuccessful=false;
					mtnlLogOut(driver, log, strUserNAme, strFForderNumber, seqNo);
					}
			
			if(!dlUnsuccessful){
				try{
					
					ImplicitWait(driver, 2000);
					MDC.put("EventType", "ZipFileCreation");
				    log.info("initiated zipFiles");
					zipFiles(strBaseDir, "MTNL", strPasswordToZip);
					MDC.put("EventData","Success");
					log.info("Mail ZIP Password: "+strPasswordToZip);
					}catch(Exception e){
						MDC.put("EventData","Failure"+e.getMessage());
						log.error("zipFiles: "+e.getMessage());
						dlUnsuccessful=true;
						mtnlLogOut(driver, log, strUserName,  strFForderNumber,  seqNo);
						}
				}
				//send Mail to the Borrower Mail id	
			if(!dlUnsuccessful){
				try{
					MDC.put("EventType", "EmailDownloadedFiles");
					log.info("Initiated sendMail");
					gMail.sendMessage(strUserName, strTo, strFrom, strCc,subject, body, strBaseDir + "/","MTNL.zip",log);
					MDC.put("EventData","Success");
					log.info("Mail sent successfully to "+strTo);
					
					}catch(Exception e){
						MDC.put("EventData","Failure");
						log.error("Fail to send Mail: "+e.getMessage());
						dlUnsuccessful=true;
						mtnlLogOut(driver, log, strUserName,  strFForderNumber,  seqNo);
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
							SendMailZippedPassword( strPhoneNumber, strUserNAme, "MTNL", strPasswordToZip);
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
								SendMailZippedPassword(strPhoneNumber, strUserNAme, "MTNL", strPasswordToZip);
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
				File fileToDelete = new File(strBaseDir + "/MTNL.zip");
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
				arrList.add("MTNL");
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
					mtnlLogOut(driver, log, strUserName,  strFForderNumber,  seqNo);
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
					captureScreenshot(driver, strBaseDir+"./ErrorScreenshot/errmtnl.png");
					MDC.put("EventData","Success");
					log.info("Screenshot captured Successfully"+strBaseDir+"./ErrorScreenshot/errmtnl.png");
					driver.close();
					driver.quit();
					if(!exit("MTNL",log,strUserName,strFForderNumber,seqNo)){
						showPopup("System can't delete the files beacause the file is in use");
						log.info("File couldn't be deleted");
						SendCleanUpUnsuccessfulMail(strUserName, strFrom, strCc, strFForderNumber, seqNo, "MTNL",log);
						dlUnsuccessful= true;
					}
					log.info("Exit MTNL");	

				}catch(Exception e){
					e.printStackTrace();
					MDC.put("EventType", "CaptureScreenShot");
					MDC.put("EventData","Failure");
					log.error("Failed to take screenshot");
					
					if(!exit("MTNL",log,strUserName,strFForderNumber,seqNo)){
						showPopup("The clean exit was unsuccessful as the files to be deleted are in use. Please close all the files and clear the download folder.");
						log.info("File couldn't deleted");
						SendCleanUpUnsuccessfulMail(strUserName, strFrom, strCc, strFForderNumber, seqNo, "ReliancePower",log);
						dlUnsuccessful= true;
					}
					log.info("Exit MTNL");	
				}
			
			}
			
			//Checking the status completed or Failed
			jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("BorrowerDetails");
			String strFFProductCode = jObj.get("FFProductCode").toString();
			if(!dlUnsuccessful){
				MDC.put("EventType","Datapull");
				MDC.put("EventData","Success");
				log.info("datapull for MTNL is completed");
				Generic.writeToPostbackJson("./OutPut/Postback.json", strFForderNumber,strFFProductCode,"MTNL",seqNo,googleDrivePath,"Success");
			}else{
				MDC.put("EventType","Datapull");
				MDC.put("EventData","Failure");
				log.info("datapull for MTNL is Failed");
				Generic.writeToPostbackJson("./OutPut/Postback.json", strFForderNumber,strFFProductCode,"MTNL",seqNo,googleDrivePath,"Success");
				}
			return dlUnsuccessful;
			}
		
		
		public static boolean mtnlLogOut(FirefoxDriver driver, Logger log,String strUserName, String strFForderNumber, String seqNo){
			
			MDC.put("EventType", "WebsiteLogout");
			log.info("initiated logout");
			boolean bStatus=false;
			try{
				ClickUsingJS(driver, strCancelBtn);
				MDC.put("EventData","Success");
				log.info("Clicked Logout button");
				log.info("logged out successfully from "+strloginurl);
				waitAftereLogout(driver);
				exit("MTNL",log,strUserName,strFForderNumber,seqNo);
				driver.close();
				log.info("Exit MTNL");
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

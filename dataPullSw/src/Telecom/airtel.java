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
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.api.services.drive.Drive;

import Generic.Generic;

public class airtel  extends Generic {
	static String strloginurl="https://www.airtel.in/personal/myaccount/home";
	static String StrOtpPasswordBtn="//a[@id='one']";
	static String strloginBtn="//div[@id='websealdiv' and contains(@style,'display: block')]//img[@alt='Sign in']";
	static String strBillDetails="//a[text()='Bill Details']";
	static String strBillDetailsPopup="//*[@id='check_user_type']";
	static String strSelectBillDropdown="//div[@id='select_period_msdd']";
	static String strSaveBillBtn="//a[@title='Save Bill']";
	static String strAccountStatementTable="//table[@id='your_account_staement']";
	static String strBackToHomeBtn="//a[text()='back to home']";
	static String strlogoutBtn="//a[text()='logout']";
	
	
	//Implicit Waits
		static int intMinWait=5;
		static int intMedWait=10;
		static int intMaxWait=1000;
		static String newline = System.getProperty("line.separator");
		static String googleDrivePath;
		
		
public static boolean dlairtelBill(FirefoxDriver driver,String strBaseDir, Logger log,String strUserName, String strFForderNumber, String seqNo){
	 
     try{  
			MDC.put("EventType","DataDownloadStart");
			MDC.put("EventData","Success");
			log.info("Data dwonload started");
				
			ImplicitWait(driver);
			WebDriverWait wait = new WebDriverWait(driver, intMaxWait);
			try {
				waitTillElementIsPresent(driver,StrOtpPasswordBtn , intMaxWait);
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
		    			waitTillElementIsPresent(driver, strloginBtn, intMinWait);
		    		    if((driver.switchTo().alert() != null)){
		    		    	log.info("alert was present");
							Alert alt=driver.switchTo().alert();
							String strAlertMsg= alt.getText();
							if(strAlertMsg.trim().replaceAll("\\n"," ").equalsIgnoreCase("Authentication failed! You have keyed in an invalid user name, password or client certificate.")){
								alt.accept();
								showPopup("Invalid Login attempt.Try again !");
								driver.get(strloginurl);
								intCount ++;
			  				if(intCount>=3){
			  					showPopup("Maximum number of allowed attempt over. The Utility will exit now.");
					  			driver.close();
					  			driver.quit();
					  			exit("all",log, strUserName,strFForderNumber,seqNo);
					  			System.exit(0);
					  			break;
					  			}
			  				}
							}
							
		            }catch(Exception e){
		            	e.getMessage();
		            	if(exists(driver, strBillDetails)==false){
		            		 if(exists(driver, strloginBtn)==true){
		            			showPopup("Please Enter the Password");
			            		Thread.sleep(2000);
			            		if(exists(driver, strBillDetails)==true){
			  						blnFlag=false;
	                    }
			            		
			            		}else{
				  					log.info("Navigated to wrong link.");
				                    showPopup("Navigated to wrong link. The system will now redirect you to homepage.");
				                    driver.get(strloginurl);
				                    blnFlag=true;
			            		}
		            		 if(exists(driver, strBillDetails)==true){
			  						blnFlag=false;
			  					}
		                    }else if(exists(driver, strBillDetails)==true){
				  						blnFlag=false;
		                    }
		            	
		            	}	    	
		    	
		    	
		    }
		    try{
		    	
		    	if(exists(driver, "//div[starts-with(@id,'errorBlock') and contains(text(),'We are currently unable to fetch your details.')]")){
		    		showPopup("We are currently unable to fetch your details... due to unavoidable issues Please try after sometimes");
		    		return false;
		    	}
		    	//	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("portlet2_iframe"));
		    	waitTillElementIsPresent(driver, strBillDetails, intMaxWait);			
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
		        
				Click(driver, strBillDetails);
				MDC.put("EventType","DataDownload");
				MDC.put("EventData","Success");
				log.info("Bill and payment link clicked");
				
				waitTillElementIsPresent(driver, strBillDetailsPopup, intMaxWait);
				ImplicitWait(driver);
				log.info("Bill Details link has been clicked");
				
				
				//Click(driver,strSelectBillDropdown);
				List<WebElement> list=driver.findElements(By.xpath("//div[@id='select_period_child']//li"));
				System.out.println("Bill Available for last "+list.size()+ " months");
				for(WebElement e: list){
					
					ImplicitWait(driver);					
					Click(driver,strSelectBillDropdown);					
					String MonthName=e.getText();
					ImplicitWait(driver);
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",e);	
					e.click();
					ImplicitWait(driver);	
					WebElement SaveBtn=driver.findElement(By.xpath(strSaveBillBtn));
					SaveBtn.click();
					ImplicitWait(driver);
					log.info("Clicked On the download Bill button for Month : "+MonthName);
									
					
					/**get the Bill no And Bill date Details and Print in the Log**/
					
					String BillNo=driver.findElement(By.xpath(strAccountStatementTable+"//tr[3]/td[2]")).getText();
					String BillingDate=driver.findElement(By.xpath(strAccountStatementTable+"//tr[3]/td[4]")).getText();
					log.info("The downloaded Bill no is :"+BillNo+" and BillingDate is "+ BillingDate);
					
					}
				MDC.put("EventType","DataDownloadComplete");
				MDC.put("EventData","Success");
				log.info("Data download completed for Airtel");
				}catch(Exception e){
					
					MDC.put("EventType","DataDownload");
					MDC.put("EventData","Failure");
					log.error("Data download is failed for Airtel: "+e.getMessage());
					return false;
					}
		    ImplicitWait(driver, 50000);
		    return true;
		    
		    }catch(Exception e){
		    	MDC.put("EventType","DataDownload");
				MDC.put("EventData","Failure");
				log.error("Data download is failed for Airtel: "+e.getMessage());
				return false;
				
		    	}
		    }
		
		public static boolean initiateAirtel(String strTo, String strFrom, String strCc, String strUserName, String subject, String SubjectPwd, String body, String bodyPwd, String strPasswordToZip, Logger log, String strFForderNumber,String strUserNAme,String strPhoneNumber,String seqNo) throws IOException, ParseException{
			
			JFrame jfrm =showPreLoader();
			MDC.put("EventType", "WebsiteLogin");
			MDC.put("EventData", "Success");
			log.info("Logging in to the airtel website");
					
			FirefoxDriver driver = initiate("airtel",strloginurl,log,strUserName,strFForderNumber,seqNo);
			String strBaseDir = Generic.getBase();
			log.info("Navigated to airtel url :"+strloginurl);		
			driver.manage().deleteAllCookies();
			JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("BasicInfo");
			jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("DriveConfig");
			String strDriveBaseDir = jObj.get("driveBaseDir").toString();		
			boolean dlUnsuccessful= false;
			
			try{
				log.info("Started dlairtelBill");
				closePreLoader(jfrm);
				waitTillElementIsPresent(driver, StrOtpPasswordBtn, intMaxWait);
				if(!dlairtelBill(driver, strBaseDir,log, strUserName,  strFForderNumber,  seqNo)){
					dlUnsuccessful=true;
					airtelLogOut(driver, log, strUserName,  strFForderNumber,  seqNo);
					}
				}catch(Exception e){
					log.error("dlairtelBill: "+e.getMessage());
					dlUnsuccessful=true;
					airtelLogOut(driver, log, strUserNAme, strFForderNumber, seqNo);
					}
			
			if(!dlUnsuccessful){
				try{
					
					Thread.sleep(50000);
					MDC.put("EventType", "ZipFileCreation");
				    log.info("initiated zipFiles");
					zipFiles(strBaseDir, "Airtel", strPasswordToZip);
					MDC.put("EventData","Success");
					log.info("Mail ZIP Password: "+strPasswordToZip);
					}catch(Exception e){
						MDC.put("EventData","Failure"+e.getMessage());
						log.error("zipFiles: "+e.getMessage());
						dlUnsuccessful=true;
						airtelLogOut(driver, log, strUserName,  strFForderNumber,  seqNo);
						}
				}
				//send Mail to the Borrower Mail id	
			if(!dlUnsuccessful){
				try{
					MDC.put("EventType", "EmailDownloadedFiles");
					log.info("Initiated sendMail");
					gMail.sendMessage(strUserName, strTo, strFrom, strCc,subject, body, strBaseDir + "/","Airtel.zip",log);
					MDC.put("EventData","Success");
					log.info("Mail sent successfully to "+strTo);
					
					}catch(Exception e){
						MDC.put("EventData","Failure");
						log.error("Fail to send Mail: "+e.getMessage());
						dlUnsuccessful=true;
						airtelLogOut(driver, log, strUserName,  strFForderNumber,  seqNo);
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
							SendMailZippedPassword( strPhoneNumber, strUserNAme, "Airtel", strPasswordToZip);
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
								SendMailZippedPassword(strPhoneNumber, strUserNAme, "Airtel", strPasswordToZip);
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
				File fileToDelete = new File(strBaseDir + "/Airtel.zip");
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
				arrList.add("Airtel");
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
					airtelLogOut(driver, log, strUserName,  strFForderNumber,  seqNo);
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
					captureScreenshot(driver, strBaseDir+"./ErrorScreenshot/errAirtel.png");
					MDC.put("EventData","Success");
					log.info("Screenshot captured Successfully"+strBaseDir+"./ErrorScreenshot/errAirtel.png");
					driver.close();
					driver.quit();
					if(!exit("Airtel",log,strUserName,strFForderNumber,seqNo)){
						showPopup("System can't delete the files beacause the file is in use");
						log.info("File couldn't be deleted");
						SendCleanUpUnsuccessfulMail(strUserName, strFrom, strCc, strFForderNumber, seqNo, "Airtel",log);
						dlUnsuccessful= true;
					}
					log.info("Exit Airtel");	

				}catch(Exception e){
					e.printStackTrace();
					MDC.put("EventType", "CaptureScreenShot");
					MDC.put("EventData","Failure");
					log.error("Failed to take screenshot");
					
					if(!exit("Airtel",log,strUserName,strFForderNumber,seqNo)){
						showPopup("The clean exit was unsuccessful as the files to be deleted are in use. Please close all the files and clear the download folder.");
						log.info("File couldn't deleted");
						SendCleanUpUnsuccessfulMail(strUserName, strFrom, strCc, strFForderNumber, seqNo, "ReliancePower",log);
						dlUnsuccessful= true;
					}
					log.info("Exit Airtel");	
				}
			
			}
			
			//Checking the status completed or Failed
			jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("BorrowerDetails");
			String strFFProductCode = jObj.get("FFProductCode").toString();
			if(!dlUnsuccessful){
				MDC.put("EventType","Datapull");
				MDC.put("EventData","Success");
				log.info("datapull for Airtel is completed");
				Generic.writeToPostbackJson("./OutPut/Postback.json", strFForderNumber,strFFProductCode,"Airtel",seqNo,googleDrivePath,"Success");
			}else{
				MDC.put("EventType","Datapull");
				MDC.put("EventData","Failure");
				log.info("datapull for Airtel is Failed");
				Generic.writeToPostbackJson("./OutPut/Postback.json", strFForderNumber,strFFProductCode,"Airtel",seqNo,googleDrivePath,"Success");
				}
			return dlUnsuccessful;
			}
		
		
		public static boolean airtelLogOut(FirefoxDriver driver, Logger log,String strUserName, String strFForderNumber, String seqNo){
			
			MDC.put("EventType", "WebsiteLogout");
			log.info("initiated logout");
			boolean bStatus=false;
			try{
				
				if(!(driver.findElementsByXPath(strBackToHomeBtn).size()>0)){
					ClickUsingJS(driver, strlogoutBtn);					
				}else{
					Click(driver, strBackToHomeBtn);
					ClickUsingJS(driver, strlogoutBtn);
				}
				
				MDC.put("EventData","Success");
				log.info("Clicked Logout button");
				log.info("logged out successfully from "+strloginurl);
				waitAftereLogout(driver);
				exit("airtel",log,strUserName,strFForderNumber,seqNo);
				driver.close();
				log.info("Exit Airtel");
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

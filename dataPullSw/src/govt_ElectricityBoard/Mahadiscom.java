package govt_ElectricityBoard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.api.services.drive.Drive;

import Generic.Generic;
import gMailAPI.gMail;
public class Mahadiscom extends Generic{
	static String strLoginUrl="https://wss.mahadiscom.in/wss/wss?uiActionName=getViewPayBill";
	static String m_ViewHistory = "//*[@id='billing_detail']";
	static String m_viewPayBill = "//*[@id='leftnav_lblViewPay']";
	static String PaymentHistoryLink="//*[@id='paymentHistoryLink']";
	
	static int intMinWait=5;
	static int intMedWait=10;
	static int intMaxWait=100;
	static String googleDrivePath;
	
	
public static boolean dlMahadiscomBills(FirefoxDriver driver, String strBaseDir, Logger log,String strUserName, String strFForderNumber, String seqNo)throws Exception {
	  boolean blnDlStatus= true;
      try{  
			MDC.put("EventType","DataDownloadStart");
			MDC.put("EventData","Success");
			log.info("Data dwonload started");
					
			waitTillElementIsPresent(driver, m_viewPayBill,intMaxWait);
			ImplicitWait(driver);
			Click(driver, m_viewPayBill);
			log.info("Clicked on View PayBill");
			String newline = System.getProperty("line.separator");	
			String arrMessage[] = readFile("./InputMessages/LoginMessage.txt").split("#newLine#");
	        String strMessage = "";

	        for(int intLoop=0;intLoop<arrMessage.length;intLoop++){
	           if(strMessage==""){
	        	   strMessage =arrMessage[intLoop];
	           }else{
	        	   strMessage =strMessage+ newline+arrMessage[intLoop];
	           }
	        }
			showPopup(strMessage,driver,log, strUserName,  strFForderNumber,  seqNo);
			
			boolean blnFlag= true;
	        ImplicitWait(driver, 5);
	        int intCount=0;
	         
	        while(blnFlag){
	              try{
	            	  WebDriverWait wait= new WebDriverWait(driver, 5);
	            	  if(driver.switchTo().alert() != null){
							log.info("alert was present");
							Alert alt=driver.switchTo().alert();
							String strAlertMsg= alt.getText();
							if(strAlertMsg.trim().replaceAll("\\n"," ").equalsIgnoreCase("Given combination of consumer number ,consumer type and BU do not match")){
								alt.accept();
								showPopup("Invalid Login attempt.Try again !");
								driver.get(strLoginUrl);
								intCount ++;
								
								if(intCount>=3){
									 showPopup("Maximum attempt Achieved.please run the Utility again");
		                             log.info("Maximum attempt for login achieved");
		                             driver.close();
		                             driver.quit();
		                             exit("all",log, strUserName,  strFForderNumber,  seqNo);
		                             System.exit(0);
		                             break;
									}
	                            }
	            	  	} 
	            	  }catch(Exception e) {
	                     // TODO Auto-generated catch block
	            		  e.printStackTrace();
	                     log.error("No alert is Present");
	                     if(!((exists(driver,m_viewPayBill)))){
		                      log.info("Navigated to wrong link.");
		                      showPopup("Navigated to wrong link. The system will now redirect you to homepage.");
		                      driver.get(strLoginUrl);
		                      blnFlag=true;
		                      }else{
		                    	  blnFlag=false;
		                      }
	                   
	                     
	              }
	  }	
	try{
			waitTillElementIsPresent(driver, ".//*[@id='Img1']", intMaxWait);
			//driver.findElement(By.xpath(m_ViewHistory)).click();
			ImplicitWait(driver);
		    Click(driver, m_ViewHistory);
			log.info("Clicked view history");
			
			String[] arrMsg = readFile("InputMessages\\FinFortSoftwareTakeover.txt").split("#newLine#");
	        String strMsg = "";
	        newline=System.getProperty("line.separator");
	        for(int intLoop = 0;intLoop<arrMsg.length;intLoop++){
	           if(strMsg==""){
	        	   strMsg =arrMsg[intLoop];
	           }else{
	        	   strMsg =strMsg + newline + arrMsg[intLoop];
	           }
	        }
			showPopup(strMsg,driver, log, strUserName,  strFForderNumber,  seqNo);
			
			
			waitTillElementIsPresent(driver, PaymentHistoryLink, intMedWait);
			//driver.findElement(By.xpath("//*[@id='paymentHistoryLink']")).click();
			ImplicitWait(driver);
			Click(driver, PaymentHistoryLink);
			log.info("Clicked payment history");
			
			/*Download All the Credit/Debit Card payment history*/
			
				waitTillElementIsPresent(driver, "//table[@id='grdCustPaymentByCardDetails']", intMaxWait);
				MDC.put("EventType","Data Downloading");
				MDC.put("EventData","Success");
				log.info("data download started for Credit/Debit Card payment history");
				
				List<WebElement> intRow=driver.findElements(By.xpath("//table[@id='grdCustPaymentByCardDetails']//tr"));
				int payCount=intRow.size();
				for(int intI = 2;intI<=payCount;intI++){
					String s="//table[@id='grdCustPaymentByCardDetails']//tr["+intI+"]//img[@title='View Pdf']";
					String filename=driver.findElement(By.xpath("//table[@id='grdCustPaymentByCardDetails']//tr["+intI+"]//td[3]")).getText();
					ImplicitWait(driver);
					Click(driver,s);
					if(waitUntilFileDownloaded(strBaseDir+"/"+filename+".pdf", log)==false){
						blnDlStatus= false;
					}
					log.info(filename+".pdf is downloaded");
					}
			
			
			/*Download All the Netbanking history */	
			
				MDC.put("EventType","Data Downloading");
				MDC.put("EventData","Success");
				log.info("data download started for Netbanking history");
				 intRow=driver.findElements(By.xpath("//table[@id='grdPaymentByInternetBankingDetails']//tr"));
				 payCount=intRow.size();
				 for(int intI = 2;intI<=payCount;intI++){
					 String s="//table[@id='grdPaymentByInternetBankingDetails']//tr["+intI+"]//img[@title='View Pdf']";
					 String filename=driver.findElement(By.xpath("//table[@id='grdPaymentByInternetBankingDetails']//tr["+intI+"]//td[3]")).getText();
					 ImplicitWait(driver);
					 Click(driver,s);
					 waitUntilFileDownloaded(strBaseDir+"/"+filename+".pdf", log);
					 log.info(filename+".pdf is downloaded");
					 }
				 }catch(Exception e){
					 e.getMessage();
					 blnDlStatus=false;
					 }
				
			 }catch(Exception e){
				 MDC.put("EventData","Failure");
				 log.info("data download Failed for Mahadiscom"+e.getMessage());
				 blnDlStatus= false;
				 
			 }
      System.out.println(blnDlStatus);
			return blnDlStatus;
			
		}
				
	
	public static boolean initiateMahadiscom(String strTo, String strFrom, String strCc, String strUserName, String subject, String SubjectPwd, String body, String bodyPwd, String strPasswordToZip, Logger log, String strFForderNumber,String strUserNAme,String strPhoneNumber,String seqNo) throws IOException, ParseException{
		boolean dlUnsuccessful = false;
		JFrame jfrm =showPreLoader();
		MDC.put("EventType", "WebsiteLogin");
		MDC.put("EventData", "Success");
		log.info("Logging in to the Reliance Power website");		
		log.info("navigated to Mahadiscom url: "+strLoginUrl);
		FirefoxDriver driver = initiate("Mahadiscom",strLoginUrl,log,strUserName,strFForderNumber,seqNo);
		log.info("Invoked mahadiscom URL");
		//String strBaseDir = (String) readJSON("InputData\\InputData.json").get("baseDir");
		JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("BasicInfo");
		String strBaseDir = Generic.getBase();
		
		jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("DriveConfig");
		String strDriveBaseDir = jObj.get("driveBaseDir").toString();
	
		try{
			closePreLoader(jfrm);	
			if(!dlMahadiscomBills(driver, strBaseDir,log, strUserName,  strFForderNumber,  seqNo)){
				dlUnsuccessful=true;
				}
			}catch(Exception e){
			log.error("Exception in dlMahadiscomBills"+e.getMessage());
			dlUnsuccessful=true;
			
		}
		System.out.println(dlUnsuccessful);
	
		if(!dlUnsuccessful){
			try{
				MDC.put("EventType", "ZipFileCreation");
				MDC.put("EventData","Success");
			    log.info("initiated zipFiles");
				zipFiles(strBaseDir + "/", "Mahadiscom", strPasswordToZip);
				log.info("Mail ZIP Password: "+strPasswordToZip);
			}catch(Exception e){
				MDC.put("EventData","Failure"+e.getMessage());
				log.error("Zipping failed: "+e.getMessage());
				dlUnsuccessful=true;
			}
		}
		
		if(!dlUnsuccessful){
			try{
				MDC.put("EventType", "EmailDownloadedFiles");
				log.info("Initiated sendMail");
				gMail.sendMessage(strUserName, strTo, strFrom, strCc,subject, body, strBaseDir + "/", "Mahadiscom.zip",log);
				MDC.put("EventData","Success");
				log.info("Mail sent successfully to "+strTo);
			}catch(Exception e){
				MDC.put("EventData","Failure");
				log.error("Fail to send Mail: "+e.getMessage());
				dlUnsuccessful=true;
				}
		}
		
		if(!dlUnsuccessful){
			try{
						MDC.put("EventType", "SendPassword");
						MDC.put("EventData","Success");
				
						jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("MailConfig");
						String strSenderId=jObj.get("passwordDeliveryMode").toString();
				 if(strSenderId.equalsIgnoreCase("Email")){
					    log.info("initiated sendMail - password");
						gMailAPI.gMail.sendMessage(strUserName, strTo, strFrom,strCc, SubjectPwd, bodyPwd, "", "",log);
						MDC.put("EventData","Success");
						log.info("Password: "+strPasswordToZip+" Mail sent Successfully to "+strTo);
						log.info("Password Mail Sent");
						System.out.println("Mail 2 Sent");
				 }else if(strSenderId.equalsIgnoreCase("SMS")){
					 	log.info("initiated send SMS - password");
						SendMailZippedPassword( strPhoneNumber, strUserNAme, "Mahadiscom", strPasswordToZip);
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
						 SendMailZippedPassword(strPhoneNumber, strUserNAme, "Mahadiscom", strPasswordToZip);
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
		if(!dlUnsuccessful){
		try{
			MDC.put("EventType", "GoogleDriveUpload");
			MDC.put("EventData", "Success");
			log.info("Upload to google drive initiated. Name- FFOrder: "+strFForderNumber);
			File fileToDelete = new File(strBaseDir + "/Mahadiscom.zip");
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
			arrList.add("Mahadiscom");
			String []arr = new String[arrList.size()];
			arrList.toArray(arr);
			String[] arrFolderID=gMail.createFoldersPath(arr).split("~");
			strParentFolderID=arrFolderID[0];
			googleDrivePath=arrFolderID[1];
			//String[] arr = {strDriveBaseDir,"FF Order# "+strFForderNumber,"PullSequenceNumber: "+seqNo,"Mahadiscom"};
			if(strParentFolderID!=(null)){
				if(!uploadToDriveHierarchy(service, strBaseDir+"/", strParentFolderID, log).equals("false")){
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
								//log.info("Log Out initiated");	
				
				
						//sendMail(strTo, strFrom, strCc, strUserName, strPassword, strHost, strPortNumber, SubjectPwd, bodyPwd, "");
						//gMail.sendMessage(strUserName, strTo, strFrom, strCc,subject, body, strBaseDir + "/", "Mahadiscom.zip",log);
						////log.info("Mailed Zip Password");
						//System.out.println("Mail 2 Sent");
						}catch(Exception e){
							//log.error("Sent attachments failed"+e.getMessage());
							dlUnsuccessful=true;
							e.printStackTrace();
			}
		}
		
		
		driver.close();
		//driver.quit();
		
		
		
		//Checking the status completed or Failed
				jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("BorrowerDetails");
				String strFFProductCode = jObj.get("FFProductCode").toString();
				if(!dlUnsuccessful){
					MDC.put("EventType","Datapull");
					MDC.put("EventData","Success");
					log.info("datapull for Mahadiscom is completed");
					Generic.writeToPostbackJson("./OutPut/Postback.json", strFForderNumber,strFFProductCode,"Mahadiscom",seqNo,googleDrivePath,"Success");
				}else{
					MDC.put("EventType","Datapull");
					MDC.put("EventData","Failure");
					log.info("datapull for Mahadiscom is Failed");
					Generic.writeToPostbackJson("./OutPut/Postback.json", strFForderNumber,strFFProductCode,"Mahadiscom",seqNo,googleDrivePath,"Success");
					}
				exit("mahadiscom",log, strUserName,  strFForderNumber,  seqNo);
				log.info("mahadiscom exited");	
			
		return dlUnsuccessful;
	}
}

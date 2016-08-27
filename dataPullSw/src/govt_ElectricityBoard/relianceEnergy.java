package govt_ElectricityBoard;

import gMailAPI.gMail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import Generic.Generic;

import com.google.api.services.drive.Drive;

public class relianceEnergy extends Generic {
	static String strloginurl="http://myaccount.relianceenergy.in/relmyaccount/login.do";
	static String m_LoginButton = ".//*[@id='Map']/area[1]";
	static String objMoreServices="//div[text()='More Services For You']";
	static String objMeterReadingDate="//a[text()='Meter Reading Date']";
	static String objViewBtn="//img[@src='images/view_btn.jpg']";
	static String objDLBill="//a[text()='Download / Print Bill']";
	static String objMonthDD="//*[@id='month']";
	static String newline = System.getProperty("line.separator");
	static String strLoginPage="//td[contains(text(),'feature allows you to manage your electricity connection with just a few clicks.')]";
	static String strRelianceHomePage="//button[@id='cboxClose']";
	static String strInvalidCredentials="//td[font[text()='Either your Login Name / Password is incorrect']]";
	static String objRelianceLoOut="//img[contains(@src,'logout')]";
	static String BillAndPaymentLink="//a[div[text()='Bill and Payments']]";
	static String strPaymentHistroyLInk="//a[text()='Payment History']";
	//Implicit Waits
	static int intMinWait=5;
	static int intMedWait=10;
	static int intMaxWait=100;
	static String googleDrivePath;
	
	public static boolean dlRelianceBill(FirefoxDriver driver, String strBaseDir,Logger log,String strUserName, String strFForderNumber, String seqNo)throws Exception {
		MDC.put("EventType","DataDownloadStart");
		MDC.put("EventData","Success");
		log.info("Data dwonload started");		
		boolean bStatus=false;
		
		waitTillElementIsPresent(driver, m_LoginButton, intMaxWait);
		ImplicitWait(driver);
		
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
        ImplicitWait(driver, intMinWait);
        int intCount=0;
        

        while(blnFlag){
          try{
             if(driver.findElements(By.xpath(strInvalidCredentials)).size()>0){
                    showPopup("Invalid Login attempt.Try again !");
                    log.info("Invalid login attempt");
                    driver.get(strloginurl);
                    intCount++;
                    if(intCount>=3) {
                             showPopup("Maximum attempt Achieved.please run the Utility again");
                             log.info("Maximum attempt for login achieved");
                             driver.close();
                             driver.quit();
                             exit("all",log, strUserName,  strFForderNumber,  seqNo);
                             System.exit(0);
                             break;
                    }
             }else if(!((exists(driver,strLoginPage)) || (exists(driver,strRelianceHomePage)))){
                    log.info("Navigated to wrong link.");
                    showPopup("Navigated to wrong link. The system will now redirect you to homepage.");
                    driver.get(strloginurl);
             }else if(exists(driver, strRelianceHomePage)){
                    break;
             }
            
          }catch(Exception e) {
             // TODO Auto-generated catch block
        	 MDC.put("EventType","DataDownload");
  			 MDC.put("EventData","Failure");
  			 log.error("Data download is failed for ReliancePower: "+e.getMessage());
             e.printStackTrace();
             return false;
          }
        }
        
        try{
        	MDC.put("EventType","DataDownload");
    		MDC.put("EventData","Success");
			waitTillElementIsNotPresent(driver, m_LoginButton, intMaxWait);
			ImplicitWait(driver);
			//driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
	
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
			showPopup(strMsg,driver,log, strUserName,  strFForderNumber,  seqNo);
			
			MDC.put("EventType","DataDownload");
			MDC.put("EventData","Success");
	
			ImplicitWait(driver);
			
			Actions action = new Actions(driver);
			action.sendKeys(Keys.ESCAPE).perform();
			action.sendKeys(Keys.ESCAPE).perform();
			action.sendKeys(Keys.ESCAPE).perform();
			action.sendKeys(Keys.ESCAPE).perform();
	
			ImplicitWait(driver);
			
			waitTillElementIsPresent(driver, BillAndPaymentLink, intMaxWait);
			Click(driver, BillAndPaymentLink);
			log.info("Clicked on Bill and Payments");
			
			waitTillElementIsPresent(driver, strPaymentHistroyLInk, 100);
			driver.findElement(By.xpath(strPaymentHistroyLInk)).click();
	
			File scrFile;
			scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			try {
				FileUtils.copyFile(scrFile, new File(strBaseDir
						+ "/RelianceBill_Details.png"));
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			log.info("Clicked on Download Bill");
	
			Click(driver, objDLBill);
			waitTillElementIsPresent(driver, objMonthDD, 1000);
			ImplicitWait(driver);
			WebElement wE ;
			Select select = null ;
			List<WebElement> lst;
			
			wE = driver.findElement(By.xpath(objMonthDD));
			select = new Select(wE);
			lst=select.getOptions();
			for(int i =1;i<lst.size();i++){
				select.selectByIndex(i);
				Click(driver, objViewBtn);
				File oldName = new File(strBaseDir + "/ProcessDownloadPDF.jsp");
			    File newName = new File(strBaseDir + "/"+select.getFirstSelectedOption().getText()+".pdf");
				oldName.renameTo(newName);
				log.info("Download Bill: "+select.getFirstSelectedOption().getText()+".pdf");
	
			}
			Click(driver, objMoreServices);
			log.info("Navigate to meter reading.");
			waitTillElementIsPresent(driver, objMeterReadingDate, 1000);
			ImplicitWait(driver);
			Click(driver, objMeterReadingDate);
			
			wE = driver.findElement(By.name("month"));
			select = new Select(wE);
			lst=select.getOptions();
	
			
			for(int i =1;i<12;i++){
				wE = driver.findElement(By.name("month"));
				select = new Select(wE);
				select.selectByIndex(i);
		
				String strSelected=select.getFirstSelectedOption().getText();
				//driver.findElement(By.xpath(objViewBtn)).click();
				Click(driver, objViewBtn);
				ImplicitWait(driver);
				scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				try {
					FileUtils.copyFile(scrFile, new File(strBaseDir
							+ "/"+strSelected+".png"));
				} catch (IOException e) {
					e.printStackTrace();
					log.error("Downloaded: "+strSelected);
				}
				
			}
			
			bStatus= true;
        }catch(Exception e){
        	MDC.put("EventType","DataDownload");
 			MDC.put("EventData","Failure");
 			log.error("Data download is failed for ReliancePower: "+e.getMessage());
        	bStatus= false;
        }
        MDC.put("EventType","DataDownloadComplete");
		MDC.put("EventData","Success");
		log.info("Data download completed for ReliancePower");
         
        return bStatus;
	}
	public static boolean initiateReliance(String strTo, String strFrom, String strCc, String strUserName, String subject, String SubjectPwd, String body, String bodyPwd, String strPasswordToZip, Logger log, String strFForderNumber,String strUserNAme,String strPhoneNumber,String seqNo) throws IOException, InterruptedException, ParseException{
		boolean dlUnsuccessful = false;
		JFrame jfrm =showPreLoader();
		MDC.put("EventType", "WebsiteLogin");
		MDC.put("EventData", "Success");
		log.info("Logging in to the Reliance Power website");
		
		log.info("navigated to incometax url: "+strloginurl);
		FirefoxDriver driver = initiate("ReliancePower",strloginurl,log,strUserName,strFForderNumber,seqNo);
		log.info("navigated to incometax url: "+strloginurl);
		JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("BasicInfo");
		String strBaseDir = Generic.getBase();
		
		jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("DriveConfig");
		String strDriveBaseDir = jObj.get("driveBaseDir").toString();
		
		try{
			closePreLoader(jfrm);
			if(!dlRelianceBill(driver, strBaseDir,log, strUserName,  strFForderNumber,  seqNo)){
				dlUnsuccessful= true;
			}
			log.info("initiated dlRelianceBill");
			
		}catch(Exception e){
			log.error("dlRelianceBill: "+e.getMessage());
			dlUnsuccessful = true;
			ReliancePowerLogOut(driver,log, strUserName,strFrom, strCc, strFForderNumber, seqNo);
		}
		
		if(!dlUnsuccessful){
			try{
				MDC.put("EventType", "ZipFileCreation");
			    log.info("initiated zipFiles");
				zipFiles(strBaseDir + "/", "ReliancePower", strPasswordToZip);
				MDC.put("EventData","Success");
				log.info("Mail ZIP Password: "+strPasswordToZip);

			}catch(Exception e){
				MDC.put("EventData","Failure"+e.getMessage());
				log.error("zipFiles: "+e.getMessage());
				dlUnsuccessful=true;
				ReliancePowerLogOut(driver,log, strUserName,strFrom, strCc, strFForderNumber, seqNo);
			}
		}					
		if(!dlUnsuccessful){
			try{
				MDC.put("EventType", "EmailDownloadedFiles");
				log.info("Initiated sendMail");
				gMail.sendMessage(strUserName, strTo, strFrom, strCc,subject, body, strBaseDir + "/", "ReliancePower.zip",log);
				MDC.put("EventData","Success");
				log.info("Mail sent successfully to "+strTo);
			}catch(Exception e){
				MDC.put("EventData","Failure");
				log.error("Fail to send Mail: "+e.getMessage());
				dlUnsuccessful=true;
				ReliancePowerLogOut(driver,log, strUserName,strFrom, strCc, strFForderNumber, seqNo);
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
						SendMailZippedPassword( strPhoneNumber, strUserNAme, "Reliance Power", strPasswordToZip);
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
						 SendMailZippedPassword(strPhoneNumber, strUserNAme, "Reliance Power", strPasswordToZip);
						 MDC.put("EventData","Success");
						 log.info("Password: "+strPasswordToZip+" SMS sent successfully to"+strPhoneNumber);	
						 System.out.println("SMS password  Sent");
					
				 }	
				
			}catch(Exception e){
				MDC.put("EventType", "SendPassword");
				MDC.put("EventData","Failure");
				log.error("sendMail - failed: "+e.getMessage());
				dlUnsuccessful=true;
				ReliancePowerLogOut(driver,log, strUserName,strFrom, strCc, strFForderNumber, seqNo);
			}
		}
		
		try{
			MDC.put("EventType", "GoogleDriveUpload");
			MDC.put("EventData", "Success");
			log.info("Upload to google drive initiated. Name- FFOrder: "+strFForderNumber);
			File fileToDelete = new File(strBaseDir + "/ReliancePower.zip");
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
			arrList.add("ReliancePower");
			String []arr = new String[arrList.size()];
			arrList.toArray(arr);
			String[] arrFolderID=gMail.createFoldersPath(arr).split("~");
			strParentFolderID=arrFolderID[0];
			googleDrivePath=arrFolderID[1];
			//String[] arr = {strDriveBaseDir,"FF Order# "+strFForderNumber,"PullSequenceNumber: "+seqNo,"ReliancePower"};
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
		
		if(!dlUnsuccessful){
			try {
				String newline = System.getProperty("line.separator");
				
				String strMessage = "The system has successfully completed the downloads."+ newline+
						"The system will be logged out.";
						showPopup(strMessage);
						ReliancePowerLogOut(driver,log, strUserName,strFrom, strCc, strFForderNumber, seqNo);
						
						
						} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
			
			}
		if(dlUnsuccessful){
			try{
				MDC.put("EventType", "CaptureScreenShot");
				captureScreenshot(driver, strBaseDir+"./ErrorScreenshot/errReliance.png");
				MDC.put("EventData","Success");
				log.info("Screenshot captured Successfully"+strBaseDir+"./ErrorScreenshot/errReliance.png");
				ReliancePowerLogOut(driver,log, strUserName,strFrom, strCc, strFForderNumber, seqNo);

			}catch(Exception e){
				MDC.put("EventType", "CaptureScreenShot");
				MDC.put("EventData","Failure");
				log.error("Failed to take screenshot");
				e.printStackTrace();
				log.error("Log Out Failed");
				if(!exit("ReliancePower",log, strUserName,  strFForderNumber,  seqNo)){
					showPopup("System can't delete the files beacause the file is in use");
					log.info("File couldn't deleted");
					SendCleanUpUnsuccessfulMail(strUserName, strFrom, strCc, strFForderNumber, seqNo, "ReliancePower",log);
					dlUnsuccessful= true;
					
				}
				log.info("Exit ReliancePower");	
			}
		}
		jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("BorrowerDetails");
		String strFFProductCode = jObj.get("FFProductCode").toString();
		if(!dlUnsuccessful){
			MDC.put("EventType","Datapull");
			MDC.put("EventData","Success");
			log.info("datapull for ReliancePower is completed");
			Generic.writeToPostbackJson("./OutPut/Postback.json", strFForderNumber,strFFProductCode,"RelianceEnergy",seqNo,googleDrivePath,"Success");
		}else{
			MDC.put("EventType","Datapull");
			MDC.put("EventData","Failure");
			log.info("datapull for ReliancePower is Failed");
			Generic.writeToPostbackJson("./OutPut/Postback.json", strFForderNumber,strFFProductCode,"RelianceEnergy",seqNo,googleDrivePath,"Success");
		}		
		return dlUnsuccessful;
	}
	
	public static boolean ReliancePowerLogOut(FirefoxDriver driver, Logger log, String strUserName,String strFrom,String strCc,String strFForderNumber,String seqNo ){
		
		MDC.put("EventType", "WebsiteLogout");
		log.info("initiated logout");
		boolean bStatus=false;
		try{
			if(driver.findElements(By.xpath(objRelianceLoOut)).size()>0){
				ClickUsingJS(driver, objRelianceLoOut);
				MDC.put("EventData","Success");
				log.info("Clicked Logout button");
				log.info("logged out successfully from "+strloginurl);
				waitAftereLogout(driver);
				exit("ReliancePower",log, strUserName,  strFForderNumber,  seqNo);
		
				driver.close();
				log.info("Exit ReliancePower");
			}
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
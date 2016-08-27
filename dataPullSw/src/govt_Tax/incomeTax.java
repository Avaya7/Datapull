package govt_Tax;

import gMailAPI.gMail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.json.simple.JSONObject;
import org.mortbay.log.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import Generic.Generic;

import com.google.api.services.drive.Drive;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

public class incomeTax extends Generic{
		
	private static final String UN = null;
	static String strValidateNavigationToHomepage = "//*[@id='staticContentsUrl']//*[contains(text(),'Dashboard')]";
	static String strITHomePage ="//*[@name='userName']";
	static String strForcedLogin="//input[@value='Forced Login']";
	static String strInvalidLogInXpath="(//form[@id='Login']//div[text()='Invalid User ID. Please retry.' or text()='Invalid Password. Please retry.' or text()='Incorrect Date of Birth/Incorporation. Please retry.' or text()=' Invalid Code. Please enter the number as appearing in the Image.' or text()=' Invalid Code. Please enter the number as appearing in the Image.'])[1]";
	static String strLoginUrl="https://incometaxindiaefiling.gov.in/e-Filing/UserLogin/LoginHome.html";
	static String strLaterBtn = "//div[@class='ui-dialog ui-widget ui-widget-content ui-corner-all ui-front ui-draggable ui-resizable' and contains(@style,' display: block; z-index')]//input[@value='LATER']";
	static String strErrorMessage="//div[@class='ui-dialog ui-widget ui-widget-content ui-corner-all ui-front ui-draggable ui-resizable' and contains(@style,' display: block; z-index')]//div[@class='error' and text()='Please enter your 12 digit Aadhaar Number.']";
	static String strNotify = "//span[text()='Message']/following-sibling::button";
	static String strViewReturn = "//a[text()='View Returns / Forms']";
	static String strClose = "//span[@id='ui-id-1']/following-sibling::button";
	static String objDashboard="//span[text()='Dashboard']";
	static String objPendingAction ="//a[text()='My Pending Actions']";
	static String objOutStandingDemand="//h3[contains(text(),'Details of  Outstanding demand')]";
	static String objViewResponse = "//a[text()='View']";
	static String objClickHere="//*[contains(concat(' ', normalize-space(text()), ' '), ' Click here')]";
	static String objTransaction="(//a[@title='View Response Details'])";
	static String objDlOutstanding="//a/img[@src='/itax/images/download_ack.png']";
	static String objINcTaxLogout="//a[text()='Logout']";
	static String newline = System.getProperty("line.separator");	
	
	//Profile related Objects
	static String strProfileSetting="//*[@id='header']//span[text()='Profile Settings']";
	static String strMyProfile ="//a[text()='My Profile']";
	static String strProfileDetailsTable = "//*[@id='Pan']/div/table/tbody/";
	static String strDashboard ="//*[@id='header']//span[text()='Dashboard']";
	static String objAddress="//*[@id='AddressTab']";
	static String objContactDetails="//*[@id='ContactTab']";
	static JSONObject jObj;
	static int intMinWait=0;
	static int intMedWait=5;
	static int intMaxWait=100;
	
	
	
	public static boolean dlITDocs(FirefoxDriver driver, String strBaseDir, Logger log, String strUserName,String strFrom,String strCc,String strFForderNumber,String seqNo) {
		try{
			JSONObject jObj = (JSONObject) readJSON("InputData\\datapullcfg.json").get("IncTax");
			Object arrYearsToPull = jObj.get("YearsToPullData").toString();
			
			jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("BasicInfo");
			ImplicitWait(driver);
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
	        showPopup(strMessage,driver,log, strUserName, strFForderNumber, seqNo);
			if(driver.findElements(By.xpath(strITHomePage)).size()>0){
		        	Thread.sleep(2000);
		        	Actions actions = new Actions(driver);
		            actions.sendKeys(Keys.ESCAPE).perform();
			 }
			boolean blnFlag= true;
			
			ImplicitWait(driver, intMinWait);
			
			int intCount=0;
			while(blnFlag){
				try{
					if(driver.findElements(By.xpath(strInvalidLogInXpath)).size()>0){
						showPopup("Invalid login crendentials entered. The page will be reloaded now.");
						driver.get(strLoginUrl);
						intCount++;
						if(intCount>=3) {
									MDC.put("EventType", "MaximumLogInAttempt");
									MDC.put("EventData","Failure");
									log.info("Maximum log in Attempted for "+intCount+" times");
			  						showPopup("Maximum number of allowed attempt over. The Utility will exit now.");
							        driver.close();
							        driver.quit();
							        if(!exit("all",log, strUserName,  strFForderNumber,  seqNo)){
										showPopup("The clean exit was unsuccessful as the files to be deleted are in use. Please close all the files and clear the download folder.");
										SendCleanUpUnsuccessfulMail(strUserName, strFrom, strCc, strFForderNumber, seqNo, "IncomeTax",log);
										
										
										MDC.put("EventType", "CleanUp");
										}
							        System.exit(0);
							        break;
							        }
						}
					
				ImplicitWait(driver, intMinWait);
					if(!(exists(driver,strValidateNavigationToHomepage)||exists(driver, strITHomePage)||exists(driver, strForcedLogin))){
						ImplicitWait(driver, intMinWait);
							if(!(exists(driver,strValidateNavigationToHomepage)||exists(driver, strITHomePage)||exists(driver, strForcedLogin))){	
								MDC.put("EventType", "NavigatedToWrongLink");
								MDC.put("EventData","Failure");
							    log.info("Navigated to wrong link.");
							    showPopup("Navigated to wrong link. The system will now redirect you to homepage.");
							    driver.get(strLoginUrl);
							    }
							}else if(exists(driver, strForcedLogin)){
								Click(driver, strForcedLogin);
								}else if(exists(driver,strValidateNavigationToHomepage)){
									break;
									}
					}catch(Exception e){
						// TODO Auto-generated catch block
						log.error("Something went wrong");
						e.printStackTrace();
						return false;
						}
				}

				waitTillElementIsPresent(driver, strValidateNavigationToHomepage, 100);
				String[] arrMsg = readFile("./InputMessages/FinFortSoftwareTakeOver.txt").split("#newLine#");
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
		        MDC.put("EventType", "HandleAdharCardPopup");
			
				if (driver.findElements(By.xpath(strLaterBtn)).size() > 0) {
					Click(driver, strLaterBtn);
					MDC.put("EventData","Success");
					log.info("Aadhar card popup is handled");
				}
			
				ImplicitWait(driver, 1);
				MDC.put("EventType", "HandleAdharCardMessage");
				
		        if(driver.findElements(By.xpath(strErrorMessage)).size() > 0){
		            Click(driver, strLaterBtn);
		            MDC.put("EventData","Success");
					log.info("Adharcard  Message is handled");
		        }
		
		        MDC.put("EventType", "HandleNotificationPopup");
				
				if (driver.findElements(By.xpath(strNotify)).size() > 0) {
					Click(driver, strNotify);
					MDC.put("EventData","Success");
					log.info("Notificaction popup  is handled");
				}
	
			
			waitTillElementIsPresent(driver, strViewReturn, 10);
			
			MDC.put("EventType","DataDownloadStart");
			Click(driver, strViewReturn);
			MDC.put("EventData", "Success");
			log.info("Click View Return");
			createFolder(strBaseDir
					+ "/ReturnFiled");

			intCount = driver.findElements(By.xpath("(//table[@class='grid ']/tbody/tr/td/a)")).size();
			int i = 1;
			System.out.println(intCount);
			log.info("Total return records: "+intCount);
			
			File scrFile;
			scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			try {
				FileUtils.copyFile(scrFile, new File(strBaseDir
						+ "/ReturnFiled/ViewReturn.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			String elementXpath = null;
			String strPan="(//*[@id='staticContentsUrl']/div[2]/div/table/tbody/tr/td[1])[2]";
			String strPanNo=driver.findElement(By.xpath(strPan)).getText();

			String strJsonPath=strBaseDir + "/"+strFForderNumber+"_"+strPanNo+".json";
			JsonWriter jsonWriter= null;

			for (int intK = 1; intK <= intCount; intK++) {
				ImplicitWait(driver,intMedWait);
				boolean blnDLAll= false;
				if(arrYearsToPull.toString().contains("all")){
					blnDLAll= true;
				}
				String strITRAck = "//a[@title='Click to view ITR-V/Acknowledgment']";
				String ITRForm = "//a[@title='Click to view ITR/Form']";
				String ITRXml = "//a[@title='Click to view XML File']";
				String strYear="(//table[@class='grid ']/tbody/tr/td[2])["+i+"]";
				String strFiling="(//table[@class='grid ']/tbody/tr/td[5])["+i+"]";
				String strItrForm =	"(//table[@class='grid ']/tbody/tr/td[3])["+i+"]";				
				String strFilingDate =	"(//table[@class='grid ']/tbody/tr/td[4])["+i+"]";
				String strFiledBy="(//*[@id='staticContentsUrl']/div[2]/div/table/tbody/tr/td[6])["+i+"]";
				String strFilingStatus="(//*[@id='staticContentsUrl']/div[2]/div/table/tbody/tr/td[8])["+i+"]";
		
				
				String strYearReturn =driver.findElement(By.xpath(strYear)).getText().split("-")[0]; 
				String strFilingType =driver.findElement(By.xpath(strFiling)).getText();
				String strITRFormType =driver.findElement(By.xpath(strItrForm)).getText();
				String strDate =driver.findElement(By.xpath(strFilingDate)).getText();
				String strFileDate=strDate.substring(6, 10)+strDate.substring(3, 5)+strDate.substring(0, 2);
				int intCnt=1; 
				String strID;
				String strValue;
				String jsonString = null;
				while(driver.findElements(By.xpath("//*[@id='staticContentsUrl']/div[2]/div/table/tbody/tr/th")).size() > intCnt+1){
					strID=driver.findElement(By.xpath("//*[@id='staticContentsUrl']/div[2]/div/table/tbody/tr/th["+(intCnt+1)+"]")).getText();
					strValue=driver.findElement(By.xpath("(//*[@id='staticContentsUrl']/div[2]/div/table/tbody/tr/td["+(intCnt+1)+"])["+(intK)+"]")).getText();
					if(intCnt==1){
						jsonString=strID+"~"+strValue;
					}else{
						jsonString=jsonString+"#"+strID+"~"+strValue;
					}
					intCnt++;

				}
				jsonWriter= writeToJson(strJsonPath,strYearReturn+"_"+strFilingType,jsonString,jsonWriter);

				if(blnDLAll || arrYearsToPull.toString().contains(strYearReturn) ){
					if (driver.findElements(
							By.xpath("(//table[@class='grid ']/tbody/tr/td/a)[" + i
									+ "]")).size() > 0)
		
					elementXpath = "(//table[@class='grid ']/tbody/tr/td/a)[" + i+ "]";
					ClickUsingJS(driver, elementXpath);
					 
	
					createFolder(strBaseDir+"/ReturnFiled/"+strYearReturn);
					createFolder(strBaseDir+"/ReturnFiled/"+strYearReturn+"/"+strFilingType);
					createFolder(strBaseDir+"/ReturnFiled/"+strYearReturn+"/"+strFilingType+"/"+strITRFormType);
					createFolder(strBaseDir+"/ReturnFiled/"+strYearReturn+"/"+strFilingType+"/"+strITRFormType+"/"+strFileDate);

					String completePath =strBaseDir+"/ReturnFiled/"+strYearReturn+"/"+strFilingType+"/"+strITRFormType+"/"+strFileDate;
				if (driver.findElements(By.xpath(ITRXml)).size() > 0) {
					log.info(strYearReturn+ " IT Return, Rec "+intK+ "Click ITRXml");
					ClickUsingJS(driver, ITRXml);
					File[] FileName = null;

						while(!(FileName==null)){
							FileName = finder(strBaseDir,".part",log);
						}
						FileName = finder(strBaseDir,".xml",log);
						while((FileName==null)){
							FileName = finder(strBaseDir,".xml",log);
						}
						moveFile(FileName[0].getPath(), completePath +"/"+FileName[0].getName(),log);
					}
					
					log.info(strYearReturn+ " IT Return, Rec "+intK+ "Click Ack");
					if (driver.findElements(By.xpath(strITRAck)).size() > 0) {
						System.out.println("clicked");
						ClickUsingJS(driver, strITRAck);
						File[] FileName = null;
						while(!(FileName==null)){
							FileName = finder(strBaseDir,".part",log);
						}
						FileName = finder(strBaseDir,".pdf",log);
						while((FileName==null)){
							FileName = finder(strBaseDir,".pdf",log);
						}
						moveFile(FileName[0].getPath(), completePath +"/"+FileName[0].getName(),log);

					}
					
					if (driver.findElements(By.xpath(ITRForm)).size() > 0) {
						log.info(strYearReturn+ " IT Return, Rec "+intK+ " Clicked IT Return Form");
						ClickUsingJS(driver, ITRForm);
						File[] FileName = null;
						while(!(FileName==null)){
							FileName = finder(strBaseDir,".part",log);
						}
						FileName = finder(strBaseDir,".pdf",log);
						while((FileName==null)){
							FileName = finder(strBaseDir,".pdf",log);
						}
						moveFile(FileName[0].getPath(), completePath +"/"+FileName[0].getName(),log);

					}
					ImplicitWait(driver,2);
					log.info(strYearReturn+ " IT Return, Rec "+intCount+ "Click Close");
					Click(driver, strClose);
					i++;

				}else{
					i++;
				}
			}
			closeJson(jsonWriter);
			
			jsonWriter=null;
			MDC.put("EventType", "NavigateToDashBoard");
			MDC.put("EventData","Success");
			log.info("Navigate to dashboard");
	
			waitTillElementIsPresent(driver, objDashboard, 10);
	
			Click(driver, objDashboard);
			
	
			MDC.put("EventType", "NavigateToPendingAction");
			MDC.put("EventData","Success");
			log.info("Navigate to pending action");
			waitTillElementIsPresent(driver, objPendingAction, 10);
			
			Click(driver, objPendingAction);
			ImplicitWait(driver);
	
			MDC.put("EventType", "NavigateToOutStandingDemand");
			MDC.put("EventData","Success");
			log.info("Navigate to objOutStandingDemand");
			if(driver.findElements(By.xpath(objOutStandingDemand)).size()>0){
				Click(driver, objClickHere);
				createFolder(strBaseDir+"/OutstandingDemand");
				scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				try {
					FileUtils.copyFile(scrFile, new File(strBaseDir+"/OutstandingDemand"
							+ "\\OutstandingTaxDemand.png"));
					MDC.put("EventType", "TakeScreenShot");
					MDC.put("EventData","Success");
					log.info("Screenshot taken successfully");
					String key=null;
					String value=null;
					String jsonStr = null;
					strJsonPath=strBaseDir + "/OutstandingDemand.json";
					for(int outstandingCount =0;outstandingCount<driver.findElements(By.xpath("//*[@id='ssss']/table/tbody/tr/td[1]")).size()-1;outstandingCount++){
						int rowCount=1;
						String yearOutstanding=driver.findElement(By.xpath("//*[@id='ssss']/table/tbody/tr["+(outstandingCount+2)+"]/td[1]")).getText();
						while(rowCount<driver.findElements(By.xpath("//*[@id='ssss']/table/tbody/tr[1]/td")).size()){
							key=driver.findElement(By.xpath("//*[@id='ssss']/table/tbody/tr[1]/td["+rowCount+"]/b")).getText();
							value=driver.findElement(By.xpath("//*[@id='ssss']/table/tbody/tr["+(outstandingCount+2)+"]/td["+rowCount+"]")).getText();	
							if(rowCount==1){
								jsonStr = key+"~"+value;
							}else{
								jsonStr = jsonStr+"#"+key+"~"+value;
							}
							rowCount++;
						}
						jsonWriter= writeToJson(strJsonPath,yearOutstanding,jsonStr,jsonWriter);
					}
				} catch (IOException e) {
					e.printStackTrace();
					MDC.put("EventData","Success");
					log.error("Failed to take Sceenshot");
				}
				
				closeJson(jsonWriter);

				log.info("DL Outstanding notification");
	
				for(intCount=0;intCount <driver.findElements(By.xpath(objDlOutstanding)).size();intCount++){
					log.info("DL Outstanding notification: "+(intCount+1));
					Click(driver, "("+objDlOutstanding+")["+(intCount+1)+"]");
					File[] FileName = finder(strBaseDir,".pdf",log);
					moveFile(FileName[0].getPath(), strBaseDir+"/OutstandingDemand/"+FileName[0].getName(),log);

				}
				
				for(i = 0;i<driver.findElements(By.xpath(objViewResponse)).size();i++){
					String objView="("+objViewResponse+")["+i+1+"]";
					log.info("DL View Response Details: "+(i+1));
					Click(driver, objView);
					for(int intk=0;intk<driver.findElements(By.xpath(objTransaction)).size();intk++){
						Click(driver, objTransaction);
						scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
						try {
							FileUtils.copyFile(scrFile, new File(strBaseDir+"/"
									+ "OutstandingDemand/ViewResoponse"+intk+".png"));
							MDC.put("EventType", "TakeScreenShot");
							MDC.put("EventData","Success");
							log.info("Screenshot taken successfully");
						} catch (IOException e) {
							e.printStackTrace();
							MDC.put("EventData","Success");
							log.error("Failed to take Sceenshot");
							log.error("View Response:"+(intk));
						}
					}
				}
				
			}
			MDC.put("EventType", "DataDownloadComplete");
			MDC.put("EventData","Success");
			log.info("Data download is completed for Income Tax");
			
			log.info("Get profile info.");
			if(!strGetProfileInfo(driver, log)){
				return false;
				}
			}catch(Exception e){
				return false;
				}
		
		return true;
	}
	
	
	/*initiateIT methode for initiating the Income tax Project*/

	public static boolean initiateIT(String strTo, String strFrom, String strCc, String strUserName, String subject, String SubjectPwd, String body, String bodyPwd, String strPasswordToZip, Logger log, String strFForderNumber, String strUserNAme, String strPhoneNumber, String seqNo) throws Exception{

		jObj = (JSONObject) readJSON("InputData\\datapullcfg.json").get("DriveConfig");
		String strDriveBaseDir = jObj.get("driveBaseDir").toString();
		
		String googleDrivePath = null;
		JFrame jfrm =showPreLoader();
		MDC.put("EventType", "WebsiteLogin");
		MDC.put("EventData", "Success");
		log.info("Logging in to the Income Tax website"+strLoginUrl);

		FirefoxDriver driver = initiate("IncTax",strLoginUrl,log,strUserName,strFForderNumber,seqNo);
		String strBaseDir = Generic.getBase();
		Boolean blnElementPresent = waitTillElementIsPresent(driver, strITHomePage, 100);
		if(!blnElementPresent){
			driver.quit();
			return true;
			}
		log.info("navigated to incometax url: "+strLoginUrl);
		closePreLoader(jfrm);		
		
		boolean dlUnsuccessful= false;
		boolean blnExec = true;
		try{
			log.info("Started dlITDocs");
			blnExec = dlITDocs(driver,strBaseDir, log, strUserName,strFrom, strCc, strFForderNumber, seqNo);
		}catch(Exception e){
			log.error("dlITDocs: "+e.getMessage());
			dlUnsuccessful=true;
			ITLogOut(driver,log, strUserName,strFrom, strCc, strFForderNumber, seqNo);
		}
		if(!blnExec){
			dlUnsuccessful=true;
			ITLogOut(driver,log,strUserName,strFrom, strCc, strFForderNumber, seqNo);
		}
		
		if(!dlUnsuccessful){
			try{
				MDC.put("EventType", "ZipFileCreation");
			    log.info("initiated zipFiles");
				zipFiles(strBaseDir + "/", "IncomeTax", strPasswordToZip);
				MDC.put("EventData","Success");
				log.info("Mail ZIP Password: "+strPasswordToZip);
			}catch(Exception e){
				
				MDC.put("EventData","Failure"+e.getMessage());
				log.error("zipFiles: "+e.getMessage());
				dlUnsuccessful=true;
				ITLogOut(driver,log,strUserName,strFrom, strCc, strFForderNumber, seqNo);

			}
		}					
		if(!dlUnsuccessful){
			try{
				MDC.put("EventType", "EmailDownloadedFiles");
				log.info("initiated sendMail body");
				log.info(strUserName);
				log.info(strTo);
				log.info(strFrom);
				log.info(strCc);
				log.info(body);
				log.info(subject);
				log.info(strBaseDir + "/");

				gMail.sendMessage(strUserName, strTo, strFrom, strCc,subject, body, strBaseDir + "/", "IncomeTax.zip", log);
				MDC.put("EventData","Success");
				log.info("Mail sent successfully to "+strTo);

			}catch(Exception e){
				MDC.put("EventData","Failure");
				log.error("sendMail body: "+e.getMessage());
				dlUnsuccessful=true;
			}
		}
		if(!dlUnsuccessful){
			try{
				MDC.put("EventType", "SendPassword");
				jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("MailConfig");
				String strSenderId=jObj.get("passwordDeliveryMode").toString();
				 if(strSenderId.equalsIgnoreCase("Email")){
					 
					log.info("initiated sendMail - password");
					gMailAPI.gMail.sendMessage(strUserName, strTo, strFrom,strCc, SubjectPwd, bodyPwd, "", "",log);
					MDC.put("EventData","Success");
					log.info("Password: "+strPasswordToZip+" Mail sent Successfully to"+strTo);
					System.out.println("Mail 2 Sent");
				 }else if(strSenderId.equalsIgnoreCase("SMS")){
					
					log.info("initiated send SMS - password");
					SendMailZippedPassword( strPhoneNumber, strUserNAme, "Income Tax", strPasswordToZip);
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
					 SendMailZippedPassword(strPhoneNumber, strUserNAme, "Income Tax", strPasswordToZip);
					 MDC.put("EventData","Success");
					 log.info("Password: "+strPasswordToZip+" SMS sent successfully to"+strPhoneNumber);
					 System.out.println("SMS password  Sent");
				 }
			}catch(Exception e){
				MDC.put("EventType", "SendPassword");
				MDC.put("EventData","Failure");
				log.error("sendMail - failed: "+e.getMessage());
				dlUnsuccessful=true;
				ITLogOut(driver,log,strUserName,strFrom, strCc, strFForderNumber, seqNo);
			}
		}

		if(!dlUnsuccessful){
			try{
				MDC.put("EventType", "GoogleDriveUpload");
				log.info("Upload to google drive initiated. Name- FFOrder: "+strFForderNumber);
				File fileToDelete = new File(strBaseDir + "/IncTax.zip");
		    	Drive service = gMail.getDriveService();
				deleteFolder(fileToDelete);
				String strParentFolderID= null;
				ArrayList arrList = new ArrayList();
				String[] arrnew = strDriveBaseDir.toString().split("\\\\");

				for(int k=0;k<arrnew.length;k++){
					arrList.add(arrnew[k]);
				}
				arrList.add("FF Order# "+strFForderNumber);
				arrList.add("PullSequenceNumber: "+seqNo);
				arrList.add("IncomeTax");
				String []arr = new String[arrList.size()];
				arrList.toArray(arr);
				String[] arrFolderID=gMail.createFoldersPath(arr).split("~");
				strParentFolderID=arrFolderID[0];
				googleDrivePath=arrFolderID[1];
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
					log.info("Log Out initiated");
					ITLogOut(driver, log,strUserName,strFrom, strCc, strFForderNumber, seqNo);
		
			}catch(Exception e){
				e.printStackTrace();
				log.error("Failed to Logout");
				return dlUnsuccessful=true;
			}
		}
		if(dlUnsuccessful){
			try{
				captureScreenshot(driver, strBaseDir+"./ErrorScreenshot/errIT.png");
				MDC.put("EventType", "CaptureScreenShot");
				MDC.put("EventData","Failure");
				log.info("Screenshot captured Successfully"+strBaseDir+"./ErrorScreenshot/errIT.png");
				driver.close();
		 		driver.quit();
				if(!exit("IncTax",log, strUserName,  strFForderNumber,  seqNo)){
					showPopup("The clean exit was unsuccessful as the files to be deleted are in use. Please close all the files and clear the download folder.");
					log.info("File couldn't deleted");
					SendCleanUpUnsuccessfulMail(strUserName, strFrom, strCc, strFForderNumber, seqNo, "IncomeTax",log);
				}
				log.info("Exit IncomeTax");

			}catch(Exception e){
				e.printStackTrace();
				MDC.put("EventType", "CaptureScreenShot");
				MDC.put("EventData","Failure"+strBaseDir+"./ErrorScreenshot/errIT.png");
				log.error("Failed to take screenshot"+strBaseDir+"./ErrorScreenshot/errIT.png");
				
				if(!exit("IncTax",log, strUserName,  strFForderNumber, seqNo)){
					showPopup("The clean exit was unsuccessful as the files to be deleted are in use. Please close all the files and clear the download folder.");
					log.info("File couldn't deleted");
					SendCleanUpUnsuccessfulMail(strUserName, strFrom, strCc, strFForderNumber, seqNo, "IncomeTax",log);
				}
			
				log.info("Exit IncomeTax");}
		}
		JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("BorrowerDetails");
		String strFFProductCode = jObj.get("FFProductCode").toString();
		if(!dlUnsuccessful){
			MDC.put("EventType","Datapull");
			MDC.put("EventData","Success");
			log.info("datapull for Incometax is compelted");
			
			Generic.writeToPostbackJson("./OutPut/Postback.json", strFForderNumber,strFFProductCode,"Income Tax",seqNo,googleDrivePath,"Success");
		}else{
			MDC.put("EventType","Datapull");
			MDC.put("EventData","Failure");
			log.info("datapull for Incometax is Failed");
			Generic.writeToPostbackJson("./OutPut/Postback.json", strFForderNumber,strFFProductCode,"Income Tax",seqNo,googleDrivePath,"Fail");
		}
		return dlUnsuccessful;
	}
	
	/*strGetProfileInfo method for getting the borrower profile info
	 * */
	public static boolean strGetProfileInfo(FirefoxDriver driver, Logger log) throws Exception{
	try{
									
        String strKey=null;
        String strBaseDir= getBase();
		String strJsonPath=strBaseDir + "/ProfileInfo.json";
		String strInfo="";
		String strKeyName="";
		String strValue="";
		JsonWriter jsonWriter= null;
		//Going to my profile page
		MDC.put("EventType", "GetProfileInfo");
		Click(driver, strDashboard);
		Click(driver, strProfileSetting);
		Click(driver, strMyProfile);
		
		//Reading data from Prfile tab1
		log.info("Read from profile Pan details tab");
		String keyVal=null;
		for(int intI=1;intI<7;intI++){
				 strInfo =strInfo + newline +driver.findElement(By.xpath(strProfileDetailsTable+"tr["+intI+"]/td[1]")).getText() +" : " + driver.findElement(By.xpath(strProfileDetailsTable+"tr["+intI+"]/td[2]")).getText();
				 strKey =driver.findElement(By.xpath(strProfileDetailsTable+"tr["+intI+"]/td[1]")).getText();
				 strValue =driver.findElement(By.xpath(strProfileDetailsTable+"tr["+intI+"]/td[2]")).getText();
				 if(intI==1){
					 keyVal= strKey+"~"+strValue;
				 }else{
					 keyVal = keyVal + "#"+strKey+"~"+strValue;
				 }
			 }
		jsonWriter= writeToJson(strJsonPath,"PanDetails",keyVal,null);

		Click(driver, objAddress);
		keyVal="";
		//Reading data from Prfile tab2
		log.info("Read from profile Address tab");
				strInfo =strInfo + newline + driver.findElement(By.xpath("//*[@id='UpdateCurrentAddress']/table/tbody/tr[2]/td/div[1]/label")).getText() +" : " + driver.findElement(By.xpath("//*[@id='doorNumber']")).getAttribute("value");
				strKey=driver.findElement(By.xpath("//*[@id='UpdateCurrentAddress']/table/tbody/tr[2]/td/div[1]/label")).getText();
				strValue=driver.findElement(By.xpath("//*[@id='doorNumber']")).getAttribute("value");
				if(strValue==""){
					strValue=" ";
				}
				keyVal=strKey+"~"+strValue;
				
				strInfo =strInfo + newline + driver.findElement(By.xpath("//*[@id='UpdateCurrentAddress']/table/tbody/tr[3]/td/div[1]/label")).getText() +" : " + driver.findElement(By.xpath("//*[@id='streetName']")).getAttribute("value");
				strKey=driver.findElement(By.xpath("//*[@id='UpdateCurrentAddress']/table/tbody/tr[3]/td/div[1]/label")).getText();
				strValue=driver.findElement(By.xpath("//*[@id='streetName']")).getAttribute("value");
				if(strValue==""){
					strValue=" ";
				}
				keyVal=keyVal+"#"+strKey+"~"+strValue;

				strInfo =strInfo + newline + driver.findElement(By.xpath("//*[@id='UpdateCurrentAddress']/table/tbody/tr[4]/td/div[1]/label")).getText() +" : " + driver.findElement(By.xpath("//*[@id='locality']")).getAttribute("value");
				strKey=driver.findElement(By.xpath("//*[@id='UpdateCurrentAddress']/table/tbody/tr[4]/td/div[1]/label")).getText();
				strValue=driver.findElement(By.xpath("//*[@id='locality']")).getAttribute("value");
				if(strValue==""){
					strValue=" ";
				}
				keyVal=keyVal+"#"+strKey+"~"+strValue;

				strInfo =strInfo + newline + driver.findElement(By.xpath("//*[@id='UpdateCurrentAddress']/table/tbody/tr[5]/td/div[1]/label")).getText() +" : " + driver.findElement(By.xpath("//*[@id='city']")).getAttribute("value");
				strKey=driver.findElement(By.xpath("//*[@id='UpdateCurrentAddress']/table/tbody/tr[5]/td/div[1]/label")).getText();
				strValue=driver.findElement(By.xpath("//*[@id='city']")).getAttribute("value");
				if(strValue==""){
					strValue=" ";
				}
				keyVal=keyVal+"#"+strKey+"~"+strValue;

				strInfo =strInfo + newline + driver.findElement(By.xpath("//*[@id='UpdateCurrentAddress']/table/tbody/tr[6]/td/div[1]/label")).getText() +" : " + driver.findElement(By.xpath("//*[@id='pinCode']")).getAttribute("value");
				strKey=driver.findElement(By.xpath("//*[@id='UpdateCurrentAddress']/table/tbody/tr[6]/td/div[1]/label")).getText();
				strValue=driver.findElement(By.xpath("//*[@id='pinCode']")).getAttribute("value");
				if(strValue==""){
					strValue=" ";
				}
				keyVal=keyVal+"#"+strKey+"~"+strValue;

				strInfo =strInfo + newline + driver.findElement(By.xpath("//*[@id='UpdateCurrentAddress']/table/tbody/tr[7]/td/div[1]/label")).getText() +" : " + driver.findElement(By.xpath("//*[@id='UpdateCurrentAddress_disp_currentAddress_state']")).getAttribute("value");
				strKey=driver.findElement(By.xpath("//*[@id='UpdateCurrentAddress']/table/tbody/tr[7]/td/div[1]/label")).getText();
				strValue=driver.findElement(By.xpath("//*[@id='UpdateCurrentAddress_disp_currentAddress_state']")).getAttribute("value");
				if(strValue==""){
					strValue=" ";
				}
				keyVal=keyVal+"#"+strKey+"~"+strValue;

				strInfo =strInfo + newline + driver.findElement(By.xpath("//*[@id='UpdateCurrentAddress']/table/tbody/tr[8]/td/div[1]/label")).getText() +" : " + driver.findElement(By.xpath("//*[@id='UpdateCurrentAddress_disp_currentAddress_country']")).getAttribute("value");
				strKey=driver.findElement(By.xpath("//*[@id='UpdateCurrentAddress']/table/tbody/tr[9]/td/div[1]/label")).getText();
				strValue=driver.findElement(By.xpath("//*[@id='UpdateCurrentAddress_disp_currentAddress_country']")).getAttribute("value");
				if(strValue==""){
					strValue=" ";
				}
				keyVal=keyVal+"#"+strKey+"~"+strValue;
				
				writeToJson(strJsonPath,"Address",keyVal,jsonWriter);
				 
		//Reading data from Profile tab
				keyVal= "";
				log.info("Read from profile Contact details tab");
				ClickUsingJS(driver, objContactDetails);
				strInfo =strInfo + newline + "Primary "+driver.findElement(By.xpath("//*[@id='UpdateContactAction']/table/tbody/tr[4]/td/div[1]/label")).getText() +" : " + driver.findElement(By.xpath("//*[@id='mobileNo']")).getAttribute("value");
				strKey=driver.findElement(By.xpath("//*[@id='UpdateContactAction']/table/tbody/tr[4]/td/div[1]/label")).getText();
				strValue=driver.findElement(By.xpath("//*[@id='mobileNo']")).getAttribute("value");
				if(strValue==""){
					strValue=" ";
				}
				keyVal=strKey+"~"+strValue;

				strInfo =strInfo + newline + "Primary "+ driver.findElement(By.xpath("//*[@id='UpdateCurrentAddress']/table/tbody/tr[5]/td/div[1]/label")).getText() +" : " + driver.findElement(By.xpath("//*[@id='emailId']")).getAttribute("value");
				strKey=driver.findElement(By.xpath("//*[@id='UpdateContactAction']/table/tbody/tr[5]/td/div[1]/label")).getText();
				strValue=driver.findElement(By.xpath("//*[@id='emailId']")).getAttribute("value");
				if(strValue==""){
					strValue=" ";
				}
				keyVal=keyVal+"#"+strKey+"~"+strValue;
				
				writeToJson(strJsonPath,"Address",keyVal,jsonWriter);
				closeJson(jsonWriter);
				
				MDC.put("EventData","Success");
				Log.info("Profile Information"+strInfo);
				return true;
			}catch(Exception e){
				e.printStackTrace();						
				MDC.put("EventData","Failure"+e.getMessage());
				log.error("Failed To get the profile information"+e.getMessage());
				return false;
				}
	}
	
	
	
	/*LogOut Methode for the Income tax*/
	
	public static boolean ITLogOut(FirefoxDriver driver, Logger log, String strUserName,String strFrom,String strCc,String strFForderNumber,String seqNo ){
		
		MDC.put("EventType", "WebsiteLogout");
		MDC.put("EventData","Success");
		log.info("initiated logout");
		boolean bStatus=false;
		
		try{
			if(driver.findElements(By.xpath(objINcTaxLogout)).size()>0){
				ClickUsingJS(driver, objINcTaxLogout);
				log.info("Logout clicked");
				waitAftereLogout(driver);
				MDC.put("EventData","Success");
				log.info("Logout clicked");
				log.info("logged out successfully from "+strLoginUrl);
				driver.close();
					if(!exit("IncTax",log, strUserName,  strFForderNumber, seqNo)){
						showPopup("The clean exit was unsuccessful as the files to be deleted are in use. Please close all the files and clear the download folder.");
						MDC.put("EventData", "Failure");
						log.info("File couldn't deleted");
						SendCleanUpUnsuccessfulMail(strUserName, strFrom, strCc, strFForderNumber, seqNo, "IncomeTax",log);
						}
				log.info("Exit IncomeTax");
				}
			bStatus=true;
			}catch(Exception e){
						MDC.put("EventType", "WebsiteLogout");
						MDC.put("EventData","Failure");
						log.error("Failed to logout  From "+strLoginUrl+ "+e.getMessage()");
						e.printStackTrace();
						bStatus=false;
						}
		return bStatus;
		}
	}

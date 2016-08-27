package Generic;

import gMailAPI.gMail;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sound.midi.MidiDevice.Info;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mortbay.log.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.Select;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.ParentReference;
import com.google.gson.stream.JsonWriter;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;



public class Generic {
	private static String UserNAme;
	private static String EmailId; 
	private static String longPhoneNumber;
	private static String strFForderNo;
	static JSONArray arrDataPullEntity = new JSONArray();
	static String dataPullToExe;
	static String ZoomPercent;
	private static String strApiKey;
	static int resendOTPCount;
	static String strBaseDir;
	static String CurrentWindowHandle;

	
	public static void initialize(){
		
			try {
				arrDataPullEntity.add(readJSON("./InputData/datapullcfg.json").get("dataPullToRun"));
			} catch (IOException | ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				dataPullToExe=arrDataPullEntity.get(0).toString();
				if(dataPullToExe.equalsIgnoreCase("IncTax")){
				JSONArray  arrIT = new JSONArray();
	
				try {
					arrIT.add(readJSON("./InputData/datapullcfg.json").get("IncTax"));
					JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("IncTax");
					ZoomPercent = jObj.get("ZoomInPercent").toString();
				} catch (IOException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(dataPullToExe.equalsIgnoreCase("Vodafone")){
				JSONArray  arrIT = new JSONArray();
	
				try {
					arrIT.add(readJSON("./InputData/datapullcfg.json").get("Vodafone"));
					JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("Vodafone");
					ZoomPercent = jObj.get("ZoomInPercent").toString();
				} catch (IOException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(dataPullToExe.equalsIgnoreCase("ReliancePower")){
				JSONArray  arrIT = new JSONArray();
	
				try {
					arrIT.add(readJSON("./InputData/datapullcfg.json").get("ReliancePower"));
					JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("ReliancePower");
					ZoomPercent = jObj.get("ZoomInPercent").toString();
				} catch (IOException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(dataPullToExe.equalsIgnoreCase("Mahadiscom")){
				JSONArray  arrmd = new JSONArray();
	
				try {
					arrmd.add(readJSON("./InputData/datapullcfg.json").get("Mahadiscom"));
					JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("Mahadiscom");
					ZoomPercent = jObj.get("ZoomInPercent").toString();
				} catch (IOException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(dataPullToExe.equalsIgnoreCase("airtel")){
				JSONArray  arrmd = new JSONArray();
	
				try {
					arrmd.add(readJSON("./InputData/datapullcfg.json").get("airtel"));
					JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("airtel");
					ZoomPercent = jObj.get("ZoomInPercent").toString();
				} catch (IOException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(dataPullToExe.equalsIgnoreCase("MTNL")){
				JSONArray  arrmd = new JSONArray();
	
				try {
					arrmd.add(readJSON("./InputData/datapullcfg.json").get("MTNL"));
					JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("MTNL");
					ZoomPercent = jObj.get("ZoomInPercent").toString();
				} catch (IOException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(dataPullToExe.equalsIgnoreCase("BSNL")){
				JSONArray  arrmd = new JSONArray();
	
				try {
					arrmd.add(readJSON("./InputData/datapullcfg.json").get("BSNL"));
					JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("BSNL");
					ZoomPercent = jObj.get("ZoomInPercent").toString();
				} catch (IOException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(dataPullToExe.equalsIgnoreCase("ReliancePostPaid")){
				JSONArray  arrmd = new JSONArray();
	
				try {
					arrmd.add(readJSON("./InputData/datapullcfg.json").get("ReliancePostPaid"));
					JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("ReliancePostPaid");
					ZoomPercent = jObj.get("ZoomInPercent").toString();
				} catch (IOException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(dataPullToExe.equalsIgnoreCase("BSNLLandline")){
				JSONArray  arrmd = new JSONArray();
	
				try {
					arrmd.add(readJSON("./InputData/datapullcfg.json").get("BSNLLandline"));
					JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("BSNLLandline");
					ZoomPercent = jObj.get("ZoomInPercent").toString();
				} catch (IOException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(dataPullToExe.equalsIgnoreCase("IdeaPostpaid")){
				JSONArray  arrmd = new JSONArray();
	
				try {
					arrmd.add(readJSON("./InputData/datapullcfg.json").get("IdeaPostpaid"));
					JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("IdeaPostpaid");
					ZoomPercent = jObj.get("ZoomInPercent").toString();
				} catch (IOException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(dataPullToExe.equalsIgnoreCase("Best")){
				JSONArray  arrIT = new JSONArray();
	
				try {
					arrIT.add(readJSON("./InputData/datapullcfg.json").get("Best"));
					JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("Best");
					ZoomPercent = jObj.get("ZoomInPercent").toString();
				} catch (IOException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	
	
	}
	
	public static Boolean zipFiles(String destinationZipFilePath,String ZipFileName,String Password) throws ZipException{
		ZipParameters zipParameters = new ZipParameters();

        // Set how you want to encrypt files
        zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        if(!Password.trim().equals("")){
	        // Set encryption of files to true
	        zipParameters.setEncryptFiles(true);
	
	        // Set encryption method
	        zipParameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
	        // Set key strength
	        //zipParameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_128);
	
	        // Set password
	        zipParameters.setPassword(Password);
	        System.out.println("Password protected Zip file of Directory "
	                +destinationZipFilePath+"/"+ZipFileName+".zip"+" have been created.");

        }
        // Create ZIP file
        ZipFile zipFile = new ZipFile(destinationZipFilePath+"/"+ZipFileName+".zip");
        zipFile.addFolder(destinationZipFilePath, zipParameters);
        File file = new File(destinationZipFilePath+"/"+ZipFileName+".zip");
        if(file.exists())
        {
        	return true;
        }else
        {
        	return false;
        }

	}
				
	public static void deleteFolder(File folder) {
	    File[] files = folder.listFiles();
	    if(files!=null) { //some JVMs return null for empty dirs
	        for(File f: files) {
	            if(f.isDirectory()) {
	                deleteFolder(f);
	            } else {
	                f.delete();
	            }
	        }
	    }
	    folder.delete();
	}
	public static boolean waitTillElementIsPresent(WebDriver driver, String elementXpath,int maxTime) throws InterruptedException{
		boolean blnExist= true;
		int intTime =0;
		try{
			while(blnExist){
				int intCountWE = driver.findElements(By.xpath(elementXpath)).size();
				if(intCountWE>0) {
					blnExist=true;
					break;
				}else{
					Thread.sleep(1000);
					intTime++;
					System.out.println("Time: "+System.currentTimeMillis());
	
				}
				if(intTime>maxTime){
					System.out.println("Timed out! Breaking out~~");
					blnExist= false;
				}
			}
		}catch(Exception e){
			blnExist= false;
		}
		return blnExist;
	}
	
	public static void WriteToCSV(String sFileName, String strText) throws IOException{
	    FileWriter writer = new FileWriter(sFileName);
	    writer.write(strText);
	    writer.flush();
	    writer.close();
	}
	
	public static void WriteToLog(String sFileName, String strText) throws IOException{
	    FileWriter writer = new FileWriter(sFileName,true);
	    writer.write(strText);
	    writer.flush();
	    writer.close();
	}
	
	public static boolean ClickUsingJS(FirefoxDriver driver,String elementXpath) throws Exception{
		boolean bStatus=false;
		String timeToDelay=null;
		int minWait =2000;
		try{
			waitTillElementIsPresent(driver, elementXpath, 10);
			JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("BasicInfo");
			timeToDelay= (String) jObj.get("timeDelaybetweenClicksInMilliSeconds");
			Thread.sleep(Integer.parseInt(timeToDelay));
			WebElement buttonOrLink=driver.findElementByXPath(elementXpath);
			System.out.println("asd");
			driver.executeScript("arguments[0].click();", buttonOrLink);
			Thread.sleep(minWait);
			bStatus=true;
		}catch(Exception e){
			bStatus=false;
			e.printStackTrace();
		}
		return bStatus;
	}
	
	/*Click method*/
	public static boolean Click(FirefoxDriver driver,String elementXpath) throws Exception{
		boolean bStatus=false;
		int minWait =2000;
		String timeToDelay=null;
		//try{
			waitTillElementIsPresent(driver, elementXpath, 10);
			JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("BasicInfo");
		    timeToDelay= (String) jObj.get("timeDelaybetweenClicksInMilliSeconds");
			Thread.sleep(Integer.parseInt(timeToDelay));
			WebElement buttonOrLink=(driver).findElementByXPath(elementXpath);
			buttonOrLink.click();
			Thread.sleep(minWait);
			bStatus=true;
			
//		}catch(Exception e){
//			bStatus=false;
//			e.printStackTrace();
//		}
		return bStatus;
	}
	
	public static void outputToLog() throws FileNotFoundException{
		try {
			JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("BasicInfo");
			String strBaseDir = jObj.get("baseDir").toString();

		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void waitTillElementIsNotPresent(WebDriver driver, String elementXpath,int maxTime) throws InterruptedException{
		boolean blnExist= true;
		int intTime =0;
		while(blnExist){
			int intCountWE = driver.findElements(By.xpath(elementXpath)).size();
			if(intCountWE==0) {
				blnExist=true;
				System.out.println("Element Disappeared!!");
				break;
			}else{
				Thread.sleep(10000);
				intTime++;
				System.out.println("Time: "+System.currentTimeMillis());

			}
			if(intTime>maxTime){
				System.out.println("Timed out! Breaking out~~");
				driver.quit();
				break;
			}
		}
	}
	public static String readFile(String path) 
	{
		String line = "";
	    BufferedReader in = null;
		String strAllLines = "";

	    try {
			in = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			while ((line = in.readLine()) != null) {
			    // Skip lines that are empty or only contain whitespace
			    
				if(!line.trim().isEmpty()){
					if(strAllLines==""){
						strAllLines=line;
					}else{
						strAllLines=strAllLines+line;
					}
					
				}else{
					strAllLines = strAllLines + "#newLine#";
			    }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strAllLines;
	}
	

	public static void ImplicitWait(WebDriver driver)
	{
	driver.manage().timeouts().implicitlyWait(15,TimeUnit.SECONDS);
	}

	public static void SelectItemFromDropdownByText(WebDriver driver,String strObjectXpath, String strTextValueToselect){
        WebElement Dropdown =driver.findElement(By.xpath(strObjectXpath));
        Select sel = new Select(Dropdown);
        sel.selectByVisibleText(strTextValueToselect);
    }
	
	public static void ImplicitWait(WebDriver driver, int intTime)
	
	{
	driver.manage().timeouts().implicitlyWait(intTime,TimeUnit.SECONDS);
	}


//	public static String getMainWindowHandle(WebDriver driver) {
//		return driver.getWindowHandle();
//		driver.
//	}

	protected static String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }
	static protected String strVersion="";
	public static long termsAndConditionPopup() throws FileNotFoundException, IOException, ParseException{
		initialize();
		JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("TermsAndCondition");
		int intFontSize =Integer.parseInt(jObj.get("termsAndcondition_popup_textSize").toString());
		int intPopUpHeight =Integer.parseInt(jObj.get("termsAndcondition_popup_height").toString());
		int intPopUpWidth =Integer.parseInt(jObj.get("termsAndcondition_popup_width").toString());
		String strFontsName =(String) jObj.get("termsAndCondition_popup_FontName").toString();

		//String strVersion="";
		String strUtility = dataPullToExe;//(String) readJSON("./InputData/datapullcfg.json").get("dataPulls");
		 jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("VersionInfo");
		if(strUtility.equalsIgnoreCase("vodafone")){
			strVersion = jObj.get("Vodafone_Version").toString();
			Log.info(strVersion);
		}else if(strUtility.equalsIgnoreCase("inctax")){
			strVersion = jObj.get("IT_Version").toString();
			Log.info(strVersion);
		}else if(strUtility.equalsIgnoreCase("ReliancePower")){
			strVersion = jObj.get("Reliance_Version").toString();
			Log.info(strVersion);
		}else if(strUtility.equalsIgnoreCase("BSNL")){
			strVersion = jObj.get("BSNL_Version").toString();
			Log.info(strVersion);
		}else if(strUtility.equalsIgnoreCase("MTNL")){
			strVersion = jObj.get("MTNL_Version").toString();
			Log.info(strVersion);
		}else if(strUtility.equalsIgnoreCase("Mahadiscom")){
			strVersion = jObj.get("Mahadiscom_Version").toString();
			Log.info(strVersion);
		}else if(strUtility.equalsIgnoreCase("airtel")){
			strVersion = jObj.get("airtel_Version").toString();
			Log.info(strVersion);
		}else if(strUtility.equalsIgnoreCase("ReliancePostPaid")){
			strVersion = jObj.get("ReliancePostPaid_Version").toString();
			Log.info(strVersion);
		}else if(strUtility.equalsIgnoreCase("BSNLLandline")){
			strVersion = jObj.get("BSNLLandline_Version").toString();
			Log.info(strVersion);
		}else if(strUtility.equalsIgnoreCase("IdeaPostpaid")){
			strVersion = jObj.get("IdeaPostpaid_Version").toString();
			Log.info(strVersion);
		}else if(strUtility.equalsIgnoreCase("Best")){
			strVersion = jObj.get("Best_Version").toString();
			Log.info(strVersion);
		}

        String newline = System.getProperty("line.separator");
        String arrTermsAndConditions[] = readFile("./InputMessages/TermsAndCondition.txt").split("#newLine#");
        String strTermsAndCondition = "";

        for(int intLoop=0;intLoop<arrTermsAndConditions.length;intLoop++){
           if(strTermsAndCondition==""){
                 strTermsAndCondition =arrTermsAndConditions[intLoop];
           }else{
                 strTermsAndCondition =strTermsAndCondition+ newline+arrTermsAndConditions[intLoop];
           }
        }

  JPanel panel = new JPanel();
  panel.setPreferredSize( new Dimension( intPopUpWidth,intPopUpHeight ) );
  panel.setAutoscrolls(true);
  StyleContext context = new StyleContext();
  StyledDocument document = new DefaultStyledDocument(context);
  Style style = context.getStyle(StyleContext.DEFAULT_STYLE);
  StyleConstants.setAlignment(style, StyleConstants.ALIGN_JUSTIFIED);
  StyleConstants.setFontFamily(style, strFontsName);
  StyleConstants.setFontSize(style, intFontSize);
  StyleConstants.setSpaceAbove(style, 4);
  StyleConstants.setSpaceBelow(style, 4);
  try {
     document.insertString(document.getLength(), strTermsAndCondition, style);
   } catch (BadLocationException badLocationException) {
     System.err.println("Oops");
       }
     JTextPane textPane = new JTextPane(document);
     textPane.setEditable(false);
     JScrollPane scrollPane = new JScrollPane(textPane);
     scrollPane.setPreferredSize( new Dimension( intPopUpWidth,intPopUpHeight ) );
     panel.add(scrollPane);
     Object[] options = { "Accept", "Decline" };
     long strOutput = JOptionPane.showOptionDialog(null, panel, "v"+strVersion+" - Terms and Conditions",
	 JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
	 null, options, options[0]);
     System.out.println(strOutput);
     return strOutput;
 }
	public static String popupInput(String strFForderNo,String strUserNAme,String strEmailId,String strPhoneNumber,String BorrowerEmailID_Again,String strRunSequenceNo) throws FileNotFoundException, IOException, ParseException{
		String userInfoPopupFontName="";
		int userInfoPopupFontSize=0;
		
		try {
			JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("PopupConfig");
			 userInfoPopupFontName = (String) jObj.get("userInfoPopupFontName");
			 userInfoPopupFontSize = Integer.parseInt((String) jObj.get("userInfoPopupFontSize"));
		} catch (IOException | ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("BorrowerDetails");
		 if(strUserNAme.equals(""))
			 strUserNAme=(String) jObj.get("UserName");
		 
		 if(strEmailId.equals(""))
			 strEmailId=(String) jObj.get("MailID");
		if(strPhoneNumber.equals(""))
		 strPhoneNumber=(String) jObj.get("PhoneNumber");
		if(strFForderNo.equals("")) 
			strFForderNo=(String) jObj.get("FFOrderNo");
		if(strRunSequenceNo.equals(""))
			strRunSequenceNo=jObj.get("PullSequenceNO").toString();

			JTextField Name = new JTextField();
			JTextField EmailID = new JTextField();
			JTextField VerifyEmailID = new JTextField();
			JTextField MobileNO = new JTextField();
			JTextField FFOrderNo=new JTextField();
			JTextField PullSequenceNO=new JTextField();
			VerifyEmailID.setTransferHandler(null);
			
		Object[] message = {
		    "Borrower Name:", Name,
		    "Email ID:", EmailID,
		    "Verify Email ID:",VerifyEmailID,
		    "Mobile No:",MobileNO,
		    "FF Order No :",FFOrderNo,
		    "Pull Sequence No :",PullSequenceNO,
		};
		MobileNO.addKeyListener(new  KeyAdapter(){
			 public void keyTyped(KeyEvent e) {
		            char vChar = e.getKeyChar();
		            if (!(Character.isDigit(vChar)
		                  || (vChar == KeyEvent.VK_BACK_SPACE)
		                  || (vChar == KeyEvent.VK_CLEAR)
		                  || (vChar == KeyEvent.VK_PERIOD)
		                  || (vChar == KeyEvent.VK_PLUS)
		                  || (vChar == KeyEvent.VK_MINUS))) {
		               // getToolkit().beep();
		                e.consume();
		            }

		}
		});

		if(!strUserNAme.equalsIgnoreCase("")){
			Name.setText(strUserNAme);
			Name.setEnabled(false);
		}
		
		if(!strEmailId.equalsIgnoreCase("")){
			EmailID.setText(strEmailId);
			EmailID.setEnabled(false);
		}

		if(!strPhoneNumber.equalsIgnoreCase("")){
			MobileNO.setText(strPhoneNumber);
			MobileNO.setEnabled(false);
		}

		if(!strFForderNo.equals("")){
			FFOrderNo.setText(strFForderNo);
			FFOrderNo.setEnabled(false);
		}
		if(!strRunSequenceNo.equals("")){
			PullSequenceNO.setText(strRunSequenceNo);
			PullSequenceNO.setEnabled(false);
		}
		
		if(!BorrowerEmailID_Again.equalsIgnoreCase("")){
			VerifyEmailID.setText(BorrowerEmailID_Again);
			VerifyEmailID.setEnabled(false);
		}
	
		Boolean blnIDmatch=false;
		Boolean blnBlank=false;
		Boolean blnValidMob=false;

		while(!(blnIDmatch&&blnBlank&&blnValidMob)){
			//popupInput();
			//JOptionPane.showConfirmDialog(null, message, "User Information", JOptionPane.PLAIN_MESSAGE);
			UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font(userInfoPopupFontName,Font.PLAIN,userInfoPopupFontSize))); 
			UIManager.put("OptionPane.messageFont", new FontUIResource(new Font(userInfoPopupFontName,Font.PLAIN,userInfoPopupFontSize))); 
			Name.setFont(new Font(userInfoPopupFontName,Font.PLAIN,userInfoPopupFontSize));
			EmailID.setFont(new Font(userInfoPopupFontName,Font.PLAIN,userInfoPopupFontSize));
			VerifyEmailID.setFont(new Font(userInfoPopupFontName,Font.PLAIN,userInfoPopupFontSize));
			EmailID.setFont(new Font(userInfoPopupFontName,Font.PLAIN,userInfoPopupFontSize));
			MobileNO.setFont(new Font(userInfoPopupFontName,Font.PLAIN,userInfoPopupFontSize));
			FFOrderNo.setFont(new Font(userInfoPopupFontName,Font.PLAIN,userInfoPopupFontSize));
			PullSequenceNO.setFont(new Font(userInfoPopupFontName,Font.PLAIN,userInfoPopupFontSize));
			
			int response=JOptionPane.showConfirmDialog(null, message, "User Information", JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
			 if (response == JOptionPane.CANCEL_OPTION) {
				 
				 MDC.put("EventType", "datapullCancelledByBorrower");
				 Log.info("Datapull cancelled by Borrower");
		         System.exit(0);
			 }
			if(EmailID.getText().trim().equals("") || Name.getText().trim().equals("")||VerifyEmailID.getText().trim().equals("")||MobileNO.getText().trim().equals("")||FFOrderNo.getText().trim().equals("")||PullSequenceNO.getText().trim().equals("")){
				MDC.put("EventData", "Field left blank");
				Log.info("Field Left blank");
				showPopup("No field should be left blank.");
				blnBlank= false;
			}else{
				blnBlank= true;
			} 
			
			if(blnBlank && !EmailID.getText().toString().equalsIgnoreCase(VerifyEmailID.getText().toString())){
				MDC.put("EventData", "Email address doesn't match");
				Log.info("Email ID doesn't match");
				showPopup("Email address doesn't match.");
				VerifyEmailID.setEditable(true);
				EmailID.setEnabled(true);
				blnIDmatch= false;
		    }else{
		    	blnIDmatch=true;
		    }
			Pattern pattern = Pattern.compile("\\d{10}");
			Matcher matcher = pattern.matcher(MobileNO.getText().toString());
			Matcher matcher1 = pattern.matcher(strPhoneNumber);
			if (!(matcher.matches()|| matcher1.matches())){
				 	MDC.put("EventData", "Invalid Mobile No"+strPhoneNumber);
				 	Log.info("Invalid Mobile Number");
					showPopup("Invalid Mobile Number");					
					MobileNO.setEnabled(true);
					blnValidMob=false;
					}else{
						blnValidMob=true;
						}
			}
		return Name.getText()+"~"+EmailID.getText()+"~"+MobileNO.getText()+"~"+FFOrderNo.getText()+"~"+PullSequenceNO.getText();
	}

public static void showPopup(String strMessage, FirefoxDriver driver,Logger log,String UserNAme,String strFForderNo,String strPullSeqNo){
	Object[] options = { "OK","Cancel"};
	String userInfoPopupFontName="";
	int userInfoPopupFontSize=0;
	MDC.put("EventType","PopUp");
	MDC.put("EventData", "Displayed");
	log.info(strMessage+"is displayed");
	try {
		JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("PopupConfig");
		 userInfoPopupFontName = (String) jObj.get("userInfoPopupFontName");
		 userInfoPopupFontSize = Integer.parseInt((String) jObj.get("userInfoPopupFontSize"));
	} catch (IOException | ParseException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font(userInfoPopupFontName,Font.PLAIN,userInfoPopupFontSize))); 
	UIManager.put("OptionPane.messageFont", new FontUIResource(new Font(userInfoPopupFontName,Font.PLAIN,userInfoPopupFontSize))); 

	int Option=JOptionPane.showOptionDialog(null, strMessage, "Info",
			JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,	
			null, options, options[0]);
	if (Option == JOptionPane.YES_OPTION) {
		MDC.put("EventData", "Success");
	      log.info("Clicked OK on PopUp");

	} else if (Option == JOptionPane.NO_OPTION) {
      driver.close();
      MDC.put("EventData", "Failure");
      log.info("Clicked Cancel on PopUp");
      try {
		exit("all",log, UserNAme, strFForderNo, strPullSeqNo);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      System.exit(0);
	
	}
}

	public static void showPopup(String strMessage){
		MDC.put("EventType", "PopUp");
		MDC.put("EventData", "Displayed");
		Log.info(strMessage);
		Object[] options = { "OK"};
		String userInfoPopupFontName="";
		int userInfoPopupFontSize=0;
		try {
			 JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("PopupConfig");
			 userInfoPopupFontName = (String) jObj.get("userInfoPopupFontName");
			 userInfoPopupFontSize = Integer.parseInt((String) jObj.get("userInfoPopupFontSize"));
		} catch (IOException | ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font(userInfoPopupFontName,Font.PLAIN,userInfoPopupFontSize))); 
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font(userInfoPopupFontName,Font.PLAIN,userInfoPopupFontSize))); 
	
		int n=JOptionPane.showOptionDialog(null, strMessage, "Info",
				JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,	
				null, options, options[0]);
	}
	
	public static void showPopupWithOKCancel(String strMessage){
		
		Object[] options = { "OK","Cancel"};
		String userInfoPopupFontName="";
		int userInfoPopupFontSize=0;
		try {
			 JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("PopupConfig");
			 userInfoPopupFontName = (String) jObj.get("userInfoPopupFontName");
			 userInfoPopupFontSize = Integer.parseInt((String) jObj.get("userInfoPopupFontSize"));
		} catch (IOException | ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font(userInfoPopupFontName,Font.PLAIN,userInfoPopupFontSize))); 
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font(userInfoPopupFontName,Font.PLAIN,userInfoPopupFontSize))); 
	
		int n=JOptionPane.showOptionDialog(null, strMessage, "Info",
				JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,	
				null, options, options[0]);
		if(n==1){
			System.exit(0);
		}
	}

	public static long generateRandomNumber(){
		long number = (long) Math.floor(Math.random() * 9000L) + 1000L;
		return number;
	}
		
	public static boolean exit(String Module,Logger log,String UserNAme,String strFForderNo,String strPullSeqNo)
			throws IOException {
		MDC.put("EventType","FinalCleanup");
		MDC.put("EventData","Success");
		log.info("Clean Up initiated for "+Module);
		String strBaseDir = null;
		try {
			JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("BasicInfo");
			strBaseDir = jObj.get("baseDir").toString();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try{
			String strDir = null;
			File theDir = new File(strBaseDir);
			if (!theDir.exists()) {
				theDir.mkdir();
				}
			if (Module.trim().equalsIgnoreCase("IncTax")) {
				strBaseDir=Generic.getBase();
				strDir = strBaseDir;
				File file = new File(strDir + "/");
				FileUtils.deleteDirectory(file);
				MDC.put("EventData","Success");
				log.info("directory is deleted "+file);
				file.mkdir();
			} else if (Module.trim().equalsIgnoreCase("ReliancePower")) {
				strBaseDir=Generic.getBase();

				strDir = strBaseDir;
				File file = new File(strDir + "/");
				FileUtils.deleteDirectory(file);
				MDC.put("EventData","Success");
				log.info("directory is deleted "+file);
				file.mkdir();
			} else if (Module.trim().equalsIgnoreCase("vodafone")) {
				strBaseDir=Generic.getBase();

				strDir = strBaseDir;
				File file = new File(strDir + "/");
				FileUtils.deleteDirectory(file);
				MDC.put("EventData","Success");
				log.info("directory is deleted "+file);
				file.mkdir();
			}else if (Module.trim().equalsIgnoreCase("Mahadiscom")) {
				strBaseDir=Generic.getBase();
				strDir = strBaseDir;
				File file = new File(strDir + "/");
				FileUtils.deleteDirectory(file);
				MDC.put("EventData","Success");
				log.info("directory is deleted "+file);
				file.mkdir();
			}else if (Module.trim().equalsIgnoreCase("airtel")) {
				strBaseDir=Generic.getBase();
				strDir = strBaseDir;
				File file = new File(strDir + "/");
				FileUtils.deleteDirectory(file);
				MDC.put("EventData","Success");
				log.info("directory is deleted "+file);
				file.mkdir();
			}else if (Module.trim().equalsIgnoreCase("MTNL")) {
				strBaseDir=Generic.getBase();
				strDir = strBaseDir;
				File file = new File(strDir + "/");
				FileUtils.deleteDirectory(file);
				MDC.put("EventData","Success");
				log.info("directory is deleted "+file);
				file.mkdir();
			}else if (Module.trim().equalsIgnoreCase("BSNL")) {
				strBaseDir=Generic.getBase();
				strDir = strBaseDir;
				File file = new File(strDir + "/");
				FileUtils.deleteDirectory(file);
				MDC.put("EventData","Success");
				log.info("directory is deleted "+file);
				file.mkdir();
			}else if (Module.trim().equalsIgnoreCase("IdeaPostpaid")) {
				strBaseDir=Generic.getBase();
				strDir = strBaseDir;
				File file = new File(strDir + "/");
				FileUtils.deleteDirectory(file);
				MDC.put("EventData","Success");
				log.info("directory is deleted "+file);
				file.mkdir();
			}else if (Module.trim().equalsIgnoreCase("ReliancePostPaid")) {
				strBaseDir=Generic.getBase();
				strDir = strBaseDir;
				File file = new File(strDir + "/");
				FileUtils.deleteDirectory(file);
				MDC.put("EventData","Success");
				log.info("directory is deleted "+file);
				file.mkdir();
			}else if (Module.trim().equalsIgnoreCase("BSNLLandline")) {
				strBaseDir=Generic.getBase();
				strDir = strBaseDir;
				File file = new File(strDir + "/");
				FileUtils.deleteDirectory(file);
				MDC.put("EventData","Success");
				log.info("directory is deleted "+file);
				file.mkdir();
			}else if (Module.trim().equalsIgnoreCase("Best")) {
				strBaseDir=Generic.getBase();
				strDir = strBaseDir;
				File file = new File(strDir + "/");
				FileUtils.deleteDirectory(file);
				MDC.put("EventData","Success");
				log.info("directory is deleted "+file);
				file.mkdir();
			}else if (Module.trim().equalsIgnoreCase("all")){
				strBaseDir=Generic.getBase();	
				strDir = strBaseDir;
				File file = new File(strDir + "/");
				FileUtils.deleteDirectory(file);
			}
			MDC.put("EventData", "Success");
			log.info("Final Cleanup Completed");
		}catch(Exception e){
			e.printStackTrace();
			MDC.put("EventData","Failure");
			log.error("Failed to clear the directory. Please close all the files and re-run the utility.");
			showPopup("Failed to clear the directory. Please close all the files and re-run the utility.");
			String strFrom= null;
			String strCc = null;
			JSONObject jObj;
			try {
				jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("MailConfig");
				 strFrom=jObj.get("fromEmail").toString();
			     strCc=jObj.get("ccEmails").toString();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			SendCleanUpUnsuccessfulMail(UserNAme, strFrom, strCc, strFForderNo,strPullSeqNo,Module,log);
			return false;
		}
		return true;
	}
	public static String getBase(){
		return strBaseDir;
	}
	public static FirefoxDriver initiate(String Module, String URL, Logger log,String UserNAme,String strFForderNo,String strPullSeqNo)
			throws IOException, ParseException {
		JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("BasicInfo");
		String BaseDir = jObj.get("baseDir").toString();

		try{
			String strDir = null;
			File theDir = new File(BaseDir);
			if (!theDir.exists()) {
				theDir.mkdir();
			}
			if (Module.trim().equalsIgnoreCase("IncTax")) {
				if(strBaseDir!=null){
					File file = new File(strBaseDir + "/");
					FileUtils.deleteDirectory(file);
				}
				strBaseDir=getBaseDir(BaseDir,strFForderNo,strPullSeqNo,"IncomeTax").toString();
				strDir = strBaseDir ;
				File file = new File(strDir + "/");
				FileUtils.deleteDirectory(file);
				file.mkdir();
			} else if (Module.trim().equalsIgnoreCase("ReliancePower")) {
				strBaseDir=getBaseDir(BaseDir,strFForderNo,strPullSeqNo,"ReliancePower").toString();
				strDir = strBaseDir;
				File file = new File(strDir + "/");
				FileUtils.deleteDirectory(file);
				file.mkdir();
			} else if (Module.trim().equalsIgnoreCase("vodafone")) {
				strBaseDir=getBaseDir(BaseDir,strFForderNo,strPullSeqNo,"vodafone").toString();
				strDir = strBaseDir;
				File file = new File(strDir + "/");
				FileUtils.deleteDirectory(file);
				file.mkdir();
			}else if (Module.trim().equalsIgnoreCase("Mahadiscom")) {
				strBaseDir=getBaseDir(BaseDir,strFForderNo,strPullSeqNo,"Mahadiscom").toString();
				strDir = strBaseDir;
				File file = new File(strDir + "/");
				FileUtils.deleteDirectory(file);
				file.mkdir();
			}else if (Module.trim().equalsIgnoreCase("airtel")) {
				strBaseDir=getBaseDir(BaseDir,strFForderNo,strPullSeqNo,"airtel").toString();
				strDir = strBaseDir;
				File file = new File(strDir + "/");
				FileUtils.deleteDirectory(file);
				file.mkdir();
			}else if (Module.trim().equalsIgnoreCase("MTNL")) {
				strBaseDir=getBaseDir(BaseDir,strFForderNo,strPullSeqNo,"MTNL").toString();
				strDir = strBaseDir;
				File file = new File(strDir + "/");
				FileUtils.deleteDirectory(file);
				file.mkdir();
			}else if (Module.trim().equalsIgnoreCase("BSNL")) {
				strBaseDir=getBaseDir(BaseDir,strFForderNo,strPullSeqNo,"BSNL").toString();
				strDir = strBaseDir;
				File file = new File(strDir + "/");
				FileUtils.deleteDirectory(file);
				file.mkdir();
			}else if (Module.trim().equalsIgnoreCase("ReliancePostPaid")) {
				strBaseDir=getBaseDir(BaseDir,strFForderNo,strPullSeqNo,"ReliancePostPaid").toString();
				strDir = strBaseDir;
				File file = new File(strDir + "/");
				FileUtils.deleteDirectory(file);
				file.mkdir();
			}else if (Module.trim().equalsIgnoreCase("BSNLLandline")) {
				strBaseDir=getBaseDir(BaseDir,strFForderNo,strPullSeqNo,"BSNLLandline").toString();
				strDir = strBaseDir;
				File file = new File(strDir + "/");
				FileUtils.deleteDirectory(file);
				file.mkdir();
			}else if (Module.trim().equalsIgnoreCase("IdeaPostpaid")) {
				strBaseDir=getBaseDir(BaseDir,strFForderNo,strPullSeqNo,"IdeaPostpaid").toString();
				strDir = strBaseDir;
				File file = new File(strDir + "/");
				FileUtils.deleteDirectory(file);
				file.mkdir();
			}else if (Module.trim().equalsIgnoreCase("Best")) {
				strBaseDir=getBaseDir(BaseDir,strFForderNo,strPullSeqNo,"Best").toString();
				strDir = strBaseDir;
				File file = new File(strDir + "/");
				FileUtils.deleteDirectory(file);
				file.mkdir();
			}
		}catch(Exception e){
			MDC.put("EventData","Failure");
			log.error("Failed to clear the directory. Please close all the files and re-run the utility.");
			showPopup("Failed to clear the directory. Please close all the files and re-run the utility.");
			String strFrom= null;
			String strCc = null;
			e.printStackTrace();
			try {
				jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("MailConfig");
				 strFrom=jObj.get("fromEmail").toString();
			     strCc=jObj.get("ccEmails").toString();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			SendCleanUpUnsuccessfulMail(UserNAme, strFrom, strCc, strFForderNo,strPullSeqNo ,Module,log);
		}

		FirefoxProfile newProfile = new FirefoxProfile();
		newProfile.setPreference("browser.cache.disk.enable", false);
		newProfile.setPreference("browser.download.folderList", 2);
		newProfile.setPreference("browser.download.panel.shown", false);
		newProfile.setPreference("browser.download.panel.firstSessionCompleted", false);
		newProfile.setPreference("pdfjs.disabled", true);
		newProfile.setPreference("browser.download.dir", strBaseDir);

		newProfile.setPreference("browser.helperApps.alwaysAsk.force", false);
		newProfile.setPreference("browser.download.manager.showWhenStarting",
				false);
		//newProfile.setAssumeUntrustedCertificateIssuer(false);
		//newProfile.setAcceptUntrustedCertificates(true);
		newProfile
				.setPreference(
						"browser.helperApps.neverAsk.saveToDisk",
						"application/vnd.hzn-3d-crossword;video/3gpp;video/3gpp2;application/vnd.mseq;application/vnd.3m.post-it-notes;application/vnd.3gpp.pic-bw-large;application/vnd.3gpp.pic-bw-small;application/vnd.3gpp.pic-bw-var;application/vnd.3gp2.tcap;application/x-7z-compressed;application/x-abiword;application/x-ace-compressed;application/vnd.americandynamics.acc;application/vnd.acucobol;application/vnd.acucorp;audio/adpcm;application/x-authorware-bin;application/x-athorware-map;application/x-authorware-seg;application/vnd.adobe.air-application-installer-package+zip;application/x-shockwave-flash;application/vnd.adobe.fxp;application/pdf;application/vnd.cups-ppd;application/x-director;applicaion/vnd.adobe.xdp+xml;application/vnd.adobe.xfdf;audio/x-aac;application/vnd.ahead.space;application/vnd.airzip.filesecure.azf;application/vnd.airzip.filesecure.azs;application/vnd.amazon.ebook;application/vnd.amiga.ami;applicatin/andrew-inset;application/vnd.android.package-archive;application/vnd.anser-web-certificate-issue-initiation;application/vnd.anser-web-funds-transfer-initiation;application/vnd.antix.game-component;application/vnd.apple.installe+xml;application/applixware;application/vnd.hhe.lesson-player;application/vnd.aristanetworks.swi;text/x-asm;application/atomcat+xml;application/atomsvc+xml;application/atom+xml;application/pkix-attr-cert;audio/x-aiff;video/x-msvieo;application/vnd.audiograph;image/vnd.dxf;model/vnd.dwf;text/plain-bas;application/x-bcpio;application/octet-stream;image/bmp;application/x-bittorrent;application/vnd.rim.cod;application/vnd.blueice.multipass;application/vnd.bm;application/x-sh;image/prs.btif;application/vnd.businessobjects;application/x-bzip;application/x-bzip2;application/x-csh;text/x-c;application/vnd.chemdraw+xml;text/css;chemical/x-cdx;chemical/x-cml;chemical/x-csml;application/vn.contact.cmsg;application/vnd.claymore;application/vnd.clonk.c4group;image/vnd.dvb.subtitle;application/cdmi-capability;application/cdmi-container;application/cdmi-domain;application/cdmi-object;application/cdmi-queue;applicationvnd.cluetrust.cartomobile-config;application/vnd.cluetrust.cartomobile-config-pkg;image/x-cmu-raster;model/vnd.collada+xml;text/csv;application/mac-compactpro;application/vnd.wap.wmlc;image/cgm;x-conference/x-cooltalk;image/x-cmx;application/vnd.xara;application/vnd.cosmocaller;application/x-cpio;application/vnd.crick.clicker;application/vnd.crick.clicker.keyboard;application/vnd.crick.clicker.palette;application/vnd.crick.clicker.template;application/vn.crick.clicker.wordbank;application/vnd.criticaltools.wbs+xml;application/vnd.rig.cryptonote;chemical/x-cif;chemical/x-cmdf;application/cu-seeme;application/prs.cww;text/vnd.curl;text/vnd.curl.dcurl;text/vnd.curl.mcurl;text/vnd.crl.scurl;application/vnd.curl.car;application/vnd.curl.pcurl;application/vnd.yellowriver-custom-menu;application/dssc+der;application/dssc+xml;application/x-debian-package;audio/vnd.dece.audio;image/vnd.dece.graphic;video/vnd.dec.hd;video/vnd.dece.mobile;video/vnd.uvvu.mp4;video/vnd.dece.pd;video/vnd.dece.sd;video/vnd.dece.video;application/x-dvi;application/vnd.fdsn.seed;application/x-dtbook+xml;application/x-dtbresource+xml;application/vnd.dvb.ait;applcation/vnd.dvb.service;audio/vnd.digital-winds;image/vnd.djvu;application/xml-dtd;application/vnd.dolby.mlp;application/x-doom;application/vnd.dpgraph;audio/vnd.dra;application/vnd.dreamfactory;audio/vnd.dts;audio/vnd.dts.hd;imag/vnd.dwg;application/vnd.dynageo;application/ecmascript;application/vnd.ecowin.chart;image/vnd.fujixerox.edmics-mmr;image/vnd.fujixerox.edmics-rlc;application/exi;application/vnd.proteus.magazine;application/epub+zip;message/rfc82;application/vnd.enliven;application/vnd.is-xpr;image/vnd.xiff;application/vnd.xfdl;application/emma+xml;application/vnd.ezpix-album;application/vnd.ezpix-package;image/vnd.fst;video/vnd.fvt;image/vnd.fastbidsheet;application/vn.denovo.fcselayout-link;video/x-f4v;video/x-flv;image/vnd.fpx;image/vnd.net-fpx;text/vnd.fmi.flexstor;video/x-fli;application/vnd.fluxtime.clip;application/vnd.fdf;text/x-fortran;application/vnd.mif;application/vnd.framemaker;imae/x-freehand;application/vnd.fsc.weblaunch;application/vnd.frogans.fnc;application/vnd.frogans.ltf;application/vnd.fujixerox.ddd;application/vnd.fujixerox.docuworks;application/vnd.fujixerox.docuworks.binder;application/vnd.fujitu.oasys;application/vnd.fujitsu.oasys2;application/vnd.fujitsu.oasys3;application/vnd.fujitsu.oasysgp;application/vnd.fujitsu.oasysprs;application/x-futuresplash;application/vnd.fuzzysheet;image/g3fax;application/vnd.gmx;model/vn.gtw;application/vnd.genomatix.tuxedo;application/vnd.geogebra.file;application/vnd.geogebra.tool;model/vnd.gdl;application/vnd.geometry-explorer;application/vnd.geonext;application/vnd.geoplan;application/vnd.geospace;applicatio/x-font-ghostscript;application/x-font-bdf;application/x-gtar;application/x-texinfo;application/x-gnumeric;application/vnd.google-earth.kml+xml;application/vnd.google-earth.kmz;application/vnd.grafeq;image/gif;text/vnd.graphviz;aplication/vnd.groove-account;application/vnd.groove-help;application/vnd.groove-identity-message;application/vnd.groove-injector;application/vnd.groove-tool-message;application/vnd.groove-tool-template;application/vnd.groove-vcar;video/h261;video/h263;video/h264;application/vnd.hp-hpid;application/vnd.hp-hps;application/x-hdf;audio/vnd.rip;application/vnd.hbci;application/vnd.hp-jlyt;application/vnd.hp-pcl;application/vnd.hp-hpgl;application/vnd.yamaha.h-script;application/vnd.yamaha.hv-dic;application/vnd.yamaha.hv-voice;application/vnd.hydrostatix.sof-data;application/hyperstudio;application/vnd.hal+xml;text/html;application/vnd.ibm.rights-management;application/vnd.ibm.securecontainer;text/calendar;application/vnd.iccprofile;image/x-icon;application/vnd.igloader;image/ief;application/vnd.immervision-ivp;application/vnd.immervision-ivu;application/reginfo+xml;text/vnd.in3d.3dml;text/vnd.in3d.spot;mode/iges;application/vnd.intergeo;application/vnd.cinderella;application/vnd.intercon.formnet;application/vnd.isac.fcs;application/ipfix;application/pkix-cert;application/pkixcmp;application/pkix-crl;application/pkix-pkipath;applicaion/vnd.insors.igm;application/vnd.ipunplugged.rcprofile;application/vnd.irepository.package+xml;text/vnd.sun.j2me.app-descriptor;application/java-archive;application/java-vm;application/x-java-jnlp-file;application/java-serializd-object;text/x-java-source,java;application/javascript;application/json;application/vnd.joost.joda-archive;video/jpm;image/jpeg;video/jpeg;application/vnd.kahootz;application/vnd.chipnuts.karaoke-mmd;application/vnd.kde.karbon;aplication/vnd.kde.kchart;application/vnd.kde.kformula;application/vnd.kde.kivio;application/vnd.kde.kontour;application/vnd.kde.kpresenter;application/vnd.kde.kspread;application/vnd.kde.kword;application/vnd.kenameaapp;applicatin/vnd.kidspiration;application/vnd.kinar;application/vnd.kodak-descriptor;application/vnd.las.las+xml;application/x-latex;application/vnd.llamagraphics.life-balance.desktop;application/vnd.llamagraphics.life-balance.exchange+xml;application/vnd.jam;application/vnd.lotus-1-2-3;application/vnd.lotus-approach;application/vnd.lotus-freelance;application/vnd.lotus-notes;application/vnd.lotus-organizer;application/vnd.lotus-screencam;application/vnd.lotus-wordro;audio/vnd.lucent.voice;audio/x-mpegurl;video/x-m4v;application/mac-binhex40;application/vnd.macports.portpkg;application/vnd.osgeo.mapguide.package;application/marc;application/marcxml+xml;application/mxf;application/vnd.wolfrm.player;application/mathematica;application/mathml+xml;application/mbox;application/vnd.medcalcdata;application/mediaservercontrol+xml;application/vnd.mediastation.cdkey;application/vnd.mfer;application/vnd.mfmp;model/mesh;appliation/mads+xml;application/mets+xml;application/mods+xml;application/metalink4+xml;application/vnd.ms-powerpoint.template.macroenabled.12;application/vnd.ms-word.document.macroenabled.12;application/vnd.ms-word.template.macroenabed.12;application/vnd.mcd;application/vnd.micrografx.flo;application/vnd.micrografx.igx;application/vnd.eszigno3+xml;application/x-msaccess;video/x-ms-asf;application/x-msdownload;application/vnd.ms-artgalry;application/vnd.ms-ca-compressed;application/vnd.ms-ims;application/x-ms-application;application/x-msclip;image/vnd.ms-modi;application/vnd.ms-fontobject;application/vnd.ms-excel;application/vnd.ms-excel.addin.macroenabled.12;application/vnd.ms-excelsheet.binary.macroenabled.12;application/vnd.ms-excel.template.macroenabled.12;application/vnd.ms-excel.sheet.macroenabled.12;application/vnd.ms-htmlhelp;application/x-mscardfile;application/vnd.ms-lrm;application/x-msmediaview;aplication/x-msmoney;application/vnd.openxmlformats-officedocument.presentationml.presentation;application/vnd.openxmlformats-officedocument.presentationml.slide;application/vnd.openxmlformats-officedocument.presentationml.slideshw;application/vnd.openxmlformats-officedocument.presentationml.template;application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;application/vnd.openxmlformats-officedocument.spreadsheetml.template;application/vnd.openxmformats-officedocument.wordprocessingml.document;application/vnd.openxmlformats-officedocument.wordprocessingml.template;application/x-msbinder;application/vnd.ms-officetheme;application/onenote;audio/vnd.ms-playready.media.pya;vdeo/vnd.ms-playready.media.pyv;application/vnd.ms-powerpoint;application/vnd.ms-powerpoint.addin.macroenabled.12;application/vnd.ms-powerpoint.slide.macroenabled.12;application/vnd.ms-powerpoint.presentation.macroenabled.12;appliation/vnd.ms-powerpoint.slideshow.macroenabled.12;application/vnd.ms-project;application/x-mspublisher;application/x-msschedule;application/x-silverlight-app;application/vnd.ms-pki.stl;application/vnd.ms-pki.seccat;application/vn.visio;video/x-ms-wm;audio/x-ms-wma;audio/x-ms-wax;video/x-ms-wmx;application/x-ms-wmd;application/vnd.ms-wpl;application/x-ms-wmz;video/x-ms-wmv;video/x-ms-wvx;application/x-msmetafile;application/x-msterminal;application/msword;application/x-mswrite;application/vnd.ms-works;application/x-ms-xbap;application/vnd.ms-xpsdocument;audio/midi;application/vnd.ibm.minipay;application/vnd.ibm.modcap;application/vnd.jcp.javame.midlet-rms;application/vnd.tmobile-ivetv;application/x-mobipocket-ebook;application/vnd.mobius.mbk;application/vnd.mobius.dis;application/vnd.mobius.plc;application/vnd.mobius.mqy;application/vnd.mobius.msl;application/vnd.mobius.txf;application/vnd.mobius.daf;tex/vnd.fly;application/vnd.mophun.certificate;application/vnd.mophun.application;video/mj2;audio/mpeg;video/vnd.mpegurl;video/mpeg;application/mp21;audio/mp4;video/mp4;application/mp4;application/vnd.apple.mpegurl;application/vnd.msician;application/vnd.muvee.style;application/xv+xml;application/vnd.nokia.n-gage.data;application/vnd.nokia.n-gage.symbian.install;application/x-dtbncx+xml;application/x-netcdf;application/vnd.neurolanguage.nlu;application/vnd.na;application/vnd.noblenet-directory;application/vnd.noblenet-sealer;application/vnd.noblenet-web;application/vnd.nokia.radio-preset;application/vnd.nokia.radio-presets;text/n3;application/vnd.novadigm.edm;application/vnd.novadim.edx;application/vnd.novadigm.ext;application/vnd.flographit;audio/vnd.nuera.ecelp4800;audio/vnd.nuera.ecelp7470;audio/vnd.nuera.ecelp9600;application/oda;application/ogg;audio/ogg;video/ogg;application/vnd.oma.dd2+xml;applicatin/vnd.oasis.opendocument.text-web;application/oebps-package+xml;application/vnd.intu.qbo;application/vnd.openofficeorg.extension;application/vnd.yamaha.openscoreformat;audio/webm;video/webm;application/vnd.oasis.opendocument.char;application/vnd.oasis.opendocument.chart-template;application/vnd.oasis.opendocument.database;application/vnd.oasis.opendocument.formula;application/vnd.oasis.opendocument.formula-template;application/vnd.oasis.opendocument.grapics;application/vnd.oasis.opendocument.graphics-template;application/vnd.oasis.opendocument.image;application/vnd.oasis.opendocument.image-template;application/vnd.oasis.opendocument.presentation;application/vnd.oasis.opendocumen.presentation-template;application/vnd.oasis.opendocument.spreadsheet;application/vnd.oasis.opendocument.spreadsheet-template;application/vnd.oasis.opendocument.text;application/vnd.oasis.opendocument.text-master;application/vnd.asis.opendocument.text-template;image/ktx;application/vnd.sun.xml.calc;application/vnd.sun.xml.calc.template;application/vnd.sun.xml.draw;application/vnd.sun.xml.draw.template;application/vnd.sun.xml.impress;application/vnd.sun.xl.impress.template;application/vnd.sun.xml.math;application/vnd.sun.xml.writer;application/vnd.sun.xml.writer.global;application/vnd.sun.xml.writer.template;application/x-font-otf;application/vnd.yamaha.openscoreformat.osfpvg+xml;application/vnd.osgi.dp;application/vnd.palm;text/x-pascal;application/vnd.pawaafile;application/vnd.hp-pclxl;application/vnd.picsel;image/x-pcx;image/vnd.adobe.photoshop;application/pics-rules;image/x-pict;application/x-chat;aplication/pkcs10;application/x-pkcs12;application/pkcs7-mime;application/pkcs7-signature;application/x-pkcs7-certreqresp;application/x-pkcs7-certificates;application/pkcs8;application/vnd.pocketlearn;image/x-portable-anymap;image/-portable-bitmap;application/x-font-pcf;application/font-tdpfr;application/x-chess-pgn;image/x-portable-graymap;image/png;image/x-portable-pixmap;application/pskc+xml;application/vnd.ctc-posml;application/postscript;application/xfont-type1;application/vnd.powerbuilder6;application/pgp-encrypted;application/pgp-signature;application/vnd.previewsystems.box;application/vnd.pvi.ptid1;application/pls+xml;application/vnd.pg.format;application/vnd.pg.osasli;tex/prs.lines.tag;application/x-font-linux-psf;application/vnd.publishare-delta-tree;application/vnd.pmi.widget;application/vnd.quark.quarkxpress;application/vnd.epson.esf;application/vnd.epson.msf;application/vnd.epson.ssf;applicaton/vnd.epson.quickanime;application/vnd.intu.qfx;video/quicktime;application/x-rar-compressed;audio/x-pn-realaudio;audio/x-pn-realaudio-plugin;application/rsd+xml;application/vnd.rn-realmedia;application/vnd.realvnc.bed;applicatin/vnd.recordare.musicxml;application/vnd.recordare.musicxml+xml;application/relax-ng-compact-syntax;application/vnd.data-vision.rdz;application/rdf+xml;application/vnd.cloanto.rp9;application/vnd.jisp;application/rtf;text/richtex;application/vnd.route66.link66+xml;application/rss+xml;application/shf+xml;application/vnd.sailingtracker.track;image/svg+xml;application/vnd.sus-calendar;application/sru+xml;application/set-payment-initiation;application/set-reistration-initiation;application/vnd.sema;application/vnd.semd;application/vnd.semf;application/vnd.seemail;application/x-font-snf;application/scvp-vp-request;application/scvp-vp-response;application/scvp-cv-request;application/svp-cv-response;application/sdp;text/x-setext;video/x-sgi-movie;application/vnd.shana.informed.formdata;application/vnd.shana.informed.formtemplate;application/vnd.shana.informed.interchange;application/vnd.shana.informed.package;application/thraud+xml;application/x-shar;image/x-rgb;application/vnd.epson.salt;application/vnd.accpac.simply.aso;application/vnd.accpac.simply.imp;application/vnd.simtech-mindmapper;application/vnd.commonspace;application/vnd.ymaha.smaf-audio;application/vnd.smaf;application/vnd.yamaha.smaf-phrase;application/vnd.smart.teacher;application/vnd.svd;application/sparql-query;application/sparql-results+xml;application/srgs;application/srgs+xml;application/sml+xml;application/vnd.koan;text/sgml;application/vnd.stardivision.calc;application/vnd.stardivision.draw;application/vnd.stardivision.impress;application/vnd.stardivision.math;application/vnd.stardivision.writer;application/vnd.tardivision.writer-global;application/vnd.stepmania.stepchart;application/x-stuffit;application/x-stuffitx;application/vnd.solent.sdkm+xml;application/vnd.olpc-sugar;audio/basic;application/vnd.wqd;application/vnd.symbian.install;application/smil+xml;application/vnd.syncml+xml;application/vnd.syncml.dm+wbxml;application/vnd.syncml.dm+xml;application/x-sv4cpio;application/x-sv4crc;application/sbml+xml;text/tab-separated-values;image/tiff;application/vnd.to.intent-module-archive;application/x-tar;application/x-tcl;application/x-tex;application/x-tex-tfm;application/tei+xml;text/plain;application/vnd.spotfire.dxp;application/vnd.spotfire.sfs;application/timestamped-data;applicationvnd.trid.tpt;application/vnd.triscape.mxs;text/troff;application/vnd.trueapp;application/x-font-ttf;text/turtle;application/vnd.umajin;application/vnd.uoml+xml;application/vnd.unity;application/vnd.ufdl;text/uri-list;application/nd.uiq.theme;application/x-ustar;text/x-uuencode;text/x-vcalendar;text/x-vcard;application/x-cdlink;application/vnd.vsf;model/vrml;application/vnd.vcx;model/vnd.mts;model/vnd.vtu;application/vnd.visionary;video/vnd.vivo;applicatin/ccxml+xml,;application/voicexml+xml;application/x-wais-source;application/vnd.wap.wbxml;image/vnd.wap.wbmp;audio/x-wav;application/davmount+xml;application/x-font-woff;application/wspolicy+xml;image/webp;application/vnd.webturb;application/widget;application/winhlp;text/vnd.wap.wml;text/vnd.wap.wmlscript;application/vnd.wap.wmlscriptc;application/vnd.wordperfect;application/vnd.wt.stf;application/wsdl+xml;image/x-xbitmap;image/x-xpixmap;image/x-xwindowump;application/x-x509-ca-cert;application/x-xfig;application/xhtml+xml;application/xml;application/xcap-diff+xml;application/xenc+xml;application/patch-ops-error+xml;application/resource-lists+xml;application/rls-services+xml;aplication/resource-lists-diff+xml;application/xslt+xml;application/xop+xml;application/x-xpinstall;application/xspf+xml;application/vnd.mozilla.xul+xml;chemical/x-xyz;text/yaml;application/yang;application/yin+xml;application/vnd.ul;application/zip;application/vnd.handheld-entertainment+xml;application/vnd.zzazz.deck+xml");
		newProfile.setPreference("plugin.scan.Acrobat", "99.0");
		newProfile.setPreference("plugin.scan.plid.all", false);
		newProfile.setPreference("browser.helperApps.alwaysAsk.force", false);

		newProfile
				.setPreference(
						"browser.helperApps.neverAsk.saveToDisk",
						"application/vnd.hzn-3d-crossword;video/3gpp;video/3gpp2;application/vnd.mseq;application/vnd.3m.post-it-notes;application/vnd.3gpp.pic-bw-large;application/vnd.3gpp.pic-bw-small;application/vnd.3gpp.pic-bw-var;application/vnd.3gp2.tcap;application/x-7z-compressed;application/x-abiword;application/x-ace-compressed;application/vnd.americandynamics.acc;application/vnd.acucobol;application/vnd.acucorp;audio/adpcm;application/x-authorware-bin;application/x-athorware-map;application/x-authorware-seg;application/vnd.adobe.air-application-installer-package+zip;application/x-shockwave-flash;application/vnd.adobe.fxp;application/pdf;application/vnd.cups-ppd;application/x-director;applicaion/vnd.adobe.xdp+xml;application/vnd.adobe.xfdf;audio/x-aac;application/vnd.ahead.space;application/vnd.airzip.filesecure.azf;application/vnd.airzip.filesecure.azs;application/vnd.amazon.ebook;application/vnd.amiga.ami;applicatin/andrew-inset;application/vnd.android.package-archive;application/vnd.anser-web-certificate-issue-initiation;application/vnd.anser-web-funds-transfer-initiation;application/vnd.antix.game-component;application/vnd.apple.installe+xml;application/applixware;application/vnd.hhe.lesson-player;application/vnd.aristanetworks.swi;text/x-asm;application/atomcat+xml;application/atomsvc+xml;application/atom+xml;application/pkix-attr-cert;audio/x-aiff;video/x-msvieo;application/vnd.audiograph;image/vnd.dxf;model/vnd.dwf;text/plain-bas;application/x-bcpio;application/octet-stream;image/bmp;application/x-bittorrent;application/vnd.rim.cod;application/vnd.blueice.multipass;application/vnd.bm;application/x-sh;image/prs.btif;application/vnd.businessobjects;application/x-bzip;application/x-bzip2;application/x-csh;text/x-c;application/vnd.chemdraw+xml;text/css;chemical/x-cdx;chemical/x-cml;chemical/x-csml;application/vn.contact.cmsg;application/vnd.claymore;application/vnd.clonk.c4group;image/vnd.dvb.subtitle;application/cdmi-capability;application/cdmi-container;application/cdmi-domain;application/cdmi-object;application/cdmi-queue;applicationvnd.cluetrust.cartomobile-config;application/vnd.cluetrust.cartomobile-config-pkg;image/x-cmu-raster;model/vnd.collada+xml;text/csv;application/mac-compactpro;application/vnd.wap.wmlc;image/cgm;x-conference/x-cooltalk;image/x-cmx;application/vnd.xara;application/vnd.cosmocaller;application/x-cpio;application/vnd.crick.clicker;application/vnd.crick.clicker.keyboard;application/vnd.crick.clicker.palette;application/vnd.crick.clicker.template;application/vn.crick.clicker.wordbank;application/vnd.criticaltools.wbs+xml;application/vnd.rig.cryptonote;chemical/x-cif;chemical/x-cmdf;application/cu-seeme;application/prs.cww;text/vnd.curl;text/vnd.curl.dcurl;text/vnd.curl.mcurl;text/vnd.crl.scurl;application/vnd.curl.car;application/vnd.curl.pcurl;application/vnd.yellowriver-custom-menu;application/dssc+der;application/dssc+xml;application/x-debian-package;audio/vnd.dece.audio;image/vnd.dece.graphic;video/vnd.dec.hd;video/vnd.dece.mobile;video/vnd.uvvu.mp4;video/vnd.dece.pd;video/vnd.dece.sd;video/vnd.dece.video;application/x-dvi;application/vnd.fdsn.seed;application/x-dtbook+xml;application/x-dtbresource+xml;application/vnd.dvb.ait;applcation/vnd.dvb.service;audio/vnd.digital-winds;image/vnd.djvu;application/xml-dtd;application/vnd.dolby.mlp;application/x-doom;application/vnd.dpgraph;audio/vnd.dra;application/vnd.dreamfactory;audio/vnd.dts;audio/vnd.dts.hd;imag/vnd.dwg;application/vnd.dynageo;application/ecmascript;application/vnd.ecowin.chart;image/vnd.fujixerox.edmics-mmr;image/vnd.fujixerox.edmics-rlc;application/exi;application/vnd.proteus.magazine;application/epub+zip;message/rfc82;application/vnd.enliven;application/vnd.is-xpr;image/vnd.xiff;application/vnd.xfdl;application/emma+xml;application/vnd.ezpix-album;application/vnd.ezpix-package;image/vnd.fst;video/vnd.fvt;image/vnd.fastbidsheet;application/vn.denovo.fcselayout-link;video/x-f4v;video/x-flv;image/vnd.fpx;image/vnd.net-fpx;text/vnd.fmi.flexstor;video/x-fli;application/vnd.fluxtime.clip;application/vnd.fdf;text/x-fortran;application/vnd.mif;application/vnd.framemaker;imae/x-freehand;application/vnd.fsc.weblaunch;application/vnd.frogans.fnc;application/vnd.frogans.ltf;application/vnd.fujixerox.ddd;application/vnd.fujixerox.docuworks;application/vnd.fujixerox.docuworks.binder;application/vnd.fujitu.oasys;application/vnd.fujitsu.oasys2;application/vnd.fujitsu.oasys3;application/vnd.fujitsu.oasysgp;application/vnd.fujitsu.oasysprs;application/x-futuresplash;application/vnd.fuzzysheet;image/g3fax;application/vnd.gmx;model/vn.gtw;application/vnd.genomatix.tuxedo;application/vnd.geogebra.file;application/vnd.geogebra.tool;model/vnd.gdl;application/vnd.geometry-explorer;application/vnd.geonext;application/vnd.geoplan;application/vnd.geospace;applicatio/x-font-ghostscript;application/x-font-bdf;application/x-gtar;application/x-texinfo;application/x-gnumeric;application/vnd.google-earth.kml+xml;application/vnd.google-earth.kmz;application/vnd.grafeq;image/gif;text/vnd.graphviz;aplication/vnd.groove-account;application/vnd.groove-help;application/vnd.groove-identity-message;application/vnd.groove-injector;application/vnd.groove-tool-message;application/vnd.groove-tool-template;application/vnd.groove-vcar;video/h261;video/h263;video/h264;application/vnd.hp-hpid;application/vnd.hp-hps;application/x-hdf;audio/vnd.rip;application/vnd.hbci;application/vnd.hp-jlyt;application/vnd.hp-pcl;application/vnd.hp-hpgl;application/vnd.yamaha.h-script;application/vnd.yamaha.hv-dic;application/vnd.yamaha.hv-voice;application/vnd.hydrostatix.sof-data;application/hyperstudio;application/vnd.hal+xml;text/html;application/vnd.ibm.rights-management;application/vnd.ibm.securecontainer;text/calendar;application/vnd.iccprofile;image/x-icon;application/vnd.igloader;image/ief;application/vnd.immervision-ivp;application/vnd.immervision-ivu;application/reginfo+xml;text/vnd.in3d.3dml;text/vnd.in3d.spot;mode/iges;application/vnd.intergeo;application/vnd.cinderella;application/vnd.intercon.formnet;application/vnd.isac.fcs;application/ipfix;application/pkix-cert;application/pkixcmp;application/pkix-crl;application/pkix-pkipath;applicaion/vnd.insors.igm;application/vnd.ipunplugged.rcprofile;application/vnd.irepository.package+xml;text/vnd.sun.j2me.app-descriptor;application/java-archive;application/java-vm;application/x-java-jnlp-file;application/java-serializd-object;text/x-java-source,java;application/javascript;application/json;application/vnd.joost.joda-archive;video/jpm;image/jpeg;video/jpeg;application/vnd.kahootz;application/vnd.chipnuts.karaoke-mmd;application/vnd.kde.karbon;aplication/vnd.kde.kchart;application/vnd.kde.kformula;application/vnd.kde.kivio;application/vnd.kde.kontour;application/vnd.kde.kpresenter;application/vnd.kde.kspread;application/vnd.kde.kword;application/vnd.kenameaapp;applicatin/vnd.kidspiration;application/vnd.kinar;application/vnd.kodak-descriptor;application/vnd.las.las+xml;application/x-latex;application/vnd.llamagraphics.life-balance.desktop;application/vnd.llamagraphics.life-balance.exchange+xml;application/vnd.jam;application/vnd.lotus-1-2-3;application/vnd.lotus-approach;application/vnd.lotus-freelance;application/vnd.lotus-notes;application/vnd.lotus-organizer;application/vnd.lotus-screencam;application/vnd.lotus-wordro;audio/vnd.lucent.voice;audio/x-mpegurl;video/x-m4v;application/mac-binhex40;application/vnd.macports.portpkg;application/vnd.osgeo.mapguide.package;application/marc;application/marcxml+xml;application/mxf;application/vnd.wolfrm.player;application/mathematica;application/mathml+xml;application/mbox;application/vnd.medcalcdata;application/mediaservercontrol+xml;application/vnd.mediastation.cdkey;application/vnd.mfer;application/vnd.mfmp;model/mesh;appliation/mads+xml;application/mets+xml;application/mods+xml;application/metalink4+xml;application/vnd.ms-powerpoint.template.macroenabled.12;application/vnd.ms-word.document.macroenabled.12;application/vnd.ms-word.template.macroenabed.12;application/vnd.mcd;application/vnd.micrografx.flo;application/vnd.micrografx.igx;application/vnd.eszigno3+xml;application/x-msaccess;video/x-ms-asf;application/x-msdownload;application/vnd.ms-artgalry;application/vnd.ms-ca-compressed;application/vnd.ms-ims;application/x-ms-application;application/x-msclip;image/vnd.ms-modi;application/vnd.ms-fontobject;application/vnd.ms-excel;application/vnd.ms-excel.addin.macroenabled.12;application/vnd.ms-excelsheet.binary.macroenabled.12;application/vnd.ms-excel.template.macroenabled.12;application/vnd.ms-excel.sheet.macroenabled.12;application/vnd.ms-htmlhelp;application/x-mscardfile;application/vnd.ms-lrm;application/x-msmediaview;aplication/x-msmoney;application/vnd.openxmlformats-officedocument.presentationml.presentation;application/vnd.openxmlformats-officedocument.presentationml.slide;application/vnd.openxmlformats-officedocument.presentationml.slideshw;application/vnd.openxmlformats-officedocument.presentationml.template;application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;application/vnd.openxmlformats-officedocument.spreadsheetml.template;application/vnd.openxmformats-officedocument.wordprocessingml.document;application/vnd.openxmlformats-officedocument.wordprocessingml.template;application/x-msbinder;application/vnd.ms-officetheme;application/onenote;audio/vnd.ms-playready.media.pya;vdeo/vnd.ms-playready.media.pyv;application/vnd.ms-powerpoint;application/vnd.ms-powerpoint.addin.macroenabled.12;application/vnd.ms-powerpoint.slide.macroenabled.12;application/vnd.ms-powerpoint.presentation.macroenabled.12;appliation/vnd.ms-powerpoint.slideshow.macroenabled.12;application/vnd.ms-project;application/x-mspublisher;application/x-msschedule;application/x-silverlight-app;application/vnd.ms-pki.stl;application/vnd.ms-pki.seccat;application/vn.visio;video/x-ms-wm;audio/x-ms-wma;audio/x-ms-wax;video/x-ms-wmx;application/x-ms-wmd;application/vnd.ms-wpl;application/x-ms-wmz;video/x-ms-wmv;video/x-ms-wvx;application/x-msmetafile;application/x-msterminal;application/msword;application/x-mswrite;application/vnd.ms-works;application/x-ms-xbap;application/vnd.ms-xpsdocument;audio/midi;application/vnd.ibm.minipay;application/vnd.ibm.modcap;application/vnd.jcp.javame.midlet-rms;application/vnd.tmobile-ivetv;application/x-mobipocket-ebook;application/vnd.mobius.mbk;application/vnd.mobius.dis;application/vnd.mobius.plc;application/vnd.mobius.mqy;application/vnd.mobius.msl;application/vnd.mobius.txf;application/vnd.mobius.daf;tex/vnd.fly;application/vnd.mophun.certificate;application/vnd.mophun.application;video/mj2;audio/mpeg;video/vnd.mpegurl;video/mpeg;application/mp21;audio/mp4;video/mp4;application/mp4;application/vnd.apple.mpegurl;application/vnd.msician;application/vnd.muvee.style;application/xv+xml;application/vnd.nokia.n-gage.data;application/vnd.nokia.n-gage.symbian.install;application/x-dtbncx+xml;application/x-netcdf;application/vnd.neurolanguage.nlu;application/vnd.na;application/vnd.noblenet-directory;application/vnd.noblenet-sealer;application/vnd.noblenet-web;application/vnd.nokia.radio-preset;application/vnd.nokia.radio-presets;text/n3;application/vnd.novadigm.edm;application/vnd.novadim.edx;application/vnd.novadigm.ext;application/vnd.flographit;audio/vnd.nuera.ecelp4800;audio/vnd.nuera.ecelp7470;audio/vnd.nuera.ecelp9600;application/oda;application/ogg;audio/ogg;video/ogg;application/vnd.oma.dd2+xml;applicatin/vnd.oasis.opendocument.text-web;application/oebps-package+xml;application/vnd.intu.qbo;application/vnd.openofficeorg.extension;application/vnd.yamaha.openscoreformat;audio/webm;video/webm;application/vnd.oasis.opendocument.char;application/vnd.oasis.opendocument.chart-template;application/vnd.oasis.opendocument.database;application/vnd.oasis.opendocument.formula;application/vnd.oasis.opendocument.formula-template;application/vnd.oasis.opendocument.grapics;application/vnd.oasis.opendocument.graphics-template;application/vnd.oasis.opendocument.image;application/vnd.oasis.opendocument.image-template;application/vnd.oasis.opendocument.presentation;application/vnd.oasis.opendocumen.presentation-template;application/vnd.oasis.opendocument.spreadsheet;application/vnd.oasis.opendocument.spreadsheet-template;application/vnd.oasis.opendocument.text;application/vnd.oasis.opendocument.text-master;application/vnd.asis.opendocument.text-template;image/ktx;application/vnd.sun.xml.calc;application/vnd.sun.xml.calc.template;application/vnd.sun.xml.draw;application/vnd.sun.xml.draw.template;application/vnd.sun.xml.impress;application/vnd.sun.xl.impress.template;application/vnd.sun.xml.math;application/vnd.sun.xml.writer;application/vnd.sun.xml.writer.global;application/vnd.sun.xml.writer.template;application/x-font-otf;application/vnd.yamaha.openscoreformat.osfpvg+xml;application/vnd.osgi.dp;application/vnd.palm;text/x-pascal;application/vnd.pawaafile;application/vnd.hp-pclxl;application/vnd.picsel;image/x-pcx;image/vnd.adobe.photoshop;application/pics-rules;image/x-pict;application/x-chat;aplication/pkcs10;application/x-pkcs12;application/pkcs7-mime;application/pkcs7-signature;application/x-pkcs7-certreqresp;application/x-pkcs7-certificates;application/pkcs8;application/vnd.pocketlearn;image/x-portable-anymap;image/-portable-bitmap;application/x-font-pcf;application/font-tdpfr;application/x-chess-pgn;image/x-portable-graymap;image/png;image/x-portable-pixmap;application/pskc+xml;application/vnd.ctc-posml;application/postscript;application/xfont-type1;application/vnd.powerbuilder6;application/pgp-encrypted;application/pgp-signature;application/vnd.previewsystems.box;application/vnd.pvi.ptid1;application/pls+xml;application/vnd.pg.format;application/vnd.pg.osasli;tex/prs.lines.tag;application/x-font-linux-psf;application/vnd.publishare-delta-tree;application/vnd.pmi.widget;application/vnd.quark.quarkxpress;application/vnd.epson.esf;application/vnd.epson.msf;application/vnd.epson.ssf;applicaton/vnd.epson.quickanime;application/vnd.intu.qfx;video/quicktime;application/x-rar-compressed;audio/x-pn-realaudio;audio/x-pn-realaudio-plugin;application/rsd+xml;application/vnd.rn-realmedia;application/vnd.realvnc.bed;applicatin/vnd.recordare.musicxml;application/vnd.recordare.musicxml+xml;application/relax-ng-compact-syntax;application/vnd.data-vision.rdz;application/rdf+xml;application/vnd.cloanto.rp9;application/vnd.jisp;application/rtf;text/richtex;application/vnd.route66.link66+xml;application/rss+xml;application/shf+xml;application/vnd.sailingtracker.track;image/svg+xml;application/vnd.sus-calendar;application/sru+xml;application/set-payment-initiation;application/set-reistration-initiation;application/vnd.sema;application/vnd.semd;application/vnd.semf;application/vnd.seemail;application/x-font-snf;application/scvp-vp-request;application/scvp-vp-response;application/scvp-cv-request;application/svp-cv-response;application/sdp;text/x-setext;video/x-sgi-movie;application/vnd.shana.informed.formdata;application/vnd.shana.informed.formtemplate;application/vnd.shana.informed.interchange;application/vnd.shana.informed.package;application/thraud+xml;application/x-shar;image/x-rgb;application/vnd.epson.salt;application/vnd.accpac.simply.aso;application/vnd.accpac.simply.imp;application/vnd.simtech-mindmapper;application/vnd.commonspace;application/vnd.ymaha.smaf-audio;application/vnd.smaf;application/vnd.yamaha.smaf-phrase;application/vnd.smart.teacher;application/vnd.svd;application/sparql-query;application/sparql-results+xml;application/srgs;application/srgs+xml;application/sml+xml;application/vnd.koan;text/sgml;application/vnd.stardivision.calc;application/vnd.stardivision.draw;application/vnd.stardivision.impress;application/vnd.stardivision.math;application/vnd.stardivision.writer;application/vnd.tardivision.writer-global;application/vnd.stepmania.stepchart;application/x-stuffit;application/x-stuffitx;application/vnd.solent.sdkm+xml;application/vnd.olpc-sugar;audio/basic;application/vnd.wqd;application/vnd.symbian.install;application/smil+xml;application/vnd.syncml+xml;application/vnd.syncml.dm+wbxml;application/vnd.syncml.dm+xml;application/x-sv4cpio;application/x-sv4crc;application/sbml+xml;text/tab-separated-values;image/tiff;application/vnd.to.intent-module-archive;application/x-tar;application/x-tcl;application/x-tex;application/x-tex-tfm;application/tei+xml;text/plain;application/vnd.spotfire.dxp;application/vnd.spotfire.sfs;application/timestamped-data;applicationvnd.trid.tpt;application/vnd.triscape.mxs;text/troff;application/vnd.trueapp;application/x-font-ttf;text/turtle;application/vnd.umajin;application/vnd.uoml+xml;application/vnd.unity;application/vnd.ufdl;text/uri-list;application/nd.uiq.theme;application/x-ustar;text/x-uuencode;text/x-vcalendar;text/x-vcard;application/x-cdlink;application/vnd.vsf;model/vrml;application/vnd.vcx;model/vnd.mts;model/vnd.vtu;application/vnd.visionary;video/vnd.vivo;applicatin/ccxml+xml,;application/voicexml+xml;application/x-wais-source;application/vnd.wap.wbxml;image/vnd.wap.wbmp;audio/x-wav;application/davmount+xml;application/x-font-woff;application/wspolicy+xml;image/webp;application/vnd.webturb;application/widget;application/winhlp;text/vnd.wap.wml;text/vnd.wap.wmlscript;application/vnd.wap.wmlscriptc;application/vnd.wordperfect;application/vnd.wt.stf;application/wsdl+xml;image/x-xbitmap;image/x-xpixmap;image/x-xwindowump;application/x-x509-ca-cert;application/x-xfig;application/xhtml+xml;application/xml;application/xcap-diff+xml;application/xenc+xml;application/patch-ops-error+xml;application/resource-lists+xml;application/rls-services+xml;aplication/resource-lists-diff+xml;application/xslt+xml;application/xop+xml;application/x-xpinstall;application/xspf+xml;application/vnd.mozilla.xul+xml;chemical/x-xyz;text/yaml;application/yang;application/yin+xml;application/vnd.ul;application/zip;application/vnd.handheld-entertainment+xml;application/vnd.zzazz.deck+xml");

		FirefoxDriver driver = new FirefoxDriver(newProfile);
		driver.manage().window().maximize();
		driver.get(URL);

		Generic.setBrowserZoom(driver,ZoomPercent);
		return driver;
	}
	
	public static JSONObject readJSON(String strJsonPath) throws FileNotFoundException, IOException, ParseException{
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader(strJsonPath));  
		JSONObject jsonObject = (JSONObject) obj; 
		return jsonObject;
	}
	
	public static boolean setBrowserZoom(FirefoxDriver driver,String strZoomPercent){
		int zoomPercent=Integer.parseInt(strZoomPercent);
		int actualZoom = 0;
		if(zoomPercent>99){
			if(zoomPercent>100 && zoomPercent<111){
				actualZoom =1;	
			}else if(zoomPercent>110 && zoomPercent<121){
				actualZoom =2;	
			}else if(zoomPercent>120 && zoomPercent<140){
				actualZoom=3;
			}else if(zoomPercent>140 && zoomPercent<151){
				actualZoom=4;
			}else if(zoomPercent>150 && zoomPercent<171){
				actualZoom=5;
			}else if(zoomPercent>170 && zoomPercent<201){
				actualZoom=6;
			}else if(zoomPercent>200 && zoomPercent<241){
				actualZoom=7;
			}else if(zoomPercent>240 && zoomPercent<301){
				actualZoom=8;
			}
	
			for(int intLoop=0;intLoop<actualZoom;intLoop++){
				driver.findElement(By.tagName("html")).sendKeys(Keys.chord(Keys.CONTROL, Keys.ADD));
			}
		}
		
		if(zoomPercent<100){
			if(zoomPercent>0 && zoomPercent<31){
				actualZoom =5;	
			}else if(zoomPercent>30 && zoomPercent<51){
				actualZoom =4;	
			}else if(zoomPercent>50 && zoomPercent<71){
				actualZoom=3;
			}else if(zoomPercent>70 && zoomPercent<81){
				actualZoom=2;
			}else if(zoomPercent>80 && zoomPercent<91){
				actualZoom=1;
			}
			
			for(int intLoop=0;intLoop<actualZoom;intLoop++){
				driver.findElement(By.tagName("html")).sendKeys(Keys.chord(Keys.CONTROL, Keys.SUBTRACT));
			}
		}
		return true;
	}
	
	public static String readPropertyFile(String strPropertyFilePath,String strProperty){
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(strPropertyFilePath);
			prop.load(input);
			// get the property value and print it out
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop.getProperty(strProperty);
	  }
	
	public static boolean sendOTP(Long lngPhno, Logger log){
		
		try {
				MDC.put("EventType", "sendingOTP");
				
				JSONObject jObj;
				strApiKey=System.getenv("otpAPIKey");
				if(strApiKey==null){
					 jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("OtpAndSmsConfig");
					strApiKey=jObj.get("otpAPIKey").toString();
					
				}else{
					log.error("API key not found !");
				}
				jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("OtpAndSmsConfig");
				String strOtpProvider=jObj.get("OtpProviderURL").toString();
				
				String strAutoGenerateOTPUrl=strOtpProvider+ strApiKey	+ "/SMS/"+ lngPhno +"/AUTOGEN/FINFORT";
				HttpResponse<JsonNode> response = null;		
				response = Unirest.get(strAutoGenerateOTPUrl).asJson();
				org.json.JSONObject jsonObj=response.getBody().getObject();
				String strSessionID = jsonObj.getString("Details");
				String strSentStatus = jsonObj.getString("Status");
				if(strSentStatus.equalsIgnoreCase("Success")){
					MDC.put("EventData", "Success");
					log.info("OTP Sent successfully");
					try {
						if(InputOtpPopup(strSessionID,lngPhno, log)){
							return true;
						}
					} catch (UnirestException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					}else{
						MDC.put("EventData", "Failure");
						log.info("Invalid Mobile Number entered");
						showPopup("Invalid Mobile No, Please enter a valid Mobile no");
						return false;
					}
		}catch(Exception e){
			MDC.put("EventData", "Failure");
			log.info("Failed to send OTP");
			System.out.println(e.getMessage());
			return false;
		}
		return false;
	}
	public static boolean InputOtpPopup(String strSesID, long intPhoneNumber, Logger log) throws UnirestException, FileNotFoundException, IOException, ParseException {
		boolean bStatus= false;
		JPanel panel= new JPanel();
		panel.setVisible(true);
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setPreferredSize(new Dimension(400, 80));
		final JTextField textBox=new JTextField();
		textBox.setText("Enter OTP here");
		textBox.setVisible(true);
		//textBox.setLayout(new FlowLayout());;
		
		
		textBox.addFocusListener(new FocusListener() {
		    public void focusGained(FocusEvent e) {
		        textBox.setText("");
		    }

		    public void focusLost(FocusEvent e) {
		        // nothing
		    }
		});
		String newline = System.getProperty("line.separator");
		JLabel label= new JLabel();
		label.setText("<html>You must have recieved an OTP on your phone number.Please  enter the same here to confirm it is you who have initiated the download.</html>");
		label.setVisible(true);
		panel.setLayout(new BorderLayout());
		panel.add(label);
		panel.add(textBox,BorderLayout.PAGE_END);
		
		String[] options={"Submit","Resend OTP"};
		int n=JOptionPane.showOptionDialog(label,panel, "Enter OTP", JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		String strOTP=textBox.getText();

		JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("OtpAndSmsConfig");
		String strOtpProvider=jObj.get("OtpProviderURL").toString();
		String strOTPVerifyUrl=strOtpProvider+ strApiKey +"/SMS/VERIFY/"+strSesID+"/"+strOTP;
		
		jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("OtpAndSmsConfig");
    	int intOtpRetryMax=Integer.parseInt((String)jObj.get("maxOTPRetries"));
		
		if(n==JOptionPane.NO_OPTION){ //resend otp
			resendOTPCount++;
			MDC.put("EventType", "OTPResend");
			MDC.put("EventData","Success");
			
			log.info("OTP resend for"+resendOTPCount+" And Attempt left:"+(intOtpRetryMax-resendOTPCount));
			while(resendOTPCount>intOtpRetryMax){
				
				MDC.put("EventType","OTPMaxRetriesExceeded");
				MDC.put("EventDate","Failure");
				log.info("Resend OTP Entered "+resendOTPCount+" times");
				showPopup("OTP can be re-generated for a maximum of 3 times. The system will exit now.");
				System.exit(0);
				log.info("System Exited");
			}
						
			String strAutoGenerateOTPUrl=strOtpProvider+ strApiKey	+ "/SMS/"+ intPhoneNumber +"/AUTOGEN/FINFORT";
			HttpResponse<JsonNode> request = Unirest.get(strAutoGenerateOTPUrl).asJson();
			@SuppressWarnings("static-access")
			org.json.JSONObject jsonObj=request.getBody().getObject();
			String strSessionID = jsonObj.getString("Details");
			String strSentStatus = jsonObj.getString("Status");
			if(strSentStatus.equalsIgnoreCase("Success")){
				bStatus=InputOtpPopup(strSessionID,intPhoneNumber,log);
				}else
					bStatus= false;
			
		}else if(n==JOptionPane.YES_OPTION){
			try {
				
				HttpResponse<JsonNode> response = Unirest.get(strOTPVerifyUrl).asJson();
				org.json.JSONObject jsonObj=response.getBody().getObject();
				String strOTPMatchedStatus = jsonObj.getString("Details");
				if(strOTPMatchedStatus.equalsIgnoreCase("OTP Matched")){
					MDC.put("EventType","OTPStatus");
					MDC.put("EventData",strOTPMatchedStatus);
					log.info("OTP is Matched");
					System.out.println("OTP matched !");
					bStatus = true;
				}else if(strOTPMatchedStatus.equalsIgnoreCase("OTP Expired")){
					System.out.println("OTP Expired !");
					MDC.put("EventType","OTPStatus");
					MDC.put("EventData",strOTPMatchedStatus);
					log.info("OTP is Expired");
					showPopup("The OTP you entered is Expired,click resend OTP");
					bStatus=InputOtpPopup(strSesID, intPhoneNumber, log);
				}
				else{
					MDC.put("EventType","OTPStatus");
					MDC.put("EventData","Failure");
					log.info("Entered OTP is incorrect");
					showPopup("The OTP you entered is incorrect,click resend OTP");
					bStatus=InputOtpPopup(strSesID, intPhoneNumber,log);
				}
			} catch (UnirestException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				}
			}
		return bStatus;
		}
	
	public static boolean exists(WebDriver driver,String Xpath){
		if(driver.findElements(By.xpath(Xpath)).size()>0){
			return true;
		}else
			return false;
	}
	
	public static void waitAftereLogout(FirefoxDriver driver) 
    {
        try{
        	JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("BasicInfo");
        	int longtimeToLogout=Integer.parseInt((String)jObj.get("TimeToDisplayPostLogoutScreenInSeconds"));
        	Thread.sleep(longtimeToLogout);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

	public static void captureScreenshot(WebDriver driver,String strPath) throws IOException{
				File scrFile;
				scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(scrFile, new File(strPath));
	}

	public static JFrame showPreLoader(){
				MDC.put("EventType","Preloader");
				MDC.put("EventData", "Success");
		        Log.info("Preloader started");
				final JFrame frame=new JFrame();
				frame.getContentPane().setBackground(Color.WHITE);		
				frame.setUndecorated(true);
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				int height = screenSize.height;
				int width = screenSize.width;
				frame.setSize(width, height);
				frame.setLocationRelativeTo(null);
				frame.setState((int) Float.MAX_VALUE);
				frame.setFocusable(true);
				ImageIcon loading = new ImageIcon("./InputData/loader.gif");
				frame.add(new JLabel("Loading...", loading, JLabel.CENTER));
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setAlwaysOnTop(true);
				frame.setVisible(true);
				return frame;
				
	}
	
	public static void closePreLoader(JFrame frame){
		 frame.setVisible(false);
         frame.dispose();
         MDC.put("EventType","Preloader");
		 MDC.put("EventData", "Success");
         Log.info("Pre-loader closed");
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject WriteJSON(String strJsonPath,String strArrayName,String strData){
		JSONObject obj = new JSONObject();
		JSONArray arrUserInfo = new JSONArray();
		arrUserInfo.add(strData);
		obj.put(strArrayName, arrUserInfo);
		
		FileWriter file;
		try {
			file = new FileWriter(strJsonPath);
			file.write(obj.toJSONString());
			System.out.println("Successfully Copied JSON Object to File...");
			System.out.println("\nJSON Object: " + obj);
			file.flush();  
			file.close(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return obj;
	}
	
	public static boolean WriteToJSON(String strJsonPath,String strKey, String strValue ){
		JSONObject obj = new JSONObject();
		try{
			obj.put(strKey, strValue);
			FileWriter file = new FileWriter(strJsonPath);
			file.write(obj.toJSONString());
			file.flush();
			file.close();
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public static boolean WriteMultipleJSON(String strJsonPath,String strKey, String strValue ){
		
		try{
			JSONObject obj = new JSONObject();
			JSONArray company = new JSONArray();
//			company.add("Compnay: eBay");
//			company.add("Compnay: Paypal");
//			company.add("Compnay: Google");
//			obj.put("Company List", company);
//	 
			for(int i=0;i<=5;i++){
			obj.put(strKey, strValue);
			}
			FileWriter file = new FileWriter(strJsonPath);
			file.write(obj.toJSONString());
		file.flush();
//			file.close();
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public static void createFolder(String strFolderPath){
				File theDir = new File(strFolderPath);
				if (!theDir.exists()) {
					theDir.mkdir();
				}
	}
		
  public static void moveFile(String oldLocation, String newLocation,Logger log){
	  	MDC.put("EventType","DataDownload");
	  	System.out.println("move");
			File file = new File(
					oldLocation+".part");
			while (file.exists()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					MDC.put("EventData","Failure");
					log.error("Failed to identify if the file is downloaded");
				}
			}
			try {
				if(Files.exists(Paths.get(newLocation))){
					Files.delete(Paths.get(newLocation));
				}
				Files.move(Paths.get(oldLocation), Paths.get(newLocation));
				if(Files.exists(Paths.get(oldLocation))){
					Files.delete(Paths.get(oldLocation));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				MDC.put("EventData","Failure");
				log.error("Failed to move the file to the required destination");

			}
	}
	
	public static File[] finder( String dirName,final String pattern,Logger log){
		    	try{
		    		System.out.println("Finder");
		    		Thread.sleep(2000);
					File dir = new File(dirName);
			    	File[] fileName= dir.listFiles(new FilenameFilter() { 
				    	         public boolean accept(File dir, String filename)
				    	              { return filename.endsWith(pattern); }
				    	} );
			    	System.out.println(fileName[0].toString());
			    	log.info("File: "+fileName[0].toString());
			    	return fileName;
		    	}catch(Exception e){
		    		System.out.println("Finder- error");

		    		System.out.println(e.getMessage());
		    		return null;
		    	}
    }
	
	public static void SendMailZippedPassword(String strPhoneNumber, String strUserNAme, String strUtilityName,String strPassword){
		
		JSONObject jObj;			
			try {
					jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("OtpAndSmsConfig");
					String strSenderId=jObj.get("TransactionSMSSenderID").toString();
					String strTemplate=jObj.get("TransactionSMSTemplate").toString();
					strApiKey=System.getenv("API_HOME");
					if(strApiKey==null){
						 jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("OtpAndSmsConfig");
						strApiKey=jObj.get("otpAPIKey").toString();
						
					}
					jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("OtpAndSmsConfig");
					String strOtpProvider=jObj.get("OtpProviderURL").toString();

					String strMAilPasswordURL=strOtpProvider+ strApiKey +"/ADDON_SERVICES/SEND/TSMS";
					HttpResponse<JsonNode> response = Unirest.post(strMAilPasswordURL)
							.header("content-type", "application/x-www-form-urlencoded")
							.body("From="+strSenderId+"&To="+strPhoneNumber+"&TemplateName="+strTemplate+"&VAR1="+strUserNAme+"&VAR2="+strUtilityName+"&VAR3="+strPassword+"") .asJson();
					org.json.JSONObject jsonObj=response.getBody().getObject();
					String strSessionID = jsonObj.getString("Details");
					String strSentStatus = jsonObj.getString("Status");
					System.out.println(strSessionID);
					System.out.println(strSentStatus);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}

	
	public void listFilesForFolder(final File folder) {
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	            System.out.println(fileEntry.getName());
	        }
	    }
	}
	


	
    public void listFilesAndFolders(String directoryName){

        File directory = new File(directoryName);

        //get all the files from a directory

        File[] fList = directory.listFiles();

        for (File file : fList){

            System.out.println(file.getName());

        }

    }

    /**

     * List all the files under a directory

     * @param directoryName to be listed

     */

    public void listFiles(String directoryName){

        File directory = new File(directoryName);

        //get all the files from a directory

        File[] fList = directory.listFiles();

        for (File file : fList){

            if (file.isFile()){

                System.out.println(file.getName());

            }

        }

    }

    /**

     * List all the folder under a directory

     * @param directoryName to be listed

     */

    public void listFolders(String directoryName){

        File directory = new File(directoryName);

        //get all the files from a directory

        File[] fList = directory.listFiles();

        for (File file : fList){

            if (file.isDirectory()){

                System.out.println(file.getName());

            }

        }

    }

    /**
	 * uploadToDriveHierarchy
     * Upload a directory and its subdirectories to google drive
     * @param directoryName to be listed
     * @throws IOException 
     */

	public static String uploadToDriveHierarchy(Drive service, String folderPathToUpload, String strParentFolderID, Logger log){
		MDC.put("EventType","UploadtoDrive");
        File directory = new File(folderPathToUpload);
        List<ParentReference> listParentReference = new ArrayList<ParentReference>();
        File[] fList = directory.listFiles();
       
	    List <File> tempList = new ArrayList <File>();

        int i=0;
        for (File files : fList){
        	if(files.isFile()){
        		tempList.add(files);
        		i++;
        	}
        }
        
        for (File fileDir : fList){
        	if(fileDir.isDirectory()){
        		tempList.add(fileDir);
        		i++;
        	}
        }
        File[] sortedFiles = tempList.toArray(new File[tempList.size()]);
        
        for (File file : sortedFiles){
            if (file.isFile()){
	    		try{
	    			gMail.insertFileIntoDrive(service,file.getName(), "", strParentFolderID, null, file.getAbsolutePath());
	    			MDC.put("EventData",file.getName()+" Uploaded");
	        		log.info("Uploaded "+file.getName()+" in google drive");
	    		}catch(Exception e){
	    			MDC.put("EventData","Failure");
	        		log.error("Failed to upload "+file.getName()+" in google drive. Exception: "+e.getMessage());
	        		return "false";
	    		}

            } else if (file.isDirectory()){
            	listParentReference.add(new ParentReference().setId(strParentFolderID));
            	ParentReference strTempParentFolderID=listParentReference.get(0);
            	listParentReference.clear();
            	listParentReference.add(strTempParentFolderID);
            	try{
            		strParentFolderID= gMail.createFolder(service, file.getName(), listParentReference).getId();
            		MDC.put("EventData","Success");
            		log.info("Created folder "+file.getName()+" in google drive");

            	}catch(Exception e){
            		MDC.put("EventData","Failure");
            		log.error("Failed to create folder "+file.getName()+" in google drive. Exception: "+e.getMessage());
            		return "false";
            	}
            	uploadToDriveHierarchy(service,file.getAbsolutePath(),strParentFolderID,log);
            }
        }
		return strParentFolderID;

	    }
		
	public static void writeToPostbackJson(String strJsonPath,String strFForderNo, String strFFProdCode,String strDataPullEntity,String StrPullSeqNo,String strGoogleDrivePath, String Status ){
		try{
			JSONObject json = new JSONObject();
			json.put("FFOrderNo", strFForderNo);
			json.put("FFProdCode", strFFProdCode);
			json.put("Datapull", strDataPullEntity);
			json.put("PullSeqNo", StrPullSeqNo);
			json.put("GooglePath", strGoogleDrivePath);
			json.put("Status", Status);
		
			FileWriter jsonFileWriter = new FileWriter(strJsonPath); 
			jsonFileWriter.write(json.toJSONString()); 
			jsonFileWriter.flush(); 
			jsonFileWriter.close();
			
			MDC.put("EventType","PostBackJSON");
			MDC.put("EventDat","Success");
			Log.info("written to Postback.json file");
		}catch(Exception e){
			MDC.put("EventType","PostBackJSON");
			MDC.put("EventDat","Failue");
			Log.info("failed to write into Postback.json file: "+e.getMessage());
			
		}
		

		
	}
 public static boolean SendCleanUpUnsuccessfulMail(String strUserName,String strFrom,String strCC,String strFFOrderNo, String strPullSequenceNo,String strDataPullEntity, Logger log ){
	 boolean bStatus=false;
		 try{
			 
		 String[] arrMsg = readFile("./InputMessages/SpecialMessageCleanUnsuccessful.txt").split("#newLine#");
	        String strMsg = "";
	       String newline=System.getProperty("line.separator");
	        for(int intLoop = 0;intLoop<arrMsg.length;intLoop++){
	           if(strMsg==""){
	        	   strMsg =arrMsg[intLoop];
	           }else{
	        	   strMsg=strMsg + newline + arrMsg[intLoop];
	           }
	        }
	        String subject = "Spl Msg: FFOrder Number: "+strFFOrderNo+"- PullSequence Number: "+strPullSequenceNo+" - Utility: "+strDataPullEntity+"-Borrower: "+strUserName;
	        String strBody = strMsg.replace("<<FFOrderNo>>", strFFOrderNo);
	        strBody = strBody.replace("<<PullSequenceNO>>", strPullSequenceNo);
	        JSONObject jObj = (JSONObject) readJSON("./InputData/datapullcfg.json").get("FFCustomerSupportDetails");
	        String strTo=jObj.get("FFOpsCustomerSupportPersonEmailID").toString();
	        
	       	gMail.sendMessage(strUserName,  strTo, strFrom, strCC,subject, strBody,"","",log);
	       	System.exit(0);

			bStatus=true;
			
			}catch(Exception e){
				bStatus=false;
				e.printStackTrace();
			}
		 return bStatus;
		 }
	   
 	public static File getBaseDir(String BaseDir,String strFForderNumber, String seqNo,String dataPullEntity){
		File newDir = new File(BaseDir);
		if (!newDir.exists()) {
			newDir.mkdir();
			}
		newDir = new File(newDir+"/"+strFForderNumber);
		if (!newDir.exists()) {
			newDir.mkdir();
			}

		newDir = new File(newDir+"/"+seqNo);
		if (!newDir.exists()) {
			newDir.mkdir();
			}
		
		newDir = new File(newDir+"/"+dataPullEntity);
		if (!newDir.exists()) {
			newDir.mkdir();
			}
		
		String format = String.format("%%0%dd", 3);
	      int num=1;
	      File newDirToCreate = null;
	      boolean flag= false; 
	      while(!flag){
	  		newDirToCreate = new File(newDir+"/"+String.format(format, num));
	  		if(!newDirToCreate.exists()){
	  			newDirToCreate.mkdir();
	  			flag = true;
	  		}
	  		num++;
	      }      
		return newDirToCreate.getAbsoluteFile();
 	} 	 
 	
 	public static JsonWriter writeToJson(String jsonPath, String strResource, String NameValues, JsonWriter jsonWriter){
 		try {
 			if(jsonWriter==null){
 	 		    jsonWriter = new JsonWriter(new FileWriter(jsonPath));
 	 		    jsonWriter.beginObject();
 			}
 		    jsonWriter.name(strResource);
 		    jsonWriter.beginArray();
 		    jsonWriter.beginObject();
 		    String[] arrNameValues = NameValues.split("#");
 		    for(int i=0;i<arrNameValues.length;i++){
 		    	String[] arrIndNameVal = arrNameValues[i].split("~");
	    		jsonWriter.name(arrIndNameVal[0]);
	 		    try{
		    		jsonWriter.value(arrIndNameVal[1]);
	 		    }catch(Exception e){
	 		    	jsonWriter.value("");
	 		    }
 		    }
	    	jsonWriter.endObject();
 		    jsonWriter.endArray();
 		} catch (IOException e) {
 		    System.out.println(e.getMessage());
 		}
		return jsonWriter;
 	}
 	
 	public static void closeJson(JsonWriter jsonWriter){
        try {
            jsonWriter.endObject();
			jsonWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 	}
 	
 	public static boolean waitUntilFileDownloaded(String filePathString,Logger log) throws Exception{
		boolean bStatus=false;		 
		try{
			File f = new File(filePathString);
			Thread.sleep(5000);
			for(int i=1;i<=10;i++){				
				if(f.isFile()){		     
					bStatus=true;
					break;
				}else{
					Thread.sleep(5000);
					}
				}
			}catch(Exception e){
			
			e.printStackTrace();
			bStatus=false;
		}
		return bStatus;
	}
 	/**
	 * SwitchToWindow method switch to the frame when only one frame is available
	 *
	 */
	public static boolean SwitchToWindow(FirefoxDriver driver,Logger log) throws Exception{
		boolean bStatus=false;		 
		try{
			Set<String> Allwindowhandles=driver.getWindowHandles();
			CurrentWindowHandle=driver.getWindowHandle();
			for (String currentWindowHandle : Allwindowhandles) {
				if (!currentWindowHandle.equals(CurrentWindowHandle)) {
					driver.switchTo().window(currentWindowHandle);
			 bStatus=true;
			 
		       }
	        }	
		}catch(Exception e){
			
			e.printStackTrace();
		}
		return bStatus;
      }
	
	/**
	 * SwitchToParentWindow method switch to the frame when only one frame is available
	 * @return Boolean value True/False based on success of this function
	 * @author	Sarath Gorantla
	 */
	public static boolean SwitchToParentWindow(FirefoxDriver driver,Logger log) throws Exception{
		boolean bStatus=false;		 
		try{
			
			driver.close();
			 driver.switchTo().window(CurrentWindowHandle);
			 bStatus=true;	
		}catch(Exception e){
			
			e.printStackTrace();
		}
		return bStatus;
      }
	
}
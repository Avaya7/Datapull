package gMailAPI;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.mortbay.log.Log;
import org.testng.log4testng.Logger;

import Generic.Generic;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;

public class gMail {
    /** Application name. */
    private static final String APPLICATION_NAME =
            "Gmail API Finfort";

	public static String getCredentialDirectory(String utility){
	    try {
	    	if(utility.equalsIgnoreCase("mail")){
				JSONObject jObj = (JSONObject) Generic.readJSON("./InputData/datapullcfg.json").get("MailConfig");
				return jObj.get("mailTokenJSON").toString();
	    	}else if(utility.equalsIgnoreCase("drive")){
				JSONObject jObj = (JSONObject) Generic.readJSON("./InputData/datapullcfg.json").get("DriveConfig");
				return jObj.get("driveTokenJSON").toString();
	    	}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    /** Directory to store user credentials for this application. */

	
    private static final java.io.File DATA_STORE_DIR_Mail = new java.io.File(
            "InputData/"+getCredentialDirectory("mail"));
    private static final java.io.File DATA_STORE_DIR_Drive = new java.io.File(
            "InputData/"+getCredentialDirectory("drive"));

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
        JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/gmail-java-quickstart.json
     */
    private static final List<String> SCOPES_Gmail =
        Arrays.asList(GmailScopes.MAIL_GOOGLE_COM,
        		"https://www.googleapis.com/auth/gmail.send",
        		"https://www.googleapis.com/auth/gmail.compose"
        		);
    
    public static FileDataStoreFactory getDataStorefactory(String Utility){
        try {
        	if(Utility.equalsIgnoreCase("mail")){
        		DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR_Mail);
        	}else if(Utility.equalsIgnoreCase("drive")){
        		DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR_Drive);
        	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return DATA_STORE_FACTORY;
    }
    
    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            //DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR_Mail);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }
    
    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            //DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR_Drive);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }
    private static final List<String> SCOPES_Drive =
            Arrays.asList("https://www.googleapis.com/auth/drive",
            		"https://www.googleapis.com/auth/drive.file",
            		"https://www.googleapis.com/auth/drive.appdata");

        
    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     * @throws ParseException 
     */
    public static Credential authorize(String Utility) throws IOException, ParseException {
        // Load client secrets.
    	System.out.println("get ser");
    	 JSONObject jObj = (JSONObject) Generic.readJSON("./InputData/datapullcfg.json").get("BasicInfo");
		String strBaseDir = jObj.get("baseDir").toString();
		InputStream in=null;
		
		jObj = (JSONObject) Generic.readJSON("./InputData/datapullcfg.json").get("MailConfig");
		String strEmailClient = jObj.get("mailAPIJSON").toString();
		System.out.println("strEmailClient");
		
		jObj = (JSONObject) Generic.readJSON("./InputData/datapullcfg.json").get("DriveConfig");
		String strDriveClient = jObj.get("driveAPIJSON").toString();
			
		if(Utility.equalsIgnoreCase("mail")){
			in = new FileInputStream("InputData/"+strEmailClient);
		}else if(Utility.equalsIgnoreCase("drive")){
			in = new FileInputStream("InputData/"+strDriveClient);
		}
        
        System.out.println("input stream="+ in);
    	GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
			System.out.println("isEmpty "+clientSecrets.isEmpty());
			System.out.println("toStr "+clientSecrets.toString());
        // Build flow and trigger user authorization request.
		if(Utility.equalsIgnoreCase("mail")){
			DATA_STORE_FACTORY=getDataStorefactory("mail");
			GoogleAuthorizationCodeFlow flow = 
	                new GoogleAuthorizationCodeFlow.Builder(
	                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES_Gmail)
	                .setDataStoreFactory(DATA_STORE_FACTORY)
	                .setAccessType("offline")
	                .build();
			Credential credential = new AuthorizationCodeInstalledApp(
		            flow, new LocalServerReceiver()).authorize("user");
	        return credential;

		}else if(Utility.equalsIgnoreCase("drive")){
			DATA_STORE_FACTORY=getDataStorefactory("drive");
			GoogleAuthorizationCodeFlow flow = 
	                new GoogleAuthorizationCodeFlow.Builder(
	                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES_Drive)
	                .setDataStoreFactory(DATA_STORE_FACTORY)
	                .setAccessType("offline")
	                .build();
			Credential credential = new AuthorizationCodeInstalledApp(
		            flow, new LocalServerReceiver()).authorize("user");
	        return credential;
		}
		return null;
    }

    /**
     * Build and return an authorized Gmail client service.
     * @return an authorized Gmail client service
     * @throws IOException
     * @throws ParseException 
     */
    public static Gmail getGmailService() throws IOException, ParseException {
        Credential credential = authorize("mail");
        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
    
    /**
     * Create a MimeMessage using the parameters provided.
     *
     * @param to Email address of the receiver.
     * @param from Email address of the sender, the mailbox account.
     * @param subject Subject of the email.
     * @param bodyText Body text of the email.
     * @param fileDir Path to the directory containing attachment.
     * @param filename Name of file to be attached.
     * @return MimeMessage to be used to send email.
     * @throws MessagingException
     */
    public static MimeMessage createEmailWithAttachment(String to, String from,String cc, String subject,
        String bodyText, String fileDir, String filename) throws MessagingException, IOException {
      Properties props = new Properties();
      Session session = Session.getDefaultInstance(props, null);

      MimeMessage email = new MimeMessage(session);
      InternetAddress tAddress = new InternetAddress(to);
      InternetAddress ccAddress = new InternetAddress(cc);
      InternetAddress fAddress = new InternetAddress(from);

      email.setFrom(fAddress);
      email.addRecipient(javax.mail.Message.RecipientType.TO, tAddress);
      email.addRecipient(javax.mail.Message.RecipientType.CC,ccAddress);
      email.setSubject(subject);

      MimeBodyPart mimeBodyPart = new MimeBodyPart();
      mimeBodyPart.setContent(bodyText, "text/plain");
      mimeBodyPart.setHeader("Content-Type", "text/plain; charset=\"UTF-8\"");

      Multipart multipart = new MimeMultipart();
      multipart.addBodyPart(mimeBodyPart);

      mimeBodyPart = new MimeBodyPart();
      DataSource source = new FileDataSource(fileDir + filename);

      mimeBodyPart.setDataHandler(new DataHandler(source));
      mimeBodyPart.setFileName(filename);
      String contentType = Files.probeContentType(FileSystems.getDefault()
          .getPath(fileDir, filename));
      mimeBodyPart.setHeader("Content-Type", contentType + "; name=\"" + filename + "\"");
      mimeBodyPart.setHeader("Content-Transfer-Encoding", "base64");

      multipart.addBodyPart(mimeBodyPart);

      email.setContent(multipart);

      return email;
    }
    
    /**
     * Create a Message from an email
     *
     * @param email Email to be set to raw of message
     * @return Message containing base64url encoded email.
     * @throws IOException
     * @throws MessagingException
     */
    public static Message createMessageWithEmail(MimeMessage email)
        throws MessagingException, IOException {
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      email.writeTo(bytes);
      String encodedEmail = Base64.encodeBase64URLSafeString(bytes.toByteArray());
      Message message = new Message();
      message.setRaw(encodedEmail);
      return message;
    }
    
    public static MimeMessage createEmail(String to, String from,String cc, String subject,
    	      String bodyText) throws MessagingException {
    	    Properties props = new Properties();
    	    Session session = Session.getDefaultInstance(props, null);

    	    MimeMessage email = new MimeMessage(session);
    	    InternetAddress tAddress = new InternetAddress(to);
    	    InternetAddress ccAddress = new InternetAddress(cc);
    	    InternetAddress fAddress = new InternetAddress(from);

    	    email.setFrom(new InternetAddress(from));
    	    email.addRecipient(javax.mail.Message.RecipientType.TO,
    	                       new InternetAddress(to));
    	    email.addRecipient(javax.mail.Message.RecipientType.CC,
                    new InternetAddress(cc));
    	    email.setSubject(subject);
    	    email.setText(bodyText);
    	    return email;
    	  }

    /**
     * Send an email from the user's mailbox to its recipient.
     *
     * @param service Authorized Gmail API instance.
     * @param userId User's email address. The special value "me"
     * can be used to indicate the authenticated user.
     * @param email Email to be sent.
     * @throws MessagingException
     * @throws IOException
     * @throws ParseException 
     */
  public static void sendMessage(String userId,String to, String from,String cc, String subject,String bodyText, String fileDir, String filename,org.apache.log4j.Logger log){
      try{
		  Gmail service = getGmailService();

	      MimeMessage mimeMessage;

	      if(!fileDir.isEmpty()){

		      mimeMessage = createEmailWithAttachment(to,
		    		  from,
		    		  cc,
		    		  subject,
		    		  bodyText,
		    		  fileDir,
		    		  filename);
	      }else{

	    	   mimeMessage =createEmail(to,
		    		  from,
		    		  cc,
		    		  subject,
		    		  bodyText);
	      }

	      Message message = service.users().messages().send(userId, createMessageWithEmail(mimeMessage)).execute();

	      System.out.println("Message id: " + message.getId());
      }catch(Exception e){
    	  log.error("Mail sending failed :"+e.getMessage());
      }
    }
  
  /**
   * Insert new file.
   *
   * @param title Title of the file to insert, including the extension.
   * @param description Description of the file to insert.
   * @param parentId Optional parent folder's ID.
   * @param mimeType MIME type of the file to insert.
   * @param filename Filename of the file to insert.
   * @return Inserted file metadata if successful, {@code null} otherwise.
   */
  public static File insertFileIntoDrive(Drive service, String title, String description,
      String parentId, String mimeType, String filename) {
    // File's metadata.
    File body = new File();
    body.setTitle(title);
    body.setDescription(description);
    body.setMimeType(mimeType);
    // Set the parent folder.
    if (parentId != null && parentId.length() > 0) {
      body.setParents(
          Arrays.asList(new ParentReference().setId(parentId)));
    }

    // File's content.
    java.io.File fileContent = new java.io.File(filename);
    FileContent mediaContent = new FileContent(mimeType, fileContent);
    try {
    	File file = service.files().insert(body, mediaContent).execute();
    	return file;
    } catch (IOException e) {
      System.out.println("An error occured: " + e);
      return null;
    }
  }

  /**
   * Build and return an authorized Drive client service.
   * @return an authorized Drive client service
   * @throws IOException
   */
  public static Drive getDriveService() throws IOException {
      Credential credential = null;
	try {
		credential = authorize("Drive");
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      return new Drive.Builder(
              HTTP_TRANSPORT, JSON_FACTORY, credential)
              .setApplicationName(APPLICATION_NAME)
              .build();
  }
  public static String getID(String strFileName) throws IOException {
	  	String strID=null;
	    List<File> result = new ArrayList<File>();
    	Drive service = getDriveService();
	    com.google.api.services.drive.Drive.Files.List request = service.files().list();

	    do {
	      try {
	        FileList files = request.execute();

	        result.addAll(files.getItems());
	        request.setPageToken(files.getNextPageToken());
	      } catch (IOException e) {
	        System.out.println("An error occurred: " + e);
	        request.setPageToken(null);
	      }
	    } while (request.getPageToken() != null &&
	             request.getPageToken().length() > 0);

	    for(int intI = 0;intI<result.size();intI++){
	    	if(result.get(intI).getTitle().toString().equalsIgnoreCase(strFileName)){
	    		strID = result.get(intI).getId();
	    	}
	    }
	    return strID;
	  }
  
  /**
   * 
   * @param service google drive instance
   * @param title the title (name) of the folder (the one you search for)
   * @param parentId the parent Id of this folder (use root) if the folder is in the main directory of google drive
   * @return google drive file object 
   * @throws IOException
   */
  public static File getExistsFolder(Drive service,String title,String parentId) throws IOException 
  {
      Drive.Files.List request;
      //Drive service = getDriveService();
      request = service.files().list();
      String query = "mimeType='application/vnd.google-apps.folder' AND trashed=false AND title='" + title + "' AND '" + parentId + "' in parents";
      System.out.println("isFolderExists(): Query= " + query);
      request = request.setQ(query);
      FileList files = request.execute();
      System.out.println("isFolderExists(): List Size =" + files.getItems().size());
      if (files.getItems().size() == 0) //if the size is zero, then the folder doesn't exist
          return null;
      else
          //since google drive allows to have multiple folders with the same title (name)
          //we select the first file in the list to return
          return files.getItems().get(0);
  }
  
  /**
   * 
   * @param service google drive instance
   * @param title the folder's title
   * @param listParentReference the list of parents references where you want the folder to be created, 
   * if you have more than one parent references, then a folder will be created in each one of them  
   * @return google drive file object   
   * @throws IOException
   */
  public static File createFolder(Drive service,String title,List<ParentReference> listParentReference) throws IOException
  {
      File body = new File();
      body.setTitle(title);
      body.setParents(listParentReference);
      body.setMimeType("application/vnd.google-apps.folder");
      File file = service.files().insert(body).execute(); 
      return file;
  }
  
  /**
   * 
   * @param titles list of folders titles 
   * i.e. if your path like this folder1/folder2/folder3 then pass them in this order createFoldersPath(service, folder1, folder2, folder3)
   * @return parent reference of the last added folder in case you want to use it to create a file inside this folder.
   * @throws IOException
   */	
  public static String createFoldersPath(String[] titles) throws IOException
  {
      List<ParentReference> listParentReference = new ArrayList<ParentReference>();
      File file = null;
      String strDrivePath = null;
      Drive service = getDriveService();
      for(int i=0;i<titles.length;i++)
      {
    	  if(i==0){
    		  strDrivePath =titles[i];
    	  }else{
    		  strDrivePath=strDrivePath+"/"+titles[i];
    	  }
          file = getExistsFolder(service,titles[i], (file==null)?"root":file.getId());
          if (file == null)
          {
              file = createFolder(service, titles[i], listParentReference);
          }
          listParentReference.clear();
          listParentReference.add(new ParentReference().setId(file.getId()));
      }
      
      String format = String.format("%%0%dd", 3);
      int num=1;
      String parentFolder =file.getId();
      while(true){
    	  file = getExistsFolder(service,String.format(format, num), parentFolder);
          if (file == null){
        	  file = createFolder(service, String.format(format, num), listParentReference);
        	  strDrivePath=strDrivePath+"/"+String.format(format, num);
        	  break;
          }else{
        	  num++;
          }
      }      
      return file.getId()+"~"+strDrivePath;
  }

}
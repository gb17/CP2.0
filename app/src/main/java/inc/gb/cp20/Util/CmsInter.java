package inc.gb.cp20.Util;

/**
 * Created by GB on 3/10/16.
 */
public interface CmsInter {


    String AL_INV_CLNT = "Invalid Client ID!!";
    String AL_IvalidStatus = "Invalid status.Plz contact administrator";
    String AL_InvalidVersion = "Invalid Version";
    String AL_InvalidInstanceID = "User already configured";

    int NORMAL_TYPE = 0;
    int ERROR_TYPE = 1;
    int SUCCESS_TYPE = 2;
    int WARNING_TYPE = 3;
    int CUSTOM_IMAGE_TYPE = 4;
    int PROGRESS_TYPE = 5;

    int LIST_LANDING = 1;
    int LIST_TAG_DOC = 2;

    int TAG_DOC_LEFT = 46;
    int TAG_DOC_RIGHT = 47;


    String AppVerison = "1.0";

    String FLAG = "Y";

    String Change_PWD = "13";
    String INVALID_INSTANCE = "4";
    String SUCESSS = "0";
    String AL_TRANSACTION_DELETE = "Do you want to abort this transaction and start over,All previously saved would be deleted";
    String AL_SRESP_NOK = "Server response not ok";
    String AL_CON_ESTB_FAIL = "Could not establish connection ";
    String AL_SNOT_REAC = "Server not reachable: ";
    String AL_INITIAL_SPC = "Initial Space not allowed";
    String Al_Error = "Error in transaction";
    String AL_IS_SPECIAL = "Special characters not allowed.";
    String AL_BLANK = "Please enter mandatory details.";
    String AL_SPACE = "Spaces not allowed.";
    String AL_INV_OLD_PWD = "Incorrect Old Password.";
    String AL_NUM = " should not contain number.";
    String AL_PWD_LEN = "Password length should be minimum 8 characters.";
    String AL_OLD_NEW_PWD = "Old & New Password cannot be same.";
    String AL_NO_ALPHA_NUM_PWD = "Password should be alphanumeric.";
    String AL_No_Per = "No permission";
    String AL_Inv_Version = "Invalid version,Perform Sync...";
    String AL_CONFIRM_PWD = "New Password and Confirm Password mismatch.";
    String AL_PWD_CNG = "Password changed.";
    String AL_Rec = "No Data";// ;"No records";
    String AL_INV_PASS = "Enter valid Password.";
    String AL_OLD_PASS_INVALID = "Old password is not correct";
    String AL_LAST_ATTEMPT = "Last attempt. Wrong password will lock your account.";
    String AL_EXIT = "Are you sure you want to exit?";
    String AL_INV_VERSION = "Invalid version.";
    String AL_REC = "Data not available!";
    String AL_PWDCONF = "It will take 40-50 minutes to configure.";
    String AL_CONF_SUC = "System configured.";
    String AL_CONF_FAIL = "Partial data downloaded! Do you want to try again?";
    String AL_TRAN_FAIL = "Error in transaction.";
    String AL_Inv_Pwd = "Invalid Credentials";
    String AL_ERROR = "Communication error";
    String AL_NO_PER = "No permission";
    String AL_INVALID_PWD = "Incorrect Password!";
    String AL_INV_CLNTID = "Invalid client id";
    String AL_INVALID_USERNAMEPWD = "Invalid Username/Password";
    String AL_SER_NOK = "Server response not ok";
    String AL_SER_NREC = "Server not reachable";
    String AL_TRANS_FAIL = "Transaction fail";
    String AL_NO_CON = "Could not establish connection.";
    String AL_NOT_FOUND = "Unable to load data";
    String AL_SYNC_SUC = "Sync successful";
    String AL_IR_FAIL = "Unable to load data!";
    String AL_UTD_FAIL = "Unable to  data!";
    String AL_Data = "No data";
    String AL_INVALID_INSID = " Your Application is configured already, in case of re-installation, Please contact Administrator ";
    String AL_HASHKEY = " Security Violation !!!! Please contact Application Administrator ";
    String AL_Download = "Please download data";
    String AL_InvalidRole = "Role Changed:Plz contact Adminstrator";

    String AL_NETERROR = "No Network.";

    String AL_PassWORDPOlicy = "Password Policy not set:Plz contact Adminstrator";
    String AL_ATTACH = "Only one attachment allowed";
    String al_photo = "Please take an image first";
    String al_delete_img = "Are you sure you want to delete the image";
    String INDEX_TYPE = "indexType";
    String AL_App_not_found = "No Application Found";
    String Al_NoAttach = "Sorry this file cannot be attached";
    String AL_MSG_LST_CHK = "Select atleast one recipent";
    String AL_MSD_MAND = "Please enter some text";
    String AL_TXN_DLT_FAILURE = "Transaction cannot be deleted due to network unavailability";
    String Spec_Parse_char_Not_allw = "Special char ( $   %   ^   ;   , )  not allowed";
    String EMAIL_SENT_MSG = "Email Request has been send, customer will receive email in next 10 mins.";
    String EMAIL_NOT_AVAILABLE = "Your E-mail ID is not available ";
    String COMPLETE_NA = "Please complete Need Analysis before proceeding";


}
package inc.gb.cp20.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * @author chandra.s
 */
public class Utility {


    /***********************************************************************
     * @ Purpose : This method will hide keyboard on a particular editText
     ***********************************************************************/
    public static void hideKeyboard(EditText et) {
        et.setFocusable(false);
        et.setFocusableInTouchMode(false);
        et.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });
    }


    /***********************************************************************
     * @ Purpose : This method checks whether entered character is special
     * character or not.
     ***********************************************************************/
    public static boolean isSpecial(String str, String pattern) {
        for (int i = 0; i < str.length(); i++) {
            for (int j = 0; j < pattern.length(); j++) {
                if (str.charAt(i) == pattern.charAt(j)) {
                    return true;
                }
            }
        }
        return false;

    }

    public static boolean isSpecialChars(String str, int asciiCode) {
        int ascii = 0;
        boolean isSpecial = false;
        if (asciiCode == -1) {
            for (int i = 0; i < str.length(); i++) {
                ascii = (int) str.toLowerCase(Locale.ENGLISH).charAt(i);
                if (!((ascii >= 97 && ascii <= 122)
                        || (ascii >= 48 && ascii <= 57) || (ascii == 32))) {
                    isSpecial = true;
                    if (isSpecial) {
                        return isSpecial;
                    }
                }
            }
        } else {
            for (int i = 0; i < str.length(); i++) {
                ascii = (int) str.toLowerCase(Locale.ENGLISH).charAt(i);
                if (!((ascii >= 97 && ascii <= 122)
                        || (ascii >= 48 && ascii <= 57) || (ascii == 32) || ascii == asciiCode)) {
                    isSpecial = true;
                    if (isSpecial) {
                        return isSpecial;
                    }
                }
            }
        }
        return isSpecial;
    }

    /***********************************************************************
     * @ Purpose : All method are date and time related methods.
     ***********************************************************************/
    public static final String getDate(Date date) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        cal.setTime(date);
        int int_year = cal.get(Calendar.YEAR);
        int int_month = cal.get(Calendar.MONTH) + 1;
        int int_date = cal.get(Calendar.DATE);
        Calendar calt = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        calt.setTime(new Date());
        int int_hh = calt.get(Calendar.HOUR_OF_DAY);
        int int_mm = calt.get(Calendar.MINUTE);
        String str_dt = (int_date < 10 ? "0" + int_date : "" + int_date) + "/"
                + (int_month < 10 ? "0" + int_month : "" + int_month) + "/"
                + int_year + " " + (int_hh < 10 ? "0" + int_hh : "" + int_hh)
                + ":" + (int_mm < 10 ? "0" + int_mm : "" + int_mm);
        return str_dt;
    }


    public static String getDateDiffString(Date dateOne, Date dateTwo) {
        long timeOne = dateOne.getTime();
        long timeTwo = dateTwo.getTime();
        long oneDay = 1000 * 60 * 60 * 24;
        long delta = (timeTwo - timeOne) / oneDay;

        if (delta > 0) {
            return "" + delta;
        } else {
            delta *= -1;
            return "" + delta;
        }
    }

    public static String timeconverter(String time) {
        String converttime = "";
        try {
            SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm",
                    Locale.ENGLISH);
            SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a",
                    Locale.ENGLISH);
            Date date = parseFormat.parse(time);
            converttime = displayFormat.format(date);

        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return converttime;
    }

    public static String dateconverter(String givendate, String reqformat,
                                       String gvnformat) {
        String strDate = "";
        try {
            // create SimpleDateFormat object with source string date format
            SimpleDateFormat sdfSource = new SimpleDateFormat(gvnformat,
                    Locale.ENGLISH);// "dd/MM/yy"
            // parse the string into Date object
            Date date = sdfSource.parse(givendate);

            // create SimpleDateFormat object with desired date format
            SimpleDateFormat sdfDestination = new SimpleDateFormat(reqformat,
                    Locale.ENGLISH);

            // parse the date into another format
            strDate = sdfDestination.format(date);

        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        return strDate;
    }


    /***********************************************************************
     * @ Purpose : These methods are used to show alerts and Toasts.
     ***********************************************************************/
    public static void displayToast(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showTempAlert(Context context, String message) {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
        alertbox.setMessage(message).setPositiveButton("OK",
                new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertbox.create();
        alertbox.show();
    }

    public static void showSweetAlert(Context context, String message, int ALERT_TYPE) {

        final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, ALERT_TYPE);
        sweetAlertDialog.setTitleText(message)
                .setConfirmText("Ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                .show();
    }

    public static void showSweetAlertofFnish(final Context context, String message, int ALERT_TYPE) {

        final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, ALERT_TYPE);
        sweetAlertDialog.setTitleText(message)
                .setConfirmText("Ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sweetAlertDialog.dismiss();
                        ((Activity) context).finish();
                    }
                })
                .show();
    }


    /***********************************************************************
     * @ Purpose : These methods are used to Download file and zip files.
     * @author shubham.y
     ***********************************************************************/
    // public static String downloadZipFile(String stUrl) {
    // String str = "fail";
    // try {
    // String strUrl = stUrl;
    // URL url = new URL(strUrl);
    // HttpURLConnection c = (HttpURLConnection) url.openConnection();
    // c.setRequestMethod("GET");
    // c.setDoOutput(true);
    // c.connect();
    //
    // String PATH = Environment.getExternalStorageDirectory() + "/temp/";

    // File file = new File(PATH);
    // file.mkdirs();
    // String fileName = strUrl.substring(strUrl.lastIndexOf("/") + 1);
    // File outputFile = new File(file, fileName);
    // FileOutputStream fos = new FileOutputStream(outputFile);
    // InputStream is = c.getInputStream();
    //
    // byte[] buffer = new byte[1024*1024];
    // int len1 = 0;
    // while ((len1 = is.read(buffer)) != -1) {
    // fos.write(buffer, 0, len1);
    // }
    // fos.close();
    // is.close();
    // str = PATH + fileName;
    // c.disconnect();
    // // }
    // } catch (IOException e) {

    // str+=CmsInter.CAP+e.toString();
    // }

    // return str;
    //
    // }
    public static String downloadZipFile(String stUrl) {
        String str = "fail";
        try {

            String strUrl = stUrl;
            URL url = new URL(strUrl);
            // HttpURLConnection c = (HttpURLConnection) url.openConnection();
            // c.setRequestMethod("GET");
            // c.setDoOutput(true);
            // c.connect();
            URLConnection conexion = url.openConnection();
            conexion.connect();
            int count;
            String PATH = Environment.getExternalStorageDirectory() + "/temp/";
            File file = new File(PATH);
            file.mkdirs();
            String fileName = strUrl.substring(strUrl.lastIndexOf("/") + 1);
            File outputFile = new File(file, fileName);
            // FileOutputStream fos = new FileOutputStream(outputFile);
            // InputStream is = c.getInputStream();

            int lenghtOfFile = conexion.getContentLength();
            Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(outputFile);

            // byte[] buffer = new byte[1024*1024];
            // int len1 = 0;
            // while ((len1 = is.read(buffer)) != -1) {
            // fos.write(buffer, 0, len1);
            // }
            // fos.close();
            // is.close();
            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
            str = PATH + fileName;
            // conexion.disconnect();
            // }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }


    public static String downloadFile(String path, Context context) {
        if (path.contains("\\")) {
            path = path.replace('\\', '/');
        }
        String file = "";
        try {
            URL url = new URL(path);
            HttpURLConnection httpConnection = (HttpURLConnection) url
                    .openConnection();
            httpConnection.setRequestMethod("GET");
            httpConnection.setDoOutput(true);
            httpConnection.connect();

            String fileName = path.substring(path.lastIndexOf("/") + 1);
            String filePath = ((Activity) context).getFilesDir() + "/"
                    + fileName;
            File outputFile = new File(filePath);
            FileOutputStream fos = new FileOutputStream(outputFile);
            InputStream is = httpConnection.getInputStream();

            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }
            fos.close();
            is.close();
            if (outputFile.exists()) {
                file = filePath;
            }

        } catch (IOException e) {
            Log.d("Exception in downloadZipFile---", "Error: " + e);
            file = "";
        }
        return file;
    }

    /***********************************************************************
     * @ Purpose : This method is used to unzip a zip file and store it in given
     * directory whether the zip has multiple zip inside one zip file.
     * @author shubham.y
     ***********************************************************************/
    public static String unZipFile(File ziparchive, String directory) {
        String str = "fail";
        String folder = "";
        try {


            ZipFile zipfile = new ZipFile(ziparchive);

            for (Enumeration<? extends ZipEntry> e = zipfile.entries(); e
                    .hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                String path = entry.getName();
                File file = new File(directory + path);
                if (file.exists()) {
                    recursiveDelete(file);

                }
                unzipEntry(zipfile, entry, directory);
                str = "success";
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        if (str.equalsIgnoreCase("success")) {
            folder = ziparchive.getAbsolutePath();
            // ziparchive.renameTo();
            File to = new File(ziparchive.getAbsolutePath()
                    + System.currentTimeMillis());
            ziparchive.renameTo(to);
            to.delete();
            // ziparchive.delete();
            folder = folder.substring(0, folder.lastIndexOf("/"));
            File f = new File(folder);
            // f.delete();l
        }
        return str;
    }


    public static void unzipEntry(ZipFile zipfile, ZipEntry entry,
                                  String outputDir) throws IOException {
        if (entry.isDirectory()) {
            new File(outputDir, entry.getName()).mkdirs();
            return;
        }
        File outputFile = new File(outputDir, entry.getName());
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }
        BufferedInputStream inputStream = new BufferedInputStream(
                zipfile.getInputStream(entry));
        BufferedOutputStream outputStream = new BufferedOutputStream(
                new FileOutputStream(outputFile));
        try {
            IOUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            System.out.println("IOUtils Exception in utility");
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
            outputStream.close();
            inputStream.close();
        }
    }

    /***********************************************************************
     * @ Purpose : This method is used to delete directory recursively.
     * @author sandeep.s
     ***********************************************************************/
    public static void recursiveDelete(File dir) {
        if (dir.isDirectory())
            for (File child : dir.listFiles())
                recursiveDelete(child);
        dir.delete();
    }

    @SuppressLint("NewApi")
    public static void showPdf(String pdfPath,
                               boolean isStartActivityForResult, int requestCode, Context context) {
        File file = new File(pdfPath);
        if (file.exists()) {
            try {
                file.setReadable(true, false);
                Uri path = Uri.fromFile(file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(path, "application/pdf");
                if (isStartActivityForResult) {
                    ((Activity) context).startActivityForResult(intent,
                            requestCode);
                } else {
                    context.startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utility.showTempAlert(context,
                        "No application found to view pdf.");
            }
        } else {
            Utility.showTempAlert(context, "File not found.");
        }
    }

    /***********************************************************************
     * @ Purpose : This method is used to set editText maximum character(size).
     * @author shubham.y
     ***********************************************************************/
    public static void setEdittextMaxSize(EditText editText, int length) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(length);
        editText.setFilters(FilterArray);
    }

    /***********************************************************************
     * @ Purpose : These method are used to validate email address.
     * @author shubham.y
     * @author chandra.s
     ***********************************************************************/
    public static boolean isEmail(String email) {
        if (!email.equalsIgnoreCase("")) {
            // String expression ="^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            String expression = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            CharSequence inputStr = email;
            Pattern pattern = Pattern.compile(expression,
                    Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(inputStr);
            if (matcher.matches()) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /***********************************************************************
     * @ Purpose : These method are used to validate fields.
     ***********************************************************************/
    public static boolean isEmpty(String str) {
        if (str.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isSpace(String str) {
        int ascii = 0;
        for (int i = 0; i < str.length(); i++) {
            ascii = (int) str.charAt(i);
            if (ascii == 32) {
                return true;
            }
        }
        return false;
    }

    public static boolean lengthCheck(String str, int length) {
        if (str.length() < length) {
            return true;
        }

        return false;
    }

    public static boolean rangeCheck(String str, int min, int max) {
        if (str.length() < min && str.length() > max) {
            return true;
        }

        return false;
    }

    public static boolean isZeroAtStart(String str) {
        int ascii = 0;
        if (str.length() != 0)
            ascii = (int) str.charAt(0);
        if (ascii == 48) {
            return true;
        }
        return false;
    }


    public static int Donullchk(String string_Data) {
        int int_Value;
        if (string_Data.equals("")) {
            int_Value = 0;
        } else {
            int_Value = Integer.parseInt(string_Data);
        }
        return int_Value;
    }

    public static boolean isNumeric(String str) {
        int ascii = 0;
        boolean isNumeric = false;
        for (int i = 0; i < str.length(); i++) {
            ascii = (int) str.charAt(i);
            if ((ascii >= 48 && ascii <= 57)) {
                isNumeric = true;
                if (isNumeric) {
                    return isNumeric;
                }
            }
        }
        return isNumeric;
    }

    public static boolean isInitSpace(String str) {
        if (str.startsWith(" ") && !str.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isMidSpace(String str) {
        int ascii = 0;
        for (int i = 0; i < str.length(); i++) {
            ascii = (int) str.charAt(i);
            if (ascii == 32) {
                return true;
            }
        }
        return false;
    }

    /***********************************************************************
     * @ Purpose : This method is used to generate unique number.
     ***********************************************************************/
    public static String getUniqueNo() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.ENGLISH).format(Calendar.getInstance().getTime());
        return timeStamp;
    }

    /***********************************************************************
     * @ Purpose : This method return the IPAdrees of the device.
     ***********************************************************************/
    public static String getIPAdress() {
        String IPaddress = "";
        InetAddress ownIP = null;
        try {
            ownIP = InetAddress.getLocalHost();
            IPaddress = ownIP.getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ownIP = null;
        }
        return IPaddress;
    }

    /***********************************************************************
     * @ Purpose : This Method returns IMEI number for GSM Device and MDN or
     * MEID for CDMA Device.
     ***********************************************************************/
    public static String getIMEINumber(Context context) {
        String IMEINumber = "";
        TelephonyManager telephonyManager;
        try {
            telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            IMEINumber = telephonyManager.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            telephonyManager = null;
        }
        return IMEINumber;
    }

    /***********************************************************************
     * @ Purpose : This Method returns unique android device Id.
     ***********************************************************************/
    public static String getDeviceId(Context context) {
        String deviceID = "";
        try {
            deviceID = Secure.getString(context.getContentResolver(),
                    Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceID;
    }

    /***********************************************************************
     * @ Purpose : This Method returns phone number / line number of android
     * device.
     ***********************************************************************/
    public static String getLineNumber(Context context) {
        String mobile_No = "";
        TelephonyManager telephonyManager = null;
        try {
            telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            mobile_No = telephonyManager.getLine1Number();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            telephonyManager = null;
        }
        return mobile_No;
    }

    /***********************************************************************
     * @ Purpose : This method is used to delete files from Internal memory.
     * @author shubham.y
     ***********************************************************************/
    public static void deleteInternalFiles(String filename, Context context) {
        File f = new File(context.getFilesDir() + "/" + filename);
        if (f.exists()) {
            f.delete();
        }
    }

    public static int getFieldData(String id, String[][] val) {
        // TODO Auto-generated method stub
        int pos = -1;
        for (int i = 0; i < val.length; i++) {
            if (id.equalsIgnoreCase(val[i][1])) {
                pos = i;
                return pos;
            }
        }
        return pos;
    }

	/*
     * public static int getageinyear(String enterDate){
	 * 
	 * Date dateOfBirth = Utility.getDateFromString(enterDate, CmsInter.FSLASH);
	 * Calendar dob = Calendar.getInstance(); dob.setTime(dateOfBirth); Calendar
	 * today = Calendar.getInstance(); int AGE = today.get(Calendar.YEAR) -
	 * dob.get(Calendar.YEAR); if (today.get(Calendar.MONTH) <
	 * dob.get(Calendar.MONTH)) { AGE--; }else if(today.get(Calendar.MONTH) ==
	 * dob .get(Calendar.MONTH) && today.get(Calendar.DAY_OF_MONTH) < dob
	 * .get(Calendar.DAY_OF_MONTH)) { AGE--; } return AGE; }
	 */


    /***********************************************************************
     * @ Purpose : This method is used to delete directory recursively.
     * @author shubham.y
     ***********************************************************************/
    public static void recursiveDeleteFilesFromFilesFolder(File dir) {

        if (dir.isDirectory())
            for (File child : dir.listFiles())
                recursiveDelete(child);
    }

    public static String currency(String s) {

        int j = s.length();

        int arrayLength = (int) Math.ceil(((s.length() / (double) 3)));
        String[] result = new String[arrayLength];
        String no = "";

        int firstIndex = 0;
        for (int i = result.length - 1; i > 0; i--) {
            result[i] = s.substring(j - 3, j);
            j -= 3;
        }
        result[firstIndex] = s.substring(0, j);

        for (int q = 0; q < result.length; q++) {
            if (q < result.length - 1) {
                no = no + result[q] + ",";
            } else if (q == result.length - 1) {
                no = no + result[q];
            }
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.#");

        return no;
    }

    public static String com_date(String date, String serverdate) {
        String datevalid = "";
        int[] abc = new int[3];
        int[] pqr = new int[3];
        SimpleDateFormat simp = new SimpleDateFormat("dd/MM/yyyy");
        // Date Sysdate = new Date();
        String Currentdate = serverdate;
        /*
         * Currentdate = Currentdate.substring(0, 10); Currentdate =
		 * Currentdate.replace('-', '/'); Currentdate =
		 * Utility.dateconverter(Currentdate, "dd/MM/yyyy", "MM/dd/yyyy");
		 */
        Calendar cal = Calendar.getInstance();
        Calendar cal3 = Calendar.getInstance();
        Calendar cal4 = Calendar.getInstance();
        Calendar cal5 = Calendar.getInstance();
        Calendar cal6 = Calendar.getInstance();

        String[] com_date = date.split("/");
        String[] sys_date = Currentdate.split("/");
        for (int i = 0; i < 3; i++) {
            abc[i] = Integer.parseInt(com_date[i]);
            pqr[i] = Integer.parseInt((sys_date[i]));
        }
        cal3.set(abc[2], abc[1] - 1, abc[0]);
        cal6.set(pqr[2], pqr[1] - 1, pqr[0]);
        cal.set(pqr[2], Calendar.APRIL, 1);
        cal4.set(pqr[2], Calendar.DECEMBER, 31);
        cal5.set(pqr[2], Calendar.OCTOBER, 15);
        if (cal6.after(cal) && cal6.before(cal4)) {
            if (cal3.before(cal)) {
                datevalid = "Date of commencement cannot be before 1st April "
                        + cal6.get(Calendar.YEAR);

            }
        } else {
            cal4.set(pqr[2] - 1, Calendar.DECEMBER, 31);
            if (cal6.after(cal4)) {
                cal5.set(pqr[2] - 1, Calendar.APRIL, 1);
                if (cal6.before(cal)) {
                    if (cal3.before(cal5)) {

                        datevalid = "Date of commencement cannot be before 1st April "
                                + (pqr[2] - 1);

                    }
                }
            }

        }

		/*
         * if (cal6.after(cal5) && (cal6.before(cal4) || cal6.equals(cal4))) {
		 * 
		 * if (cal3.before(cal5)) {
		 * 
		 * datevalid = "Date of commencement cannot be before 15th October " +
		 * cal6.get(Calendar.YEAR);
		 * 
		 * } } else { cal4.set(pqr[2] - 1, Calendar.DECEMBER, 31); if
		 * (cal6.after(cal4)) { cal5.set(pqr[2] - 1, Calendar.OCTOBER, 15); if
		 * (cal6.before(cal)) { if (cal3.before(cal5)) {
		 * 
		 * datevalid = "Date of commencement cannot be before 15th October " +
		 * (pqr[2] - 1);
		 * 
		 * } } }
		 * 
		 * }
		 */
        return datevalid;
    }

    public static String[] backdate(String DOC, String Currentdate,
                                    int frequency, int MP) {
        double backdating = 0.0;
        String[] com_date = DOC.split("/");
        int freq;
        int[] pqr = new int[3];
        int[] abc = new int[3];
        double[] dates = new double[12];
        int[] months = new int[12];
        double[] back_interest = new double[12];
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        int count = 0;

        String[] sys_date = Currentdate.split("/");
        for (int i = 0; i < 3; i++) {
            abc[i] = Integer.parseInt(sys_date[i]);
            pqr[i] = Integer.parseInt(com_date[i]);
        }
        cal1.set(pqr[2], pqr[1] - 1, pqr[0]);
        cal2.set(abc[2], abc[1] - 1, abc[0]);

        dates[0] = daysBetween(cal1.getTime(), cal2.getTime());

        if (daysBetween(cal1.getTime(), cal2.getTime()) < 31) {
            if (dates[0] > 0) {
                count++;
                dates[0] = 0;
            }
        }

        if (dates[0] > 0) {
            count++;
        }

        months[0] = (int) Math.round(dates[0] / 31.0);

        back_interest[0] = Math.round(roundFourDecimals((Math.pow(1.0075,
                months[0])) - 1) * MP);

        int pay = 0;
        if (frequency == 12) {
            freq = 30;
            pay = 1;
        } else if (frequency == 4) {
            freq = 90;
            pay = 3;
        } else if (frequency == 2) {
            freq = 180;
            pay = 6;
        } else {
            freq = 360;
            pay = 12;
        }

        for (int i = 1; i < 12; i++) {
            dates[i] = Math.max(dates[i - 1] - freq, 0);

            months[i] = Math.max(months[i - 1] - pay, 0);
            back_interest[i] = (roundFourDecimals(Math.pow(1.0075, months[i])) - 1)
                    * MP;
            back_interest[i] = Math.round(back_interest[i]);
            back_interest[i] = Math.ceil(back_interest[i]);
            // }
            if (dates[i] > 0) {

                count++;
            }
        }

        for (int i = 0; i < 12; i++) {

            backdating = backdating + back_interest[i];

        }

        int init_prem;
        long service_tax;
        init_prem = count * MP;
        service_tax = Math.round(init_prem * 0.035);
        // + Math.round(init_prem * 0.0009);

        double[] backdate = {init_prem, service_tax, backdating,
                (service_tax + backdating + init_prem)};
        String[] back_policy = new String[4];

        for (int i = 0; i < 4; i++) {
            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            back_policy[i] = String.valueOf(decimalFormat.format(backdate[i]));
        }

        return back_policy;
    }

    public static int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

    public static double roundFourDecimals(double d) {
        DecimalFormat fourDForm = new DecimalFormat("#.####");
        return Double.valueOf(fourDForm.format(d));
    }

    public static final String getDatewithoutTime(Date date) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        cal.setTime(date);
        int int_year = cal.get(Calendar.YEAR);
        int int_month = cal.get(Calendar.MONTH) + 1;
        int int_date = cal.get(Calendar.DATE);
        Calendar calt = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        calt.setTime(new Date());

        String str_dt = (int_date < 10 ? "0" + int_date : "" + int_date) + "/"
                + (int_month < 10 ? "0" + int_month : "" + int_month) + "/"
                + int_year;
        return str_dt;
    }

    public static byte[] readFully(InputStream stream) throws IOException {
        byte[] buffer = new byte[8192];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int bytesRead;
        while ((bytesRead = stream.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }
        return baos.toByteArray();
    }

    public static double roundOff(double d, int a) {
        double b;
        String hash = "#.";

        for (int i = 0; i < a; i++) {
            hash = hash + "#";
        }
        DecimalFormat DForm = new DecimalFormat(hash);
        b = Double.valueOf(DForm.format(d));

        return b;
    }

    public static double rounding(double d) {

        String value = "" + d;

        if ((value).contains(".49999999")) {

            d = Math.round(Utility.roundOff(d, 1));
        } else if (value.contains("499999996E7")) {

            d = Math.round(Utility.roundOff(d, 1));

        } else {

            d = Math.round(d);

        }

        return d;
    }

    public static double calpmtForMonthly(double amount, int period,
                                          double percent) {
        double abc = 0;
        double pqr = 0;

        period = period * 12;
        percent = percent / 12;

        for (int i = 0; i < period; i++) {
            abc = abc + Math.pow((1 + percent), i);
            pqr = (amount / abc);

        }
        return pqr;
    }

    public static int diffInDates(String DOC, String DOB) {

        String[] com_date = DOC.split("/");
        String[] birth_date = DOB.split("/");

        int[] date1 = new int[3];
        int[] date2 = new int[3];

        for (int i = 0; i < 3; i++) {
            date1[i] = Integer.parseInt(birth_date[i]);
            date2[i] = Integer.parseInt(com_date[i]);
        }

        // date1[0]:day,date[1]:month,date1[2]:year
        int years = 0, months = 0, days = 0;

        years = date2[2] - date1[2];
        months = date2[1] - date1[1];
        days = date2[0] - date1[0];

        if (months < 0) {

            years = years - 1;
        }

        if (months == 0 && days < 0) {

            years = years - 1;
        }
        return years;

    }

    /***********************************************************************
     * @ Purpose : This method will allow alphabets, numbers and given specific
     * special character in given string Caution - please insure that if allow
     * character contains hyphen(-) then it should be first character example-
     * "-&()_{};",.="
     ***********************************************************************/
    public static boolean validateSpecial(String str, String allowcahr) {
        boolean isvalidspe = true;

        Pattern pattern = Pattern.compile("^([" + allowcahr + "a-zA-Z0-9 ])*");
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()) {
            isvalidspe = true;
        } else {
            isvalidspe = false;
        }
        return isvalidspe;
    }

    public static boolean validateOnlyNotallowedSpecial(String str,
                                                        String allowcahr) {

        boolean isvalidspe = true;

        Pattern pattern = Pattern.compile("[" + allowcahr + "]");
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()) {
            isvalidspe = false;
        } else {
            isvalidspe = true;
        }
        return isvalidspe;
    }

    public static int getLastImageId(Context context) {
        final String[] imageColumns = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA};
        final String imageOrderBy = MediaStore.Images.Media._ID + " DESC";
        Cursor imageCursor = null;
        int id = 0;
        try {
            imageCursor = context.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageColumns,
                    null, null, imageOrderBy);
            if (imageCursor.moveToFirst()) {
                id = imageCursor.getInt(imageCursor
                        .getColumnIndex(MediaStore.Images.Media._ID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            imageCursor.close();
        }
        return id;
    }

    public static void removeImage(int id, Context context) {
        ContentResolver cr = context.getContentResolver();
        cr.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Images.Media._ID + "=?",
                new String[]{Long.toString(id)});
    }

    public static boolean validateMobileNo(String mobNo) {
        boolean isInvalidmobNo = true;
        if (mobNo.equals("")) {
            isInvalidmobNo = false;
        } else {
            if (mobNo.startsWith("9") || mobNo.startsWith("8")
                    || mobNo.startsWith("7")) {
                isInvalidmobNo = false;
            } else {
                isInvalidmobNo = true;
            }
        }

        return isInvalidmobNo;
    }

    public static void populate_li_name(final Object[] name_field) {

        ((EditText) name_field[0]).addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

                ((EditText) name_field[2]).setText(((EditText) name_field[0])
                        .getText().toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }
        });

        ((EditText) name_field[1]).addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

                ((EditText) name_field[3]).setText(((EditText) name_field[1])
                        .getText().toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }
        });

    }

    public static boolean validateStartwithspcl(String str, String allowdspcl) {
        boolean isInvalidmobNo = true;
        if (str.equals("")) {
            isInvalidmobNo = true;
        } else {

            for (int j = 0; j < allowdspcl.length(); j++) {
                if (str.charAt(0) == allowdspcl.charAt(j)) {
                    isInvalidmobNo = false;

                }
            }

        }

        return isInvalidmobNo;
    }

    public static double calculate_sa(int ppt, double premium) {
        double sum_assured = 0;
        if (ppt == 1) {
            sum_assured = 2 * premium;

        } else if (ppt == 5) {
            sum_assured = 10 * premium;
        } else {
            sum_assured = 20 * premium;
        }
        return sum_assured;
    }

    public static double service_tax(double premium, double taxRate) {
        double tax1 = 0;

        tax1 = Math.round(premium * taxRate);
        // + Math.round(premium * 0.0009);
        return tax1;
    }

    // public static double premium_with_service_tax1(double premium) {
    // double tax2 = 0;
    // tax2 = premium + Math.round(premium * 0.035);
    // //+ Math.round(premium * 0.00045);
    // return tax2;
    // }
    //
    // public static double premium_with_service_tax2(double premium) {
    // double tax2 = 0;
    // tax2 = premium + Math.round(premium * 0.0175);
    // //+ Math.round(premium * 0.00045);
    // return tax2;
    // }

    public static void calculate_age(String date0, String date1, EditText age) {

        if (!date1.equals("")) {
            int val = Utility.diffInDates(date0, date1);
            age.setText(String.valueOf(val));

        } else {
            age.setText("");
        }
    }


    public static void DeleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                DeleteRecursive(child);

        fileOrDirectory.delete();
    }

    public static void generateNoteOnSD(String sFileName, String sBody) {
        try {
            String sCurrentLine = "";
            File root = new File(Environment.getExternalStorageDirectory(),
                    "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            if (gpxfile.exists()) {

                BufferedReader br = new BufferedReader(new FileReader(gpxfile));

                while ((sCurrentLine = br.readLine()) != null) {
                    sCurrentLine += sCurrentLine + "\n";
                    System.out.println(sCurrentLine);
                }
            } else {
                sCurrentLine = "URL------------------------------------Timt to download(sec)-------Unzip Sts\n";
            }
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sCurrentLine + sBody);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
            // importError = e.getMessage();
            // iError();
        }
    }


    /**
     * The methods isUpperCase(char ch) and isLowerCase(char ch) of the
     * Character class are static so we use the Class.method() format; the
     * charAt(int index) method of the String class is an instance method, so
     * the instance, which, in this case, is the variable `input`, needs to be
     * used to call the method.
     **/
    public static String counterforupperandlower(String Lable, String input,
                                                 int minUpper, int minLower, int mindigit, int minspclchar) {

        String c = "hjdg$h&jk8^i0ssh6";
        Pattern pt = Pattern.compile("[^a-zA-Z0-9]");
        Matcher match = pt.matcher(c);
        while (match.find()) {
            String s = match.group();
            c = c.replaceAll("\\" + s, "");
        }
        System.out.println(c);

        String Msg = "";
        int upperCase = 0, lowerCase = 0, digit = 0, spcl = 0;
        for (int k = 0; k < input.length(); k++) {

            // Check for uppercase letters.
            if (Character.isUpperCase(input.charAt(k)))
                upperCase++;

            // Check for lowercase letters.
            if (Character.isLowerCase(input.charAt(k)))
                lowerCase++;

            if (Character.isDigit(input.charAt(k)))
                digit++;
        }

        if (!(upperCase >= minUpper)) {
            Msg = Lable + " must contain minmum " + minUpper
                    + " uppercase Character";
        } else if (!(lowerCase >= minLower)) {

            Msg = Lable + " must contain minmum " + minLower
                    + " lowercase Character";
        } else if (!(digit >= mindigit)) {
            Msg = Lable + " must contain minmum " + mindigit + " digit";

        } else if (!(minspclchar <= isSpecialCharsCount(input))) {
            Msg = Lable + " must contain minmum " + mindigit
                    + " Special Characters";
        }

        return Msg;

    }

    public static int isSpecialCharsCount(String input) {
        int count = 0;
        Pattern pt = Pattern.compile("[^a-zA-Z0-9]");
        Matcher match = pt.matcher(input);
        while (match.find()) {
            String s = match.group();
            count++;
        }

        return count;
    }

    public static String getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return formatSize(totalBlocks * blockSize);
    }

    public static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null)
            resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

    public static long dirSize(File dir, boolean MB) {

        if (dir.exists()) {
            long result = 0;
            File[] fileList = dir.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // Recursive call if it's a directory
                if (fileList[i].isDirectory()) {
                    result += dirSize(fileList[i], true);
                } else {
                    // Sum the file size in bytes
                    result += fileList[i].length();
                }
            }

            if (MB) {
                result = result / (1000 * 1000);
            }
            return result; // return the file size
        }
        return 0;
    }


}
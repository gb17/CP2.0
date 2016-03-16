package inc.gb.cp20.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import inc.gb.cp20.Models.TBBRAND;
import inc.gb.cp20.Models.TBDBL;
import inc.gb.cp20.Models.TBDCP;
import inc.gb.cp20.Models.TBDMENU;
import inc.gb.cp20.Models.TBDPG;
import inc.gb.cp20.Models.TBDPS;
import inc.gb.cp20.Models.TBDSP;
import inc.gb.cp20.Models.TBDTH;
import inc.gb.cp20.Models.TBPARTY;
import inc.gb.cp20.Models.TBTBC;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;


public class DBHandler extends SQLiteOpenHelper {

    Context context;
    private static DBHandler DBHnadlerInstance = null;
    private static final String DATABASE_NAME = "CWEPV8";
    private static final int DATABASE_VERSION = 2;
    static SQLiteDatabase db;
    public static String TBUPW = "TBUPW";
    public static String TBCVR = "TBCVR";
    public static String TBBRAND = "TBBRAND";

    private DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

        db = this.getWritableDatabase();

    }

    static {
        // register our models
        cupboard().register(TBDCP.class);
        cupboard().register(TBDTH.class);
        cupboard().register(TBDPG.class);
        cupboard().register(TBDBL.class);
        cupboard().register(TBDSP.class);
        cupboard().register(TBTBC.class);
        cupboard().register(TBPARTY.class);
        cupboard().register(TBDPS.class);
        cupboard().register(TBBRAND.class);
        cupboard().register(TBDMENU.class);


    }

    public static DBHandler getInstance(Context ctx) {

        if (DBHnadlerInstance == null) {
            DBHnadlerInstance = new DBHandler(ctx);

        }
        return DBHnadlerInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < newVersion) {
        }
    }

    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        // if(dbExist){
        // //do nothing - database already exist
        // }else{

        // By calling this method and empty database will be created into the
        // default system path
        // of your application so we are gonna be able to overwrite that
        // database with our database.
        this.getReadableDatabase();

        try {

            copyDataBase();

        } catch (IOException e) {

            throw new Error("Error copying database");

        }
        // }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each
     * time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = context.getDatabasePath(DATABASE_NAME) + "";

            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {

            // database does't exist yet.

        }

        if (checkDB != null) {

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    private void copyDataBase() throws IOException {

        // Open your local db as the input stream
        InputStream myInput = context.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = context.getDatabasePath(DATABASE_NAME) + "";

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    // create all required tables

    public void createTables() {

        SQLiteDatabase db = this.getWritableDatabase();

        try {

            db.execSQL("CREATE TABLE " + TBCVR
                    + "( COL0 TEXT,COL1 TEXT,COL2 TEXT,COL3 TEXT)");

            db.execSQL("CREATE TABLE " + TBUPW
                    + "( USERNAME TEXT,VERSION TEXT,OLDPWD TEXT,VAL TEXT,CLIENTID TEXT,CONTROLNO TEXT)");
            cupboard().withDatabase(db).createTables();
            db.execSQL("CREATE TABLE IF NOT EXISTS TBNAME (COL0 text, COL1 text, COL2 text, COL3 text, COL4 text, COL5 text,COL6 text, COL7 text, COL8 text, COL9 text, COL10 text, COL11 text, COL12 text, COL13 text, COL14 text, COL15 text, COL16 text)");
            db.execSQL("CREATE TABLE IF NOT EXISTS TBPHTAG (COL0 text, COL1 text, COL2 text, COL3 text, COL4 text, COL5 text)");
            //db.execSQL("DELETE FROM TBNAME");
            db.execSQL("CREATE TABLE IF NOT EXISTS TXN102 (COL0 text, COL1 text, COL2 text, COL3 text, COL4 text, COL5 text,COL6 text, COL7 text, COL8 text, COL9 text, COL10 text, COL11 text, COL12 text, COL13 text, COL14 text, COL15 text, COL16 text, COL17 text, COL18 text, COL19 text, COL20 text)");


        } catch (Exception exp) {
            System.out.println("Error in table creation " + exp);
            exp.printStackTrace();
        } finally {
            // db.close();
            close();
        }
    }

    // create all required tables
    public boolean executscript() {
        long starttime = System.currentTimeMillis();

        boolean TAG_CHECK = true;
        int count = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        File dirFiles = context.getFilesDir();
        for (String strFile : dirFiles.list()) {
            if (strFile.endsWith(".sql")) {
                try {
                    count = DbUtils.executeSqlScript(context, db, strFile,
                            true, false);
                    if (count > 0) {

                        String path = context.getFilesDir() + "/" + strFile;
                        File file = new File(path);
                        file.delete();
                        TAG_CHECK = true;
                    }

                } catch (IOException e) {
                    TAG_CHECK = false;
                }
            }
        }

        return TAG_CHECK;

    }

    public void tp() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            DbUtils.executeSqlScript(context, db, "kb.sql", true, false);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String[][] genericSelect(String query, int noCols) {
        String strData[][] = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = null;
        try {
            cur = db.rawQuery(query, new String[]{});
            int noOfRows = cur.getCount();
            int count = 0;
            if (noOfRows > 0) {
                strData = new String[noOfRows][noCols];
                while (cur.moveToNext()) {
                    for (int i = 0; i < noCols; i++) {
                        strData[count][i] = cur.getString(i);
                    }
                    count++;
                }
            }
        } catch (Exception e) {
            System.out.println("In getallData block------->" + e);
        } finally {
            if (cur != null && !cur.isClosed())
                cur.close();
            // db.close();
            close();
        }
        return strData;
    }

    public boolean genricDelete(String Tabel_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Tabel_name, null, null) > 0;
    }


    public Cursor getAllData(String TabelName, String groupBy, String Where, String[] whereArgs) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = null;
        return db.query(TabelName, null, Where, whereArgs, groupBy, null, null);

    }

    public Cursor getCusrsor(String SQl) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery(SQl, new String[]{});

    }


    public int check_control_table() {
        String TABLE = "";
        int a = 0;
        String[][] tablesname = genericSelect(
                "SELECT TAG FROM TBTBC", 1);

        for (int i = 0; i < tablesname.length; i++) {

            TABLE = "TB" + tablesname[i][0];
            try {
                int cnt = 0;
                String countQuery = "SELECT * FROM " + TABLE;
                SQLiteDatabase db = this.getReadableDatabase();
                try {
                    Cursor cursor = db.rawQuery(countQuery, null);
                    cnt = cursor.getCount();//MOB_COUNT
                    cursor.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    cnt = 0;
                }
                if (cnt > 0) {
                    String newTable = TABLE.substring(2, TABLE.length());
                    String countQuery1 = "SELECT REC_COUNT FROM TBTBC Where TAG='"
                            + newTable + "'";
                    Cursor cursor1 = db.rawQuery(countQuery1, null);
                    cursor1.moveToNext();
                    int old = Integer.parseInt(cursor1.getString(cursor1.getColumnIndex("REC_COUNT")));// REC_COUNT
                    int diff = old - cnt;
                    String query = "UPDATE TBTBC set MOB_COUNT = '"
                            + diff + "' Where TAG='"
                            + newTable + "'";
                    db.execSQL(query);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            TABLE = "";
        }

        return 11;
    }

    public String hardcodequery(String query) {

        String strReturn = "success";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL(query);
        } catch (Exception e) {
            strReturn = "error";
            e.printStackTrace();
        }

        return strReturn;
    }

    public String insertDATA(String tablename, String data[]) {

        String strReturn = "success";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();

            for (int i = 0; i < data.length; i++) {

                cv.put("COL" + i, data[i]);
            }

            long rowId = db.insert(tablename, null, cv);
            if (rowId == -1) {
                strReturn = "error";
            }

        } catch (Exception exp) {
            strReturn = "error";
            exp.printStackTrace();
        } finally {

            // db.close();
            close();
        }
        return strReturn;

    }

    public String UpdateSystable(HashMap<String, String> hasmap,
                                 String lastsyncDate, String LASTLOGIN, String PWDCHNGdate,
                                 String CPFFLAG, String pwdchnageFirsttime) {

        String strReturn = "success";
        SQLiteDatabase db = this.getWritableDatabase();

        //	db.execSQL("DELETE FROM  " + CmsInter.TBSYS000);

        try {
            ContentValues cv = new ContentValues();
            cv.put("COL0", hasmap.get("ClientID"));
            cv.put("COL1", hasmap.get("Username"));
            cv.put("COL2", hasmap.get("Password"));
            cv.put("COL3", hasmap.get("INSTANCEID"));
            cv.put("COL4", lastsyncDate);
            cv.put("COL5", LASTLOGIN);
            // COL6 is auto increment
            cv.put("COL7", hasmap.get("LastPasswordChangeDate"));
            cv.put("COL8", CPFFLAG);
            cv.put("COL9", "");
            cv.put("COL10", pwdchnageFirsttime);
            cv.put("COL11", "");
            cv.put("COL12", "");
            // 	long rowId = db.insert(CmsInter.TBSYS000, null, cv);
//			if (rowId == -1) {
//				strReturn = "error";
//			}

        } catch (Exception exp) {
            strReturn = "error";
            exp.printStackTrace();
        } finally {

            // db.close();
            close();
        }
        return strReturn;

    }

    public String[][] genericSelect(String select, String tableName,
                                    String where, String groupBy, String having, int noCols) {
        String strData[][] = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + select + " FROM " + tableName;
        if (!where.equals("")) {
            query = query + " WHERE " + where;
        }
        if (!groupBy.equals("")) {
            query = query + " GROUP BY " + groupBy;
        }
        if (!having.equals("")) {
            query = query + " HAVING " + having;
        }
        Cursor cur = null;
        try {
            cur = db.rawQuery(query, new String[]{});
            int noOfRows = cur.getCount();
            int count = 0;
            if (noOfRows > 0) {
                strData = new String[noOfRows][noCols];
                while (cur.moveToNext()) {
                    for (int i = 0; i < noCols; i++) {
                        strData[count][i] = cur.getString(i);
                    }
                    count++;
                }
            }
        } catch (Exception e) {
            System.out.println("In getallData block------->" + e);
        } finally {
            if (cur != null && !cur.isClosed())
                cur.close();
            // db.close();
            close();
        }

        return strData;
    }

    public Cursor getAllBooks(String tableName) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT *  FROM " + tableName;

        Cursor cur = null;
        try {
            cur = db.rawQuery(query, new String[]{});
            int noOfRows = cur.getCount();
            if (noOfRows > 0) {
            }
            cur.moveToFirst();
        } catch (Exception e) {
            System.out.println("In getallData block------->" + e);
        } finally {
            // if (cur != null && !cur.isClosed())
            // cur.close();
            // db.close();
            // close();
        }
        return cur;

    }

    public String[][] getTagData(String tag) {
        String strData[][] = null;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = null;
        try {
            cur = db.rawQuery("SELECT * FROM " + tag + " order by COL1", null);
            int noOfRows = cur.getCount();
            int noOfColoums = cur.getColumnCount();
            int k = 0;
            if (noOfRows > 0) {
                strData = new String[noOfRows][noOfColoums];
                while (cur.moveToNext()) {
                    for (int i = 0; i < noOfColoums; i++)
                        strData[k][i] = cur.getString(i).toString();
                    k++;
                }
            } else {
                strData = new String[1][1];
                strData[0][0] = "nodata";
            }

        } catch (Exception e) {
            strData = new String[1][1];
            strData[0][0] = "nodata";
        } finally {
            if (cur != null && !cur.isClosed())
                cur.close();
            // db.close();
        }
        return strData;
    }

}

package inc.gb.cp20.AlphaList;

import android.content.Context;
import android.database.Cursor;

import java.util.Collections;
import java.util.Vector;

import inc.gb.cp20.DB.DBHandler;

public class UserService {

    public static Vector<DrList_POJO> getUserList(Context ctx, String TabelName, String Where) {
        Vector<DrList_POJO> drListPOJOList = new Vector<DrList_POJO>();

        DrList_POJO drListPOJO;

        DBHandler dbHandler = DBHandler.getInstance(ctx);


        // Get all sampleData from db
        try {
            Cursor cursor = null;
            if (Where.equals("")) {
                cursor = dbHandler.getAllData(TabelName, null, null, null);
            } else {
                cursor = dbHandler.getCusrsor("SELECT *  FROM TBPARTY WHERE COL1 like  '%" + Where + "%'");
            }

            cursor.moveToFirst();

            if (cursor.getCount() != 0) {
                drListPOJOList = new Vector<DrList_POJO>();
                do {
                    drListPOJO = new DrList_POJO();
                    drListPOJO.setCOL0(cursor.getString(cursor.getColumnIndex("COL0")));
                    drListPOJO.setCOL1(cursor.getString(cursor.getColumnIndex("COL1")));
                    drListPOJO.setCOL2(cursor.getString(cursor.getColumnIndex("COL2")));
                    drListPOJO.setCOL3(cursor.getString(cursor.getColumnIndex("COL3")));
                    drListPOJO.setCOL4(cursor.getString(cursor.getColumnIndex("COL4")));
                    drListPOJO.setCOL5(cursor.getString(cursor.getColumnIndex("COL5")));
                    drListPOJO.setCOL6(cursor.getString(cursor.getColumnIndex("COL6")));
                    drListPOJO.setCOL7(cursor.getString(cursor.getColumnIndex("COL7")));
                    drListPOJO.setCOL8(cursor.getString(cursor.getColumnIndex("COL8")));
                    drListPOJO.setCOL9(cursor.getString(cursor.getColumnIndex("COL9")));
                    drListPOJO.setCOL10(cursor.getString(cursor.getColumnIndex("COL10")));
                    drListPOJO.setCOL11(cursor.getString(cursor.getColumnIndex("COL11")));
                    drListPOJO.setCOL12(cursor.getString(cursor.getColumnIndex("COL12")));
                    drListPOJO.setCOL13(cursor.getString(cursor.getColumnIndex("COL13")));
                    drListPOJO.setCOL14(cursor.getString(cursor.getColumnIndex("COL14")));
                    drListPOJO.setCOL15(cursor.getString(cursor.getColumnIndex("COL15")));
                    drListPOJO.setCOL16(cursor.getString(cursor.getColumnIndex("COL16")));
                    drListPOJO.setCOL17(cursor.getString(cursor.getColumnIndex("COL17")));
                    drListPOJOList.add(drListPOJO);
                } while (cursor.moveToNext());
            }

            // deactivate and closing cursor
            cursor.deactivate();
            cursor.close();


            Collections.sort(drListPOJOList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return drListPOJOList;
    }

}

package inc.gb.cp20.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Database utils, for example to execute SQL scripts
 */
public class DbUtils {

    static String script = "";

    public static void vacuum(SQLiteDatabase db) {
        db.execSQL("VACUUM");
    }

    /**
     * Calls {@link #executeSqlScript(Context, SQLiteDatabase, String, boolean)}
     * with transactional set to true.
     *
     * @return number of statements executed.
     */
    public static int executeSqlScript(Context context, SQLiteDatabase db,
                                       String assetFilename, boolean fromassest) throws IOException {
        return executeSqlScript(context, db, assetFilename, fromassest);
    }

    /**
     * Executes the given SQL asset in the given database (SQL file should be
     * UTF-8). The database file may contain multiple SQL statements. Statements
     * are split using a simple regular expression (something like
     * "semicolon before a line break"), not by analyzing the SQL syntax. This
     * will work for many SQL files, but check yours.
     *
     * @return number of statements executed.
     */
    public static int executeSqlScript(Context context, SQLiteDatabase db,
                                       String assetFilename, boolean transactional, boolean fromassest)
            throws IOException {
        byte[] bytes = readAsset(context, assetFilename, fromassest);
        String sql = new String(bytes, "UTF-8");
        String[] lines = sql.split(";");

        int count;
        if (transactional) {
            count = executeSqlStatementsInTx(db, lines);
        } else {
            count = executeSqlStatements(db, lines);
        }

        Log.e("error", "Executed " + count + " statements from SQL script '"
                + assetFilename + "'");
        return count;
    }

    public static int executeSqlStatementsInTx(SQLiteDatabase db,
                                               String[] statements) {
        db.beginTransaction();
        try {
            int count = executeSqlStatements(db, statements);
            db.setTransactionSuccessful();
            return count;
        } finally {
            db.endTransaction();
        }
    }

    public static int executeSqlStatements(SQLiteDatabase db,
                                           String[] statements) {
        int count = 0;

        for (String line : statements) {
            line = line.trim();

            if (line.length() > 0) {
                try {
                    db.execSQL(line);


                } catch (Exception e) {
                    System.out.print("CHK--->" + line + " EXC" + e.toString());
                    e.printStackTrace();
                }

                count++;

            }
        }

        return count;
    }

    /**
     * Copies all available data from in to out without closing any stream.
     *
     * @return number of bytes copied
     */
    public static int copyAllBytes(InputStream in, OutputStream out)
            throws IOException {
        int byteCount = 0;
        byte[] buffer = new byte[4096];
        while (true) {
            int read = in.read(buffer);
            if (read == -1) {
                break;
            }
            out.write(buffer, 0, read);
            byteCount += read;
        }
        return byteCount;
    }

    public static byte[] readAllBytes(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        copyAllBytes(in, out);
        return out.toByteArray();
    }

    public static byte[] readAsset(Context context, String filename,
                                   boolean fromAssest) throws IOException {
        InputStream in;

        if (fromAssest)
            in = context.getResources().getAssets().open(filename);
        else
            in = new FileInputStream(context.getFilesDir() + "/" + filename);
        try {
            return readAllBytes(in);
        } finally {
            in.close();
        }
    }

    public static void logTableDump(SQLiteDatabase db, String tablename) {
        Cursor cursor = db.query(tablename, null, null, null, null, null, null);
        try {
            String dump = DatabaseUtils.dumpCursorToString(cursor);
            Log.e("error", dump);
        } finally {
            cursor.close();
        }
    }
}

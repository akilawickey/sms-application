package asak.pro.sms_application.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import asak.pro.sms_application.services.models.DatabaseConstants;
import asak.pro.sms_application.services.models.Receiver_details;
import asak.pro.sms_application.services.models.Response_yes_no;
import asak.pro.sms_application.services.models.ServiceConstants;

/**
 * Created by maanadev on 1/23/16.
 */
public class DatabaseService implements DatabaseConstants, ServiceConstants {
    private static String TAG = "DB";

    //===================================================================================================================================
    //==========================================LOCAL VARIABLES=============================================================
    //===================================================================================================================================

    private Context DatabaeServiceContext;
    private SQLLiteHelper sqlLiteHelper;


    //===================================================================================================================================
    //===================================================================================================================================
    //===================================================================================================================================


    public DatabaseService(Context c) {

        DatabaeServiceContext = c;
    }
    //**********************************************************************************************************************************
    //**********************************************************************************************************************************
    //**********************************************************************************************************************************


    //===================================================================================================================================
    //===================================ASSIGN CONTEXT=======================================================================
    //===================================================================================================================================

    private SQLiteDatabase openConnection() {

        sqlLiteHelper = new SQLLiteHelper(DatabaeServiceContext);
        return sqlLiteHelper.getWritableDatabase();

    }
    //===================================================================================================================================
    //===================================================================================================================================
    //===================================================================================================================================


    //===================================================================================================================================
    //===================================OPEN CONNECTION TO DATABASE=======================================================================
    //===================================================================================================================================

    private void closeConnection() {
        sqlLiteHelper.close();
    }
    //===================================================================================================================================
    //===================================================================================================================================
    //===================================================================================================================================


    //===================================================================================================================================
    //===============================CLEAR RESOURCES=============================================================================
    //===================================================================================================================================

    //===================================================================================================================================
    //==============================================DROP TABLES=============================================================
    //===================================================================================================================================
    public void dropTable(String table) {
        SQLiteDatabase db = openConnection();
        db.execSQL("DROP TABLE IF EXISTS " + table);
        closeConnection();
    }

    //===================================================================================================================================
    //===================================================================================================================================
    //===================================================================================================================================


    //**************************************************************************************************************************************
    //**************************************************************************************************************************************
    //**************************************************************************************************************************************
    //**************************************************************************************************************************************
    //**************************************************************************************************************************************
    //**************************************************************************************************************************************

    public void insertPhoneNumbers(String msgId, ArrayList<String> numbers) {

        SQLiteDatabase db = openConnection();

        String table = (msgId + PHONE_NUMBER_TABLE_NAME);
        //CREATE A TABLE

        String sql = "CREATE TABLE IF NOT EXISTS " + table + "(" + PHONE_NUMBER_KEY_NUMBER + " VARCHAR(255) PRIMARY KEY )";
        db.execSQL(sql);

        ContentValues cv = new ContentValues();
        //PUT VALUES IN TO THE TABLE
        for (String number : numbers) {

            cv.put(PHONE_NUMBER_KEY_NUMBER, number);
            db.insert(table, null, cv);
        }
        closeConnection();
    }
    //===================================================================================================================================
    //===================================================================================================================================
    //===================================================================================================================================


    //===================================================================================================================================
    //==========================================INSERT PHONE NUMBERS========================================================================
    //===================================================================================================================================

    //===================================================================================================================================
    //========================================INSERT RECEIVERS==================================================================
    //===================================================================================================================================
    public void insertReceiver(String msgId, String keyword, String type) {

        SQLiteDatabase db = openConnection();

        ContentValues cv = new ContentValues();

        cv.put(RECEIVERS_KEY_MSGID, msgId);
        cv.put(RECEIVERS_KEY_KEYWORD, keyword);
        cv.put(RECEIVERS_KEY_RESPONSE_TYPE, type);

        db.insert(RECEIVERS_TABLE_NAME, null, cv);

        closeConnection();

    }

    //===================================================================================================================================
    //===================================================================================================================================
    //===================================================================================================================================

    //===================================================================================================================================
    //==================================INITIATE RESPONSE TABLE=================================================================
    //===================================================================================================================================
    public void initiateYesNoResponseTable(String msgId) {
        SQLiteDatabase db = openConnection();

        ContentValues cv = new ContentValues();

        cv.put(RESPONSE_KEY_MSGID, msgId);
        cv.put(RESPONSE_KEY_YES, 0);
        cv.put(RESPONSE_KEY_NO, 0);

        db.insert(RESPONSE_TABLE_YES_NO_NAME, null, cv);
        closeConnection();

    }


    //===================================================================================================================================
    //===================================================================================================================================
    //===================================================================================================================================

    //===================================================================================================================================
    //=====================================UPDATE RESPONSE TABLE==================================================================
    //===================================================================================================================================
    public void updateResponse(String msgid, String choice, String type) {

        SQLiteDatabase db = openConnection();

        if (type.equals(TYPE_YES_NO)) {

            if (choice.equals(RESPONSE_YES)) {

                String sql = "UPDATE " + RESPONSE_TABLE_YES_NO_NAME + " SET " + RESPONSE_KEY_YES + " = " + RESPONSE_KEY_YES + " + 1 WHERE " + RESPONSE_KEY_MSGID + "= '" + msgid + "'";

                db.execSQL(sql);

            }

            if (choice.equals(RESPONSE_NO)) {
                String sql = "UPDATE " + RESPONSE_TABLE_YES_NO_NAME + " SET " + RESPONSE_KEY_NO + " = " + RESPONSE_KEY_NO + "+1 WHERE " + RESPONSE_KEY_MSGID + "= '" + msgid + "'";
                db.execSQL(sql);
            }
        }

        closeConnection();

    }


    //===================================================================================================================================
    //===================================================================================================================================
    //===================================================================================================================================

    public boolean findNumber(String msgId, String phnNumber, boolean clearResources) {

        SQLiteDatabase db = openConnection();
        //COLUMNS TO GET
        String columns[] = {PHONE_NUMBER_KEY_NUMBER};
        //CREATING TABLE NAME USING msgId
        String table = (msgId + PHONE_NUMBER_TABLE_NAME);
        Cursor c = db.query(table, columns, PHONE_NUMBER_KEY_NUMBER + " = ?", new String[]{phnNumber}, null, null, null);


        if (c.getCount() != 0) {
            //CLEARING THE TABLE ENTRY SO SEARCHING IS MORE FASTER
            if (clearResources) {

                db.delete(table, PHONE_NUMBER_KEY_NUMBER + "= ?", new String[]{phnNumber});
            }
            closeConnection();
            return true;
        } else {
            closeConnection();
            return false;
        }
    }


    //===================================================================================================================================
    //===================================================================================================================================
    //===================================================================================================================================


    //===================================================================================================================================
    //==============================================LOOK FOR PHONE NUMBER==========================================================
    //===================================================================================================================================

    //===================================================================================================================================
    //====================================GET MESSAGE ID AND RESPONSE TYPE===============================================================
    //===================================================================================================================================
    public Receiver_details getMsgId(String keyWord) {
        SQLiteDatabase db = openConnection();
        String[] colounms = {RECEIVERS_KEY_MSGID, RECEIVERS_KEY_RESPONSE_TYPE};
        Cursor c = db.query(RECEIVERS_TABLE_NAME, colounms, RECEIVERS_KEY_KEYWORD + " = ?", new String[]{keyWord}, null, null, null);

        if (c.getCount() != 0) {

            int iMsgId = c.getColumnIndex(RECEIVERS_KEY_MSGID);
            int iType = c.getColumnIndex(RECEIVERS_KEY_RESPONSE_TYPE);

            c.moveToFirst();

            Receiver_details receiver_details = new Receiver_details(c.getString(iType), c.getString(iMsgId));

            closeConnection();
            return receiver_details;

        } else {
            closeConnection();
            Log.i("smsreceiver", "No such Keyword!!");
            return null;
        }

    }


    //===================================================================================================================================
    //===================================================================================================================================
    //===================================================================================================================================

    //===================================================================================================================================
    //=========================================GET COUNT=================================================
    //===================================================================================================================================
    public long getPhoneNumberCount(String msgId) {

        SQLiteDatabase db = openConnection();
        String table = msgId + PHONE_NUMBER_TABLE_NAME;
        //CHECK WHETHER TABLE EXISTS
        Cursor cursor = db.rawQuery("select * from sqlite_master where tbl_name = '" + table + "'", null);

        long count = 0;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();

                Log.i(TAG, "exist");
                count = DatabaseUtils.queryNumEntries(db, table);

            } else {
                Log.i(TAG, "Not exist");
                cursor.close();
            }
        }

        closeConnection();


        return count;
    }


    //===================================================================================================================================
    //===================================================================================================================================
    //===================================================================================================================================

    //===================================================================================================================================
    //=======================================GET RESPONSE COUNT OF YES AND NO ===========================================================
    //===================================================================================================================================
    public Response_yes_no getCountYesNo(String msgid) {

        SQLiteDatabase db = openConnection();

        Cursor c = db.query(RESPONSE_TABLE_YES_NO_NAME, new String[]{RESPONSE_KEY_YES, RESPONSE_KEY_NO}, RESPONSE_KEY_MSGID + "= ?", new String[]{msgid}, null, null, null);
        int iYes = c.getColumnIndex(RESPONSE_KEY_YES);
        int iNo = c.getColumnIndex(RESPONSE_KEY_NO);


        if (c.getCount() != 0) {

            c.moveToFirst();
            Response_yes_no response_yes_no = new Response_yes_no();
            response_yes_no.setNo(c.getInt(iNo));
            response_yes_no.setYes(c.getInt(iYes));
            closeConnection();

            return response_yes_no;

        } else {
            closeConnection();
            Response_yes_no response_yes_no = new Response_yes_no();
            response_yes_no.setNo(0);
            response_yes_no.setYes(0);
            return null;
        }


    }


    //===================================================================================================================================
    //===================================================================================================================================
    //===================================================================================================================================

    //***********************************************************************************************************************************
    //************************************SQLLiteHelper INNER CLASS***********************************************************************
    //***********************************************************************************************************************************
    private class SQLLiteHelper extends SQLiteOpenHelper {


        public SQLLiteHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //CREATING TABLE

            String sql_1 = "CREATE TABLE IF NOT EXISTS " + RECEIVERS_TABLE_NAME +
                    "(" + RECEIVERS_KEY_MSGID + " VARCHAR(255) NOT NULL ,"
                    + RECEIVERS_KEY_KEYWORD + " VARCHAR(255) PRIMARY KEY,"
                    + RECEIVERS_KEY_RESPONSE_TYPE + " VARCHAR(255) NOT NULL)";

            String sql_2 = "CREATE TABLE IF NOT EXISTS " + RESPONSE_TABLE_YES_NO_NAME +
                    "(" + RESPONSE_KEY_MSGID + " VARCHAR(255) PRIMARY KEY,"
                    + RESPONSE_KEY_YES + " INTEGER NOT NULL,"
                    + RESPONSE_KEY_NO + " INTEGER NOT NULL)";

            db.execSQL(sql_1);
            db.execSQL(sql_2);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
        }
    }

    //===================================================================================================================================
    //===================================================================================================================================
    //===================================================================================================================================


    //===================================================================================================================================
    //==============================================================================================
    //===================================================================================================================================


    //===================================================================================================================================
    //===================================================================================================================================
    //===================================================================================================================================

}

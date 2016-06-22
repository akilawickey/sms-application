package asak.pro.sms_application.services;

/**
 * Created by maanadev on 1/25/16.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import asak.pro.sms_application.services.models.Receiver_details;
import asak.pro.sms_application.services.models.ServiceConstants;


public class SmsReceiver extends BroadcastReceiver implements ServiceConstants {

    private static String TAG = "smsreceiver";

    private DatabaseService db;


    public SmsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //GET THE INTENT ACTION NAME
//        String INTENT_NAME = intent.getAction();
        //INITIATING DATABASE SERVICE
        Log.i(TAG, "Message arrived !");
        db = new DatabaseService(context);


        Bundle bundle = intent.getExtras();

        SmsMessage[] msgs = null;

        String str = "";

        if (bundle != null) {
            // Retrieve the SMS Messages received
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];

            // For every SMS message received
            for (int i = 0; i < msgs.length; i++) {
                // Convert Object array
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

                //GET MSG BODY
                String MSG = msgs[i].getMessageBody().toString();


                //GET THE KEYWORD
                String keyword = MSG.substring(0, 4);
                Log.i(TAG, "Keyword: " + keyword);
                //GET MESSAGE ID AND RESPONSE TYPE
                Receiver_details msgDetail = getMsgId(keyword);

                if (msgDetail != null) {
                    Log.i(TAG, "MsgId : " + msgDetail.getMsgId());
                    Log.i(TAG, "Response Type: " + msgDetail.getResponseType());

                    //GET SENDER'S NUMBER
                    String phnNumber = msgs[i].getOriginatingAddress();
                    Log.i(TAG, "Phone Number: " + phnNumber);
//                        Toast.makeText(context,String.valueOf(isNumberValid(msgDetail.getMsgId(),"+94712018761")),
//                                Toast.LENGTH_LONG).show();

                    if (isNumberValid(msgDetail.getMsgId(), phnNumber)) {
                        Log.i(TAG, "Number is Valid!!");
                        //GET THE USER'S CHOICE
                        String choice = MSG.toString().substring(5, 6);
                        Log.i(TAG, "Choice: " + choice);
                        //save response by ending msgId choice(ex:0 or 1) and the type of the receiver
                        saveResponse(msgDetail.getMsgId(), choice, msgDetail.getResponseType());
//
//                        Toast.makeText(context,String.valueOf(db.getCountYesNo(msgDetail.getMsgId()).getYes()),
//                                Toast.LENGTH_LONG).show();
                        Log.i(TAG, "No Yeses: " + db.getCountYesNo(msgDetail.getMsgId()).getYes());
                        Toast.makeText(context, "No Yeses: " + db.getCountYesNo(msgDetail.getMsgId()).getYes(),
                                Toast.LENGTH_LONG).show();
                    } else {
                        Log.i(TAG, "Number is not Valid!!");
                    }

                } else {
                    Log.i(TAG, "Keyword is not Valid!!");
                }


            }
        }
    }

    private void saveResponse(String msgId, String choice, String responseType) {

        db.updateResponse(msgId, choice, responseType);
    }

    private boolean isNumberValid(String msgId, String number) {
        return db.findNumber(msgId, number, false);
    }

    private Receiver_details getMsgId(String keyword) {
        Receiver_details receiver_details = db.getMsgId(keyword);
        return receiver_details;

    }


}

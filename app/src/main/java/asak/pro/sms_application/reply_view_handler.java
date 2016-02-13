package asak.pro.sms_application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.logging.Logger;

import asak.pro.sms_application.services.DatabaseService;
import asak.pro.sms_application.services.models.Receiver_details;
import asak.pro.sms_application.services.models.Response_yes_no;
import asak.pro.sms_application.services.models.ServiceConstants;

/**
 * Created by root on 2/2/16.
 */
public class reply_view_handler extends AppCompatActivity implements ServiceConstants {

    private static String TAG = "ado";
    private DatabaseService db;
    private EditText et;
    private TextView tx;
    private Button bt;





    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from new_activity.xml
        setContentView(R.layout.reply_view);


        db = new DatabaseService(this);


        Log.i(TAG, "error is .............--------: ");


        bt = (Button) findViewById(R.id.search);
        tx = (TextView) findViewById(R.id.textView2);
        et = (EditText) findViewById(R.id.editText);


        String msg = et.getText().toString();


        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Response_yes_no x = db.getCountYesNo("3779").getYes();
                //2127
//                long x = db.getPhoneNumberCount("2127");
//                tx.append("hello user"+x);
//                Response_yes_no a = new Response_yes_no();
//                a = db.getCountYesNo("2127");
//
//                Toast.makeText(savedInstanceState, "No Yeses: " + db.getCountYesNo("2127").getYes(),
//                        Toast.LENGTH_LONG).show();
                //      int a = db.getCountYesNo("2127").getYes();
                //int x = a.getNo();
                //   tx.append("num - "+x);

            }
        });


    }


}

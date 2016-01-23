package asak.pro.sms_application;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.tuenti.smsradar.Sms;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private Button send;
    private Button downbtn;
    private EditText phoneNo;
    private EditText messageBody;
    private CheckBox ch1;
    private CheckBox ch2;

    ListView listview;
    int numset[];
    static int selection_dept1,selection_dept2 = 0;
    // this is for database download part
    private static final String DEBUG_TAG = "HttpExample";
    ArrayList<Team> teams = new ArrayList<Team>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        downbtn = (Button)findViewById(R.id.down);
       // listview = (ListView) findViewById(R.id.listView);
        //this is for database part
        CheckBox ch1 = (CheckBox)findViewById(R.id.one);
        CheckBox ch2 = (CheckBox)findViewById(R.id.two);

        ch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(getApplicationContext(), "select dep1",
                            Toast.LENGTH_LONG).show();
                    selection_dept1 = 1;
                }else {
                    Toast.makeText(getApplicationContext(), "deselect dept1",
                            Toast.LENGTH_LONG).show();
                    selection_dept1 = 0;
                }
            }
        });
        ch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(getApplicationContext(), "select dep2",
                            Toast.LENGTH_LONG).show();
                    selection_dept2 = 2;
                } else {
                    Toast.makeText(getApplicationContext(), "deselect dept2",
                            Toast.LENGTH_LONG).show();
                    selection_dept2 = 0;
                }
            }
        });


        try {
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                downbtn.setEnabled(true);
            } else {
                downbtn.setEnabled(false);
            }
        }catch (Exception e){


        }


        downbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadWebpageTask(new AsyncResult() {
                    @Override
                    public void onResult(JSONObject object) {
                        processJson(object);
                    }
                }).execute("https://spreadsheets.google.com/tq?key=16QYE-NrOgCcHMlD_o1EDBTrrGrGelYm7Lw5405ZxuFw");
                Toast.makeText(getApplicationContext(), "DB ok",
                        Toast.LENGTH_SHORT).show();
            }
        });



    }


    private void processJson(JSONObject object) {
        String sms = messageBody.getText().toString();
        try {
            JSONArray rows = object.getJSONArray("rows");

            for (int r = 0; r < rows.length(); ++r) {
                JSONObject row = rows.getJSONObject(r);
                JSONArray columns = row.getJSONArray("c");

                //int position = columns.getJSONObject(0).getInt("v");
                int k = columns.getJSONObject(1).getInt("v");
//                int wins = columns.getJSONObject(3).getInt("v");
//                int draws = columns.getJSONObject(4).getInt("v");
//                int losses = columns.getJSONObject(5).getInt("v");
//                int points = columns.getJSONObject(19).getInt("v");
                Toast.makeText(getApplicationContext(),k + " Sending ...",
                        Toast.LENGTH_SHORT).show();
              //  Team team = new Team(position, name, wins, draws, losses, points);
             //   teams.add(team);
/*

                    send the sms message using sms manager
*/
                    SmsManager smsManager = SmsManager.getDefault();

                    ArrayList<String> parts = smsManager.divideMessage(sms);
                    smsManager.sendMultipartTextMessage(String.valueOf(k), null, parts, null, null);
                    // for(int i = 0;i<=10;i++){

//                    }

                    Toast.makeText(getApplicationContext(), "SMS Sent!",
                            Toast.LENGTH_LONG).show();

            }


            //final TeamsAdapter adapter = new TeamsAdapter(this, R.layout.team, teams);
          //  listview.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private void showSmsToast(Sms sms) {
        Toast.makeText(this, sms.toString(), Toast.LENGTH_LONG).show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

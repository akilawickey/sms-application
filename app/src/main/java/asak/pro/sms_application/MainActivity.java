package asak.pro.sms_application;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button startService;
    private Button stopService;
    private Button shareIntent;
    private Button send;
    private Button downbtn;
    private EditText phoneNo;
    private EditText messageBody;
    private CheckBox ch1;
    private CheckBox ch2;
    static int dept1_select = 0,dept2_select = 0;
    static String s = null;
    static String sms = null;
    ListView listview;
    int numset[];
    DataBaseHandler db;
    Time today = new Time(Time.getCurrentTimezone());

    // this is for database download part
    private static final String DEBUG_TAG = "HttpExample";
    ArrayList<Team> teams = new ArrayList<Team>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        downbtn = (Button) findViewById(R.id.down);
        messageBody = (EditText)findViewById(R.id.smsBody);
        // listview = (ListView) findViewById(R.id.listView);
        //this is for database part
        CheckBox ch1 = (CheckBox)findViewById(R.id.one);
        CheckBox ch2 = (CheckBox)findViewById(R.id.two);

        db = new DataBaseHandler(this); //create a new database handler object
        List<Contact> contacts = db.getAllContacts();
        for (Contact cn : contacts) {
            String log = "ID:" + cn.getID() + " Name: " + cn.getName();
            // Writing Contacts to log
            //Log.d("Result: ", log);
            System.out.println(log);
            // add contacts data in arrayList

        }


        ch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    dept1_select = 1;
                    Toast.makeText(getApplicationContext(), "Dept1 selected!",
                            Toast.LENGTH_LONG).show();
                } else {
                    dept1_select = 0;
                    Toast.makeText(getApplicationContext(), "Dept1 deselected!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        ch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    dept2_select = 1;
                    Toast.makeText(getApplicationContext(), "Dept2 selected!",
                            Toast.LENGTH_LONG).show();
                } else {
                    dept2_select = 0;
                    Toast.makeText(getApplicationContext(), "Dept2 deselected!",
                            Toast.LENGTH_LONG).show();
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
        } catch (Exception e) {


        }

        downbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(dept1_select == 0 && dept2_select == 0){

                    new DownloadWebpageTask(new AsyncResult() {
                        @Override
                        public void onResult(JSONObject object) {
                            processJson(object);
                        }
                    }).execute("https://spreadsheets.google.com/tq?&key=16QYE-NrOgCcHMlD_o1EDBTrrGrGelYm7Lw5405ZxuFw");
                    Toast.makeText(getApplicationContext(), "DB Connection Established",
                            Toast.LENGTH_SHORT).show();


                }else {

                        if ((dept1_select == 1)&&(dept2_select == 1)){
                            s = "%20D%20=%20%22dept2%22%20OR%20D%20=%20%22dept1";

                        }else if((dept1_select == 1)&&(dept2_select == 0)){
                            s = "%20D%20=%20%22dept1";
                        }else if((dept1_select == 0)&&(dept2_select == 1)){
                            s = "%20D%20=%20%22dept2";
                        }
                    //https://spreadsheets.google.com/tq?tq=select%20A%20where %20D%20=%20%22dept2%22%20&key=16QYE-NrOgCcHMlD_o1EDBTrrGrGelYm7Lw5405ZxuFw
                    String pass = "https://spreadsheets.google.com/tq?tq=select%20B%20where"+s+"%22%20&key=16QYE-NrOgCcHMlD_o1EDBTrrGrGelYm7Lw5405ZxuFw";
                    new DownloadWebpageTask(new AsyncResult() {
                        @Override
                        public void onResult(JSONObject object) {
                            processJson(object);
                        }
                    }).execute(pass);
                    Toast.makeText(getApplicationContext(), "DB Connection Established",
                            Toast.LENGTH_SHORT).show();


                }


            }
        });

    }

    private void processJson(JSONObject object) {

        try {

            JSONArray rows = object.getJSONArray("rows");

            for (int r = 0; r < rows.length(); ++r) {
                JSONObject row = rows.getJSONObject(r);
                JSONArray columns = row.getJSONArray("c");

                //int position = columns.getJSONObject(0).getInt("v");
                int k = columns.getJSONObject(0).getInt("v");
//                int wins = columns.getJSONObject(3).getInt("v");
//                int draws = columns.getJSONObject(4).getInt("v");
//                int losses = columns.getJSONObject(5).getInt("v");
//                int points = columns.getJSONObject(19).getInt("v");
                Toast.makeText(getApplicationContext(), k + " sending...",
                        Toast.LENGTH_SHORT).show();
                //  Team team = new Team(position, name, wins, draws, losses, points);
                //   teams.add(team);

                today.setToNow();
                String date = today.format("%Y-%m-%d %H:%M:%S");

                int rand = (int)(Math.random() * (100 - 10) + 10);

                //   System.out.println(date.toString().substring(17, 19)+rand);

                String rand_final = date.toString().substring(17, 19)+rand;

                db.addContact(new Contact(date + "\n" +rand_final));

                SmsManager smsManager = SmsManager.getDefault();
                sms = messageBody.getText().toString();

                sms = sms + " \n Send your reply as "+rand_final +"<space>option";
                ArrayList<String> parts = smsManager.divideMessage(sms);
                smsManager.sendMultipartTextMessage(String.valueOf(k), null, parts, null, null);


                Toast.makeText(getApplicationContext(), "SMS Sent",
                        Toast.LENGTH_LONG).show();

            }


            //final TeamsAdapter adapter = new TeamsAdapter(this, R.layout.team, teams);
            //  listview.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
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
        // as you specify a parent activity in AndroidManifest.xml.a
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
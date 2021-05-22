package com.example.efm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;

public class FoodInsert extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String[] courses = { "Select Possible Food Contamination", "Aluminium",
            "Plastic", "Deep freeze" };
    private ProgressDialog progressDialog;
    String ServerURL = "https://learnfriendly.000webhostapp.com/rohan/FoodInsert.php";
    String food=null;
    Button button;
    EditText quantity,expiry;
    RadioButton genderradioButton;
    RadioGroup radioGroup;
    String num=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_insert);
        Intent in = getIntent();
        num = in.getStringExtra("id");
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        quantity = findViewById(R.id.quantity);
        expiry = findViewById(R.id.expiry);
        Spinner spino = findViewById(R.id.coursesspinner);
        spino.setOnItemSelectedListener(this);

        // Create the instance of ArrayAdapter
        // having the list of courses
        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                courses);

        // set simple layout resource file
        // for each item of spinner
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        spino.setAdapter(ad);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        food = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

        //Toast.makeText(FoodInsert.this, "Please select the possible food Contamination", LENGTH_LONG).show();
    }
    public void onclickbuttonMethod(View view) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        genderradioButton = (RadioButton) findViewById(selectedId);
        if(radioGroup.getCheckedRadioButtonId() == -1){
            Toast.makeText(FoodInsert.this,"Select Veg or Non-Veg", LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(quantity.getText().toString())){
            quantity.requestFocus();
            quantity.setError("Please Mention the quantity");
        }
        else if(TextUtils.isEmpty(expiry.getText().toString())){
            expiry.requestFocus();
            expiry.setError("Please Mention the expiry days");
        }
        else if(food.equalsIgnoreCase("Select Possible Food Contamination")){
            Toast.makeText(this, "Please Select Possible Food Contamination", LENGTH_LONG).show();
        }
        else{
            //Toast.makeText(FoodInsert.this,genderradioButton.getText(), LENGTH_SHORT).show();
            //Toast.makeText(this, ""+food, LENGTH_SHORT).show();
            if(!haveNetwork())
                Toast.makeText(FoodInsert.this, "Please check your Internet connection...", Toast.LENGTH_SHORT).show();
            else{
                Toast.makeText(FoodInsert.this, "Connected...", Toast.LENGTH_SHORT).show();
                InsertData(genderradioButton.getText().toString(),quantity.getText().toString(),expiry.getText().toString(),
                      food.toString(),num.toString());
            }
        }
    }
    private boolean haveNetwork(){
        boolean have_WIFI= false;
        boolean have_MobileData = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        for(NetworkInfo info:networkInfos){
            if (info.getTypeName().equalsIgnoreCase("WIFI"))if (info.isConnected())have_WIFI=true;
            if (info.getTypeName().equalsIgnoreCase("MOBILE DATA"))if (info.isConnected())have_MobileData=true;
        }
        return have_WIFI||have_MobileData;
    }
    public void InsertData(final String name, final String number, final String lnumber, final String pass, final String address){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String NameHolder = name ;
                String Number = number ;
                String lnumberH = lnumber;
                String passH = pass;
                String addressH = address;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("name", NameHolder));
                nameValuePairs.add(new BasicNameValuePair("number", Number));
                nameValuePairs.add(new BasicNameValuePair("lnumber",lnumberH ));
                nameValuePairs.add(new BasicNameValuePair("pass",passH ));
                nameValuePairs.add(new BasicNameValuePair("address",addressH ));
                try {

                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(ServerURL);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();


                } catch (ClientProtocolException e) {

                } catch (Exception e) {

                }
                return "Data Inserted Successfully";
            }

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                progressDialog = ProgressDialog.show(FoodInsert.this, "Just a minute","Please wait....", true);
            }
            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);
                progressDialog.dismiss();
                Toast.makeText(FoodInsert.this, "Donated...You can view them in View my Donations", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(FoodInsert.this, AfterRestLogin.class);
                intent.putExtra("id",num.toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(name, number, lnumber, pass, address);
    }
}
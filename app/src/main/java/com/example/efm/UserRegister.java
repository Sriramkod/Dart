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
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserRegister extends AppCompatActivity {
    Button reg;
    String ServerURL = "https://learnfriendly.000webhostapp.com/rohan/ureg.php";
    String ServerURL_T = "https://learnfriendly.000webhostapp.com/rohan/UserUsers.php";
    EditText et1,et2,et3,et4,et5,et6,et7;private ProgressDialog progressDialog;
    String[] restnum = new String[100];
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        et1 =  findViewById(R.id.name);
        et2 = findViewById(R.id.aadhar);
        et3 =  findViewById(R.id.phone);
        et4= findViewById(R.id.psw);
        et5=findViewById(R.id.cpsw);
        et6=findViewById(R.id.address);
        et7=findViewById(R.id.email);
        reg = findViewById(R.id.reg);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(et1.getText().toString())){
                    et1.requestFocus();
                    Toast.makeText(UserRegister.this, "User name cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(et2.getText().toString())){
                    et2.requestFocus();
                    Toast.makeText(UserRegister.this, "Aadhar number cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(et3.getText().toString())){
                    et3.requestFocus();
                    Toast.makeText(UserRegister.this, "Phone number cannot be empty", Toast.LENGTH_SHORT).show();
                }

                else if(TextUtils.isEmpty(et4.getText().toString())){
                    et4.requestFocus();
                    Toast.makeText(UserRegister.this, "password cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(et5.getText().toString())){
                    et5.requestFocus();
                    Toast.makeText(UserRegister.this, "password cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(et6.getText().toString())){
                    et6.requestFocus();
                    Toast.makeText(UserRegister.this, "Address cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(et7.getText().toString())){
                    et7.requestFocus();
                    Toast.makeText(UserRegister.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(!et4.getText().toString().equals(et5.getText().toString()))
                {
                    et4.requestFocus();et5.requestFocus();
                    Toast.makeText(UserRegister.this, "The passwords did not match", Toast.LENGTH_LONG).show();
                }
                else if(et2.getText().toString().length()!=12){
                    et2.requestFocus();
                    Toast.makeText(UserRegister.this, "Invalid Aadhar number", Toast.LENGTH_LONG).show();
                }
                else if(et3.getText().toString().length()!=10){
                    et3.requestFocus();
                    et3.setError("Invalid Phone Number");
                }
                else if(!isValidPassword(et4.getText().toString()))
                {
                    et4.setError("You password should be of size >= 8 ,should contain special chracters, upper and lower case letter");
                }
                else{
                    if(!haveNetwork())
                        Toast.makeText(UserRegister.this, "Please check your Internet connection...", Toast.LENGTH_SHORT).show();
                    else{
                        //Toast.makeText(UserRegister.this, "Connected...", Toast.LENGTH_SHORT).show();
                        /*InsertData(et1.getText().toString(),et2.getText().toString(),et3.getText().toString(),
                                et4.getText().toString(),et6.getText().toString(),et7.getText().toString());*/
                        getJSON(ServerURL_T,"sri","ram");
                    }
                }
            }
        });
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
    public void InsertData(final String name, final String number, final String lnumber, final String pass, final String address,final String email){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String NameHolder = name ;
                String Number = number ;
                String lnumberH = lnumber;
                String passH = pass;
                String addressH = address;
                String emailH = email;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("name", NameHolder));
                nameValuePairs.add(new BasicNameValuePair("aadhar", Number));
                nameValuePairs.add(new BasicNameValuePair("number",lnumberH ));
                nameValuePairs.add(new BasicNameValuePair("pass",passH ));
                nameValuePairs.add(new BasicNameValuePair("address",addressH ));
                nameValuePairs.add(new BasicNameValuePair("email",emailH ));
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
                progressDialog = ProgressDialog.show(UserRegister.this, "Just a minute","Please wait....", true);
            }
            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);
                progressDialog.dismiss();
                Toast.makeText(UserRegister.this, "Registration Successfull...Please Login", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(UserRegister.this, UserLogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(name, number, lnumber, pass, address,email);
    }
    private void getJSON(final String urlWebService,final String email,final String password) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(UserRegister.this, "Checking....","Please Wait....", true);
            }


            @Override
            protected void onPostExecute(String s) {

                super.onPostExecute(s);
                progressDialog.dismiss();
                try{
                    int k = loadView(s);
                    if(k==1){
                        et3.setError("This number is already registered..please try to login");
                    }
                    else {
                        InsertData(et1.getText().toString(), et2.getText().toString(), et3.getText().toString(),
                                et4.getText().toString(), et6.getText().toString(), et7.getText().toString());
                    }
                    } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

//start
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    OutputStream outputStream = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String post_data = URLEncoder.encode("email","UTF-8")+"="+ URLEncoder.encode(email,"UTF-8")+"&"
                            +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
//end
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();

    }
    private int loadView(String json) throws JSONException {

        JSONArray jsonArray = new JSONArray(json);
        restnum = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            restnum[i] = obj.getString("number");

        }
        for(int i=0;i<restnum.length;i++){

            if(restnum[i].equals(et3.getText().toString())) {
                return 1;
            }

        }
        return 0;
    }
    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
}
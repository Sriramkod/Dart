package com.example.efm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
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

public class ViewRest extends AppCompatActivity {

String iddd=null;
TextView restname;
    String[] k = {"Onion","Pulses","Red_Chilli","Rice","Wheat","Turmeric","Groundnut"};
    String ServerURLI = "https://learnfriendly.000webhostapp.com/rohan/UROrderInsert.php";
    LinearLayout container;
    String[] webchrz = new String[100];
    String[] webchrz1 = new String[100];
    String[] webchrz2 = new String[100];
    String[] restnum = new String[100];

    ListView listView;
String user_phone=null;
    String phone=null;
    private ProgressDialog progressDialog;
    String ServerURL = "https://learnfriendly.000webhostapp.com/rohan/RestDisplay.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_rest);
        Intent in = getIntent();
        restname = findViewById(R.id.rname);
        restname.setText(""+in.getStringExtra("name"));
        iddd = in.getStringExtra("iddd");
        user_phone = in.getStringExtra("user_phone");
        container = (LinearLayout) findViewById(R.id.container);
        getJSON(ServerURL,"sri","ram");
       //
        // Toast.makeText(this, ""+iddd.toString(), Toast.LENGTH_SHORT).show();
    }
    public void addItems(String a,final String t, final String q,final String nn) {

        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.rest_row, null);

        final TextView vv = (TextView) addView.findViewById(R.id.place);
        final CardView cv = (CardView)addView.findViewById(R.id.cardv);
        final Button edit = (Button)addView.findViewById(R.id.edit);
        final Button del = (Button)addView.findViewById(R.id.delete);
        final RatingBar ratingBar = (RatingBar)addView.findViewById(R.id.ratingBar);
        vv.setText(""+a.toString());
        ratingBar.setRating((float) 3.5);

        edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewRest.this);
                  builder.setTitle("Restaurant Name : "+restname.getText().toString());
                builder.setMessage("Food Type :"+t.toString()+"\nQuantity :"+q.toString()+" Kg\nContact number : "+nn.toString())
                        .setPositiveButton("Order now", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int kk) {
                              //  Toast.makeText(ViewRest.this, "Please check your connection...", Toast.LENGTH_SHORT).show();

                                if(!haveNetwork())
                                    Toast.makeText(ViewRest.this, "Please check your Internet connection...", Toast.LENGTH_SHORT).show();
                                else{
                                    Toast.makeText(ViewRest.this, "Connected...", Toast.LENGTH_SHORT).show();
                                    InsertData(t.toString(),q.toString(),user_phone.toString(),nn.toString());
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                ;
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        /*del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = vv.getText().toString();
                String[] a = str.split("-",2);
                while (data4.moveToNext()) {
                    if(data4.getString(1).equalsIgnoreCase(a[0])){
                        id_d=data4.getInt(0);
                        break;
                    }
//                    idd = data3.getInt(0);
//                    String src= data1.getString(1);
//                    String dep = data1.getString(2);
//                    k[ind++]=src+"-"+dep;
                    //  Toast.makeText(this, ""+data.getString(2), Toast.LENGTH_SHORT).show();
                    //listData.add(data.getString(1));
                }
                ((LinearLayout) addView.getParent()).removeView(addView);
  /*              String str = vv.getText().toString();
                String[] a = str.split("-",2);
*/
               /* int id = source.indexOf(a[0]);
                id++;*/
            /*    Toast.makeText(Retrieve.this, ""+id_d, Toast.LENGTH_SHORT).show();
                mDatabaseHelper.deleteName(id_d);
            }
        });*/   
        container.addView(addView);
    }
    private void getJSON(final String urlWebService,final String email,final String password) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(ViewRest.this, "Loading Data","Please Wait....", true);
            }


            @Override
            protected void onPostExecute(String s) {
             //   Toast.makeText(ViewRest.this, " "+s, Toast.LENGTH_SHORT).show();
                super.onPostExecute(s);
                progressDialog.dismiss();
                try{

                    loadIntoListView(s);
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

    private void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        webchrz = new String[jsonArray.length()];
        webchrz1 = new String[jsonArray.length()];
        webchrz2 = new String[jsonArray.length()];
        restnum = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            webchrz[i] = obj.getString("ftype");
            webchrz1[i] = obj.getString("quantity");
            restnum[i] = obj.getString("num");
            webchrz2[i] = "  Type: "+webchrz[i]+" || "+"Quantity:"+webchrz1[i]+"Kg.";
        }
        for(int i=0;i<webchrz2.length;i++){
            if(webchrz2[i]!=null) {
                if(iddd.equalsIgnoreCase(restnum[i]))

                    addItems(webchrz2[i],webchrz[i],webchrz1[i],restnum[i]);
            }
        }

    }



/*
  private void getJSON(final String urlWebService) {

       class GetJSON extends AsyncTask<Void, Void, String> {

           @Override
           protected void onPreExecute() {
               super.onPreExecute();
               progressDialog = ProgressDialog.show(RestDisplay.this, "Loading Data","Please Wait....", true);
           }


           @Override
           protected void onPostExecute(String s) {
               super.onPostExecute(s);
               progressDialog.dismiss();
               if(s!=null){
               try {
                   loadIntoListView(s);
               } catch (JSONException e) {
                   e.printStackTrace();
               }
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
              /*     String post_data = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"
                           +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                *///   bufferedWriter.write(post_data);
/*                   bufferedWriter.flush();
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

private void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] webchrz = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject obj = jsonArray.getJSONObject(i);
        webchrz[i] = obj.getString("quantity");
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, webchrz);
        listView.setAdapter(arrayAdapter);
        }
*/
public void InsertData(final String name, final String number, final String lnumber, final String pass)
{

    class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String NameHolder = name ;
            String Number = number ;
            String lnumberH = lnumber;
            String passH = pass;


            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("ftype", NameHolder));
            nameValuePairs.add(new BasicNameValuePair("quantity", Number));
            nameValuePairs.add(new BasicNameValuePair("uphone",lnumberH ));
            nameValuePairs.add(new BasicNameValuePair("rphone",passH ));

            try {

                HttpClient httpClient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost(ServerURLI);

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
            progressDialog = ProgressDialog.show(ViewRest.this, "Just a minute","Please wait....", true);
        }
        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            progressDialog.dismiss();
            Toast.makeText(ViewRest.this, "Your Order is Placed", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ViewRest.this, AfterUserLogin.class);
                intent.putExtra("id",user_phone.toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
        }
    }
    SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

    sendPostReqAsyncTask.execute(name, number, lnumber, pass);
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
}
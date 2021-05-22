package com.example.efm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
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

public class RestDisplay extends AppCompatActivity {
    String[] k = {"Onion","Pulses","Red_Chilli","Rice","Wheat","Turmeric","Groundnut"};
    LinearLayout container;
    String[] webchrz = new String[100];
    String[] webchrz1 = new String[100];
    String[] webchrz2 = new String[100];
    String[] restnum = new String[100];
    String[] fid=  new String[100];
    ListView listView;

    String phone=null;
    private ProgressDialog progressDialog;
    String ServerURL = "https://learnfriendly.000webhostapp.com/rohan/RestDisplay.php";
    String ServerURLD = "https://learnfriendly.000webhostapp.com/rohan/RestDonateDelete.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_display);
        Intent in = getIntent();
        phone = in.getStringExtra("id");
        container = (LinearLayout) findViewById(R.id.container);

       // listView = (ListView) findViewById(R.id.listView);
        /*TextView emptyText = (TextView)findViewById(android.R.id.empty);
        emptyText.setVisibility(View.VISIBLE);
        listView.setEmptyView(emptyText);
*/
        listView = (ListView) findViewById(R.id.listView);
   //     getJSON("https://learnfriendly.000webhostapp.com/rohan/RestDisplay.php");
      getJSON(ServerURL,"sri","ram");

    }
    public void addItems(String a, final String fid) {
        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.item_row, null);

        final TextView vv = (TextView) addView.findViewById(R.id.place);

        final Button edit = (Button)addView.findViewById(R.id.edit);

        edit.setVisibility(View.INVISIBLE);

        final Button del = (Button)addView.findViewById(R.id.delete);
        vv.setText(""+a.toString());


        /*edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String str = vv.getText().toString();
                String[] a = str.split("-",2);
                int id = source.indexOf(a[0]);
                id++;
                while (data3.moveToNext()) {
                    if(data3.getString(1).equalsIgnoreCase(a[0])){
                        idd=data3.getInt(0);
                        break;
                    }
//                    idd = data3.getInt(0);
//                    String src= data1.getString(1);
//                    String dep = data1.getString(2);
//                    k[ind++]=src+"-"+dep;
                    //  Toast.makeText(this, ""+data.getString(2), Toast.LENGTH_SHORT).show();
                    //listData.add(data.getString(1));
                }
                Intent intent = new Intent(Retrieve.this,UpdateData.class);
                intent.putExtra("id",String.valueOf(idd));
                Toast.makeText(Retrieve.this, ""+idd, Toast.LENGTH_LONG).show();
                startActivity(intent);
                //Toast.makeText(Retrieve.this, "Will be added soon...", Toast.LENGTH_SHORT).show();

            }
        });*/
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RestDisplay.this);
                builder.setTitle("Delete this item ");
               builder.setMessage("Are you Sure! Do you want to delete this item ")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int kk) {
                                //  Toast.makeText(ViewRest.this, "Please check your connection...", Toast.LENGTH_SHORT).show();

                                if(!haveNetwork())
                                    Toast.makeText(RestDisplay.this, "Please check your Internet connection...", Toast.LENGTH_SHORT).show();
                                else{
                                    Toast.makeText(RestDisplay.this, "Connected...", Toast.LENGTH_SHORT).show();
                                    InsertData(fid.toString());
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
    container.addView(addView);
    }
    private void getJSON(final String urlWebService,final String email,final String password) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(RestDisplay.this, "Loading Data","Please Wait....", true);
            }


            @Override
            protected void onPostExecute(String s) {
             //   Toast.makeText(RestDisplay.this, " "+s, Toast.LENGTH_SHORT).show();
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
        fid = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            fid[i]=obj.getString("id");
            webchrz[i] = obj.getString("ftype");
            webchrz1[i] = obj.getString("quantity");
            restnum[i] = obj.getString("num");
            webchrz2[i] = "  Type: "+webchrz[i]+" || "+"Quantity:"+webchrz1[i]+"Kg.";
        }
        for(int i=0;i<webchrz2.length;i++){
            if(webchrz2[i]!=null) {
                if(phone.equalsIgnoreCase(restnum[i]))

                addItems(webchrz2[i],fid[i]);
            }
        }

    }
    public void InsertData(final String name)
    {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String NameHolder = name ;


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("id", NameHolder));

                try {

                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(ServerURLD);

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
                progressDialog = ProgressDialog.show(RestDisplay.this, "Deleting the item","Please wait....", true);
            }
            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);
                progressDialog.dismiss();
                Toast.makeText(RestDisplay.this, "Item deleted Sucessfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RestDisplay.this, RestDisplay.class);
                intent.putExtra("id",phone.toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(name);
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

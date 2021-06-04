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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class UserOrdersRestView extends AppCompatActivity {
    LinearLayout container;
    String[] webchrz = new String[100];
    String[] webchrz1 = new String[100];
    String[] webchrz2 = new String[100];
    String[] restnum = new String[100];
    String[] fid=  new String[100];
    ListView listView;
    String[] user_phone =  new String[100];
    String[] rest_phone =  new String[100];
    String phone=null;
    private ProgressDialog progressDialog;
    String ServerURL = "https://learnfriendly.000webhostapp.com/rohan/UserOrdersDisplay.php";
    String ServerURLD = "https://learnfriendly.000webhostapp.com/rohan/RestDonateDelete.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_orders_rest_view);
        Intent in = getIntent();
        phone = in.getStringExtra("id");
        container = (LinearLayout) findViewById(R.id.container);

         listView = (ListView) findViewById(R.id.listView);

        getJSON(ServerURL,"sri","ram");
    }
    public void addItems(final String a, final String fid, final String userphone) {
        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.item_row, null);

        final TextView vv = (TextView) addView.findViewById(R.id.place);

        final Button edit = (Button)addView.findViewById(R.id.edit);

        edit.setVisibility(View.INVISIBLE);

        final Button del = (Button)addView.findViewById(R.id.delete);

        del.setText("View");
        vv.setText(""+userphone.toString());


        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserOrdersRestView.this);
                builder.setTitle("Order from : "+userphone.toString());
                builder.setMessage("Items and Quantity : "+a.toString())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int kk) {
                                //  Toast.makeText(ViewRest.this, "Please check your connection...", Toast.LENGTH_SHORT).show();


                            }
                        });
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
                progressDialog = ProgressDialog.show(UserOrdersRestView.this, "Loading Data","Please Wait....", true);
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
        user_phone = new String[jsonArray.length()];
        rest_phone = new String[jsonArray.length()];
        fid = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            fid[i]=obj.getString("id");
            webchrz[i] = obj.getString("ftype");
            webchrz1[i] = obj.getString("quantity");
            user_phone[i] = obj.getString("uphone");
            rest_phone[i] = obj.getString("rphone");
            webchrz2[i] = "  Type: "+webchrz[i]+" || "+"Quantity:"+webchrz1[i]+"Kg.";
        }
        for(int i=0;i<rest_phone.length;i++){
            if(rest_phone[i]!=null) {
                if(phone.equalsIgnoreCase(rest_phone[i]))
                    addItems(webchrz2[i],fid[i],user_phone[i]);
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
}
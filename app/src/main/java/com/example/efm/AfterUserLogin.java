package com.example.efm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

public class AfterUserLogin extends AppCompatActivity {
    LinearLayout container;
    String[] webchrz = new String[100];
    String[] webchrz1 = new String[100];
    String[] webchrz2 = new String[100];
    String[] restnum = new String[100];
    String[] idd = new String[100];String[] namee = new String[100];String[] lnumber  = new String[100];
    String[] numberr = new String[100];String[] address = new String[100];
    ListView listView;
String user_phone=null;
    String phone=null;
    private ProgressDialog progressDialog;
    String ServerURL = "https://learnfriendly.000webhostapp.com/rohan/AfterUserLogin.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_user_login);
        Intent inte = getIntent();
        user_phone = inte.getStringExtra("id");
        container = (LinearLayout) findViewById(R.id.container); getJSON(ServerURL,"sri","ram");
    }
    public void addItems(String a,String ids) {
        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.item_row, null);

        final TextView vv = (TextView) addView.findViewById(R.id.place);
        final TextView iddd = (TextView) addView.findViewById(R.id.iddd);
        final Button edit = (Button)addView.findViewById(R.id.edit);
        final Button del = (Button)addView.findViewById(R.id.delete);
        del.setVisibility(View.INVISIBLE);
        vv.setText(""+a.toString());
        iddd.setText(""+ids.toString());

        edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
             //   Toast.makeText(AfterUserLogin.this, ""+iddd.getText().toString(), Toast.LENGTH_SHORT).show();
             Intent in  = new Intent(AfterUserLogin.this,ViewRest.class);
             in.putExtra("iddd",iddd.getText().toString());
             in.putExtra("name",vv.getText().toString());
             in.putExtra("user_phone",user_phone.toString());
             startActivity(in);
            }
        });
        container.addView(addView);
    }
    private void getJSON(final String urlWebService,final String email,final String password) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(AfterUserLogin.this, "Loading Data","Please Wait....", true);
            }


            @Override
            protected void onPostExecute(String s) {
              //  Toast.makeText(AfterUserLogin.this, " "+s, Toast.LENGTH_SHORT).show();
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
        idd = new String[jsonArray.length()];namee= new String[jsonArray.length()];lnumber= new String[jsonArray.length()];
        numberr= new String[jsonArray.length()];address= new String[jsonArray.length()];
//        webchrz = new String[jsonArray.length()];
//        webchrz1 = new String[jsonArray.length()];
//        webchrz2 = new String[jsonArray.length()];
//        restnum = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            idd[i] = obj.getString("id");
            namee[i] = obj.getString("name");
            lnumber[i] = obj.getString("lnumber");numberr[i] = obj.getString("number");
            address[i] = obj.getString("address");
            //webchrz2[i] = "  Type: "+webchrz[i]+" || "+"Quantity:"+webchrz1[i]+"Kg.";
        }
        for(int i=0;i<namee.length;i++){
            if(namee[i]!=null) {
                //if(phone.equalsIgnoreCase(restnum[i]))

                    addItems(namee[i],numberr[i]);
            }
        }

    }

}


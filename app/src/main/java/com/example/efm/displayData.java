package com.example.efm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class displayData extends AppCompatActivity {

    ListView listView;
    String email="jjiii";
    String password = "jhbhbuhy";
    String[] webchrz = new String[100];
    String[] webchrz1 = new String[100];
    String[] webchrz2 = new String[100];
    private ProgressDialog progressDialog;
    String ServerURL = "https://learnfriendly.000webhostapp.com/onionDisplay.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);
       /* Intent intent1 = getIntent();
        email = intent1.getStringExtra("aadhar").toString();
        password = email;*/
        listView = (ListView) findViewById(R.id.listView);
        TextView emptyText = (TextView)findViewById(android.R.id.empty);
        emptyText.setVisibility(View.VISIBLE);
        listView.setEmptyView(emptyText);
        /*String ServerURL = "https://learnfriendly.000webhostapp.com/onionDisplay.php";
        Intent intent = getIntent();
        String text = intent.getStringExtra("item").toString();
        if(text.equalsIgnoreCase("onion"))
            ServerURL = "https://learnfriendly.000webhostapp.com/onionDisplay.php";
        else if(text.equalsIgnoreCase("wheat"))
            ServerURL = "https://learnfriendly.000webhostapp.com/wheatDisplay.php";
        else if(text.equalsIgnoreCase("turmeric"))
            ServerURL = "https://learnfriendly.000webhostapp.com/turmericDisplay.php";
        else if(text.equalsIgnoreCase("maize"))
            ServerURL = "https://learnfriendly.000webhostapp.com/maizeDisplay.php";
        else if(text.equalsIgnoreCase("rice"))
            ServerURL = "https://learnfriendly.000webhostapp.com/riceDisplay.php";
        else if(text.equalsIgnoreCase("mirchi"))
            ServerURL = "https://learnfriendly.000webhostapp.com/mirchiDisplay.php";
        else if(text.equalsIgnoreCase("ground"))
            ServerURL = "https://learnfriendly.000webhostapp.com/groundDisplay.php";
*/
        getJSON(ServerURL,email,password);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                Object clickItemObj = adapterView.getAdapter().getItem(index);
                Toast.makeText(displayData.this, "You clicked " + clickItemObj.toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(displayData.this, " "+webchrz1[index], Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getJSON(final String urlWebService,final String email,final String password) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(displayData.this, "Loading Data","Please Wait....", true);
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                try {
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
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            webchrz[i] = obj.getString("quantity");
            webchrz1[i] = obj.getString("price");
            webchrz2[i] = " Quantity: "+webchrz[i]+"Kg(s) "+"|| Price: "+webchrz1[i]+" per Kg.";
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, webchrz2);
        listView.setAdapter(arrayAdapter);
    }
}
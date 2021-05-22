package com.example.efm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AfterRestLogin extends AppCompatActivity {
    String num=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_rest_login);
        Intent in = getIntent();
        num = in.getStringExtra("id");

    }

    public void viewmyDonations(View view) {
        Intent in = new Intent(AfterRestLogin.this,RestDisplay.class);
        in.putExtra("id",num);
        startActivity(in);
    }

    public void donate(View view) {
        Intent in = new Intent(AfterRestLogin.this,FoodInsert.class);
        in.putExtra("id",num);
        startActivity(in);
    }
}
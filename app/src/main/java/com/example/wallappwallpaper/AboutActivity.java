package com.example.wallappwallpaper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("About");

        TextView danLink = (TextView) findViewById(R.id.danLink);
        TextView andreiLink = (TextView) findViewById(R.id.andreiLink);

        danLink.setOnClickListener(v->
        {
            String val = danLink.getText().toString();
            Uri webpage = Uri.parse(val);
            Intent intent12 = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(intent12);
        });

        andreiLink.setOnClickListener(v->
        {
            String val = andreiLink.getText().toString();
            Uri webpage = Uri.parse(val);
            Intent intent12 = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(intent12);
        });



        setSupportActionBar(toolbar);
/*
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());*/



    }
}
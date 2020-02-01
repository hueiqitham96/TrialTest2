package com.example.trialtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddWebsiteActivity extends AppCompatActivity {

    private EditText urlText;
    private String url;

    private final String TAG = "Trialtest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_website);
    }

    /** Add url to the database*/
    public void addURL(View v){
        // Animation for buttons
        //final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.click);
        //v.startAnimation(myAnim);

        urlText = (EditText) findViewById(R.id.url);

        url = urlText.getText().toString();

        if(url.equals("")){
            Toast.makeText(AddWebsiteActivity.this, "You must enter url", Toast.LENGTH_SHORT).show();
            return;
        }

        // store the value obtain into database table when add button is clicked
        DBHandler myDB = new DBHandler(this, "websiteDB.db",null,1);
        Website website = new Website(url);
        myDB.addWebsite(website);
        //Toast.makeText(getApplicationContext(), "Url inserted", Toast.LENGTH_SHORT).show();
        Log.d(TAG,"URL inserted");

        startActivity(new Intent(AddWebsiteActivity.this,MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(AddWebsiteActivity.this, MainActivity.class));
        finish();
    }
}

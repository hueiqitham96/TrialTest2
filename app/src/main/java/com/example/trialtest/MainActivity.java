package com.example.trialtest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private final String TAG = "Trialtest";

    private ListView list;
    private Cursor cursor;

    SQLiteDatabase myDB;
    DBHandler myHandler;

    private int[] allUrlID;
    private int selectedID;
    private String[] allUrl;
    private List<Website> websiteUrl = new ArrayList<Website>();
    private boolean ascending = true;

    private int i =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myHandler = new DBHandler(this,"websiteDB.db",null,1);
        myDB = myHandler.getWritableDatabase();

        allUrlID = new int[getCount()];
        allUrl = new String[getCount()];
        //allName = new String[Integer.parseInt(getUrl())];
        getAllUrls();

        list = (ListView) findViewById(R.id.listView);
        final ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allUrl);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {

                selectedID = allUrlID[myItemInt];
                deleteUrl(selectedID);
                adapter.notifyDataSetChanged();
                finish();
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });

        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.click);

        // enter record sound activity
        Button addButton = (Button)findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(myAnim);
                startActivity(new Intent(MainActivity.this,AddWebsiteActivity.class));
                Log.d(TAG, "Add Website Activity");
            }
        });

        Button sortButton = (Button)findViewById(R.id.sortButton);
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(myAnim);
                sortURL(ascending);
                ascending = !ascending;
                Log.d(TAG, "Sorted");
            }
        });


    }


    /** get the total number of record in the database table */
    public int getCount()
    {
        Cursor cursor =  myDB.rawQuery("SELECT COUNT (*) FROM websiteName",null);
        cursor.moveToFirst();
        int result = cursor.getInt(0);
        cursor.close();
        return result;
    }

    /** get url */
    public void getAllUrls() {

        cursor = myDB.query("websiteName", null, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                allUrl[i] = cursor.getString(cursor.getColumnIndex("url"));
                allUrlID[i] = cursor.getInt(cursor.getColumnIndex("_id"));
                i++;
            } while(cursor.moveToNext());
        }
    }

    public void sortURL(boolean asc){
        myHandler = new DBHandler(this, "websiteDB.db",null,1);
        websiteUrl = myHandler.findAllUrl();

        // list for storing the database data
        List<String> allURL = new ArrayList<String>();

        // add values from database table to the list created
        for (int i = 0 ; i < websiteUrl.size();i++){
            Website data = websiteUrl.get(i);
            if (data != null){
                allURL.add(data.getUrl()) ;
            }
        }

        if(asc){
            Collections.sort(allURL);
            list.setAdapter(new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,allURL));
            Log.d(TAG,"ascending"+ allURL);
        }else if(asc == false){
            //Collections.reverse(allURL);
            Collections.sort(allURL, Collections.reverseOrder());
            Log.d(TAG,"reverse"+ allURL);
            list.setAdapter(new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,allURL));
        }
        //list.setAdapter(new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,allURL));
    }

    /** Delete the item from the database*/
    public void deleteUrl(int id){

        myHandler = new DBHandler(this,"websiteDB.db",null,1);
        id = selectedID;
        boolean result = myHandler.deleteUrl(id);
        if(result){
            Toast.makeText(getApplicationContext(), "Item deleted", Toast.LENGTH_SHORT).show();
            Log.d(TAG,"Item deleted");
            //startActivity(new Intent(WebsiteActivity.this, MainActivity.class));
            //finish();
        }else{
            Toast.makeText(getApplicationContext(), "No Item Found", Toast.LENGTH_SHORT).show();
        }
    }


    /*public String getUrl(){
        myHandler = new DBHandler(this, "websiteDB.db",null,1);
        websiteUrl = myHandler.findAllUrl();

        // list for storing the database data
        List<String> allURL = new ArrayList<String>();

        // add values from database table to the list created
        for (int i = 0 ; i < websiteUrl.size();i++){
            Website data = websiteUrl.get(i);
            if (data != null){
                allURL.add(data.getUrl()) ;
            }
        }

        // list for storing the database data
        List<String> urlString = new ArrayList<String>();

        for (int i = 0; i < allURL.size(); i++){
            String url = allURL.get(i);

            Document doc = null;
            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String title = doc.title();
            urlString.add(title);
        }

    return String.valueOf(urlString);
    }
*/
    @Override
    protected void onPause() {
        super.onPause();
        finish();
        Log.d(TAG, "Activity onPause");
    }

}

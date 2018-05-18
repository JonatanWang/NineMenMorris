package com.zyw.nnm.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.zyw.nnm.Activities.MainActivity;
import com.zyw.nnm.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LoadActivity extends AppCompatActivity {

    Context c;
    ArrayList<String> filenames = new ArrayList<>();
    ArrayList<String> newFilenames = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        c=this;


        for(int i=0; i<fileList().length; i++){

            String fileName = fileList()[i];
            if(fileName.contains("gameStateView_")) {
                filenames.add(fileName);
                fileName = fileName.split("_")[1];
                SimpleDateFormat parser = new SimpleDateFormat("MM-dd-yyyy-HH-mm-ss");
                Date date = new Date();
                try {
                    date = parser.parse(fileName);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd-yyyy HH:mm:ss");
                String formattedDate = formatter.format(date);
                System.out.println(date);
                newFilenames.add(0,"Save date: " + formattedDate);
            }

        }

            ListView  listView= findViewById(R.id.loaded_files);
            listView.setOnItemClickListener(new ListHandler());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, newFilenames);

            listView.setAdapter(adapter);




        }
    private class ListHandler implements  ListView.OnItemClickListener{



        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Log.d("game", "onItemSelected: " + filenames.get(i));
            Intent intent = new Intent(c, MainActivity.class);
            intent.putExtra("fileName", filenames.get(i) );
            c.startActivity(intent);
        }
    }

    }



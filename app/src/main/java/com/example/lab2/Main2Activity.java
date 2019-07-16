package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Main2Activity extends AppCompatActivity {

    private TextView tvResult,tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        tvName = (TextView) findViewById(R.id.tvName);
        tvResult = (TextView) findViewById(R.id.tvResult);

        GetTask getTask = new GetTask();
        getTask.execute("http://dotplays.com/android/bai1.php?food=today");
    }

    class GetTask extends AsyncTask<String, Long, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = httpURLConnection.getInputStream();

                Scanner sc = new Scanner(inputStream);

                String data = "";

                while (sc.hasNext()) {
                    data = data + sc.nextLine();
                }
                sc.close();

                return data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Intent intent = getIntent();
            tvName.setText(intent.getStringExtra("result")+intent.getStringExtra("name"));
            tvResult.setText(s);
        }
    }
    }


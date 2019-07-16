package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {
    private TextInputLayout textUsername;
    private EditText edtUsername;
    private TextInputLayout textPassword;
    private EditText edtPassword;
    private TextInputLayout textName;
    private EditText edtName;
    private Button btnLogin;


    private SpotsDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        edtUsername = (EditText) findViewById(R.id.edtUsername);

        edtPassword = (EditText) findViewById(R.id.edtPassword);

        edtName = (EditText) findViewById(R.id.edtName);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        progressDialog = new SpotsDialog(MainActivity.this);
        progressDialog = new SpotsDialog(MainActivity.this);
        initView();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostTask postTask = new PostTask();
                postTask.execute("http://dotplays.com/android/login.php");
            }
        });
    }

    private void initView() {

    }

    class PostTask extends AsyncTask<String, Long, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                String user = edtUsername.getText().toString();
                String pass = edtPassword.getText().toString();
                String name = edtName.getText().toString();


                // khoi tao param
                StringBuilder params = new StringBuilder();

                params.append("&");
                params.append("username="+user);
                params.append("&");
                params.append("password="+pass);

                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter
                        (new OutputStreamWriter(os, "UTF-8"));

                // dua param vao body cua request
                writer.append(params);

                // giai phong bo nho
                writer.flush();
                // ket thuc truyen du lieu vao output
                writer.close();
                os.close();


                // lay du lieu tra ve
                StringBuilder response = new StringBuilder();

                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }
                }


                return response.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
//            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this,Main2Activity.class);
            intent.putExtra("result",s);
            intent.putExtra("name",edtName.getText().toString());

            intent.putExtra("user",edtUsername.getText().toString());
            intent.putExtra("pass",edtPassword.getText().toString());
            startActivity(intent);
        }
    }

}

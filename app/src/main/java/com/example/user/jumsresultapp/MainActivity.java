package com.example.user.jumsresultapp;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private Button b;
    private TextView t;
    private WebView webView;
    private ProgressBar progressBar;
    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        init();
    }

    private void init() {
        flag = 0;
        b = (Button)findViewById(R.id.button);
        t = (TextView)findViewById(R.id.textView);
        webView = (WebView)findViewById(R.id.webView);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.INVISIBLE);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    flag=0;
                    process();
                } catch (Exception e) {
                    b.setText("Exception");
                    e.printStackTrace();
                }
            }
        });
    }

    private void displayResult(String url) {
        progressBar.setVisibility(View.INVISIBLE);
        b.setText("Found");
        webView.loadUrl(url);
    }

    private class MyAsyncTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            String name = strings[1];
            int fl=0;
            URL u = null;
            try {
                u = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Scanner s=null;
            try {
                s=new Scanner(u.openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            while(s.hasNext())
            {
                String ss=s.nextLine();
                if(ss.indexOf(name)!=-1)
                {
                    fl=1;
                }
            }
            if(fl==0)
                return "";
            else
                return url;
        }

        @Override
        protected void onPostExecute(String url) {
            if(url.equals(""))
                return;
            flag=1;
            progressBar.setVisibility(View.INVISIBLE);
            webView.loadUrl(url);
        }
    }

    private void process()throws Exception {
        progressBar.setVisibility(View.VISIBLE);
        String name = t.getText().toString();
        String url = "http://juadmission.jdvu.ac.in/jums_exam/student_odd/result/view_print_result_be.jsp?exam_roll=CSE185001";
        int i=1;
        //b.setText("hello");
       // Thread.sleep(1000);
        while(i<=8 && flag==0)
        {
            // t.setText(i);
        //    b.setText("hellopaa");
           // Toast.makeText(this,url,Toast.LENGTH_SHORT).show();
          //  Thread.sleep(1000);

            String params[] = {url,name};
            new MyAsyncTask().execute(params);
            if(flag==1)
                return;
            i++;
            url=url.substring(0,url.length()-1)+String.valueOf(i);
        }

        while(i<=80 && flag==0)
        {
            String params[] = {url,name};
            new MyAsyncTask().execute(params);
            if(flag==1)
                return;
            i++;
            url=url.substring(0,url.length()-2)+String.valueOf(i);
        }
    }
}

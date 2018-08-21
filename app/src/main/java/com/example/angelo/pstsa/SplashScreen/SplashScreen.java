package com.example.angelo.pstsa.SplashScreen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.angelo.pstsa.LoginMethod.LoginActivity;
import com.example.angelo.pstsa.MainActivity;
import com.example.angelo.pstsa.R;
import com.example.angelo.pstsa.RequestHandler.RequestHandler;
import com.example.angelo.pstsa.SQLiteDatabase.DatabaseHelper;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity{
    private Timer timer;
    private ProgressBar progressBar;
    private int i=0;
    TextView textView;
    DatabaseHelper db;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            progressBar=(ProgressBar)findViewById(R.id.progressBar);
            db = new DatabaseHelper(this);
            progressBar.setProgress(0);
            textView=(TextView)findViewById(R.id.textView);
            textView.setText("");

            final long period = 30;
            timer=new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //this repeats every 100 ms
                    if (i<100){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(String.valueOf(i)+"%");
                            }
                        });
                        progressBar.setProgress(i);
                        i++;
                    }else{
                        //LoginAccount();
                        //closing the timer
                        timer.cancel();
                        //Intent intent =new Intent(SplashScreen.this,LoginActivity.class);
                        //startActivity(intent);
                        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                        startActivity(intent);
                        // close this activity
                        finish();
                    }
                }
            }, 0, period);

        }

    private void LoginAccount(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SplashScreen.this, "Signing", "Please Wait...",true,false);
                Looper.prepare();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Reset UI
                //Toast.makeText(view.getContext().getApplicationContext(),s,Toast.LENGTH_LONG).show();
                if (s.equals("Welcome")){
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    intent.putExtra("username", GetUsername());
                    intent.putExtra("password", GetPassword());
                    startActivity(intent);
                    //SplashScreen.this.finish();
                }else if (s.equals("Please Login")){
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                    //SplashScreen.this.finish();
                }else{
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                    //SplashScreen.this.finish();
                }
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                HashMap<String,String> data = new HashMap<>();
                //data.put("juser", pageantName.getText().toString());
                data.put("auser", GetUsername());
                data.put("apass", GetPassword());
                data.put("atype", "Admin");
                String result = rh.sendPostRequest("http://" + GetServerIP() + "/psts/check_user.php",data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute();
    }

    public String GetUsername(){
            String username = "";
            Cursor account = db.GetAccount();
            if (account.moveToNext()){
                username = account.getString(1);
            }
            return username;
    }

    public String GetPassword(){
        String password = "";
        Cursor account = db.GetAccount();
        if (account.moveToNext()){
            password = account.getString(2);
        }
        return password;
    }

    public String GetServerIP(){
        //fetch server IP
        String IP = "";
        Cursor server_list = db.GetIP();
        if (server_list != null && server_list.getCount() > 0){
            if (server_list.moveToNext()){
                IP = server_list.getString(1);
            }
        }
        return IP;
    }
}

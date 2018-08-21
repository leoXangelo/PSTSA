package com.example.angelo.pstsa.UpdateMethods;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.view.View;
import android.widget.Button;

import com.example.angelo.pstsa.R;
import com.example.angelo.pstsa.RequestHandler.RequestHandler;
import com.example.angelo.pstsa.SQLiteDatabase.DatabaseHelper;

import java.util.HashMap;

public class UpdateCriteria extends AppCompatActivity {
    String cid, cname, cpercent, ename;
    Button btnCriteria;
    AppCompatAutoCompleteTextView eventName;
    TextInputEditText criteriaName, criteriaPercent;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criteria);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        btnCriteria = (Button) findViewById(R.id.btnCriteria);
        eventName = (AppCompatAutoCompleteTextView) findViewById(R.id.eventName);
        criteriaName = (TextInputEditText) findViewById(R.id.criteriaName);
        criteriaPercent = (TextInputEditText) findViewById(R.id.criteriaPercentage);
        fab.setVisibility(View.GONE);
        cid = getIntent().getExtras().getString("cid");
        cname = getIntent().getExtras().getString("cname");
        cpercent = getIntent().getExtras().getString("cpercent");
        ename = getIntent().getExtras().getString("ename");
        db = new DatabaseHelper(this);
        btnCriteria.setText("Update");

        criteriaName.setText(cname);
        criteriaPercent.setText(cpercent);
        eventName.setText(ename);

        btnCriteria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCriteria(cid, eventName.getText().toString(),
                        criteriaName.getText().toString(),
                        criteriaPercent.getText().toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void updateCriteria(final String criteria_ID, final String event_name, final String criteria_name, final String criteria_percent){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UpdateCriteria.this, "Uploading", "Please Wait...",true,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Reset UI
                //Toast.makeText(view.getContext().getApplicationContext(),s,Toast.LENGTH_LONG).show();
                if (s != null){
                    AlertDialog.Builder alert = new AlertDialog.Builder(UpdateCriteria.this);
                    alert.setTitle("Pageant Scoring and Tabulation System");
                    alert.setMessage(s.toString());
                    alert.setCancelable(false);
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UpdateCriteria.this.finish();
                        }
                    });
                    AlertDialog build = alert.create();
                    build.show();

                }
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("cid", criteria_ID);
                data.put("ename", event_name);
                data.put("cname", criteria_name);
                data.put("cpercent", criteria_percent);
                String result = rh.sendPostRequest("http://" + GetServerIP() + "/psts/update_criteria.php",data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute();
    }

    public String GetServerIP(){
        String IP = "";
        Cursor result = db.GetIP();
        if (result != null && result.getCount() > 0){
            if (result.moveToNext()){
                IP = result.getString(1);
            }
        }else{
        }

        return IP;
    }
}

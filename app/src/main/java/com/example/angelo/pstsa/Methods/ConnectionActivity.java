package com.example.angelo.pstsa.Methods;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angelo.pstsa.R;
import com.example.angelo.pstsa.RequestHandler.RequestHandler;
import com.example.angelo.pstsa.SQLiteDatabase.DatabaseHelper;

import java.util.HashMap;

public class ConnectionActivity extends Fragment {
    public View view;
    TextInputEditText ipaddress;
    Button btnIp;
    DatabaseHelper db;
    TextView server, device;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_ipaddress, container, false);
        ipaddress = (TextInputEditText) view.findViewById(R.id.Ip);
        btnIp = (Button) view.findViewById(R.id.btnIP);
        db = new DatabaseHelper(view.getContext());
        server = (TextView) view.findViewById(R.id.txtserver);
        device = (TextView) view.findViewById(R.id.txtdevice);
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start,
                                       int end, Spanned dest, int dstart, int dend) {
                if (end > start) {
                    String destTxt = dest.toString();
                    String resultingTxt = destTxt.substring(0, dstart) +
                            source.subSequence(start, end) +
                            destTxt.substring(dend);
                    if (!resultingTxt.matches ("^\\d{1,3}(\\." +
                            "(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
                        return "";
                    } else {
                        String[] splits = resultingTxt.split("\\.");
                        for (int i=0; i<splits.length; i++) {
                            if (Integer.valueOf(splits[i]) > 255) {
                                return "";
                            }
                        }
                    }
                }
                return null;
            }
        };
        ipaddress.setFilters(filters);

        btnIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ipaddress.getText().toString().equals("")){
                    SaveIP(ipaddress.getText().toString());
                    GetServerIP();
                    UpdateAddress(ipaddress.getText().toString());
                }else{
                    Toast.makeText(view.getContext(), "Please specify valid IP", Toast.LENGTH_SHORT).show();
                }
            }
        });

        GetServerIP();
        device.setText("Device: Not yet available");
        return view;
    }

    private void UpdateAddress(final String ip){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(view.getContext(), "Uploading", "Please Wait...",true,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Reset UI
                //Toast.makeText(view.getContext().getApplicationContext(),s,Toast.LENGTH_LONG).show();
                if (s != null){
                    AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                    alert.setTitle("Pageant Scoring and Tabulation System");
                    alert.setMessage(s.toString());
                    alert.setCancelable(true);

                    AlertDialog build = alert.create();
                    build.show();
                }
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("ip", ip);
                String result = rh.sendPostRequest("http://" + GetIP() + "/psts/update_ip.php",data);
                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute();
    }
    public void SaveIP(String IP){
        Cursor result = db.GetIP();
        if (result != null && result.getCount() > 0){
            String ID = "";
            if (result.moveToNext()){
                ID = String.valueOf(result.getInt(0));
            }
            Boolean res = db.UpdateIP(ID, IP);
            if (res == true){
                //Message here (optional)
            }else{
                //Message here (optional)
            }
        }else{
            Boolean res = db.SaveIP(IP);
            if (res == true){
                //Message here (optional)
            }else{
                //Message here (optional)
            }
        }
    }
    public String GetIP(){
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
    public void GetServerIP(){
        Cursor result = db.GetIP();
        if (result != null && result.getCount() > 0){
            if (result.moveToNext()){
                server.setText("Server: " + result.getString(1));
            }
        }else{
            server.setText("Server:");
        }
    }
}

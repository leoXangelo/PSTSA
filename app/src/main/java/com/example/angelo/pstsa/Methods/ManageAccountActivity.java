package com.example.angelo.pstsa.Methods;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angelo.pstsa.LoginMethod.LoginActivity;
import com.example.angelo.pstsa.R;
import com.example.angelo.pstsa.RequestHandler.RequestHandler;
import com.example.angelo.pstsa.SQLiteDatabase.DatabaseHelper;

import java.util.HashMap;

public class ManageAccountActivity extends Fragment {
    public View view;
    TextInputEditText username, password, confirmpass;
    TextView error;
    DatabaseHelper db;
    Button btnUpdate;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_manage_account, container, false);
        db = new DatabaseHelper(view.getContext());
        username = (TextInputEditText) view.findViewById(R.id.username);
        password = (TextInputEditText) view.findViewById(R.id.password);
        confirmpass = (TextInputEditText) view.findViewById(R.id.confirm);
        error = (TextView) view.findViewById(R.id.error);
        btnUpdate = (Button) view.findViewById(R.id.btnUpdate);
        confirmpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")){
                    error.setVisibility(View.GONE);
                }else{
                    if (s.toString().equals(password.getText().toString())){
                        error.setTextColor(Color.parseColor("#006442"));
                        error.setText("Password match");
                        error.setVisibility(View.VISIBLE);
                    }else{
                        error.setTextColor(Color.parseColor("#F22613"));
                        error.setText("Password do not match");
                        error.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmpass.getText().toString().equals("")){
                    Toast.makeText(view.getContext(), "Confirm your password", Toast.LENGTH_SHORT).show();
                }else{
                    if (error.getText().toString().equals("Password do not match")){
                        Toast.makeText(view.getContext(), "Password do not match", Toast.LENGTH_SHORT).show();
                    }else{
                        //Execute update here
                        UpdateAccount();
                        SaveAccount();
                        Intent intent = new Intent(view.getContext(), LoginActivity.class);
                        startActivity(intent);
                        ManageAccountActivity.this.getActivity().finish();
                    }
                }
            }
        });
        return view;
    }


    private void UpdateAccount(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(view.getContext(), "Update Account", "Please Wait...",true,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Reset UI
                //Toast.makeText(view.getContext().getApplicationContext(),s,Toast.LENGTH_LONG).show();
                if (s != null){
                    //Toast.makeText(view.getContext(), "Account update " + s,Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(view.getContext(), "No network",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                HashMap<String,String> data = new HashMap<>();
                //data.put("juser", pageantName.getText().toString());
                data.put("username", username.getText().toString());
                data.put("password", password.getText().toString());
                String result = rh.sendPostRequest("http://" + GetServerIP() + "/psts/update_admin_account.php",data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute();
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

    public void SaveAccount(){
        int ID = 0;
        Cursor check_account = db.GetAccount();
        if (!username.getText().toString().equals("") && !password.getText().toString().equals("")){
            if (check_account != null && check_account.getCount() > 0){
                if (check_account.moveToNext()){
                    ID = check_account.getInt(0);
                }
                Boolean update = db.UpdateAccount(String.valueOf(ID), "", "");
                if (update == true){
                    //Message here
                    //Toast.makeText(view.getContext(), "Update Account", Toast.LENGTH_SHORT).show();
                }else{
                    //Message here
                }
            }else {
                Boolean insert = db.SaveAccount(username.getText().toString(), password.getText().toString());
                if (insert == true){
                    //Message here
                    //Toast.makeText(this, "Insert Account", Toast.LENGTH_SHORT).show();
                }else{
                    //Message here
                }
            }
        }
    }
}

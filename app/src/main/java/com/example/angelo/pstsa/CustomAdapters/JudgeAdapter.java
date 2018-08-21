package com.example.angelo.pstsa.CustomAdapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.BottomSheetBehavior;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.angelo.pstsa.Objects.Criteria;
import com.example.angelo.pstsa.Objects.Judge;
import com.example.angelo.pstsa.R;
import com.example.angelo.pstsa.RequestHandler.RequestHandler;
import com.example.angelo.pstsa.SQLiteDatabase.DatabaseHelper;
import com.example.angelo.pstsa.UpdateMethods.UpdateCriteria;

import java.util.ArrayList;
import java.util.HashMap;

public class JudgeAdapter extends BaseAdapter {
    Context context;
    ArrayList<Judge> judges;
    DatabaseHelper db;
    public JudgeAdapter(Context context, ArrayList<Judge> criteria){
        this.context = context;
        this.judges = criteria;
    }

    @Override
    public int getCount() {
        return judges.size();
    }

    @Override
    public Object getItem(int position) {
        return judges.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Judge judge = judges.get(position);
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_judge_list, null);
        }
        db = new DatabaseHelper(context);
        TextView aname, atype, astatus;
        LinearLayout alogin;
        final ToggleButton btnStatus;
        btnStatus = (ToggleButton) convertView.findViewById(R.id.btnStatus);
        aname = (TextView) convertView.findViewById(R.id.judgeName);
        atype = (TextView) convertView.findViewById(R.id.judgeType);
        astatus = (TextView) convertView.findViewById(R.id.judgeStatus);
        alogin = (LinearLayout) convertView.findViewById(R.id.checkLogin);

        if (judge.getJudge_login().equals("Logout")){
            alogin.setBackgroundColor(Color.parseColor("#ea4335"));
        }else {
            alogin.setBackgroundColor(Color.parseColor("#87D37C"));
        }

        if (judge.getJudge_status().equals("Enable")){
            btnStatus.setChecked(false);
        }else {
            btnStatus.setChecked(true);
        }

        btnStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                uploadCriteria(String.valueOf(judge.getID()), btnStatus.getText().toString());
            }
        });
        aname.setText(judge.getJudge_name());
        atype.setText(judge.getJudge_type());
        astatus.setText(judge.getJudge_login());
        return convertView;
    }

    private void uploadCriteria(final String id, final String status){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(context, "Pageant Scoring and Tabulation System", "Please Wait...",true,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Reset UI
                //Toast.makeText(view.getContext().getApplicationContext(),s,Toast.LENGTH_LONG).show();
                if (s != null){
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
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
                data.put("status", status);
                data.put("aid", id);
                String result = rh.sendPostRequest("http://" + GetIP() + "/psts/update_judge_status.php",data);
                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute();
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
}

package com.example.angelo.pstsa.Methods;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.angelo.pstsa.GetCriteria.CriteriaSenderReceiver;
import com.example.angelo.pstsa.GetJudge.JudgeSenderReceiver;
import com.example.angelo.pstsa.R;
import com.example.angelo.pstsa.RequestHandler.RequestHandler;
import com.example.angelo.pstsa.SQLiteDatabase.DatabaseHelper;

import java.util.HashMap;

public class JudgeActivity extends Fragment {
    public View view, bottomSheet;
    DatabaseHelper db;
    SwipeRefreshLayout refresh;
    BottomSheetBehavior behavior;
    ListView judge_list;
    TextInputEditText create_username, create_password, create_name;
    Button btnSubmit;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_judge, container, false);
        judge_list = (ListView) view.findViewById(R.id.judgeList);
        bottomSheet = (View) view.findViewById(R.id.bSheet);
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        db = new DatabaseHelper(view.getContext());
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        create_username = (TextInputEditText) view.findViewById(R.id.create_username);
        create_password = (TextInputEditText) view.findViewById(R.id.create_password);
        create_name = (TextInputEditText) view.findViewById(R.id.create_name);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        //Update listview
        new JudgeSenderReceiver(view.getContext(),
                "http://" + GetServerIP() + "/psts/select_judge.php",
                "Judge",
                judge_list, refresh).execute();
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new JudgeSenderReceiver(view.getContext(),
                        "http://" + GetServerIP() + "/psts/select_judge.php",
                        "Judge",
                        judge_list, refresh).execute();
            }
        });
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                switch (newState){
                    case BottomSheetBehavior.STATE_DRAGGING:
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;

                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
        return view;
    }
    private void CreateAccount(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(JudgeActivity.this.getActivity(), "Signing", "Please Wait...",true,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Reset UI
                //Toast.makeText(view.getContext().getApplicationContext(),s,Toast.LENGTH_LONG).show();
                if (s != null){
                    AlertDialog.Builder alert = new AlertDialog.Builder(JudgeActivity.this.getActivity());
                    alert.setTitle("Pageant Scoring and Tabulation System");
                    alert.setMessage(s.toString());
                    alert.setCancelable(true);
                    alert.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            }else {
                                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            }
                            judge_list.invalidateViews();
                        }
                    });
                    alert.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            judge_list.invalidateViews();
                        }
                    });
                    AlertDialog build = alert.create();
                    build.show();
                }
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                HashMap<String,String> data = new HashMap<>();
                //data.put("juser", pageantName.getText().toString());
                data.put("auser", create_username.getText().toString());
                data.put("apass", create_password.getText().toString());
                data.put("atype", "Judge");
                data.put("aname", create_name.getText().toString());
                String result = rh.sendPostRequest("http://" + GetServerIP() + "/psts/create_user.php",data);

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

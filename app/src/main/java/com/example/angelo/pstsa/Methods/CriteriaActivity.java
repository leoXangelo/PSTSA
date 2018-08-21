package com.example.angelo.pstsa.Methods;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.angelo.pstsa.GetCriteria.CriteriaSenderReceiver;
import com.example.angelo.pstsa.R;
import com.example.angelo.pstsa.RequestHandler.RequestHandler;
import com.example.angelo.pstsa.SQLiteDatabase.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class CriteriaActivity extends Fragment {
    public View view, bottomSheet;
    AppCompatAutoCompleteTextView eventName;
    TextInputEditText criteriaName, criteriaPercent;
    Button btnCriteria;
    DatabaseHelper db;
    SearchView search;
    ListView criteriaList;
    Spinner spinEventName;
    Button activate;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_criteria, container, false);
        //initialize
        bottomSheet = (View) view.findViewById(R.id.bSheet);
        activate = (Button) view.findViewById(R.id.btnActivate);
        spinEventName = (Spinner) view.findViewById(R.id.spinEventName);
        eventName = (AppCompatAutoCompleteTextView) view.findViewById(R.id.eventName);
        criteriaName = (TextInputEditText) view.findViewById(R.id.criteriaName);
        criteriaPercent = (TextInputEditText) view.findViewById(R.id.criteriaPercentage);
        btnCriteria = (Button) view.findViewById(R.id.btnCriteria);
        search = (SearchView) view.findViewById(R.id.search);
        criteriaList = (ListView) view.findViewById(R.id.criteriaList);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);

        db = new DatabaseHelper(view.getContext());
        //Methods
        final BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
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

        btnCriteria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadCriteria(eventName.getText().toString(),
                        criteriaName.getText().toString(),
                        criteriaPercent.getText().toString());
            }
        });
        new com.example.angelo.pstsa.PopulateCriteriaEventSpinner.CriteriaSenderReceiver(view.getContext(),
                "http://" + GetServerIP() + "/psts/admin_select_criteria.php",
                "SELECT DISTINCT ename FROM criteria",
                spinEventName).execute();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    new CriteriaSenderReceiver(view.getContext(),
                            "http://" + GetServerIP() + "/psts/admin_select_criteria.php",
                            "SELECT * FROM criteria WHERE ename = '" + spinEventName.getSelectedItem().toString()+"';",
                            criteriaList,
                            bottomSheet,
                            activate).execute();
                    new com.example.angelo.pstsa.PopulateCriteriaEventSpinner.CriteriaSenderReceiver(view.getContext(),
                            "http://" + GetServerIP() + "/psts/admin_select_criteria.php",
                            "SELECT DISTINCT ename FROM criteria",
                            spinEventName).execute();
                }else {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });


        spinEventName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new CriteriaSenderReceiver(CriteriaActivity.this.getActivity(),
                        "http://" + GetServerIP() + "/psts/admin_select_criteria.php",
                        "SELECT * FROM criteria WHERE ename = '" + spinEventName.getSelectedItem().toString()+"';",
                        criteriaList,
                        bottomSheet,
                        activate).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getStatus = "";
                if (activate.getText().toString().equalsIgnoreCase("Activate")){
                    getStatus = "Active";
                    //activate.setText("Deactivate");
                }else{
                    getStatus = "Inactive";
                    //activate.setText("Activate");
                }
                Activate_Deactivate(getStatus, spinEventName.getSelectedItem().toString());
            }
        });
/*
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                new CriteriaSenderReceiver(view.getContext(),
                        "http://" + GetServerIP() + "/psts/select_criteria.php",
                        query.toString(),
                        criteriaList,
                        bottomSheet).execute();
                Toast.makeText(view.getContext(), query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
*/
        //Fetch Cookies
        GetCookies();
        return view;
    }

    private void Activate_Deactivate(final String txtstatus, final String txtevent){
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
                    new CriteriaSenderReceiver(view.getContext(),
                            "http://" + GetServerIP() + "/psts/admin_select_criteria.php",
                            "SELECT * FROM criteria WHERE ename = '" + spinEventName.getSelectedItem().toString()+"';",
                            criteriaList,
                            bottomSheet,
                            activate).execute();
                    SaveCookies();
                    GetCookies();
                }
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("status", txtstatus);
                data.put("ename", txtevent);
                String result = rh.sendPostRequest("http://" + GetServerIP() + "/psts/update_criteria_status.php",data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute();
    }

    private void uploadCriteria(final String event_name, final String criteria_name, final String criteria_percent){
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

                    SaveCookies();
                    GetCookies();
                }
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("ename", event_name);
                data.put("cname", criteria_name);
                data.put("cpercent", criteria_percent);
                String result = rh.sendPostRequest("http://" + GetServerIP() + "/psts/save_criteria.php",data);

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

    public void SaveCookies(){
        Cursor cookies = db.GetCookies();
        boolean str = false;
        if (cookies != null && cookies.getCount() > 0){
            while (cookies.moveToNext()){
                if (eventName.getText().toString().equals(cookies.getString(1))) {
                    str = true;
                    break;
                }
            }
        }

        if (str == false){
            Boolean res = db.SaveCookies(eventName.getText().toString());
            if (res == true){
                //Message here
            }else {
                //Message here
            }
        }
    }

    public void GetCookies(){
        Cursor cookies = db.GetCookies();
        ArrayList<String> get_cookies = new ArrayList<>();
        if (cookies != null && cookies.getCount() > 0){
            while (cookies.moveToNext()){
                get_cookies.add(cookies.getString(1));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, get_cookies);
            eventName.setAdapter(adapter);
        }
    }
}

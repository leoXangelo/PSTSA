package com.example.angelo.pstsa.CustomAdapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.design.widget.BottomSheetBehavior;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angelo.pstsa.GetCriteria.CriteriaSenderReceiver;
import com.example.angelo.pstsa.Objects.Criteria;
import com.example.angelo.pstsa.R;
import com.example.angelo.pstsa.RequestHandler.RequestHandler;
import com.example.angelo.pstsa.SQLiteDatabase.DatabaseHelper;
import com.example.angelo.pstsa.UpdateMethods.UpdateCriteria;

import java.util.ArrayList;
import java.util.HashMap;

public class CriteriaAdapter extends BaseAdapter {
    Context context;
    ArrayList<Criteria> criteria;
    View bottomSheet;
    DatabaseHelper db;
    public CriteriaAdapter(Context context, ArrayList<Criteria> criteria, View bottomSheet){
        this.context = context;
        this.criteria = criteria;
        this.bottomSheet = bottomSheet;
    }

    @Override
    public int getCount() {
        return criteria.size();
    }

    @Override
    public Object getItem(int position) {
        return criteria.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Criteria criterias = criteria.get(position);
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_criteria_list, null);
        }

        TextView cname, cpercent, designation, status;
        Button btnEdit, btnDel;
        db = new DatabaseHelper(context);
        status = (TextView) convertView.findViewById(R.id.status);
        cname = (TextView) convertView.findViewById(R.id.cname);
        cpercent = (TextView) convertView.findViewById(R.id.cpercent);
        designation = (TextView) convertView.findViewById(R.id.eventDesignation);
        btnEdit = (Button) convertView.findViewById(R.id.btnEdit);
        btnDel = (Button) convertView.findViewById(R.id.btnDel);
        status.setText("Status: "+criterias.getStatus());
        cname.setText(criterias.getCriteria_Name());
        cpercent.setText(criterias.getCriteria_Percentage() + "%");
        designation.setText(criterias.getEvent_Name());
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

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateCriteria.class);
                intent.putExtra("cid", String.valueOf(criterias.getCriteria_ID()));
                intent.putExtra("cname", criterias.getCriteria_Name());
                intent.putExtra("cpercent", String.valueOf(criterias.getCriteria_Percentage()));
                intent.putExtra("ename", criterias.getEvent_Name());
                context.startActivity(intent);
                if(behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Remove(String.valueOf(criterias.getCriteria_ID()), position);
            }
        });

        return convertView;
    }

    private void Remove(final String ID, final int pos){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(context, "Uploading", "Please Wait...",true,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Reset UI
                //Toast.makeText(view.getContext().getApplicationContext(),s,Toast.LENGTH_LONG).show();
                if (s != null){
                    Toast.makeText(context,s,Toast.LENGTH_LONG).show();
                    if (s.equalsIgnoreCase("Success")){
                        criteria.remove(pos);
                        notifyDataSetChanged();
                    }
                }
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("ID", ID);
                data.put("table_name", "criteria");
                data.put("column_id", "cid");
                String result = rh.sendPostRequest("http://" + GetServerIP() + "/psts/remove.php",data);

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

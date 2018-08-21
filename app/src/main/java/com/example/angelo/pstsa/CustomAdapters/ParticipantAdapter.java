package com.example.angelo.pstsa.CustomAdapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.BottomSheetBehavior;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angelo.pstsa.Objects.Participant;
import com.example.angelo.pstsa.R;
import com.example.angelo.pstsa.RequestHandler.RequestHandler;
import com.example.angelo.pstsa.SQLiteDatabase.DatabaseHelper;
import com.example.angelo.pstsa.UpdateMethods.UpdateParticipant;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class ParticipantAdapter extends BaseAdapter {
    Context context;
    ArrayList<Participant> participants;
    View bottomSheet;
    DatabaseHelper db;
    public ParticipantAdapter(Context context, ArrayList<Participant> participants, View bottomSheet){
        this.context = context;
        this.participants = participants;
        this.bottomSheet = bottomSheet;
    }

    @Override
    public int getCount() {
        return participants.size();
    }

    @Override
    public Object getItem(int position) {
        return participants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Participant participant = participants.get(position);
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_participant_list, null);
        }
        TextView num, name, course, age, gender, height, stat;
        ImageView avatar;
        Button btnEdit, btnRemove;
        db = new DatabaseHelper(context);
        btnEdit = (Button) convertView.findViewById(R.id.btnEdit);
        btnRemove = (Button) convertView.findViewById(R.id.btnRemove);
        avatar = (ImageView) convertView.findViewById(R.id.avatar);
        num = (TextView) convertView.findViewById(R.id.txtNum);
        name = (TextView) convertView.findViewById(R.id.txtName);
        age = (TextView) convertView.findViewById(R.id.txtAge);
        gender = (TextView) convertView.findViewById(R.id.txtGender);
        height = (TextView) convertView.findViewById(R.id.txtHeight);
        course = (TextView) convertView.findViewById(R.id.txtCourse);
        stat = (TextView) convertView.findViewById(R.id.txtStats);
        Bitmap bitmap = null;
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
        /*Picasso.with(context)
                .load(participant.getP_imgurl() + participant.getP_Image() + participant.getP_imgfolder())
                .placeholder(R.mipmap.person)
                .resize(400,500)
                .centerCrop()
                .into(avatar);
                */
        if (participant.getP_Gender().equals("Male")){
            Picasso.get()
                    .load("http://" + participant.getP_imgurl() + participant.getP_imgfolder())
                    .placeholder(R.mipmap.person)
                    .resize(768, 1280)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .error(R.mipmap.person)
                    .into(avatar);
        }else{
            Picasso.get()
                    .load("http://" + participant.getP_imgurl() + participant.getP_imgfolder())
                    .placeholder(R.mipmap.female)
                    .resize(768, 1280)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .error(R.mipmap.female)
                    .into(avatar);
        }


        num.setText("#: "+participant.getP_Number()+"");
        name.setText(participant.getP_Name()+"");
        course.setText(participant.getP_Course()+"");
        age.setText(participant.getP_Age()+"");
        gender.setText(participant.getP_Gender()+"");
        height.setText(participant.getP_Height()+"");
        stat.setText(participant.getP_Vstat()+"");

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateParticipant.class);
                intent.putExtra("pid", participant.getP_ID());
                intent.putExtra("num", participant.getP_Number());
                intent.putExtra("name", participant.getP_Name());
                intent.putExtra("course", participant.getP_Course());
                intent.putExtra("age", participant.getP_Age());
                intent.putExtra("gender", participant.getP_Gender());
                intent.putExtra("height", participant.getP_Height());
                intent.putExtra("stat", participant.getP_Vstat());
                intent.putExtra("img", "http://" + participant.getP_imgurl() + participant.getP_imgfolder());
                context.startActivity(intent);
                if(behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Remove(String.valueOf(participant.getP_ID()), position);
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
                        participants.remove(pos);
                        notifyDataSetChanged();
                    }
                }
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("ID", ID);
                data.put("table_name", "participant");
                data.put("column_id", "pid");
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

package com.example.angelo.pstsa.UpdateMethods;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.angelo.pstsa.R;
import com.example.angelo.pstsa.RequestHandler.RequestHandler;
import com.example.angelo.pstsa.SQLiteDatabase.DatabaseHelper;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class UpdateParticipant extends AppCompatActivity {
    Button btnSelectImage, btnSave;
    TextInputEditText pnumber, pname, pcourse, page, pheight, pstatus;
    Spinner pgender;
    DatabaseHelper db;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap = null;
    ImageView avatar;
    private Uri filePath;
    //Value pass
    int pid, age, num;
    String name, course, gender, height, stat, img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant);
        pnumber = (TextInputEditText) findViewById(R.id.pnumber);
        pname = (TextInputEditText) findViewById(R.id.pname);
        pcourse = (TextInputEditText) findViewById(R.id.pcourse);
        page = (TextInputEditText) findViewById(R.id.page);
        pstatus = (TextInputEditText) findViewById(R.id.pstatus);
        pheight = (TextInputEditText) findViewById(R.id.pheight);
        pgender = (Spinner) findViewById(R.id.pgender);
        btnSelectImage = (Button) findViewById(R.id.btnSelectImage);
        btnSave = (Button) findViewById(R.id.btnSave);
        avatar = (ImageView) findViewById(R.id.avatarIcon);
        //SQLite DB
        db = new DatabaseHelper(this);
        //Hide
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        pid = getIntent().getExtras().getInt("pid");
        age = getIntent().getExtras().getInt("age");
        num = getIntent().getExtras().getInt("num");
        name = getIntent().getExtras().getString("name");
        course = getIntent().getExtras().getString("course");
        gender = getIntent().getExtras().getString("gender");
        height = getIntent().getExtras().getString("height");
        stat = getIntent().getExtras().getString("stat");
        img = getIntent().getExtras().getString("img");

        pnumber.setText(num+"");
        pname.setText(name);
        pcourse.setText(course);
        if (gender == "Male"){
            pgender.setSelection(0);
        }else if(gender == "Female"){
            pgender.setSelection(1);
        }
        pheight.setText(height);
        pstatus.setText(stat);
        page.setText(age+"");
        Picasso.get()
                .load(img)
                .placeholder(R.mipmap.person)
                .error(R.mipmap.person)
                .into(avatar);

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        btnSave.setText("Update");
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateParticipant();
            }
        });


    }

    private void showFileChooser() {
        String[] mimeTypes = {"image/jpeg", "image/png"};
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT)
                .setType("image/*")
                .putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(UpdateParticipant.this.getContentResolver(), filePath);
                avatar.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void updateParticipant(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UpdateParticipant.this, "Updating", "Please wait...",true,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Toast.makeText(UpdateParticipant.this,GetServerIP(),Toast.LENGTH_LONG).show();
                if (s != null){
                    AlertDialog.Builder alert = new AlertDialog.Builder(UpdateParticipant.this);
                    alert.setTitle("Pageant Scoring and Tabulation System");
                    alert.setMessage(s.toString());
                    alert.setCancelable(true);
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UpdateParticipant.this.finish();
                        }
                    });
                    AlertDialog build = alert.create();
                    build.show();
                }
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String result = "";
                if (bitmap != null){
                    String uploadImage = getStringImage(bitmap);

                    HashMap<String,String> data = new HashMap<>();
                    data.put("pid", String.valueOf(pid));
                    data.put("pnum", pnumber.getText().toString());
                    data.put("pname", pname.getText().toString());
                    data.put("pcourse", pcourse.getText().toString());
                    data.put("page", page.getText().toString());
                    data.put("pgender", pgender.getSelectedItem().toString());
                    data.put("pheight", pheight.getText().toString());
                    data.put("pstat", pstatus.getText().toString());
                    data.put("img", uploadImage);
                    result = rh.sendPostRequest("http://" + GetServerIP() + "/psts/update_participant.php",data);

                }else {
                    //String uploadImage = getStringImage(bitmap);

                    HashMap<String,String> data = new HashMap<>();
                    data.put("pid", String.valueOf(pid));
                    data.put("pnum", pnumber.getText().toString());
                    data.put("pname", pname.getText().toString());
                    data.put("pcourse", pcourse.getText().toString());
                    data.put("page", page.getText().toString());
                    data.put("pgender", pgender.getSelectedItem().toString());
                    data.put("pheight", pheight.getText().toString());
                    data.put("pstat", pstatus.getText().toString());
                    data.put("img", "");
                    result = rh.sendPostRequest("http://" + GetServerIP() + "/psts/update_participant.php",data);

                }
                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
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

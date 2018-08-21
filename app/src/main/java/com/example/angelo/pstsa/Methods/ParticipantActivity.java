package com.example.angelo.pstsa.Methods;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.angelo.pstsa.GetParticipant.ParticipantSenderReceiver;
import com.example.angelo.pstsa.R;
import com.example.angelo.pstsa.RequestHandler.RequestHandler;
import com.example.angelo.pstsa.SQLiteDatabase.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class ParticipantActivity extends Fragment {
    public View view, bottomSheet;
    Button btnSelectImage, btnSave;
    Spinner pgender, filterParticipant;
    TextInputEditText pnumber, pname, pcourse, page, pheight, pstatus;
    DatabaseHelper db;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap = null;
    ImageView avatar;
    private Uri filePath;
    ListView participantList;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_participant, container, false);
        bottomSheet = (View) view.findViewById(R.id.bSheet);
        pnumber = (TextInputEditText) view.findViewById(R.id.pnumber);
        pname = (TextInputEditText) view.findViewById(R.id.pname);
        pcourse = (TextInputEditText) view.findViewById(R.id.pcourse);
        page = (TextInputEditText) view.findViewById(R.id.page);
        pstatus = (TextInputEditText) view.findViewById(R.id.pstatus);
        pheight = (TextInputEditText) view.findViewById(R.id.pheight);
        pgender = (Spinner) view.findViewById(R.id.pgender);
        btnSelectImage = (Button) view.findViewById(R.id.btnSelectImage);
        btnSave = (Button) view.findViewById(R.id.btnSave);
        avatar = (ImageView) view.findViewById(R.id.avatarIcon);
        //Bottom Sheet
        filterParticipant = (Spinner) view.findViewById(R.id.filterParticipant);
        participantList = (ListView) view.findViewById(R.id.participantList);
        //SQLite DB
        db = new DatabaseHelper(view.getContext());
        //
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmap == null){
                    AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                    alert.setTitle("Warning");
                    alert.setMessage("Please select image");
                    alert.setCancelable(true);

                    AlertDialog build = alert.create();
                    build.show();
                }else{
                    if (ValidateFields().equals("")){
                        //Upload participant info
                        uploadImage();
                    }else{
                        AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                        alert.setTitle("Pageant Scoring and Tabulation System");
                        alert.setMessage(ValidateFields());
                        alert.setCancelable(true);

                        AlertDialog build = alert.create();
                        build.show();
                    }
                }
            }
        });
/*
        pgender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (bitmap != null){

                }else{
                    switch (pgender.getSelectedItem().toString()){
                        case "Male":
                            Bitmap male = BitmapFactory.decodeResource(view.getContext().getResources(),
                                    R.mipmap.person);
                            avatar.setImageBitmap(male);
                            bitmap = male;
                            break;
                        case "Female":
                            Bitmap female = BitmapFactory.decodeResource(view.getContext().getResources(),
                                    R.mipmap.female);
                            avatar.setImageBitmap(female);
                            bitmap = female;
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/
        final BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        //Event Listeners
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
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
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        //Method for bottomsheet show/hide
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    new ParticipantSenderReceiver(ParticipantActivity.this.getActivity(),
                            "http://" + GetServerIP() + "/psts/select_participant.php",
                            filterParticipant.getSelectedItem().toString(),
                            participantList,
                            bottomSheet).execute();
                }else {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
        filterParticipant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new ParticipantSenderReceiver(ParticipantActivity.this.getActivity(),
                        "http://" + GetServerIP() + "/psts/select_participant.php",
                        filterParticipant.getSelectedItem().toString(),
                        participantList,
                        bottomSheet).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
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
                bitmap = MediaStore.Images.Media.getBitmap(view.getContext().getContentResolver(), filePath);
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

    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(view.getContext(), "Uploading", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(view.getContext(),GetServerIP(),Toast.LENGTH_LONG).show();
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setTitle("Pageant Scoring and Tabulation System");
                alert.setMessage(s.toString());
                alert.setCancelable(true);

                AlertDialog build = alert.create();
                build.show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();
                data.put("pnum", pnumber.getText().toString());
                data.put("pname", pname.getText().toString());
                data.put("pcourse", pcourse.getText().toString());
                data.put("page", page.getText().toString());
                data.put("pgender", pgender.getSelectedItem().toString());
                data.put("pheight", pheight.getText().toString());
                data.put("pstat", pstatus.getText().toString());
                data.put("imgip", GetServerIP());
                data.put("img", uploadImage);
                String result = rh.sendPostRequest("http://" + GetServerIP() + "/psts/save_participant.php",data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    public String ValidateFields(){
        String msg = "";
        if (pnumber.getText().toString().equals("") &&
                !pname.getText().toString().equals("") &&
                !page.getText().toString().equals("")){
            msg = "Participant number is required";
        }else if (!pnumber.getText().toString().equals("") &&
                pname.getText().toString().equals("") &&
                !page.getText().toString().equals("")){
            msg = "Participant name is required";
        }else if (!pnumber.getText().toString().equals("") &&
                !pname.getText().toString().equals("") &&
                page.getText().toString().equals("")){
            msg = "Participant age is required";
        }

        return msg;
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

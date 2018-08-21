package com.example.angelo.pstsa.Methods;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.angelo.pstsa.BuildConfig;
import com.example.angelo.pstsa.CustomAdapters.CustomListView5;
import com.example.angelo.pstsa.R;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static android.provider.ContactsContract.Directory.PACKAGE_NAME;

public class FileActivity extends AppCompatActivity {
    ListView fileList;
    private File[] imagelist;
    String[] pdflist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        fileList = (ListView) findViewById(R.id.fileList);
        RetrieveFile();

        fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File myDir = new File(Environment.getExternalStorageDirectory() + "/PSTS");
                myDir.mkdirs();
                File file = new File(myDir, "/" + fileList.getItemAtPosition(position).toString());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri fileUri = FileProvider.getUriForFile(FileActivity.this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        file);
                getApplicationContext().grantUriPermission(PACKAGE_NAME, fileUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(fileUri, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                //Toast.makeText(FileActivity.this, fileUri+"", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void RetrieveFile() {
        ArrayList<String> inFiles = new ArrayList();
        File images = new File(Environment.getExternalStorageDirectory() + "/PSTS/");
        imagelist = images.listFiles(new FilenameFilter(){
            public boolean accept(File dir, String name)
            {
                return ((name.endsWith(".pdf")));
            }
        });
        //pdflist = new String[imagelist.length];
        for(int i = 0;i<imagelist.length;i++)
        {
            inFiles.add(imagelist[i].getName());
        }
        fileList.setAdapter(new CustomListView5(this, 0, inFiles, fileList));
    }
}

package com.example.angelo.pstsa.CustomAdapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.angelo.pstsa.R;

import java.io.File;
import java.util.List;

public class CustomListView5 extends ArrayAdapter<String> {
    private Context context;
    private List<String> list;
    ListView lv;
    public CustomListView5(Context context, int resource, List<String> objects, ListView lv) {
        super(context, resource, objects);
        this.context = context;
        this.list = objects;
        this.lv = lv;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_file_list, null);
        }
        TextView filename = (TextView) convertView.findViewById(R.id.name);
        filename.setText(list.get(position));

        return convertView;
    }


}

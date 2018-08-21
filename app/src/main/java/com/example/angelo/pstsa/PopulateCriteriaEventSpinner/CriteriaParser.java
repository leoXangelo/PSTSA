package com.example.angelo.pstsa.PopulateCriteriaEventSpinner;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.angelo.pstsa.CustomAdapters.CriteriaAdapter;
import com.example.angelo.pstsa.Objects.Criteria;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by MastahG on 3/19/2018.
 */

public class CriteriaParser extends AsyncTask<Void,Void,Integer> {

    Context c;
    String data;
    ArrayList<String> names = new ArrayList<>();
    Spinner eventName;
    ArrayList<Criteria> criterias = new ArrayList<>();

    public CriteriaParser(Context c, String data, Spinner eventName) {
        this.c = c;
        this.data = data;
        this.eventName = eventName;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... params) {
        return this.parse();
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);

        if(integer==1)
        {
            //BIND TO LISTVIEW
            ArrayAdapter adapter=new ArrayAdapter(c,android.R.layout.simple_list_item_1,names);
            eventName.setAdapter(adapter);
            //CriteriaAdapter adapter = new CriteriaAdapter(c, criterias, bottomSheet);
            //lv.setAdapter(adapter);

        }else {
            Toast.makeText(c,"Unable to Parse",Toast.LENGTH_SHORT).show();
        }
    }

    private int parse()
    {
        //URL url = null;
        try
        {
            JSONArray ja=new JSONArray(data);
            JSONObject jo=null;

            criterias.clear();
            Criteria criteria;

            for(int i=0;i<ja.length();i++)
            {
                jo=ja.getJSONObject(i);
                //int id = jo.getInt("cid");
                //String cname = jo.getString("cname");
                //int cpercent = jo.getInt("cpercent");
                String ename = jo.getString("ename");
                //String cstatus = jo.getString("cstatus");

                criteria = new Criteria();

                //event.setEvent_ID(id);
                //event.setEvent_Name(ename);
                //event.setEvent_Date(edate);
                //event.setEvent_Place(eplace);
                //criteria.setCriteria_ID(id);
                //criteria.setCriteria_Name(cname);
                //criteria.setCriteria_Percentage(cpercent);
                criteria.setEvent_Name(ename);
                //criteria.setStatus(cstatus);

                //criterias.add(criteria);
                //listEvent.add(ename);
                names.add(ename);
            }

            return 1;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
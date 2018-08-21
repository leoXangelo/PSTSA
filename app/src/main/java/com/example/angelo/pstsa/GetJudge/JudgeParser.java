package com.example.angelo.pstsa.GetJudge;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.angelo.pstsa.CustomAdapters.CriteriaAdapter;
import com.example.angelo.pstsa.CustomAdapters.JudgeAdapter;
import com.example.angelo.pstsa.Objects.Criteria;
import com.example.angelo.pstsa.Objects.Judge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by MastahG on 3/19/2018.
 */

public class JudgeParser extends AsyncTask<Void,Void,Integer> {

    Context c;
    String data;
    ListView lv;
    ArrayList<Judge> judges = new ArrayList<>();

    public JudgeParser(Context c, String data, ListView lv) {
        this.c = c;
        this.data = data;
        this.lv = lv;
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
            //ArrayAdapter adapter=new ArrayAdapter(c,android.R.layout.simple_list_item_1,names);
            JudgeAdapter adapter = new JudgeAdapter(c, judges);
            lv.setAdapter(adapter);

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

            judges.clear();
            Judge judge;

            for(int i=0;i<ja.length();i++)
            {
                jo=ja.getJSONObject(i);
                int id = jo.getInt("aid");
                String aname = jo.getString("aname");
                String atype = jo.getString("atype");
                String astatus = jo.getString("astatus");
                String alogin = jo.getString("alogin");

                judge = new Judge();

                judge.setID(id);
                judge.setJudge_name(aname);
                judge.setJudge_type(atype);
                judge.setJudge_status(astatus);
                judge.setJudge_login(alogin);

                judges.add(judge);
                //listEvent.add(ename);
            }

            return 1;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
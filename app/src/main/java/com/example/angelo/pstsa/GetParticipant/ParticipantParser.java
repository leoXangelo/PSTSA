package com.example.angelo.pstsa.GetParticipant;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.angelo.pstsa.CustomAdapters.ParticipantAdapter;
import com.example.angelo.pstsa.Objects.Participant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MastahG on 3/19/2018.
 */

public class ParticipantParser extends AsyncTask<Void,Void,Integer> {

    Context c;
    String data;
    ListView lv;
    View bottomSheet;
    ArrayList<Participant> participants = new ArrayList<>();
    public ParticipantParser(Context c, String data, ListView lv, View bottomSheet) {
        this.c = c;
        this.data = data;
        this.lv = lv;
        this.bottomSheet = bottomSheet;
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
            ParticipantAdapter adapter = new ParticipantAdapter(c, participants, bottomSheet);
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

            participants.clear();
            Participant participant;
            for(int i=0;i<ja.length();i++)
            {
                jo=ja.getJSONObject(i);
                int id = jo.getInt("pid");
                int pnum = jo.getInt("pnum");
                String pname = jo.getString("pname");
                String pcourse = jo.getString("pcourse");
                int page = jo.getInt("page");
                String pgender = jo.getString("pgender");
                String pheight = jo.getString("pheight");
                String pvstat = jo.getString("pstat");
                String imgurl = jo.getString("imgip");
                String imgfolder = jo.getString("imgdirectory");

                //url = new URL(jo.getString("pimg"));
                //Bitmap imgUrl = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                //ByteArrayOutputStream stream = new ByteArrayOutputStream();
                //imgUrl.compress(Bitmap.CompressFormat.PNG, 100, stream);
                participant = new Participant();

                //event.setEvent_ID(id);
                //event.setEvent_Name(ename);
                //event.setEvent_Date(edate);
                //event.setEvent_Place(eplace);
                participant.setP_ID(id);
                participant.setP_Number(pnum);
                participant.setP_Name(pname);
                participant.setP_Course(pcourse);
                participant.setP_Age(page);
                participant.setP_Gender(pgender);
                participant.setP_Height(pheight);
                participant.setP_Vstat(pvstat);
                participant.setP_imgurl(imgurl);
                participant.setP_imgfolder(imgfolder);

                participants.add(participant);
                //listEvent.add(ename);
            }

            return 1;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
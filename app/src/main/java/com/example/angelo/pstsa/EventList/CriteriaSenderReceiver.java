package com.example.angelo.pstsa.EventList;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.angelo.pstsa.MysqlConnect.Connector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

/**
 * Created by MastahG on 3/19/2018.
 */

public class CriteriaSenderReceiver extends AsyncTask<Void,Void,String> {

    Context c;
    String urlAddress;
    String query;
    ListView lv;
    View bottomSheet;
    ProgressDialog pd;
    Spinner eventList;
    public CriteriaSenderReceiver(Context c, String urlAddress, String query, Spinner eventList) {
        this.c = c;
        this.urlAddress = urlAddress;
        this.query = query;
        this.eventList = eventList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pd=new ProgressDialog(c);
        pd.setTitle("Pageant Scoring and Tabulation System");
        pd.setMessage("Please wait...");
        pd.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        return this.sendAndReceive();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        pd.dismiss();

        //RESET LISTVIEW
        //lv.setAdapter(null);

        if(s != null)
        {
            if(!s.contains("null"))
            {
                CriteriaParser p = new CriteriaParser(c, s, eventList);
                p.execute();

                //noNetworkImg.setVisibility(View.INVISIBLE);
                //noDataImg.setVisibility(View.INVISIBLE);

            }else
            {
                //noNetworkImg.setVisibility(View.INVISIBLE);
                //noDataImg.setVisibility(View.VISIBLE);
                Toast.makeText(c,"No Data ",Toast.LENGTH_SHORT).show();
            }
        }else {
            //.setVisibility(View.VISIBLE);
            //noDataImg.setVisibility(View.INVISIBLE);
            Toast.makeText(c,"No network",Toast.LENGTH_SHORT).show();
        }

    }

    private String sendAndReceive()
    {
        HttpURLConnection con= Connector.connect(urlAddress);
        if(con==null)
        {
            return null;
        }

        try
        {
            OutputStream os=con.getOutputStream();

            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os));
            bw.write(new DataPackager(query).packageData());

            bw.flush();

            //RELEASE RES
            bw.close();
            os.close();

            //SOME RESPONSE ????
            int responseCode=con.getResponseCode();

            //DECODE
            if(responseCode==con.HTTP_OK)
            {
                //RETURN SOME DATA stream
                InputStream is=con.getInputStream();

                //READ IT
                BufferedReader br=new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response=new StringBuffer();

                if(br != null)
                {
                    while ((line=br.readLine()) != null)
                    {
                        response.append(line+"n");
                    }

                }else {
                    return null;
                }

                return response.toString();

            }else {
                return String.valueOf(responseCode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
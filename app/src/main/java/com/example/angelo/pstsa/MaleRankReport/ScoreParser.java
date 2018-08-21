package com.example.angelo.pstsa.MaleRankReport;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import com.example.angelo.pstsa.Formatter.DecimalFormatter;
import com.example.angelo.pstsa.Formatter.DecimalFormatter1;
import com.example.angelo.pstsa.Formatter.PercentlFormatter;
import com.example.angelo.pstsa.Objects.Graph;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by MastahG on 3/19/2018.
 */

public class ScoreParser extends AsyncTask<Void,Void,Integer> {

    Context c;
    String data;
    ListView lv;
    ArrayList<Graph> graphs = new ArrayList<>();
    ArrayList<String> judge_names = new ArrayList<>();
    HorizontalBarChart male, female;
    int judgeCount;
    public ScoreParser(Context c, String data, HorizontalBarChart male, HorizontalBarChart female, int judgeCount) {
        this.c = c;
        this.data = data;
        this.male = male;
        this.female = female;
        this.judgeCount = judgeCount;
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
            DecimalFormat twoDForm = new DecimalFormat("##.##");
            //yr.setInverted(true);
            double total_score = 0;
            Graph s;
            String tmp = "";
            int index = 0;
            ArrayList<BarEntry> scores = new ArrayList<>();
            ArrayList<String> nameList = new ArrayList<>();
            for (int x = 0; x < graphs.size(); x++){
                s = graphs.get(x);
                if (!s.getPname().equals(tmp)) {
                    nameList.add(index, s.getPname());
                    tmp = s.getPname();
                    index++;
                }
                //scores.add(new BarEntry(x, s.getTotal()));
            }


            for (int x = 0; x < nameList.size(); x++){
                //nameList.add(x, s.getPname());
                for (int i = 0; i < graphs.size(); i++){
                    s = graphs.get(i);
                    if (s.getPname().equals(nameList.get(x).toString())){
                        total_score+=s.getTotal();
                    }
                }
                double final_rank = total_score / judgeCount;
                scores.add(new BarEntry(x, (float) final_rank));
                total_score = 0;
            }

            BarDataSet dataSet = new BarDataSet(scores, "Participants");
            dataSet.setDrawValues(true);
            // Create a data object from the dataSet
            BarData data = new BarData(dataSet);
            // Format data as percentage
            data.setValueFormatter(new DecimalFormatter1());

            male.setDrawBarShadow(false);
            // if more than 60 entries are displayed in the chart, no values will be
            // drawn
            male.setMaxVisibleValueCount(60);
            // scaling can now only be done on x- and y-axis separately
            male.setPinchZoom(true);
            // draw shadows for each bar that show the maximum value
            // mChart.setDrawBarShadow(true);
            male.setDrawGridBackground(false);

            XAxis xl = male.getXAxis();
            xl.setPosition(XAxis.XAxisPosition.BOTTOM);
            xl.setValueFormatter(new IndexAxisValueFormatter(nameList));
            xl.setDrawAxisLine(true);
            xl.setDrawGridLines(false);
            xl.setGranularity(1f);
            xl.setTextSize(18f);

            YAxis yl = male.getAxisLeft();
            yl.setDrawAxisLine(true);
            yl.setDrawGridLines(true);
            yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)
            //yl.setInverted(true);

            YAxis yr = male.getAxisRight();
            yr.setDrawAxisLine(true);
            yr.setDrawGridLines(false);
            yr.setAxisMinimum(0f); // this replaces setStartAtZero(true)
            // Make the chart use the acquired data
            male.getXAxis().setLabelCount(scores.size());
            male.setData(data);
            // Display labels for bars
            //chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(nameList));
            // Set the maximum value that can be taken by the bars
            male.getAxisLeft().setAxisMaximum(nameList.size());
            // Display scores inside the bars
            male.setDrawValueAboveBar(false);
            // Hide grid lines
            male.getAxisLeft().setEnabled(false);
            male.getAxisRight().setEnabled(false);
            // Hide graph description
            male.getDescription().setEnabled(true);
            // Hide graph legend
            male.getLegend().setEnabled(true);

            // Design
            dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
            data.setValueTextSize(10f);
            data.setValueTextColor(Color.DKGRAY);

            Description description = new Description();
            description.setText("Pageant Scoring and Tabulation System");
            //setData(12, 50);
            male.setFitBars(true);
            male.setDescription(description);
            male.animateY(1200);
            dataSet.notifyDataSetChanged();
            male.notifyDataSetChanged();
            male.invalidate();
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

            graphs.clear();
            Graph graph;

            for(int i=0;i<ja.length();i++)
            {
                jo=ja.getJSONObject(i);
                int ID = jo.getInt("oid");
                String jname = jo.getString("jname");
                String pname = jo.getString("pname");
                String pgender = jo.getString("pgender");
                int rank = jo.getInt("rank");
                String score = String.valueOf(jo.getDouble("score"));
                String ename = jo.getString("ename");
                int total = jo.getInt("total");
                int jcount = jo.getInt("jcount");

                graph = new Graph();

                graph.setID(ID);
                graph.setJname(jname);
                graph.setPname(pname);
                graph.setPgender(pgender);
                graph.setRank(rank);
                graph.setScore(Float.parseFloat(score));
                graph.setEname(ename);
                graph.setTotal(total);
                judgeCount = jcount;
                graphs.add(graph);
            }

            return 1;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
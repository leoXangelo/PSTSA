package com.example.angelo.pstsa.Methods;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.angelo.pstsa.EventList.CriteriaSenderReceiver;
import com.example.angelo.pstsa.FemaleAverageReport.FemaleScoreSenderReceiver;
import com.example.angelo.pstsa.MaleAveragingReport.MaleScoreSenderReceiver;
import com.example.angelo.pstsa.R;
import com.example.angelo.pstsa.SQLiteDatabase.DatabaseHelper;
import com.github.mikephil.charting.charts.HorizontalBarChart;

public class AnalyticsActivity extends Fragment {
    public View view;
    Spinner analytic_type, eventName;
    EditText judgeCount;
    Button analyze, btnPdf;
    protected HorizontalBarChart male, female;
    DatabaseHelper db;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_analytics, container, false);
        //mChart = (HorizontalBarChart) view.findViewById(R.id.chart1);
        db = new DatabaseHelper(view.getContext());
        analytic_type = (Spinner) view.findViewById(R.id.analytics);
        eventName = (Spinner) view.findViewById(R.id.eventName);
        btnPdf = (Button) view.findViewById(R.id.btnPrint);
        analyze = (Button) view.findViewById(R.id.btnAnalyze);
        male = (HorizontalBarChart) view.findViewById(R.id.male);
        female = (HorizontalBarChart) view.findViewById(R.id.female);
        new CriteriaSenderReceiver(view.getContext(),
                "http://" + GetServerIP() + "/psts/event_list.php",
                "",
                eventName).execute();

        analyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (analytic_type.getSelectedItem().toString().equals("Averaging")){
                    new com.example.angelo.pstsa.MaleAveragingReport.MaleScoreSenderReceiver(view.getContext(),
                            "http://" + GetServerIP() + "/psts/graphical_reports_admin.php",
                            "SELECT oid, jname, pname, pgender, rank, score, ename, SUM(score) AS total, COUNT(jname) as jcount FROM overall_ranking_table WHERE pgender = 'Male' AND ename = '"+eventName.getSelectedItem().toString()+"' GROUP BY pname ORDER BY SUM(score) ASC;",
                            male,
                            female,
                            1).execute();
                    new com.example.angelo.pstsa.FemaleAverageReport.FemaleScoreSenderReceiver(view.getContext(),
                            "http://" + GetServerIP() + "/psts/graphical_reports_admin.php",
                            "SELECT oid, jname, pname, pgender, rank, score, ename, SUM(score) AS total, COUNT(jname) as jcount FROM overall_ranking_table WHERE pgender = 'Female' AND ename = '"+eventName.getSelectedItem().toString()+"' GROUP BY pname ORDER BY SUM(score) ASC;",
                            male,
                            female,
                            1).execute();
                }else{
                    new com.example.angelo.pstsa.MaleRankReport.MaleScoreSenderReceiver(view.getContext(),
                            "http://" + GetServerIP() + "/psts/graphical_reports_admin.php",
                            "SELECT oid, jname, pname, pgender, rank, score, ename, SUM(rank) AS total, COUNT(jname) as jcount FROM overall_ranking_table WHERE pgender = 'Male' AND ename = '"+eventName.getSelectedItem().toString()+"' GROUP BY pname ORDER BY SUM(rank) DESC;",
                            male,
                            female,
                            1).execute();
                    new com.example.angelo.pstsa.FemaleRankReport.MaleScoreSenderReceiver(view.getContext(),
                            "http://" + GetServerIP() + "/psts/graphical_reports_admin.php",
                            "SELECT oid, jname, pname, pgender, rank, score, ename, SUM(rank) AS total, COUNT(jname) as jcount FROM overall_ranking_table WHERE pgender = 'Female' AND ename = '"+eventName.getSelectedItem().toString()+"' GROUP BY pname ORDER BY SUM(rank) DESC;",
                            male,
                            female,
                            1).execute();
                }
            }
        });

        btnPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (analytic_type.getSelectedItem().toString().equalsIgnoreCase("Averaging")){
                    new com.example.angelo.pstsa.AveragingPdf.RankingSenderReceiver(AnalyticsActivity.this.getActivity(),
                            "http://" + GetServerIP() + "/psts/pdf_rank.php",
                            "SELECT participant.pnum, participant.pname, SUM(overall_ranking_table.score) as Average, COUNT(DISTINCT overall_ranking_table.jname) AS count FROM participant INNER JOIN overall_ranking_table ON participant.pname = overall_ranking_table.pname WHERE overall_ranking_table.pgender = 'Male' GROUP BY overall_ranking_table.pname ORDER BY Average DESC;",
                            "Male",
                            eventName.getSelectedItem().toString()).execute();
                    new com.example.angelo.pstsa.AveragingPdf.RankingSenderReceiver(AnalyticsActivity.this.getActivity(),
                            "http://" + GetServerIP() + "/psts/pdf_rank.php",
                            "SELECT participant.pnum, participant.pname, SUM(overall_ranking_table.score) as Average, COUNT(DISTINCT overall_ranking_table.jname) AS count FROM participant INNER JOIN overall_ranking_table ON participant.pname = overall_ranking_table.pname WHERE overall_ranking_table.pgender = 'Female' GROUP BY overall_ranking_table.pname ORDER BY Average DESC;",
                            "Female",
                            eventName.getSelectedItem().toString()).execute();
                }else{
                    new RankingSenderReceiver(AnalyticsActivity.this.getActivity(),
                            "http://" + GetServerIP() + "/psts/pdf_rank.php",
                            "SELECT participant.pnum, participant.pname, SUM(overall_ranking_table.rank) as Rank FROM participant INNER JOIN overall_ranking_table ON participant.pname = overall_ranking_table.pname WHERE overall_ranking_table.pgender = 'Male' GROUP BY overall_ranking_table.pname ORDER BY Rank ASC;",
                            "Male",
                            eventName.getSelectedItem().toString()).execute();
                    new RankingSenderReceiver(AnalyticsActivity.this.getActivity(),
                            "http://" + GetServerIP() + "/psts/pdf_rank.php",
                            "SELECT participant.pnum, participant.pname, SUM(overall_ranking_table.rank) as Rank FROM participant INNER JOIN overall_ranking_table ON participant.pname = overall_ranking_table.pname WHERE overall_ranking_table.pgender = 'Female' GROUP BY overall_ranking_table.pname ORDER BY Rank ASC;",
                            "Female",
                            eventName.getSelectedItem().toString()).execute();
                }
                */
                new com.example.angelo.pstsa.JudgePdfMaleRanking.RankingSenderReceiver(AnalyticsActivity.this.getActivity(),
                        "http://" + GetServerIP() + "/psts/pdf_rank.php",
                        "SELECT participant.pnum, overall_ranking_table.jname, overall_ranking_table.pname, overall_ranking_table.pgender, overall_ranking_table.rank, overall_ranking_table.score FROM participant INNER JOIN overall_ranking_table ON participant.pname = overall_ranking_table.pname WHERE overall_ranking_table.pgender = 'Male' GROUP BY participant.pnum, overall_ranking_table.jname;",
                        "",
                        eventName.getSelectedItem().toString()).execute();
                new com.example.angelo.pstsa.JudgePdfFemaleRanking.RankingSenderReceiver(AnalyticsActivity.this.getActivity(),
                        "http://" + GetServerIP() + "/psts/pdf_rank.php",
                        "SELECT participant.pnum, overall_ranking_table.jname, overall_ranking_table.pname, overall_ranking_table.pgender, overall_ranking_table.rank, overall_ranking_table.score FROM participant INNER JOIN overall_ranking_table ON participant.pname = overall_ranking_table.pname WHERE overall_ranking_table.pgender = 'Female' GROUP BY participant.pnum, overall_ranking_table.jname;",
                        "",
                        eventName.getSelectedItem().toString()).execute();
                new com.example.angelo.pstsa.JudgePdfMaleAveraging.RankingSenderReceiver(AnalyticsActivity.this.getActivity(),
                        "http://" + GetServerIP() + "/psts/pdf_rank.php",
                        "SELECT participant.pnum, overall_ranking_table.jname, overall_ranking_table.pname, overall_ranking_table.pgender, overall_ranking_table.rank, overall_ranking_table.score FROM participant INNER JOIN overall_ranking_table ON participant.pname = overall_ranking_table.pname WHERE overall_ranking_table.pgender = 'Male' GROUP BY participant.pnum, overall_ranking_table.jname;",
                        "",
                        eventName.getSelectedItem().toString()).execute();
                new com.example.angelo.pstsa.JudgePdfFemaleAveraging.RankingSenderReceiver(AnalyticsActivity.this.getActivity(),
                        "http://" + GetServerIP() + "/psts/pdf_rank.php",
                        "SELECT participant.pnum, overall_ranking_table.jname, overall_ranking_table.pname, overall_ranking_table.pgender, overall_ranking_table.rank, overall_ranking_table.score FROM participant INNER JOIN overall_ranking_table ON participant.pname = overall_ranking_table.pname WHERE overall_ranking_table.pgender = 'Female' GROUP BY participant.pnum, overall_ranking_table.jname;",
                        "",
                        eventName.getSelectedItem().toString()).execute();

                Intent intent = new Intent(AnalyticsActivity.this.getActivity(), FileActivity.class);
                startActivity(intent);
                /*
                new com.example.angelo.pstsa.JudgePdfFemaleRanking.RankingSenderReceiver(AnalyticsActivity.this.getActivity(),
                        "http://" + GetServerIP() + "/psts/pdf_rank.php",
                        "SELECT participant.pnum, overall_ranking_table.jname, overall_ranking_table.pname, overall_ranking_table.pgender, overall_ranking_table.rank, overall_ranking_table.score FROM participant INNER JOIN overall_ranking_table ON participant.pname = overall_ranking_table.pname WHERE overall_ranking_table.pgender = 'Female' GROUP BY overall_ranking_table.pname, overall_ranking_table.jname ORDER BY SUM(overall_ranking_table.rank) ASC;",
                        "",
                        eventName.getSelectedItem().toString()).execute();
                new com.example.angelo.pstsa.JudgePdfMaleAveraging.RankingSenderReceiver(AnalyticsActivity.this.getActivity(),
                        "http://" + GetServerIP() + "/psts/pdf_rank.php",
                        "SELECT participant.pnum, overall_ranking_table.jname, overall_ranking_table.pname, overall_ranking_table.pgender, overall_ranking_table.rank, overall_ranking_table.score FROM participant INNER JOIN overall_ranking_table ON participant.pname = overall_ranking_table.pname WHERE overall_ranking_table.pgender = 'Male' GROUP BY overall_ranking_table.pname, overall_ranking_table.jname ORDER BY SUM(overall_ranking_table.score) DESC;",
                        "",
                        eventName.getSelectedItem().toString()).execute();

                new com.example.angelo.pstsa.JudgePdfFemaleAveraging.RankingSenderReceiver(AnalyticsActivity.this.getActivity(),
                        "http://" + GetServerIP() + "/psts/pdf_rank.php",
                        "SELECT participant.pnum, overall_ranking_table.jname, overall_ranking_table.pname, overall_ranking_table.pgender, overall_ranking_table.rank, overall_ranking_table.score FROM participant INNER JOIN overall_ranking_table ON participant.pname = overall_ranking_table.pname WHERE overall_ranking_table.pgender = 'Female' GROUP BY overall_ranking_table.pname, overall_ranking_table.jname ORDER BY SUM(overall_ranking_table.score) DESC;",
                        "",
                        eventName.getSelectedItem().toString()).execute();
                        */
            }
        });
        return view;
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

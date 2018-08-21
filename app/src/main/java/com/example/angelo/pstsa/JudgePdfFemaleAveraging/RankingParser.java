package com.example.angelo.pstsa.JudgePdfFemaleAveraging;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.example.angelo.pstsa.Objects.JudgeReport;
import com.example.angelo.pstsa.SQLiteDatabase.DatabaseHelper;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by MastahG on 3/19/2018.
 */

public class RankingParser extends AsyncTask<Void,Void,Integer> {

    Context c;
    String data;
    ArrayList<JudgeReport> judgeReports = new ArrayList<>();
    ArrayList<String> judge_names = new ArrayList<>();
    ArrayList<String> participant_names = new ArrayList<>();
    ArrayList<Integer> participant_number = new ArrayList<>();
    String gender;
    String eventName;
    DatabaseHelper db;
    public RankingParser(Context c, String data, String gender, String eventName) {
        this.c = c;
        this.data = data;
        this.gender = gender;
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
            try{
                DecimalFormat twoDForm = new DecimalFormat("##.##");
                Set<String> judge_list = new LinkedHashSet<>(judge_names);
                Set<String> participant_list = new LinkedHashSet<>(participant_names);
                Set<Integer> participant_numbers = new LinkedHashSet<>(participant_number);
                ArrayList<Integer> getNumber = new ArrayList<>(participant_numbers);
                db = new DatabaseHelper(c);
                JudgeReport judgeReport;
                int x = 0, number;
                int trank = 0;
                int res = db.deleteFemaleAverage("Female");
                if (res > 0){
                    //Toast.makeText(c, "Success", Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(c, "No data deleted", Toast.LENGTH_SHORT).show();
                }
                for (String name : participant_list){
                    number = getNumber.get(x);
                    for (int i = 0; i < judgeReports.size(); i++){
                        judgeReport = judgeReports.get(i);
                        if (judgeReport.getPname().equals(name)){
                            trank += judgeReport.getScore();
                        }
                    }
                    Boolean result = db.SaveFemaleAverage(number, name, "Female", trank, 0);
                    if (result == true){

                    }else {

                    }
                    x++;
                    trank = 0;
                }
            }catch (Exception e){
                Toast.makeText(c, e+"", Toast.LENGTH_SHORT).show();
            }

            int rank = 1;
            int tmp = 0;
            int loop = 0;
            boolean initial = false;
            Cursor res = db.GetFemaleAverage();
            if (res != null && res.getCount() > 0){
                while (res.moveToNext()){
                    if (res.getInt(4) == tmp){
                        db.UpdateFemaleAverage(String.valueOf(res.getInt(1)), rank);
                        loop++;
                    }else{
                        if (initial == false){
                            db.UpdateFemaleAverage(String.valueOf(res.getInt(1)), rank);
                            initial = true;
                        }else{
                            if (loop > 0){
                                rank = rank + (loop + 1);
                                loop = 0;
                            }else{
                                rank++;
                            }
                            db.UpdateFemaleAverage(String.valueOf(res.getInt(1)), rank);
                        }
                    }
                    tmp = res.getInt(4);
                }
            }

            try {
                DecimalFormat twoDForm = new DecimalFormat("##.##");
                Set<String> judge_list = new LinkedHashSet<>(judge_names);
                Set<String> participant_list = new LinkedHashSet<>(participant_names);
                Document document = new Document(PageSize.LEGAL.rotate());
                PdfPTable table;
                Font col;
                Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 18);
                Font bf13 = new Font(Font.FontFamily.TIMES_ROMAN, 12);
                PdfPCell[] cells;
                Font font, font1, font2;
                File file;
                String filename;
                Date date = new Date();
                String timeStamp = new SimpleDateFormat("MMMM dd, yyyy").format(date);
                filename = eventName + " Judges Score Averaging [Female] " + timeStamp;
                File myDir = new File(Environment.getExternalStorageDirectory() + "/PSTS");
                if (!myDir.exists()) {
                    myDir.mkdirs();
                }
                float[] width = new float[judge_list.size() + 5];
                for (int x = 0; x < judge_list.size() + 5; x++) {
                    width[x] = BaseField.BORDER_WIDTH_THIN;
                }
                table = new PdfPTable(width);
                table.getDefaultCell().setVerticalAlignment(8);
                table.getDefaultCell().setPadding(10.0f);
                col = new Font(Font.FontFamily.HELVETICA, 10.0f, 1);
                col.setColor(BaseColor.DARK_GRAY);
                table.addCell(new Phrase("#.", col));
                table.addCell(new Phrase("Participant", col));
                table.addCell(new Phrase("Gender", col));
                for (String judge_name : judge_list) {
                    table.addCell(new Phrase(judge_name, col));
                }
                table.addCell(new Phrase("Total", col));
                table.addCell(new Phrase("Rank", col));
                table.setHeaderRows(1);
                cells = table.getRow(0).getCells();
                BaseColor header = WebColors.getRGBColor("#26A65B");

                for (PdfPCell backgroundColor : cells) {
                    backgroundColor.setBackgroundColor(header);
                }

                Font font3 = new Font(Font.FontFamily.HELVETICA, 10.0f, 1);
                int itr = 0;
                JudgeReport jd;
                //Sub-Header
                insertCell(table, "FEMALE", Element.ALIGN_CENTER, judge_list.size() + 5, bf12);
                for (String participant : participant_list){
                    double trank = 0;
                    //Loop 1
                    for (int i =0; i < judgeReports.size(); i++) {
                        jd = judgeReports.get(i);
                        if (participant.equalsIgnoreCase(jd.getPname())) {
                            table.addCell(new Phrase("" + jd.getPnum(), font3));
                            table.addCell(new Phrase("" + participant, font3));
                            table.addCell(new Phrase("" + jd.getPgender(), font3));
                            break;
                        }
                    }
                    //Loop 2
                    int check_judge = 0;
                    for (int i =0; i < judgeReports.size(); i++){
                        jd = judgeReports.get(i);
                        if (participant.equalsIgnoreCase(jd.getPname())){
                            table.addCell(new Phrase(""+jd.getScore(), font3));
                            trank += (double)jd.getScore();
                            check_judge++;
                        }
                    }
                    if (check_judge < judge_list.size()){
                        for (int x = 0; x < judge_list.size() - check_judge; x++){
                            table.addCell(new Phrase("?", font3));
                        }
                    }
                    table.addCell(new Phrase(""+twoDForm.format(trank / judge_list.size()), font3));

                    //Algo for rank
                    Cursor res1 = db.GetFemaleAverage();
                    if (res1 != null && res1.getCount() > 0){
                        while (res1.moveToNext()){
                            if (res1.getString(2).equals(participant)){
                                if (res1.getInt(5) == 1){
                                    table.addCell(new Phrase("WINNER", font3));
                                }else if (res1.getInt(5) == 2){
                                    table.addCell(new Phrase("1st RUNNER-UP", font3));
                                }else if (res1.getInt(5) == 3){
                                    table.addCell(new Phrase("2nd RUNNER-UP", font3));
                                }else if (res1.getInt(5) == 4){
                                    table.addCell(new Phrase("3rd RUNNER-UP", font3));
                                }else{
                                    table.addCell(new Phrase(""+res1.getInt(5), font3));
                                }
                            }
                        }
                    }
                }

                insertCell(table, eventName, Element.ALIGN_CENTER, judge_list.size() + 5, bf13);

                file = new File(myDir, "/" + filename + ".pdf");
                file.createNewFile();
                try {
                    PdfWriter.getInstance(document, new FileOutputStream(file));
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                document.open();
                font = new Font(Font.FontFamily.COURIER, 18.0f, 1);
                font1 = new Font(Font.FontFamily.COURIER, 10.0f);
                font2 = new Font(Font.FontFamily.HELVETICA, 24.0f, 1);
                Paragraph datetime1 = new Paragraph("(" + timeStamp + ")" + "" + System.lineSeparator() + "" + System.lineSeparator() + "", font1);
                Paragraph paragraph = new Paragraph("Pageant Scoring and Tabulation System" + System.lineSeparator() + "", font2);
                Paragraph paragraph1 = new Paragraph("JUDGING RESULT [Averaging]" + System.lineSeparator() + "", font);
                //Paragraph BITS = new Paragraph("" + gender + System.lineSeparator(), font);
                //BITS.setAlignment(1);
                paragraph1.setAlignment(1);
                paragraph.setAlignment(1);
                datetime1.setAlignment(1);
                document.add(paragraph);
                document.add(paragraph1);
                //document.add(BITS);
                document.add(datetime1);
                document.add(table);
                document.close();
            }catch (Exception e){
                Toast.makeText(c, e+"", Toast.LENGTH_SHORT).show();
            }
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

            judgeReports.clear();
            JudgeReport judgeReport;

            for(int i=0;i<ja.length();i++)
            {
                jo=ja.getJSONObject(i);
                int pnum = jo.getInt("pnum");
                String jname = jo.getString("jname");
                String pname = jo.getString("pname");
                String pgender = jo.getString("pgender");
                int rank = jo.getInt("rank");
                double score = jo.getDouble("score");

                judgeReport = new JudgeReport();

                judgeReport.setPnum(pnum);
                judgeReport.setJname(jname);
                judgeReport.setPname(pname);
                judgeReport.setPgender(pgender);
                judgeReport.setRank(rank);
                judgeReport.setScore((float) score);

                judge_names.add(jname);
                participant_names.add(pname);
                judgeReports.add(judgeReport);
                participant_number.add(pnum);
            }

            return 1;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private void insertCell(PdfPTable table, String text, int align, int colspan, Font font){

        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        cell.setPadding(5);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        cell.setBackgroundColor(BaseColor.YELLOW);
        //in case there is no text and you wan to create an empty row
        if(text.trim().equalsIgnoreCase("")){
            cell.setMinimumHeight(15f);
        }
        //add the call to the table
        table.addCell(cell);

    }
}
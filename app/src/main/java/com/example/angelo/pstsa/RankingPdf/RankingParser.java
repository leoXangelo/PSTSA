package com.example.angelo.pstsa.RankingPdf;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.angelo.pstsa.Formatter.DecimalFormatter;
import com.example.angelo.pstsa.Objects.Graph;
import com.example.angelo.pstsa.Objects.Rank;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
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

import static android.content.ContentValues.TAG;

/**
 * Created by MastahG on 3/19/2018.
 */

public class RankingParser extends AsyncTask<Void,Void,Integer> {

    Context c;
    String data;
    ArrayList<Rank> ranks = new ArrayList<>();
    String gender;
    String eventName;
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
                DecimalFormat twoDForm = new DecimalFormat("##.####");
                Document document = new Document();
                PdfPTable table;
                Font col;
                PdfPCell[] cells;
                Font font, font1, font2;
                File file;
                String filename;
                Date date = new Date();
                String timeStamp = new SimpleDateFormat("MMMM dd, yyyy").format(date);
                filename = eventName + " Ranking " + timeStamp + " ["+gender+"]";
                File myDir = new File(Environment.getExternalStorageDirectory() + "/PSTS");
                if (!myDir.exists()) {
                    myDir.mkdirs();
                }
                table = new PdfPTable(new float[]{BaseField.BORDER_WIDTH_THIN, BaseField.BORDER_WIDTH_THIN, BaseField.BORDER_WIDTH_THIN});
                table.getDefaultCell().setVerticalAlignment(8);
                table.getDefaultCell().setPadding(10.0f);
                col = new Font(Font.FontFamily.HELVETICA, 10.0f, 1);
                col.setColor(BaseColor.WHITE);
                table.addCell(new Phrase("#.", col));
                table.addCell(new Phrase("Name", col));
                table.addCell(new Phrase("Rank", col));
                table.setHeaderRows(1);
                cells = table.getRow(0).getCells();
                for (PdfPCell backgroundColor : cells) {
                    backgroundColor.setBackgroundColor(BaseColor.DARK_GRAY);
                }
            /*
            List<Supply> rec = this.db.RetrieveAlSupply();
            font = new Font(Font.FontFamily.HELVETICA, 10.0f, 1);
            for (Supply cn : rec) {
                table.addCell(new Phrase(cn.getVarietyname(), font));
                table.addCell(new Phrase(cn.getLocalname(), font));
                table.addCell(new Phrase(cn.getType(), font));
                table.addCell(new Phrase(String.valueOf(cn.getTotalweight() + " Kg(s)"), font));
                table.addCell(new Phrase(String.valueOf(cn.getPriceperweight() + " Php"), font));
            }
            */
                Font font3 = new Font(Font.FontFamily.HELVETICA, 10.0f, 1);
                Rank rank;
                for (int x = 0; x < ranks.size(); x++){
                    rank = ranks.get(x);
                    table.addCell(new Phrase(""+rank.getPnum(), font3));
                    table.addCell(new Phrase(""+rank.getPname(), font3));
                    table.addCell(new Phrase(""+rank.getRank(), font3));
                }

                //Toast.makeText(c, ""+ranks.size(), Toast.LENGTH_SHORT).show();
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
                Paragraph paragraph = new Paragraph("Pageant Scoring and Information System" + System.lineSeparator() + "", font2);
                Paragraph paragraph1 = new Paragraph(gender + " RANKING RESULT" + System.lineSeparator() + "", font);
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
                Toast.makeText(c, "Ranking Report Created: Check Internal Storage", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Toast.makeText(c,""+e,Toast.LENGTH_SHORT).show();
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

            ranks.clear();
            Rank rank;

            for(int i=0;i<ja.length();i++)
            {
                jo=ja.getJSONObject(i);
                int pnum = jo.getInt("pnum");
                String pname = jo.getString("pname");
                int frank = jo.getInt("Rank");

                rank = new Rank();

                rank.setPnum(pnum);
                rank.setPname(pname);
                rank.setRank(frank);

                ranks.add(rank);
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
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        //in case there is no text and you wan to create an empty row
        if(text.trim().equalsIgnoreCase("")){
            cell.setMinimumHeight(10f);
        }
        //add the call to the table
        table.addCell(cell);

    }
}
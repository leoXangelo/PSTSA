package com.example.angelo.pstsa.Objects;

public class Average {
    public int Pnum;
    public String Pname;
    public float Average;

    public Average() {
    }

    public Average(int pnum, String pname, float average) {
        Pnum = pnum;
        Pname = pname;
        Average = average;
    }

    public Average(String pname, float average) {
        Pname = pname;
        Average = average;
    }

    public int getPnum() {
        return Pnum;
    }

    public void setPnum(int pnum) {
        Pnum = pnum;
    }

    public String getPname() {
        return Pname;
    }

    public void setPname(String pname) {
        Pname = pname;
    }

    public float getAverage() {
        return Average;
    }

    public void setAverage(float average) {
        Average = average;
    }
}

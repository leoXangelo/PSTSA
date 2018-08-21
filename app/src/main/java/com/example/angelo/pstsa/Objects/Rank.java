package com.example.angelo.pstsa.Objects;

public class Rank {
    public int Pnum;
    public String Pname;
    public int Rank;

    public Rank() {
    }

    public Rank(int pnum, String pname, int rank) {
        Pnum = pnum;
        Pname = pname;
        Rank = rank;
    }

    public Rank(String pname, int rank) {
        Pname = pname;
        Rank = rank;
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

    public int getRank() {
        return Rank;
    }

    public void setRank(int rank) {
        Rank = rank;
    }
}

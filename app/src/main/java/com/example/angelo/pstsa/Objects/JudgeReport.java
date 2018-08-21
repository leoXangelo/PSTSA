package com.example.angelo.pstsa.Objects;

public class JudgeReport {
    public int Pnum;
    public String Jname;
    public String Pname;
    public String Pgender;
    public int Rank;
    public float Score;

    public JudgeReport() {
    }

    public JudgeReport(int pnum, String jname, String pname, String pgender, int rank, float score) {
        Pnum = pnum;
        Jname = jname;
        Pname = pname;
        Pgender = pgender;
        Rank = rank;
        Score = score;
    }

    public JudgeReport(String jname, String pname, String pgender, int rank, float score) {
        Jname = jname;
        Pname = pname;
        Pgender = pgender;
        Rank = rank;
        Score = score;
    }

    public int getPnum() {
        return Pnum;
    }

    public void setPnum(int pnum) {
        Pnum = pnum;
    }

    public String getJname() {
        return Jname;
    }

    public void setJname(String jname) {
        Jname = jname;
    }

    public String getPname() {
        return Pname;
    }

    public void setPname(String pname) {
        Pname = pname;
    }

    public String getPgender() {
        return Pgender;
    }

    public void setPgender(String pgender) {
        Pgender = pgender;
    }

    public int getRank() {
        return Rank;
    }

    public void setRank(int rank) {
        Rank = rank;
    }

    public float getScore() {
        return Score;
    }

    public void setScore(float score) {
        Score = score;
    }
}

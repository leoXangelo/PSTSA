package com.example.angelo.pstsa.Objects;

public class Graph {
    public int ID;
    public String Jname;
    public String Pname;
    public String Pgender;
    public int Rank;
    public float Score;
    public String Ename;
    public int Total;

    public Graph() {
    }

    public Graph(int ID, String jname, String pname, String pgender, int rank, float score, String ename, int total) {
        this.ID = ID;
        Jname = jname;
        Pname = pname;
        Pgender = pgender;
        Rank = rank;
        Score = score;
        Ename = ename;
        Total = total;
    }

    public Graph(String jname, String pname, String pgender, int rank, float score, String ename, int total) {
        Jname = jname;
        Pname = pname;
        Pgender = pgender;
        Rank = rank;
        Score = score;
        Ename = ename;
        Total = total;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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

    public String getEname() {
        return Ename;
    }

    public void setEname(String ename) {
        Ename = ename;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }
}

package com.example.angelo.pstsa.Objects;

/**
 * Created by MastahG on 3/30/2018.
 */

public class ScoreSheet {
    public int id;
    public String pname;
    public String jname;
    public String pgender;
    public String cname;
    public int cpercent;
    public int score;
    public String ename;
    public float total;

    public ScoreSheet() {
    }

    public ScoreSheet(int id, String pname, String jname, String pgender, String cname, int cpercent, int score, String ename, float total) {
        this.id = id;
        this.pname = pname;
        this.jname = jname;
        this.pgender = pgender;
        this.cname = cname;
        this.cpercent = cpercent;
        this.score = score;
        this.ename = ename;
        this.total = total;
    }

    public ScoreSheet(String pname, String jname, String pgender, String cname, int cpercent, int score, String ename, float total) {
        this.pname = pname;
        this.jname = jname;
        this.pgender = pgender;
        this.cname = cname;
        this.cpercent = cpercent;
        this.score = score;
        this.ename = ename;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getJname() {
        return jname;
    }

    public void setJname(String jname) {
        this.jname = jname;
    }

    public String getPgender() {
        return pgender;
    }

    public void setPgender(String pgender) {
        this.pgender = pgender;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public int getCpercent() {
        return cpercent;
    }

    public void setCpercent(int cpercent) {
        this.cpercent = cpercent;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
}

package com.example.angelo.pstsa.Objects;

public class Judge {
    public int ID;
    public String Judge_name;
    public String Judge_type;
    public String Judge_status;
    public String Judge_login;

    public Judge() {
    }

    public Judge(int ID, String judge_name, String judge_type, String judge_status, String judge_login) {
        this.ID = ID;
        Judge_name = judge_name;
        Judge_type = judge_type;
        Judge_status = judge_status;
        Judge_login = judge_login;
    }

    public Judge(String judge_name, String judge_type, String judge_status, String judge_login) {
        Judge_name = judge_name;
        Judge_type = judge_type;
        Judge_status = judge_status;
        Judge_login = judge_login;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getJudge_name() {
        return Judge_name;
    }

    public void setJudge_name(String judge_name) {
        Judge_name = judge_name;
    }

    public String getJudge_type() {
        return Judge_type;
    }

    public void setJudge_type(String judge_type) {
        Judge_type = judge_type;
    }

    public String getJudge_status() {
        return Judge_status;
    }

    public void setJudge_status(String judge_status) {
        Judge_status = judge_status;
    }

    public String getJudge_login() {
        return Judge_login;
    }

    public void setJudge_login(String judge_login) {
        Judge_login = judge_login;
    }
}

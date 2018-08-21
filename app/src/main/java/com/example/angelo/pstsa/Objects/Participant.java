package com.example.angelo.pstsa.Objects;

/**
 * Created by Angelo on 05/03/2018.
 */

public class Participant {
    public int P_ID;
    public int P_Number;
    public String P_Name;
    public String P_Course;
    public int P_Age;
    public String P_Gender;
    public String P_Height;
    public String P_Vstat;
    public String P_Image;
    public String P_imgurl;
    public String P_imgfolder;


    public Participant() {
    }

    public Participant(int p_ID, int p_Number, String p_Name, String p_Course, int p_Age, String p_Gender, String p_Height, String p_Vstat, String p_Image, String p_imgurl, String p_imgfolder) {
        P_ID = p_ID;
        P_Number = p_Number;
        P_Name = p_Name;
        P_Course = p_Course;
        P_Age = p_Age;
        P_Gender = p_Gender;
        P_Height = p_Height;
        P_Vstat = p_Vstat;
        P_Image = p_Image;
        P_imgurl = p_imgurl;
        P_imgfolder = p_imgfolder;
    }

    public Participant(int p_Number, String p_Name, String p_Course, int p_Age, String p_Gender, String p_Height, String p_Vstat, String p_Image, String p_imgurl, String p_imgfolder) {
        P_Number = p_Number;
        P_Name = p_Name;
        P_Course = p_Course;
        P_Age = p_Age;
        P_Gender = p_Gender;
        P_Height = p_Height;
        P_Vstat = p_Vstat;
        P_Image = p_Image;
        P_imgurl = p_imgurl;
        P_imgfolder = p_imgfolder;
    }

    public int getP_ID() {
        return P_ID;
    }

    public void setP_ID(int p_ID) {
        P_ID = p_ID;
    }

    public int getP_Number() {
        return P_Number;
    }

    public void setP_Number(int p_Number) {
        P_Number = p_Number;
    }

    public String getP_Name() {
        return P_Name;
    }

    public void setP_Name(String p_Name) {
        P_Name = p_Name;
    }

    public String getP_Course() {
        return P_Course;
    }

    public void setP_Course(String p_Course) {
        P_Course = p_Course;
    }

    public int getP_Age() {
        return P_Age;
    }

    public void setP_Age(int p_Age) {
        P_Age = p_Age;
    }

    public String getP_Gender() {
        return P_Gender;
    }

    public void setP_Gender(String p_Gender) {
        P_Gender = p_Gender;
    }

    public String getP_Height() {
        return P_Height;
    }

    public void setP_Height(String p_Height) {
        P_Height = p_Height;
    }

    public String getP_Vstat() {
        return P_Vstat;
    }

    public void setP_Vstat(String p_Vstat) {
        P_Vstat = p_Vstat;
    }

    public String getP_Image() {
        return P_Image;
    }

    public void setP_Image(String p_Image) {
        P_Image = p_Image;
    }

    public String getP_imgurl() {
        return P_imgurl;
    }

    public void setP_imgurl(String p_imgurl) {
        P_imgurl = p_imgurl;
    }

    public String getP_imgfolder() {
        return P_imgfolder;
    }

    public void setP_imgfolder(String p_imgfolder) {
        P_imgfolder = p_imgfolder;
    }
}

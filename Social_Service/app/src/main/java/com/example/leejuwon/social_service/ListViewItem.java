package com.example.leejuwon.social_service;

/**
 * Created by leejuwon on 2017-07-14.
 */

public class ListViewItem {
    private String titleStr; //제목
    private String descStr; //메세지
    private String a01;
    private String a02;
    private String tel;
    private String subtitle;

    public ListViewItem(String title, String desc, String a01, String a02, String tel, String subtitle){
        this.titleStr = title;
        this.descStr = desc;
        this.tel = tel;
        this.subtitle = subtitle;
        this.a01 = a01;
        this.a02 = a02;
    }

    public ListViewItem(){}

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }

    public void setDescStr(String descStr) {
        this.descStr = descStr;
    }

    public void setA01(String a01) {
        this.a01 = a01;
    }

    public void setA02(String a02) {
        this.a02 = a02;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getSubtitle() {
        return subtitle;

    }

    public String getTitleStr() {

        return titleStr;
    }

    public String getDescStr() {
        return descStr;
    }

    public String getA01() {
        return a01;
    }

    public String getA02() {
        return a02;
    }

    public String getTel() {
        return tel;
    }
}

package com.example.trialtest;

/* class for get and set the value in the database table to the column respectively */
public class Website {

    public int _id;
    public String _url;

    public Website(){}

    public Website(int id, String url){
        this._id = id;
        this._url = url;
    }


    public Website(String url) { this._url = url; }

    public void setID(int id) {
        this._id = id;
    }

    public int getID() {
        return this._id;
    }

    public void setUrl(String url) {
        this._url = url;
    }

    public  String getUrl() {
        return this._url;
    }

}

package com.herokuapp.punchcard_app.punchd;

import android.os.Parcelable;

import java.io.Serializable;

public class Stores implements Serializable {

    private String id;
    private String url;
    private String name;
    private String address;
    private String location;
    private String link;
    private String offerName;
    private String offerDesc;
    private String offerPunchReq;
    private String offerPunchCur;


    public Stores() {
        // TODO Auto-generated constructor stub
    }

    public Stores(String id, String url, String name, String address,
                  String location, String link, String offerName, String offerDesc, String offerPunchReq, String offerPunchCur) {
        super();
        this.id = id;
        this.url = url;
        this.name = name;
        this.address = address;
        this.location = location;
        this.link = link;
        this.offerName = offerName;
        this.offerPunchReq = offerPunchReq;
        this.offerPunchCur = offerPunchCur;
    }


    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getURL() {
        return url;
    }

    public void setURL(String url) {
        this.url = url;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getOfferDesc() {
        return offerDesc;
    }

    public void setOfferDesc(String offerDesc) {
        this.offerDesc = offerDesc;
    }

    public String getOfferPunchReq() {
        return offerPunchReq;
    }

    public void setOfferPunchReq(String offerPunchReq) {
        this.offerPunchReq = offerPunchReq;
    }

    public String getOfferPunchCur() {
        return offerPunchCur;
    }

    public void setOfferPunchCur(String offerPunchCur) {
        this.offerPunchCur = offerPunchCur;
    }











}

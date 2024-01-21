package com.example.technologycompare.helper;

public class MyLike {
    private int idMylike;
    private int idPhone;

    public MyLike(int idMylike, int idPhone) {
        this.idMylike = idMylike;
        this.idPhone = idPhone;
    }

    public int getIdMylike() {
        return idMylike;
    }

    public void setIdMylike(int idMylike) {
        this.idMylike = idMylike;
    }

    public int getIdPhone() {
        return idPhone;
    }

    public void setIdPhone(int idPhone) {
        this.idPhone = idPhone;
    }
}

package com.first.administrator.project207.utils;

/**
 * Created by Administrator on 2017-04-13.
 */

public class User {

    int userCount ;
    String userName;
    String bookDate;


    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBookDate() {
        return bookDate;
    }

    public void setBookDate(String bookDate) {
        this.bookDate = bookDate;
    }

    public User(int userCount, String userName, String bookDate ){

        this.userCount = userCount;
        this.userName = userName;
        this.bookDate = bookDate;

    }
}

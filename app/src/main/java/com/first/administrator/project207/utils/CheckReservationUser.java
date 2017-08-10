package com.first.administrator.project207.utils;

/**
 * Created by Administrator on 2017-04-13.
 */

public class CheckReservationUser {

    public String getUserMemo() {
        return userMemo;
    }

    public void setUserMemo(String userMemo) {
        this.userMemo = userMemo;
    }

    int userCount;
    String userMemo;
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

    public CheckReservationUser(int userCount, String userName, String bookDate, String userMemo){

        this.userCount = userCount;
        this.userName = userName;
        this.bookDate = bookDate;
        this.userMemo = userMemo ;

    }
}
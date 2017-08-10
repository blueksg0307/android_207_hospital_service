package com.first.administrator.project207.utils;

/**
 * Created by Administrator on 2017-04-13.
 */

public class Member {

    String userName;
    String userBirth;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    int count;

    public String getUserBirth() {
        return userBirth;
    }

    public void setUserBirth(String userBirth) {
        this.userBirth = userBirth;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Member(int count, String userName, String bookDate ){


        this.userName = userName;
        this.userBirth = bookDate;
        this.count = count ;

    }
}

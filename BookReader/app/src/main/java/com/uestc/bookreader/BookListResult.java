package com.uestc.bookreader;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookListResult {

    @SerializedName("status")
    private int mStatus;

    @SerializedName("msg")
    private String mMessage;

    @SerializedName("data")
    private List<BookData> mData;

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus=status;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage=message;
    }

    public List<BookData> getData() {
        return mData;
    }

    public void setData(List<BookData> data) {
        mData=data;
    }

    public static class BookData {
        @SerializedName("bookname")
        private String bookname;
        @SerializedName("bookfile")
        private String bookfile;

        public String getBookname() {
            return bookname;
        }

        public void setBookname(String bookname) {
            this.bookname=bookname;
        }

        public String getBookfile() {
            return bookfile;
        }

        public void setBookfile(String bookfile) {
            this.bookfile=bookfile;
        }
    }


}

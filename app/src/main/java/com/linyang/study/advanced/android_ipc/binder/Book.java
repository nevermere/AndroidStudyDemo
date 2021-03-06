package com.linyang.study.advanced.android_ipc.binder;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;

/**
 * 描述:
 * Created by fzJiang on 2019-1-24
 */
public class Book implements Parcelable {

    private long bookID;
    private String bookName;

    public Book(long bookID, String bookName) {
        this.bookID = bookID;
        this.bookName = bookName;
    }

    private Book(Parcel in) {
        bookID = in.readLong();
        bookName = in.readString();
    }

    public long getBookID() {
        return bookID;
    }

    public void setBookID(long bookID) {
        this.bookID = bookID;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(bookID);
        dest.writeString(bookName);
    }

    @NotNull
    @Override
    public String toString() {
        return "bookID:" + bookID + ",bookName:" + bookName;
    }
}

package com.linyang.study.advanced.android_ipc.binder;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import java.util.List;

/**
 * 描述:
 * Created by fzJiang on 2019-1-24
 */
public interface IBookManager extends IInterface {

    String DESCRIPTOR = "com.linyang.study.advanced.android_ipc.binder.IBookManager";

    int TRANSACTION_getBookList = IBinder.FIRST_CALL_TRANSACTION;

    int TRANSACTION_addBook = IBinder.FIRST_CALL_TRANSACTION + 1;

    int TRANSACTION_registerListener = IBinder.FIRST_CALL_TRANSACTION + 2;

    int TRANSACTION_unRegisterListener = IBinder.FIRST_CALL_TRANSACTION + 3;

    List<Book> getBookList() throws RemoteException;

    void addBook(Book book) throws RemoteException;

    void registerListener(IBookListener listener) throws RemoteException;

    void unRegisterListener(IBookListener listener) throws RemoteException;

}

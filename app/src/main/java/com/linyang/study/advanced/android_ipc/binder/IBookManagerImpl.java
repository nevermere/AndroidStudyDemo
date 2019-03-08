package com.linyang.study.advanced.android_ipc.binder;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.linyang.study.app.util.L;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import androidx.annotation.NonNull;

/**
 * 描述:
 * Created by fzJiang on 2019-1-24
 */
public class IBookManagerImpl extends Binder implements IBookManager {

    private RemoteCallbackList<IBookListener> mRemoteCallbackList = new RemoteCallbackList<>();
    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();

    IBookManagerImpl() {
        this.attachInterface(this, DESCRIPTOR);
    }

    public static IBookManager asInterface(IBinder obj) {
        if (null == obj) {
            return null;
        }

        android.os.IInterface iInterface = obj.queryLocalInterface(DESCRIPTOR);
        if (iInterface instanceof IBookManager) {
            return (IBookManager) iInterface;
        }

        return new IBookManagerImpl.Proxy(obj);
    }

    @Override
    public List<Book> getBookList() {
        L.i("--------get  BookList--------" + mBookList.size());
        return mBookList;
    }

    @Override
    public void addBook(Book book) {
        L.i("--------add  Book--------" + mBookList.size());
        mBookList.add(book);
    }

    @Override
    public void registerListener(IBookListener listener) {
        if (listener != null) {
            L.i("--------registerListener--------");
            mRemoteCallbackList.register(listener);
        }
    }

    @Override
    public void unRegisterListener(IBookListener listener) {
        if (listener != null) {
            L.i("--------unRegisterListener--------");
            mRemoteCallbackList.unregister(listener);
        }
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    @Override
    public String getInterfaceDescriptor() {
        return DESCRIPTOR;
    }

    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case INTERFACE_TRANSACTION: {
                reply.writeString(DESCRIPTOR);
            }

            case TRANSACTION_getBookList: {
                data.enforceInterface(DESCRIPTOR);
                List<Book> result = this.getBookList();
                reply.writeNoException();
                reply.writeTypedList(result);
                return true;
            }

            case TRANSACTION_addBook: {
                data.enforceInterface(DESCRIPTOR);
                Book book;
                if (data.readInt() != 0) {
                    book = Book.CREATOR.createFromParcel(data);
                } else {
                    book = null;
                }
                this.addBook(book);
                reply.writeNoException();

                // 回调
                final int index = mRemoteCallbackList.beginBroadcast();
                for (int i = 0; i < index; i++) {
                    IBookListener listener = mRemoteCallbackList.getBroadcastItem(i);
                    if (listener != null) {
                        listener.onNewBookArrived(book);
                    }
                }
                mRemoteCallbackList.finishBroadcast();
                return true;
            }

            case TRANSACTION_registerListener: {
                data.enforceInterface(DESCRIPTOR);
                IBookListener listener = (IBookListener) data.readStrongBinder();
                this.registerListener(listener);
                reply.writeNoException();
                return true;
            }

            case TRANSACTION_unRegisterListener: {
                data.enforceInterface(DESCRIPTOR);
                IBookListener listener = (IBookListener) data.readStrongBinder();
                this.unRegisterListener(listener);
                reply.writeNoException();
                return true;
            }
        }
        return super.onTransact(code, data, reply, flags);
    }


    /**
     * 代理实现
     */
    private static class Proxy implements IBookManager {

        private IBinder mIBinder;

        Proxy(IBinder iBinder) {
            this.mIBinder = iBinder;
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            List<Book> result;
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                mIBinder.transact(TRANSACTION_getBookList, data, reply, 0);
                reply.readException();
                result = reply.createTypedArrayList(Book.CREATOR);
            } finally {
                data.recycle();
                reply.recycle();
            }
            return result;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                if (book != null) {
                    data.writeInt(1);
                    book.writeToParcel(data, 0);
                } else {
                    data.writeInt(0);
                }
                mIBinder.transact(TRANSACTION_addBook, data, reply, 0);
                reply.readException();
            } finally {
                data.recycle();
                reply.recycle();
            }
        }

        @Override
        public void registerListener(IBookListener listener) throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                data.writeStrongBinder((((listener != null)) ? (listener.asBinder()) : (null)));
                mIBinder.transact(TRANSACTION_registerListener, data, reply, 0);
                reply.readException();
            } finally {
                data.recycle();
                reply.recycle();
            }
        }

        @Override
        public void unRegisterListener(IBookListener listener) throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                data.writeStrongBinder((((listener != null)) ? (listener.asBinder()) : (null)));
                mIBinder.transact(TRANSACTION_unRegisterListener, data, reply, 0);
                reply.readException();
            } finally {
                data.recycle();
                reply.recycle();
            }
        }

        @Override
        public IBinder asBinder() {
            return mIBinder;
        }
    }
}

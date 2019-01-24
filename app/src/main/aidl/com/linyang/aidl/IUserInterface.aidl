// IUserInterface.aidl
package com.linyang.aidl;

import com.linyang.aidl.IResult;
import com.linyang.aidl.IUser;
import com.linyang.aidl.IUserListener;

interface IUserInterface {

    IResult registerUser(in IUser user);

    IResult login(String userName,String password);

    IResult getUserInfo(String userID);

    void registerListener(IUserListener listener);

    void unRegisterListener(IUserListener listener);

}

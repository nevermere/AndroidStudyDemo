// IUserListener.aidl
package com.linyang.aidl;

import com.linyang.aidl.IUser;

interface IUserListener {

    void onUserLogin(in IUser user);

}

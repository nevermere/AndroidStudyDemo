// ISecurityCenter.aidl
package com.linyang.binder_poll;

interface ISecurityCenter {

    String encrypt(String content);

    String decrypt(String encode);

}

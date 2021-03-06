package com.linyang.study.advanced.android_ipc.binder_poll;


import com.linyang.binder_poll.ISecurityCenter;

/**
 * 描述:
 * Created by fzJiang on 2019-1-24
 */
public class ISecurityCenterImpl extends ISecurityCenter.Stub {

    private static final char SECRET_CODE = '^';

    @Override
    public String encrypt(String content) {
        char[] chars = content.toCharArray();
        for (int i = 0, size = chars.length; i < size; i++) {
            chars[i] ^= SECRET_CODE;
        }
        return new String(chars);
    }

    @Override
    public String decrypt(String encode) {
        return encrypt(encode);
    }
}

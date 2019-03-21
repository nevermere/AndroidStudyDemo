package com.linyang.study.other.glide;

/**
 * 描述:缓存动态Url的图片实体,需要在AndroidManifest.xml中注册
 * Created by fzJiang on 2019/03/15 14:49 星期五
 */
public class DynamicImage {

    private String dynamicUrl;// 动态Url = 原Url + ?e=过期时间 + token=下载凭证

    public String getDynamicUrl() {
        return dynamicUrl;
    }

    public void setDynamicUrl(String dynamicUrl) {
        this.dynamicUrl = dynamicUrl;
    }

    /**
     * 返回未添加下载凭证前的Url
     *
     * @return
     */
    public String getImageId() {
        return dynamicUrl.contains("?") ? dynamicUrl.substring(0, dynamicUrl.indexOf("?")) : dynamicUrl;
    }
}

package com.mattiasbergstrom.resrobot;

import java.net.URL;

/**
 * Created by Mackan on 2014-09-06.
 */
public class DownloadQueueTask {
    private DownloadTask mTask;
    private URL mUrl;
    public DownloadQueueTask(DownloadTask task, URL url){
        this.mTask = task;
        this.mUrl = url;
    }

    public void setDownloadTask(DownloadTask task){
        this.mTask = task;
    }

    public DownloadTask getDownloadTask(){
        return this.mTask;
    }

    public void setURL(URL url){
        this.mUrl = url;
    }

    public URL getURL(){
        return this.mUrl;
    }
}

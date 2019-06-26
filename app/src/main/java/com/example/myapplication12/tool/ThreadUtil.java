package com.example.myapplication12.tool;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import okhttp3.RequestBody;

public class ThreadUtil {

    private String url;

    private String postJson;

    private String returnJson;

    private Boolean canReachable;

    private HttpStatus status;

    private Thread checkStatusThread;

    public void setPostJson(String postJson) {
        this.postJson = postJson;
    }

    public String getReturnJson() {
        return returnJson;
    }

    public Boolean getCanReachable() {
        return canReachable;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public ThreadUtil() {
    }

    public ThreadUtil(String url) {
        this.url = url;
    }

    public ThreadUtil request(final Method method, final Object object) {//get
        checkStatusThread = new Thread(new Runnable() {
            @Override
            public void run() {
                status = new OkHttpUtil(url).Get();
                if (status.getStatus() == 200) {
                    canReachable = true;
                    if (method != null) {
                        try {
                            method.invoke(object, null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    canReachable = false;
                }
            }
        });
        return this;
    }

    public ThreadUtil request(final RequestBody body) {//post
        checkStatusThread = new Thread(new Runnable() {
            @Override
            public void run() {
                status = new OkHttpUtil(url).Post(body);
                returnJson = status.getResponseAns();
                if (status.getStatus() == 200) {
                    canReachable = true;
                    //...
                } else {
                    canReachable = false;
                }
            }
        });
        return this;
    }

    public ThreadUtil startThread() {
        checkStatusThread.start();
        return this;
    }

    public ThreadUtil joinThread() {
        try {
            checkStatusThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

}

package com.trus.regpro.Common;

public class BaseContext {

    private static ThreadLocal threadLocal = new ThreadLocal();


    public static Long getCurrentId(){
        return (Long) threadLocal.get();
    }

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }


}

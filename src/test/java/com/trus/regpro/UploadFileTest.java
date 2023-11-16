package com.trus.regpro;

import org.junit.jupiter.api.Test;

public class UploadFileTest {

    @Test
    void upload(){
        String path = "sdsadisa.jpg";
        String suffix = path.substring(path.lastIndexOf("."));
        System.out.println(suffix);
    }
}

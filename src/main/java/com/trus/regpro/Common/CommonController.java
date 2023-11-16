package com.trus.regpro.Common;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {


    @Value("${reggie.path}")
    String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){

        if(! new File(basePath).exists())
            new File(basePath).mkdirs();

        String fileName = null;


        try {
            file.transferTo(new File(basePath+
                    (fileName = UUID.randomUUID().toString()+
                    file.getOriginalFilename().substring(
                            file.getOriginalFilename().lastIndexOf("."))
                    )));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    @RequestMapping(value = "/download",method = RequestMethod.GET)
    void download(String name, HttpServletResponse response){
        try {
            FileInputStream inputStream = new FileInputStream(new File(basePath+name));

            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            byte[] bytes = new byte[1024];

            int len = 0;


            while ((len = inputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }

            inputStream.close();
            outputStream.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package com.neuedu.controller;

import com.neuedu.common.ServerResponse;
import com.neuedu.service.IUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class UploadContronller {

    @Autowired
    IUploadService uploadService;

    @RequestMapping(value = "/upload",method = RequestMethod.GET)
    public String upload(){
        return "upload";
    }

    @Value("${img.local.path}")
    private String imgPath;

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse upload(@RequestParam("picfile")MultipartFile uploadFile){
        File newFile = null;
        if(uploadFile!=null){

            String uuid = UUID.randomUUID().toString();
            //获取扩展名
            String fileName = uploadFile.getOriginalFilename();
            System.out.println("===filename="+fileName+"===");
            String extraName = fileName.substring(fileName.lastIndexOf("."));

            String newFileName = uuid+extraName;
            System.out.println("===新名="+newFileName+"===");

            File file = new File(imgPath);
            if(!file.exists()){
                file.mkdir();
            }
            newFile = new File(file,newFileName);

            try {
                //将文件写到磁盘中
                uploadFile.transferTo(newFile);

                //将文件写到七牛云上
                //return uploadService.uploadFile(newFile);
                return ServerResponse.createServerResponseBySucess(uploadService.uploadFile(newFile));

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return ServerResponse.createServerResponseByFail("上传失败！");
    }
}

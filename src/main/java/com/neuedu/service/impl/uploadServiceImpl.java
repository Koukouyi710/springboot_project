package com.neuedu.service.impl;

import com.google.gson.Gson;
import com.neuedu.common.ServerResponse;
import com.neuedu.service.IUploadService;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class uploadServiceImpl implements IUploadService{

    @Autowired
    Auth auth;

    @Value("${qiniu.bucket}")
    private String bucketName;

    @Autowired
    UploadManager uploadManager;

    @Override
    //public ServerResponse uploadFile(File uploadFile) {
    public String uploadFile(File uploadFile) {

        //生成上传凭证
        String uploadToken = auth.uploadToken(bucketName);
        try {
            Response response = uploadManager.put(uploadFile,null,uploadToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
            //return ServerResponse.createServerResponseBySucess(null,putRet.hash);
            String key = putRet.key;
            return key;
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        return null;
    }
}

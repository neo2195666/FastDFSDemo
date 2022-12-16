package com.example.fileuploaddemo.Services.Impl;

import com.example.fileuploaddemo.Services.UploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class UploadServiceImpl implements UploadService {

    private final String superDir = "/Users/yyzusb/Downloads/tmp";

    @Override
    public boolean upload(MultipartFile file) {

        File dir = new File(superDir);
        //如果文件夹不存在
        if(!dir.exists()){
            //如果创建文件夹失败，直接退出
            if(!dir.mkdir())
                return false;
        }
        //创建当天子目录的名称
        Date current = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String subDirName = simpleDateFormat.format(current);

        //遍历子目录，查看当天文件夹是否存在
        File childFile[] = dir.listFiles();
        boolean hasCurrent = false;
        for (File child: childFile) {
            if(subDirName.equals(child.getName())) {
                //如果存在，退出循环
                hasCurrent = true;
                break;
            }
        }
        //  如果不存在，创建
        File subDir = new File(dir,subDirName);
        if(!hasCurrent){
            //如果创建当天子目录失败，退出
            if(!subDir.mkdir())
                return false;
        }

        //想subDir中上传文件
        //生成随机的文件名,保持上传文件的后缀名
        String fileName = UUID.randomUUID().toString();
        String fileOriginalName = file.getOriginalFilename();
        fileName += fileOriginalName.substring(fileOriginalName.lastIndexOf("."));

        try {
            //获取文件输入流
            InputStream inputStream = file.getInputStream();
            //要创建的文件
            File subFile = new File(subDir,fileName);
            //创建输出流
            OutputStream outputStream = new FileOutputStream(subFile) ;

            //循环读取上传的额内容，保存到磁盘
            byte[] bytes = new byte[1024];
            int count = 0;
            while ((count = inputStream.read(bytes)) != -1){
                //读取到的字节写入本地磁盘
                outputStream.write(bytes,0,count);
            }
            outputStream.flush();
            outputStream.close();

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }
}

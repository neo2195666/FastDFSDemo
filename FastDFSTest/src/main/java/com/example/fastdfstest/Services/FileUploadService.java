package com.example.fastdfstest.Services;

import com.example.fastdfstest.pojo.FdfsFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface FileUploadService {
    /**
     * 上传文件
     */
    public boolean uploadFunc(MultipartFile file);

    /**
     * 从数据库查询所有文件
     */
    public List<FdfsFile> getAll();

    /**
     * 删除指定文件
     */
    public boolean deleteFile(Integer id);


    /**
     * 下载文件
     */
    public void downloadFile(Integer id, HttpServletResponse httpServletResponse);
}

package com.example.fastdfstest.Services.Impl;


import com.example.fastdfstest.Mapper.FdfsFileMapper;
import com.example.fastdfstest.Services.FileUploadService;
import com.example.fastdfstest.Utils.FastDFSUtils;
import com.example.fastdfstest.pojo.FdfsFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    @Autowired
    private FdfsFileMapper fdfsFileMapper;
    /**
     * 连接FastDFS
     * 1、找到Tracker跟踪器。要可以上传文件的storage存储器的地址
     * 2、连接storage存储器，上传文件
     * 3、把上传的文件信息存到数据库
     * @Param file
     * @return
     */
    @Override
    public boolean uploadFunc(MultipartFile file) {

        try {
            String[] result = FastDFSUtils.upload(file.getInputStream(),file.getOriginalFilename());

            if(result == null) return false;

            System.out.println(Arrays.toString(result));
            //保存上传的文件信息到数据库
            FdfsFile fdfsFile = new FdfsFile(null,file.getOriginalFilename(),result[0],result[1],new Date());
            fdfsFileMapper.insert(fdfsFile);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public List<FdfsFile> getAll() {

        return fdfsFileMapper.selectAll();
    }

    /**
     * 1、删除FastDFS中的文件
     * 2、删除数据库记录
     * @param id
     * @return
     */
    @Override
    @Transactional
    public boolean deleteFile(Integer id) {
        //删除文件
        //查询数据库中的文件数据
        FdfsFile file = fdfsFileMapper.selectById(id);

        //调用工具类，删除文件
        boolean isDeleted = FastDFSUtils.delete(file.getGroupName(),file.getNewFileName());
        if(!isDeleted){
            //删除FastDFS中的文件失败，数据库中的数据不能删除
            return false;
        }
        //删除数据库中的数据
        fdfsFileMapper.delete(id);
        return false;
    }

    /**
     * 下载文件
     * 1、访问数据库查询文件内容
     * 2、访问FastDFS服务器下载文件
     * @param id
     * @param httpServletResponse
     */
    @Override
    public void downloadFile(Integer id, HttpServletResponse httpServletResponse) {
        try {
            // 获取要下载的文件对象
            FdfsFile file = fdfsFileMapper.selectById(id);
            // 下载文件
            byte[] tmp = FastDFSUtils.download(file.getGroupName(),file.getNewFileName());
            // 判断是否下载成
            if(tmp == null){
                //如果下载失败
                httpServletResponse.setContentType("text/html; charset=utf-8");
                httpServletResponse.getWriter().print("<h3>503服务器异常</h3>");
                httpServletResponse.getWriter().flush();
                return;
            }

            //下载成功，把从FastDFS中下载的字节数组，写到客户端
            // 设置响应头 mime-type, 全适配，代表流 octet-stream
            httpServletResponse.setContentType("application/octet-stream");
            //设置附件名称(下载的文件名称)
            System.out.println("下载的文件名称是 =>" + file.getFileName());
            httpServletResponse.setHeader("content-disposition","attachment;filename="+file.getFileName());
            //输出文件内容到客户端write(把tmp的内容输出，从下表0开始输出，输出的长度是tmp的长度)
            httpServletResponse.getOutputStream().write(tmp,0,tmp.length);
            httpServletResponse.getOutputStream().flush();
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
    }
}

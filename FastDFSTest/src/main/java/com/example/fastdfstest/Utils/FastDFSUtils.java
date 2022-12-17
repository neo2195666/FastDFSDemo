package com.example.fastdfstest.Utils;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;

import java.io.InputStream;
import java.util.Properties;

/**
 * FastDFS 客户端工具类
 */
public class FastDFSUtils {

    //初始化客户端对象
    //存储器客户端对象，文件上传下载，是保存在存储器中
    private final static StorageClient storageClient;
    static {
        try {
            // 给GlobalClient类型中的静态属性g_tracker_group赋值
            // 加载配置文件
            InputStream inputStream = FastDFSUtils.class.getClassLoader().getResourceAsStream("fdfs.properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            ClientGlobal.initByProperties(properties);

            //找跟踪器
            //跟踪器客户端，跟踪器服务器地址，是配置文件中定义的
            TrackerClient trackerClient = new TrackerClient();
            // 根据跟踪器客户端，获取一个跟踪器服务器
            TrackerServer trackerServer = trackerClient.getConnection();
            // 通过跟踪器客户端和跟踪器服务器，获取一个存储器服务器
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);

            storageClient = new StorageClient(trackerServer,storageServer);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

        /**
         * 上传文件到FastDFS
         * 需要客户端对象
         * 上传文件的时候，保存的元数据：原始文件名，文件大小，上传时间（系统当前时间）
         *
         * @return
         * @Param inputStream 上传文件的输入流
         * @Param originalFileName 上传文件的原始名称
         */
        public static String[] upload(InputStream inputStream, String originalFileName) {
        /**
         * 上传文件的常用方法
         * storageClient.upload_file(字节数，上传文件的扩展名，上传文件的元数据)
         */
        try {
            //通过文件输入流查看文件的大小。返回整数，代表字节。
            int  size = inputStream.available();
            byte[] tmp = new byte[size];
            // 读取文件内容到字节数组
            inputStream.read(tmp,0,size);
            NameValuePair[] nvps = new NameValuePair[]{
                    new NameValuePair("fileName",originalFileName),
                    new NameValuePair("size",size+""),
                    new NameValuePair("create_Time",System.currentTimeMillis()+"")
            };
            //上传文件,返回一个字符串数组。长度是2.
            // 0下表是存储器的卷名。
            // 1下标是存储器自动生成的我呢间名(包括文件夹)
            String[] result = storageClient.upload_file(tmp,originalFileName.substring(originalFileName.lastIndexOf(".")+1),nvps);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除FastDFS中的文件
     * @return
     */
    public static boolean delete(String groupName,String newFileName){
        try {
            int resultCode = storageClient.delete_file(groupName,newFileName);
            return resultCode == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 下载文件
     */
    public static byte[] download(String storageGroupName,String newFileName){
        try{
            //查询文件元数据
            NameValuePair[] nameValuePairs= storageClient.get_metadata(storageGroupName, newFileName);
            //从文件服务器下载文件
            return storageClient.download_file(storageGroupName,newFileName);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //工具类不创建对象
    private FastDFSUtils(){}
}
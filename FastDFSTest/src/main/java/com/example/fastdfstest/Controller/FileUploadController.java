package com.example.fastdfstest.Controller;

import com.example.fastdfstest.Services.FileUploadService;
import com.example.fastdfstest.pojo.FdfsFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Controller
public class FileUploadController {
    @Autowired
    private FileUploadService fileUploadService;
    @RequestMapping("/download")
    public void downloadFunc(Integer id, HttpServletResponse httpServletResponse){
        fileUploadService.downloadFile(id,httpServletResponse);
    }

    @RequestMapping("/listFile")
    public String listFile(Model model){
        List<FdfsFile> allInfo = fileUploadService.getAll();
        model.addAttribute("list",allInfo);
        return "listFile";
    }

    @RequestMapping(value = {"/","/index"})
    public String toIndex(){
        return "index";
    }

    @RequestMapping("/upload")
    public String uploadFunc(@RequestParam("file") MultipartFile file){
        System.out.println(fileUploadService.uploadFunc(file));
        return "redirect:/ok";
    }

    @RequestMapping("/ok")
    public String ok(){
        return "ok";
    }

    @RequestMapping("/deleteFile")
    public String deleteFile(Integer id){
        fileUploadService.deleteFile(id);
        return "redirect:/listFile";
    }

}

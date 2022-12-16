package com.example.fileuploaddemo.Controller;

import com.example.fileuploaddemo.Services.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadController {

    @Autowired
    public UploadService uploadService;

    @RequestMapping ("/upload")
    public String uploadFile(@RequestParam("file")MultipartFile file){
        if(uploadService.upload(file)) return "redirect:/ok";
        else return "redirect:/fail";
    }

    @GetMapping("/")
    public String toIndex(){
        return "index";
    }

    @GetMapping("/ok")
    public String ok(){
        return "ok";
    }

    @GetMapping("/fail")
    public String fail(){
        return "fail";
    }


}

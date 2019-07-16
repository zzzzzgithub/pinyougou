package com.pinyougou.shop.controller;

import com.pinyougou.util.FastDFSClient;
import entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author pdd
 * @version 1.0
 * @date 2019/7/16 11:24
 */
@RestController
public class UploadController {
    @Value("${FILE_SERVER_URL}")
    private String FAST_DFS_SERVER;

    @RequestMapping("upload")
    public Result upload(MultipartFile file) {
        try {
            //1.解析后缀名
            String oldName = file.getOriginalFilename();
            //后缀名
            String extName = oldName.substring(oldName.lastIndexOf(".") + 1);
            //2.通过FastDFS上传文件
            FastDFSClient dfsClient = new FastDFSClient("classpath:fdfs_client.conf");
            //上传文件
            String uploadFile = dfsClient.uploadFile(file.getBytes(), extName);
            //拼接返回路径
            String url = FAST_DFS_SERVER + uploadFile;
            return new Result(true, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "文件上传失败");

    }
}

package com.byk.fjj.oss.controller;

import com.byk.common.exception.BusinessException;
import com.byk.common.result.ResponseEnum;
import com.byk.common.result.Result;
import com.byk.fjj.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author byk
 */
@Api(tags = "阿里云文件管理")
//@CrossOrigin //跨域
@RestController
@RequestMapping("/api/oss/file")
public class FileController {

    @Resource
    private FileService fileService;

    //文件上传
    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public Result upload(
            @ApiParam(value = "文件", required = true)
            @RequestParam("file") MultipartFile file,
            @ApiParam(value = "模块", required = true)
            @RequestParam("module") String module){
        try {
            InputStream inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename();
            String url = fileService.upload(inputStream, module, fileName);
            //返回result对象
            return Result.ok().message("文件上传成功").data("url", url);
        } catch (IOException e) {
            throw new BusinessException(ResponseEnum.UPLOAD_ERROR, e);
        }
    }

    //删除OSS文件
    @ApiOperation("删除OSS文件")
    @DeleteMapping("/remove")
    public Result remove(
            @ApiParam(value = "要删除的文件路径", required = true)
            @RequestParam("url") String url) {
        fileService.removeFile(url);
        return Result.ok().message("删除成功");
    }
}

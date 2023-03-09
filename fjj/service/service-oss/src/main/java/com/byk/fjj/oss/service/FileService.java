package com.byk.fjj.oss.service;

import java.io.InputStream;

/**
 * @author byk
 */
public interface FileService {

    //文件上传至阿里云
    String upload(InputStream inputStream, String module, String fileName);

    //根据路径删除文件
    void removeFile(String url);
}

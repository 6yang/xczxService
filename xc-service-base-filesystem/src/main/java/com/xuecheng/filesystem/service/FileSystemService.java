package com.xuecheng.filesystem.service;

import com.alibaba.fastjson.JSON;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.xuecheng.filesystem.dao.FileSystemRepository;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.Map;

@Service
public class FileSystemService {

    @Autowired
    private FastFileStorageClient fileStorageClient;

    @Autowired
    private FileSystemRepository fileSystemRepository;

    /**
     * 上传文件
     * @param multipartFile 文件
     * @param filetag 文件标签
     * @param businesskey 业务id
     * @param metadata 元信息，json格式
     * @return : com.xuecheng.framework.domain.filesystem.response.UploadFileResult
     */
    public UploadFileResult upload(MultipartFile file,
                                   String filetag,
                                   String businesskey,
                                   String metadata) {
        if(file==null){
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEISNULL);
        }
        String ext = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
        StorePath storePath = null;
        try {
            storePath = fileStorageClient.uploadFile(file.getInputStream(), file.getSize(), ext, null);
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionCast.cast(FileSystemCode.FS_INITFDFSERROR);
        }
        String fileId =  storePath.getFullPath();
        FileSystem fileSystem = new FileSystem();
        fileSystem.setFileId(fileId);
        fileSystem.setFilePath(fileId);
        fileSystem.setBusinesskey(businesskey);
        fileSystem.setFiletag(filetag);
        fileSystem.setFileName(file.getOriginalFilename());
        fileSystem.setFileSize(file.getSize());
        fileSystem.setFileType(file.getContentType());
        if(StringUtils.isNotEmpty(metadata)){
            Map map = JSON.parseObject(metadata, Map.class);
            fileSystem.setMetadata(map);
        }
        fileSystemRepository.save(fileSystem);
        return new UploadFileResult(CommonCode.SUCCESS,fileSystem);
    }

    public ResponseResult delete(String fileId) {
        fileStorageClient.deleteFile(fileId);
        fileSystemRepository.deleteById(fileId);
        return new ResponseResult(CommonCode.SUCCESS);
    }
}

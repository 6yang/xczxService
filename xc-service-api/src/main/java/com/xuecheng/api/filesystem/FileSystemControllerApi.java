package com.xuecheng.api.filesystem;

import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import org.springframework.web.multipart.MultipartFile;

@Api(value = "文件上传接口",description = "文件上传接口，提供文件的上传、下载、删除")
public interface FileSystemControllerApi {

    /**
     * 上传文件
     * @param multipartFile 文件
     * @param filetag 文件标签
     * @param businesskey 业务id
     * @param metadata 元信息，json格式
     * @return : com.xuecheng.framework.domain.filesystem.response.UploadFileResult
     */
    UploadFileResult upload(
            MultipartFile multipartFile,
            String filetag,
            String businesskey,
            String metadata);

    /**
     * 删除文件
     * @param fileId
     * @return : com.xuecheng.framework.model.response.ResponseResult
     */
    ResponseResult delete(String fileId);


}

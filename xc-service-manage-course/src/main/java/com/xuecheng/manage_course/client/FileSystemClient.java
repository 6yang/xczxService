package com.xuecheng.manage_course.client;

import com.xuecheng.framework.model.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("XC-SERVICE-BASE-FILESYSTEM")
public interface FileSystemClient {

    @DeleteMapping("/filesystem/delete")
    ResponseResult delete(@RequestParam("fileId") String fileId);

}

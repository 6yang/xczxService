package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "cms配置管理接口",description = "cms配置管理接口，提供页面的增、删、改、查")
public interface CmsConfigControllerApi {

    @ApiOperation("根据id查询CMS配置信息")
    CmsConfig getCmsConfig(String id);

}

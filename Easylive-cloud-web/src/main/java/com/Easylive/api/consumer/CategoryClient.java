package com.Easylive.api.consumer;

import com.Easylive.entity.constants.Constants;
import com.Easylive.entity.po.CategoryInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = Constants.SERVER_NAME_ADMIN)
public interface CategoryClient {
    @RequestMapping(Constants.INNER_API_PREFIX +"/category/loadAllCategory")
    List<CategoryInfo> loadAllCategory();
}

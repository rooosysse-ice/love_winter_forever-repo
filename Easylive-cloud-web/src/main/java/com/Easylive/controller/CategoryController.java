package com.Easylive.controller;

import com.Easylive.api.consumer.CategoryClient;
import com.Easylive.entity.po.CategoryInfo;
import com.Easylive.entity.vo.ResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/category")
@Validated
public class CategoryController extends ABaseController {

    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);
    @Resource
    private CategoryClient categoryClient;

    @RequestMapping("/loadAllCategory")
    public ResponseVO loadAllCategory() {
        List<CategoryInfo> categoryInfoList = categoryClient.loadAllCategory();
        return getSuccessResponseVO(categoryInfoList);
    }

}


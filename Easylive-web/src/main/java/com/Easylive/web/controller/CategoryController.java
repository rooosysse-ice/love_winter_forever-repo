package com.Easylive.web.controller;

import com.Easylive.entity.po.CategoryInfo;
import com.Easylive.entity.query.CategoryInfoQuery;
import com.Easylive.entity.vo.ResponseVO;
import com.Easylive.service.CategoryInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/category")
@Validated
public class CategoryController extends ABaseController {

    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);
    @Resource
    private CategoryInfoService categoryInfoService;

    @RequestMapping("/loadCategory")
    public ResponseVO loadAllCategory(CategoryInfoQuery categoryInfo) {
        return getSuccessResponseVO(categoryInfoService.getAllCategoryList());
    }

}


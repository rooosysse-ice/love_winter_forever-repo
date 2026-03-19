package com.Easylive.api.provider;

import com.Easylive.entity.constants.Constants;
import com.Easylive.entity.query.VideoDanmuQuery;
import com.Easylive.entity.vo.PaginationResultVO;
import com.Easylive.service.VideoDanmuService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(Constants.INNER_API_PREFIX + "/danmu")
@Validated
public class VideoDanmuApi {

    @Resource
    private VideoDanmuService videoDanmuService;

    @RequestMapping("/delDanmByVideoId")
    public void delDanmByVideoId(@NotEmpty String videoId) {
        VideoDanmuQuery videoDanmuQuery = new VideoDanmuQuery();
        videoDanmuQuery.setVideoId(videoId);
        videoDanmuService.deleteByParam(videoDanmuQuery);
    }


    @RequestMapping("/admin/loadDanmu")
    public PaginationResultVO loadDanmu(Integer pageNo, String videoNameFuzzy) {
        VideoDanmuQuery danmuQuery = new VideoDanmuQuery();
        danmuQuery.setOrderBy("danmu_id desc");
        danmuQuery.setPageNo(pageNo);
        danmuQuery.setQueryVideoInfo(true);
        danmuQuery.setVideoNameFuzzy(videoNameFuzzy);
        PaginationResultVO resultVO = videoDanmuService.findListByPage(danmuQuery);
        return resultVO;
    }


    @RequestMapping("/admin/delDanmu")
    public void delDanmu(@NotNull Integer danmuId) {
        videoDanmuService.deleteDanmu(null, danmuId);
    }

}

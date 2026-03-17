package com.Easylive.service.impl;

import com.Easylive.api.consumer.VideoClient;
import com.Easylive.entity.constants.Constants;
import com.Easylive.entity.enums.PageSize;
import com.Easylive.entity.enums.ResponseCodeEnum;
import com.Easylive.entity.enums.SearchOrderTypeEnum;
import com.Easylive.entity.enums.UserActionTypeEnum;
import com.Easylive.entity.po.VideoDanmu;
import com.Easylive.entity.po.VideoInfo;
import com.Easylive.entity.query.SimplePage;
import com.Easylive.entity.query.VideoDanmuQuery;
import com.Easylive.entity.query.VideoInfoQuery;
import com.Easylive.entity.vo.PaginationResultVO;
import com.Easylive.exception.BusinessException;
import com.Easylive.mappers.VideoCommentMapper;
import com.Easylive.mappers.VideoDanmuMapper;
//import com.Easylive.mappers.VideoInfoMapper;
import com.Easylive.service.VideoDanmuService;
import com.Easylive.utils.StringTools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * 视频弹幕 业务接口实现
 */
@Service("videoDanmuService")
public class VideoDanmuServiceImpl implements VideoDanmuService {

    @Resource
    private VideoDanmuMapper<VideoDanmu, VideoDanmuQuery> videoDanmuMapper;

    @Resource
    private VideoClient videoClient;


    /**
     * 根据条件查询列表
     */
    @Override
    public List<VideoDanmu> findListByParam(VideoDanmuQuery param) {
        return this.videoDanmuMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(VideoDanmuQuery param) {
        return this.videoDanmuMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<VideoDanmu> findListByPage(VideoDanmuQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<VideoDanmu> list = this.findListByParam(param);
        PaginationResultVO<VideoDanmu> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(VideoDanmu bean) {
        return this.videoDanmuMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<VideoDanmu> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.videoDanmuMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<VideoDanmu> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.videoDanmuMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(VideoDanmu bean, VideoDanmuQuery param) {
        StringTools.checkParam(param);
        return this.videoDanmuMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(VideoDanmuQuery param) {
        StringTools.checkParam(param);
        return this.videoDanmuMapper.deleteByParam(param);
    }

    /**
     * 根据DanmuId获取对象
     */
    @Override
    public VideoDanmu getVideoDanmuByDanmuId(Integer danmuId) {
        return this.videoDanmuMapper.selectByDanmuId(danmuId);
    }

    /**
     * 根据DanmuId修改
     */
    @Override
    public Integer updateVideoDanmuByDanmuId(VideoDanmu bean, Integer danmuId) {
        return this.videoDanmuMapper.updateByDanmuId(bean, danmuId);
    }

    /**
     * 根据DanmuId删除
     */
    @Override
    public Integer deleteVideoDanmuByDanmuId(Integer danmuId) {
        return this.videoDanmuMapper.deleteByDanmuId(danmuId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveVideoDanmu(VideoDanmu bean) {
        VideoInfo videoInfo = videoClient.getVideoInfoByVideoId(bean.getVideoId());
        if (videoInfo == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        //是否关闭弹幕
        if (videoInfo.getInteraction() != null && videoInfo.getInteraction().contains(Constants.ONE.toString())) {
            throw new BusinessException("UP主已关闭弹幕");
        }
        this.videoDanmuMapper.insert(bean);
        this.videoClient.updateCountInfo(bean.getVideoId(), UserActionTypeEnum.VIDEO_DANMU.getField(), 1);

        videoClient.updateDocCount(bean.getVideoId(), SearchOrderTypeEnum.VIDEO_DANMU, 1);

    }

    @Override
    public void deleteDanmu(String userId, Integer danmuId) {
        VideoDanmu danmu = videoDanmuMapper.selectByDanmuId(danmuId);
        if (null == danmu) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        VideoInfo videoInfo = videoClient.getVideoInfoPostByVideoId(danmu.getVideoId());
        if (null == videoInfo) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        if (userId != null && !videoInfo.getUserId().equals(userId)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        videoDanmuMapper.deleteByDanmuId(danmuId);
    }
}
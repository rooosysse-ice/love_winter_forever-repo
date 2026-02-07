package com.Easylive.utils;

import com.Easylive.entity.config.AppConfig;
import com.Easylive.entity.constants.Constants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class FFmpegUtils {

    @Resource
    private AppConfig appConfig;


    /**
     * 生成图片缩略图
     *
     * @param filePath
     * @return
     */
    public void createImageThumbnail(String filePath) {
        final String CMD_CREATE_IMAGE_THUMBNAIL = "ffmpeg -i \"%s\" -vf scale=200:-1 \"%s\"";
        String cmd = String.format(CMD_CREATE_IMAGE_THUMBNAIL, filePath, filePath + Constants.IMAGE_THUMBNAIL_SUFFIX);
        ProcessUtils.executeCommand(cmd, appConfig.getShowFFmpegLog());
    }

}

package com.Easylive.component;

import com.Easylive.entity.config.AppConfig;
import com.Easylive.entity.constants.Constants;
import com.Easylive.entity.dto.SysSettingDto;
import com.Easylive.entity.dto.TokenUserInfoDto;
import com.Easylive.entity.dto.UploadingFileDto;
import com.Easylive.entity.enums.DateTimePatternEnum;
import com.Easylive.entity.po.CategoryInfo;
import com.Easylive.entity.po.VideoInfoFilePost;
import com.Easylive.redis.RedisUtils;
import com.Easylive.utils.DateUtil;
import com.Easylive.utils.StringTools;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

@Component
public class RedisComponent {
    @Resource
    private RedisUtils redisUtils;

    @Resource
    private AppConfig appConfig;

    public String saveCheckCode(String code) {
        // 将验证码答案写入redis，返回一个随机的uuid
        String checkCodeKey = UUID.randomUUID().toString();
        redisUtils.setex(Constants.REDIS_KEY_CHECK_CODE + checkCodeKey, code, Constants.REDIS_KEY_EXPIRES_ONE_MIN * 10);
        return checkCodeKey;
    }

    public void cleanCheckCode(String checkCodeKey) {
        redisUtils.delete(Constants.REDIS_KEY_CHECK_CODE + checkCodeKey);
    }

    public String getCheckCode(String checkCodeKey) {
        // 从redis中获取到之前写入的checkCodeKey
        return (String) redisUtils.get(Constants.REDIS_KEY_CHECK_CODE + checkCodeKey);
    }

    public void saveTokenInfo(TokenUserInfoDto tokenUserInfoDto) {
        String token = UUID.randomUUID().toString();
        tokenUserInfoDto.setExpireAt(System.currentTimeMillis() + Constants.REDIS_KEY_EXPIRES_DAY * 7);
        tokenUserInfoDto.setToken(token);
        redisUtils.setex(Constants.REDIS_KEY_TOKEN_WEB + token, tokenUserInfoDto, Constants.REDIS_KEY_EXPIRES_DAY * 7);
    }

    public void cleanToken(String token) {
        redisUtils.delete(Constants.REDIS_KEY_TOKEN_WEB + token);
    }

    public TokenUserInfoDto getTokenInfo(String token) {
        return (TokenUserInfoDto) redisUtils.get(Constants.REDIS_KEY_TOKEN_WEB + token);
    }

    public String saveTokenInfo4Admin(String account) {
        String token = UUID.randomUUID().toString();
        redisUtils.setex(Constants.REDIS_KEY_TOKEN_ADMIN + token, account, Constants.REDIS_KEY_EXPIRES_DAY);
        return token;
    }

    public String getTokenInfo4Admin(String token) {
        return (String) redisUtils.get(Constants.REDIS_KEY_TOKEN_ADMIN + token);
    }

    public void cleanToken4Admin(String token) {
        redisUtils.delete(Constants.REDIS_KEY_TOKEN_ADMIN + token);
    }

    public void saveCategoryList(List<CategoryInfo> categoryList) {
        redisUtils.set(Constants.REDIS_KEY_CATEGORY_LIST,categoryList);
    }

    public List<CategoryInfo> getCategoryList() {
        return (List<CategoryInfo>) redisUtils.get(Constants.REDIS_KEY_CATEGORY_LIST);
    }


    /**
     * 保存上传文件信息
     *
     * @param fileName
     * @param chunks
     * @return
     */
    public String savePreVideoFileInfo(String userId, String fileName, Integer chunks) {
        String uploadId = StringTools.getRandomString(Constants.LENGTH_15);
        UploadingFileDto fileDto = new UploadingFileDto();
        fileDto.setChunks(chunks);
        fileDto.setFileName(fileName);
        fileDto.setUploadId(uploadId);
        fileDto.setChunkIndex(0);

        String day = DateUtil.format(new Date(), DateTimePatternEnum.YYYYMMDD.getPattern());
        String filePath = day + "/" + userId + uploadId;

        String folder = appConfig.getProjectFolder() + Constants.FILE_FOLDER + Constants.FILE_FOLDER_TEMP + filePath;
        File folderFile = new File(folder);
        if (!folderFile.exists()) {
            folderFile.mkdirs();
        }
        fileDto.setFilePath(filePath);
        redisUtils.setex(Constants.REDIS_KEY_UPLOADING_FILE + userId + uploadId, fileDto, Constants.REDIS_KEY_EXPIRES_DAY);
        return uploadId;
    }

    public UploadingFileDto getUploadingVideoFile(String userId, String uploadId) {
        return (UploadingFileDto) redisUtils.get(Constants.REDIS_KEY_UPLOADING_FILE + userId + uploadId);
    }

    public void updateVideoFileInfo(String userId, UploadingFileDto fileDto) {
        redisUtils.setex(Constants.REDIS_KEY_UPLOADING_FILE + userId + fileDto.getUploadId(), fileDto, Constants.REDIS_KEY_EXPIRES_DAY);
    }

    public SysSettingDto getSysSettingDto() {
        SysSettingDto sysSettingDto = (SysSettingDto) redisUtils.get(Constants.REDIS_KEY_SYS_SETTING);
        if (sysSettingDto == null) {
            sysSettingDto = new SysSettingDto();
        }
        return sysSettingDto;
    }

    public Integer reportVideoPlayOnline(String fileId, String deviceId) {
        String userPlayOnlineKey = String.format(Constants.REDIS_KEY_VIDEO_PLAY_COUNT_USER, fileId, deviceId);
        String playOnlineCountKey = String.format(Constants.REDIS_KEY_VIDEO_PLAY_COUNT_ONLINE, fileId);

        if (!redisUtils.keyExists(userPlayOnlineKey)) {
            redisUtils.setex(userPlayOnlineKey, fileId, Constants.REDIS_KEY_EXPIRES_ONE_SECONDS * 8);
            return redisUtils.incrementex(playOnlineCountKey, Constants.REDIS_KEY_EXPIRES_ONE_SECONDS * 10).intValue();
        }
        //给视频在线总数量续期
        redisUtils.expire(playOnlineCountKey, Constants.REDIS_KEY_EXPIRES_ONE_SECONDS * 10);
        //给播放用户续期
        redisUtils.expire(userPlayOnlineKey, Constants.REDIS_KEY_EXPIRES_ONE_SECONDS * 8);
        Integer count = (Integer) redisUtils.get(playOnlineCountKey);
        return count == null ? 1 : count;
    }

    // 减少在线人数
    public void decrementPlayOnlineCount(String key) {
        redisUtils.decrement(key);
    }

    public void delVideoFileInfo(String userId, String uploadId) {
        redisUtils.delete(Constants.REDIS_KEY_UPLOADING_FILE + userId + uploadId);
    }

    public void addFile2DelQueue(String videoId, List<String> fileIdList) {
        redisUtils.lpushAll(Constants.REDIS_KEY_FILE_DEL + videoId, fileIdList, Constants.REDIS_KEY_EXPIRES_DAY * 7);
    }
    public List<String> getDelFileList(String videoId) {
        List<String> filePathList = redisUtils.getQueueList(Constants.REDIS_KEY_FILE_DEL + videoId);
        return filePathList;
    }

    public void cleanDelFileList(String videoId) {
        redisUtils.delete(Constants.REDIS_KEY_FILE_DEL + videoId);
    }

    public void addFile2TransferQueue(List<VideoInfoFilePost> fileList) {
        redisUtils.lpushAll(Constants.REDIS_KEY_QUEUE_TRANSFER, fileList, 0);
    }



}

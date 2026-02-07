package com.Easylive.admin.controller;

import com.Easylive.entity.config.AppConfig;
import com.Easylive.entity.constants.Constants;
import com.Easylive.entity.enums.DateTimePatternEnum;
import com.Easylive.entity.enums.ResponseCodeEnum;
import com.Easylive.entity.vo.ResponseVO;
import com.Easylive.exception.BusinessException;
import com.Easylive.utils.DateUtil;
import com.Easylive.utils.FFmpegUtils;
import com.Easylive.utils.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

@Validated
@Slf4j
@RestController
@RequestMapping("/file")
public class FileController extends ABaseController {

    @Resource
    private AppConfig appConfig;


    @Resource
    private FFmpegUtils fFmpegUtils;

    @RequestMapping("/uploadImage")
    public ResponseVO uploadCover(@NotNull MultipartFile file, @NotNull Boolean createThumbnail) throws IOException {
        String month = DateUtil.format(new Date(), DateTimePatternEnum.YYYYMM.getPattern());
        String folder = appConfig.getProjectFolder() + Constants.FILE_FOLDER + Constants.FILE_COVER + month;
        File folderFile = new File(folder);
        if (!folderFile.exists()) {
            folderFile.mkdirs();
        }
        String fileName = file.getOriginalFilename();
        String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
        String realFileName = StringTools.getRandomString(Constants.LENGTH_30) + fileSuffix;
        String filePath = folder + "/" + realFileName;
        file.transferTo(new File(filePath));
        if (createThumbnail) {
            //生成缩略图
            fFmpegUtils.createImageThumbnail(filePath);
        }
        return getSuccessResponseVO(Constants.FILE_COVER + month + "/" + realFileName);
    }


    @RequestMapping("/getResource")
    public void getResource(HttpServletResponse response, @NotEmpty String sourceName) {
        if (!StringTools.pathIsOk(sourceName)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        String suffix = StringTools.getFileSuffix(sourceName);
        response.setContentType("image/" + suffix.replace(".", ""));
        response.setHeader("Cache-Control", "max-age=2592000");
        readFile(response, sourceName);
    }

    protected void readFile(HttpServletResponse response, String filePath) {
        File file = new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER + filePath);
        if (!file.exists()) {
            return;
        }
        try (OutputStream out = response.getOutputStream(); FileInputStream in = new FileInputStream(file)) {
            byte[] byteData = new byte[1024];
            int len = 0;
            while ((len = in.read(byteData)) != -1) {
                out.write(byteData, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            log.error("读取文件异常", e);
        }
    }

}

package com.example.wps_demo.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wps_demo.dao.OnFileMapper;
import com.example.wps_demo.entity.OnFile;
import com.office.tools.FileUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author tonghui
 * @since 2022-03-10
 */
@Service
public class OnFileServiceImpl extends ServiceImpl<OnFileMapper, OnFile> implements OnFileService {

    private String path = System.getProperty("user.dir") + File.separator + "file";

    @Override
    public void download(String id, String isBrowser, HttpServletResponse response) {
        OnFile byId = baseMapper.selectById(id);

        //判断是否有值
        if (StrUtil.isNotEmpty(isBrowser)) {
            FileUtil.downloadAttachment(path + File.separator + id + "." + byId.getFileType(), byId.getFileName(), response);
        } else {
            FileUtil.downloadInline(path + File.separator + id + "." + byId.getFileType(), byId.getFileName(), response);
        }
    }

    @Override
    public void removeFile(String id) {
        OnFile byId = baseMapper.selectById(id);
        System.out.println(path);
        File file = new File(path + File.separator + id + "." + byId.getFileType());
        System.out.println("删除文件");
        FileUtils.deleteQuietly(file);

        removeById(id);
    }

    public String saveFile(byte[] bytes, String fileType) throws Exception {

        String fileId = IdUtil.simpleUUID();


        File file = new File(path + File.separator + fileId + "." + fileType);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        try (FileOutputStream out = new FileOutputStream(path + File.separator + fileId + "." + fileType)) {
            out.write(bytes);
            out.flush();
        }
        return fileId;
    }
}

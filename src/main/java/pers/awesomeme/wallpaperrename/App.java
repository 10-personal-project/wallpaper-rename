package pers.awesomeme.wallpaperrename;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import pers.awesomeme.commoncode.Util;
import pers.awesomeme.wallpaperrename.dto.WallpaperInfo;

import java.util.List;
import java.util.stream.Collectors;

public class App
{
    public static void main(String[] args)
    {
        // 检查参数
        Assert.notEmpty(args, "必须提供路径");
        Assert.isTrue(FileUtil.isDirectory(args[0]), "提供路径的不正常");
        final String DIR = args[0];

        // 获取壁纸
        List<WallpaperInfo> wallpaperInfoList = FileUtil.loopFiles(DIR, pathname -> !StrUtil.startWith(pathname.getName(), StrUtil.DOT) && Util.isImg(pathname))
                                                        .stream()
                                                        .map(WallpaperInfo::getInstance)
                                                        .collect(Collectors.toList());

        // 对壁纸进行重命名
        wallpaperInfoList.forEach(el -> FileUtil.rename(el.getFile(), el.getNewFileName(), true, true));
        Console.log("程序运行结束");
    }
}
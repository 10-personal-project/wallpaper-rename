package pers.awesomeme.wallpaperrename.dto;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.ToString;
import pers.awesomeme.commoncode.OptRuntimeException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.io.File;
import java.math.BigDecimal;

/**
 * 壁纸信息对象
 */
@Getter
@ToString
public class WallpaperInfo
{
    /**
     * 图片文件
     */
    private File file;

    /**
     * 图片文件的宽/高之比.
     */
    private BigDecimal scale;

    /**
     * 图片应该被重命名成的名字 <br/>
     * scale_n比m_md5码.扩展名 <br/>
     * 1.33_4比3_7f42a92.jpg
     */
    private String newFileName;

    /**
     * 私用构造函数
     */
    private WallpaperInfo()
    {
    }

    /**
     * getInstance，创建对象必须走这个方法
     * @param file file
     * @return WallpaperInfo
     */
    public static WallpaperInfo getInstance(File file)
    {
        WallpaperInfo wallpaperInfo = new WallpaperInfo();
        wallpaperInfo.file = file;
        wallpaperInfo.setScaleAndNewFileNameAndMd5();
        Console.log("读取壁纸：{}", wallpaperInfo.file.getAbsolutePath());
        return wallpaperInfo;
    }

    /**
     * 计算这个文件的scale 和 newFileName
     */
    private void setScaleAndNewFileNameAndMd5()
    {
        // 检查
        Assert.notNull(this.file, "必须先赋值文件才能计算");

        // 准备工作
        int[] picInfo = this.calcPicInfo();
        int width = picInfo[0];
        int height = picInfo[1];
        int divisor = picInfo[2];

        // 赋值
        this.scale = NumberUtil.round(NumberUtil.div(width, height), 2);
        String scaleStr = this.scale.toPlainString();
        this.newFileName = StrUtil.format("{}_{}比{}_{}",
                                          scaleStr,
                                          width/divisor,
                                          height/divisor,
                                          RandomUtil.randomString(7));
    }

    /**
     * 计算这个文件的三个参数.
     * @return 0-宽 1-高 2-宽和度的最大公约数
     */
    private int[] calcPicInfo()
    {
        Assert.notNull(this.file, "必须先赋值文件才能计算");

        int width;
        int height;
        try
        {
            String type = FileUtil.getType(this.file);
            ImageReader reader = ImgUtil.getReader(type);
            reader.setInput(ImageIO.createImageInputStream(this.file), true);
            width = reader.getWidth(0);
            height = reader.getHeight(0);
        }
        catch (Exception e)
        {
            throw OptRuntimeException.getInstance(e.getMessage());
        }
        int divisor = NumberUtil.divisor(width, height);

        return new int[]{width, height, divisor};
    }
}

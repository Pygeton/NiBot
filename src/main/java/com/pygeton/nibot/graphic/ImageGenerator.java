package com.pygeton.nibot.graphic;

import com.pygeton.nibot.communication.entity.mai.MaimaiBest50;
import com.pygeton.nibot.communication.entity.mai.MaimaiChartInfo;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class ImageGenerator {

    public void generateB50Image(MaimaiBest50 best50){
        List<MaimaiChartInfo> b35List = best50.getB35List();
        List<MaimaiChartInfo> b15List = best50.getB15List();
        System.out.println("done");


        Mat template = Imgcodecs.imread(Objects.requireNonNull(this.getClass().getResource("/mai/template/b50.jpg")).getPath());
        // 计算每张图片的目标大小和间隔
        int targetWidth = (int) (template.cols() / (5 + 4 * 1.5));
        int targetHeight = template.rows() / 7;
        int gapWidth = (int)(1.5 * targetWidth);

        // 读取并处理每张图片
        for (int i = 0; i < 35; i++) {
            // 读取图片
            Mat image = Imgcodecs.imread(b35List.get(i).getCoverUrl());

            // 调整图片大小
            Imgproc.resize(image, image, new Size(targetWidth, targetHeight));

            // 计算图片的目标位置
            int x = (i % 5) * (targetWidth + gapWidth);
            int y = (i / 5) * targetHeight;

            // 将图片放置在目标位置
            image.copyTo(template.submat(new Rect(x, y, targetWidth, targetHeight)));

            // 创建一个矩形
            Rect rect = new Rect(x + targetWidth, y, gapWidth, targetHeight);

            // 填充矩形
            Scalar color = new Scalar(0, 0, 255); // 这里的颜色应由外部参数决定
            Imgproc.rectangle(template, rect.tl(), rect.br(), color, -1);

            // 设置文本参数
            int fontFace = Imgproc.FONT_HERSHEY_SIMPLEX;
            double fontScale = 1.0;
            Scalar textColor = new Scalar(255, 255, 255);
            int thickness = 2;

            // 在矩形中放置三行文本
            Imgproc.putText(template, "第一行文本", new Point(x + targetWidth, y + targetHeight / 4), fontFace, fontScale * 1.5, textColor, thickness);
            Imgproc.putText(template, "第二行文本", new Point(x + targetWidth, y + targetHeight / 2), fontFace, fontScale, textColor, thickness);
            Imgproc.putText(template, "第三行文本", new Point(x + targetWidth, y + targetHeight * 3 / 4), fontFace, fontScale, textColor, thickness);
        }

        // 保存结果图片
        Imgcodecs.imwrite("D:/Codeworks/Java/NiBot/src/main/resources/test/output.jpg", template);

        System.out.println("done");
    }
}

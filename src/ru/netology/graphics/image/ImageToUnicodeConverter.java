package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class ImageToUnicodeConverter implements TextGraphicsConverter {
    private int width;
    private int height;
    private double maxRatio;
    private TextColorSchema schema;
    private String[][] picArray;

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {

        BufferedImage img = ImageIO.read(new URL(url));

        int newWidth  = img.getWidth();
        int newHeight = img.getHeight();
        int ratioImgHW = newHeight/newWidth;
        int ratioWH = newWidth/newHeight;
        if (ratioImgHW > maxRatio) {
            new BadImageSizeException(ratioImgHW, maxRatio);
        }
        if (ratioWH > maxRatio) {
            new BadImageSizeException(ratioWH, maxRatio);
        }
        if (newWidth > width) {
            double difference = newWidth/width;
            newHeight /= difference;
            newWidth = width;
        } if (newHeight > height) {
            double difference = newHeight / height;
            newWidth /= difference;
            newHeight = height;
        }


        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);

        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);

        WritableRaster bwRaster = bwImg.getRaster();
        TextColorSchema schema = new ColorConverter();
        picArray = new String[bwRaster.getHeight()][bwRaster.getWidth()];
        for (int i = 0; i < bwRaster.getWidth(); i++) {
            for (int j = 0; j < bwRaster.getHeight(); j++) {
                int color = bwRaster.getPixel(i, j, new int[3])[0];
                char c = schema.convert(color);
                picArray[j][i] = String.valueOf(c);
            }

        }
        String text = String.valueOf(painter(picArray));
        return text;
    }

    @Override
    public void setMaxWidth(int width) {
        this.width = width;

    }

    @Override
    public void setMaxHeight(int height) {
        this.height = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;

    }

    private static StringBuilder painter(String[][] array) {
        StringBuilder pic = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                pic.append(array[i][j]);
                pic.append(array[i][j]);
            }
            pic.append("\n");
        }
        return pic;
    }

}

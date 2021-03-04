package com.xiaospace.poster.util;

import com.joyoung.base.app.util.poster.model.*;
import org.springframework.util.CollectionUtils;
import sun.awt.image.BufferedImageGraphicsConfig;
import sun.font.FontDesignMetrics;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class PosterUtil {

    public static BufferedImage drawPosterBlock(PosterBlock posterBlock) throws Exception {
        BufferedImage image = newBufferedImage(posterBlock.getWidth(), posterBlock.getHeight());
        Graphics2D graphics2D = newGraphics2D(image, posterBlock.getRadius());
        List<Block> allBlock = posterBlock.getAllBlock();
        if (!CollectionUtils.isEmpty(allBlock)) {
            for (int i = 0; i < allBlock.size(); i++) {
                Block block = allBlock.get(i);
                switch (block.getBlockType()) {
                    case image:
                        drawImage(graphics2D, (ImageBlock) block);
                        break;
                    case area:
                        drawArea(graphics2D, (AreaBlock) block);
                        break;
                    case font:
                        drawFont(graphics2D, (FontBlock) block);
                        break;
                    case poster:
                        drawPoster(graphics2D, (PosterBlock) block);
                        break;
                    default:
                        break;
                }
            }
        }
        graphics2D.dispose();
        return image;
    }

    private static void drawPoster(Graphics2D graphics2D, PosterBlock posterBlock) throws Exception {
        BufferedImage bufferedImage = drawPosterBlock(posterBlock);
        Image image = bufferedImage.getScaledInstance(bufferedImage.getWidth(), bufferedImage.getHeight(), posterBlock.getHints());
        graphics2D.drawImage(image, posterBlock.getX(), posterBlock.getY(), null);
    }


    private static void drawFont(Graphics2D graphics2D, FontBlock fontBlock) throws Exception {
        String content = fontBlock.getContent();
        int lineSpacing = fontBlock.getLineSpacing();
        Integer x = fontBlock.getX();
        Integer y = fontBlock.getY();
        Integer maxWidth = fontBlock.getWidth();
        Font font = fontBlock.getFont();
        graphics2D.setPaint(new Color(fontBlock.getColor()));
        graphics2D.setFont(font);
        if (maxWidth == null) {
            graphics2D.drawString(fontBlock.getContent(), x, y);
            return;
        }
        FontDesignMetrics metrics = FontDesignMetrics.getMetrics(font);
        int wordWidth = getWordWidth(metrics, content);
        if (wordWidth <= maxWidth) {
            graphics2D.drawString(content, x, y);
        } else {
            //TODO 算法可以优化
            int height = metrics.getHeight();
            int avgSize = wordWidth / maxWidth;
            int forSize = avgSize + 1;
            int length = content.length();
            int avgIndex = length / avgSize;
            int lastIndex = 0;
            for (int i = 0; i < forSize; i++) {
                int j = 0;
                boolean isWhile = false;
                while (true && lastIndex < length) {
                    int nowIndex = lastIndex + avgIndex + j >= length ? length : lastIndex + avgIndex + j;
                    String substring = content.substring(lastIndex, nowIndex);
                    int subWidth = getWordWidth(metrics, substring);
                    if (subWidth > maxWidth) {
                        j += -1;
                        isWhile = true;
                        continue;
                    } else if (subWidth < maxWidth) {
                        if (!isWhile && nowIndex != length) {
                            j += 1;
                            continue;
                        } else {
                            graphics2D.drawString(substring, x, y + (height + lineSpacing) * i);
                            lastIndex = nowIndex;
                            break;
                        }
                    } else {
                        graphics2D.drawString(substring, x, y + (height + lineSpacing) * i);
                        lastIndex = nowIndex;
                        break;
                    }
                }
            }
        }
    }

    private static void drawImage(Graphics2D graphics2D, ImageBlock imageBlock) throws Exception {
        graphics2D.drawImage(imageBlock.getImage(), imageBlock.getX(), imageBlock.getY(), null);
    }

    private static void drawArea(Graphics2D graphics2D, AreaBlock areaBlock) throws Exception {
        graphics2D.setPaint(new Color(areaBlock.getColor()));
        Radius radius = areaBlock.getRadius();
        int arcWidth = 0;
        int arcHeight = 0;
        if (radius != null) {
            arcWidth = radius.getArcW();
            arcHeight = radius.getArcH();
        }
        graphics2D.fillRoundRect(areaBlock.getX(), areaBlock.getY(), areaBlock.getWidth(), areaBlock.getHeight(), arcWidth, arcHeight);
    }

    private static BufferedImage newBufferedImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        BufferedImageGraphicsConfig config = BufferedImageGraphicsConfig.getConfig(image);
        image = config.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        return image;
    }

    private static Graphics2D newGraphics2D(BufferedImage image, Radius radius) {
        Graphics2D gs = image.createGraphics();
        gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gs.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        if (radius != null) {
            gs.setClip(new RoundRectangle2D.Double(0, 0, image.getWidth(), image.getHeight(), radius.getArcW(), radius.getArcH()));
        }
        return gs;
    }


    /**
     * 图片圆角，还没有上去
     */
    private static BufferedImage setClip(BufferedImage srcImage, int radius) {
        int width = srcImage.getWidth();
        int height = srcImage.getHeight();
        BufferedImage image = newBufferedImage(width, height);
        Graphics2D gs = newGraphics2D(image, new Radius());
        gs.setClip(new RoundRectangle2D.Double(0, 0, width, height, radius, radius));
        gs.drawImage(srcImage, 0, 0, null);
        gs.dispose();
        return image;
    }

    public static void main(String[] args) throws Exception {
        int rgb = new Color(255, 255, 255).getRGB();
        System.out.println(rgb);
    }

    private static int getWordWidth(FontDesignMetrics metrics, String content) {
        int width = 0;
        for (int i = 0; i < content.length(); i++) {
            width += metrics.charWidth(content.charAt(i));
        }
        return width;
    }


}

package com.xiaospace.poster.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Setter
@Getter
public class FontBlock extends Block {

    private String content;
    private int lineSpacing;
    private String fontName;
    private int style;
    private int size;

    public Font getFont() throws IOException, FontFormatException {
        if (StringUtils.isEmpty(fontName)) {
            return defaultFont(style,size);
        }
        return new Font(fontName, style, size);
    }

    private Font defaultFont( int style,int fontSize) throws FontFormatException, IOException {
        ClassPathResource cpr = new ClassPathResource("font" + File.separator + "方正兰亭细黑简体.TTF");
        try (InputStream in = cpr.getInputStream()) {
            Font dynamicFont = Font.createFont(Font.TRUETYPE_FONT, in);
            Font font = dynamicFont.deriveFont(style, fontSize);
            return font;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public String toString() {
        return "FontBlock{" +
                "content='" + content + '\'' +
                ", lineSpacing=" + lineSpacing +
                ", fontName='" + fontName + '\'' +
                ", style=" + style +
                ", size=" + size +
                '}';
    }
    //扫描存在的字体
    //        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        String[] fontFamilies = ge.getAvailableFontFamilyNames();
//        for (String s : fontFamilies) {
//            log.debug("存在字体：" + s);
//        }
}



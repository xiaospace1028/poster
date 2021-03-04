package com.xiaospace.poster.model;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Setter
@Getter
public class ImageBlock extends Block {
    private String code;
    /**
     * 详情查看{@link Image#SCALE_SMOOTH}
     */
    private int hints=4;
    private Image image;

    @Override
    public String toString() {
        return "ImageBlock{" +
                "code='" + code + '\'' +
                ", hints=" + hints +
                "} " + super.toString();
    }
}

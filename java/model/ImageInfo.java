package com.xiaospace.poster.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class ImageInfo implements Serializable, Cloneable {
    private String code;
    private String targetUrl;
    private TargetType targetType;
}

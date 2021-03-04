package com.xiaospace.poster.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Optional;

@Setter
@Getter
public abstract class Block implements Serializable, Cloneable {
    /**
     * 积木类型
     */
    private BlockType blockType;
    /**
     * 备注
     */
    private String remark;
    private Radius radius;
    private int color;

    /**
     * 长宽
     */
    private Integer width;
    private Integer height;

    /**
     * 位置
     */
    private Integer x;
    private Integer y;
    private int z;
    private int sort;

    public String getRemark() {
        return Optional.ofNullable(remark).orElse("");
    }

    public Integer getWidth() throws Exception {
        if (blockType != BlockType.font && width == null) {
            throw new Exception(getRemark() + "的width参数错误");
        }
        return width;
    }

    public Integer getHeight() throws Exception {
        if (blockType != BlockType.font && height == null) {
            throw new Exception(getRemark() + "的height参数错误");
        }
        return height;
    }

    public Integer getX() throws Exception {
        if (x == null) {
            throw new Exception(getRemark() + "的 X 参数错误");
        }
        return x;
    }

    public Integer getY() throws Exception {
        if (y == null) {
            throw new Exception(getRemark() + "的 Y 参数错误");
        }
        return y;
    }

    @Override
    public String toString() {
        return "Block{" +
                "blockType=" + blockType +
                ", remark='" + remark + '\'' +
                ", radius=" + radius +
                ", color=" + color +
                ", width=" + width +
                ", height=" + height +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", sort=" + sort +
                '}';
    }
}


package com.xiaospace.poster.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Optional;

@Getter
@Setter
public class Radius implements Serializable, Cloneable {
    private Integer arcW;
    private Integer arcH;
    private Integer arc;

    public int getArcW() {
        return Optional.ofNullable(arcW).orElse(Optional.ofNullable(arc).orElse(0));
    }

    public int getArcH() {
        return Optional.ofNullable(arcH).orElse(Optional.ofNullable(arc).orElse(0));
    }
}

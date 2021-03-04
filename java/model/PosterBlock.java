package com.xiaospace.poster.model;

import lombok.Setter;
import org.springframework.util.CollectionUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
public class PosterBlock extends Block {
    /**
     * 图片池子
     */
    private List<ImageInfo> images;
    /**
     * 图片模块
     */
    private List<ImageBlock> imageBlocks;
    /**
     * 组模块
     */
    private List<PosterBlock> posterBlocks;
    /**
     * 字模块
     */
    private List<FontBlock> fontBlocks;

    /**
     * div填充模块
     */
    private List<AreaBlock> areaBlocks;

    /**
     * 详情查看{@link Image#SCALE_SMOOTH}
     */
    private int hints = 4;

    public int getHints() {
        return hints;
    }

    public Map<String, BufferedImage> imagePool(List<ImageInfo> images) throws IOException {
        Map<String, BufferedImage> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(images)) {
            for (int i = 0; i < images.size(); i++) {
                ImageInfo imageInfo = images.get(i);
                BufferedImage read = null;
                switch (imageInfo.getTargetType()) {
                    case web:
                        read = ImageIO.read(new URL(imageInfo.getTargetUrl()).openStream());
                        break;
                    case local:
                        read = ImageIO.read(new File(imageInfo.getTargetUrl()));
                        break;
                }
                map.put(imageInfo.getCode(), read);
            }
        }
        return map;
    }

    private void fillImageBlock() throws Exception {
        if (!CollectionUtils.isEmpty(imageBlocks)) {
            Map<String, BufferedImage> codeImagePool = imagePool(images);
            if (CollectionUtils.isEmpty(codeImagePool)) {
                throw new Exception("图片池为空！！！");
            } else {
                for (int i = 0; i < imageBlocks.size(); i++) {
                    ImageBlock imageBlock = imageBlocks.get(i);
                    if (!codeImagePool.containsKey(imageBlock.getCode())) {
                        throw new Exception("不存在图片：" + imageBlock.getCode());
                    }
                    BufferedImage bufferedImage = codeImagePool.get(imageBlock.getCode());
                    Image image = bufferedImage.getScaledInstance(imageBlock.getWidth(), imageBlock.getHeight(), imageBlock.getHints());
                    imageBlock.setImage(image);
                }
            }
        }
    }

    public List<Block> getAllBlock() throws Exception {
        fillImageBlock();
        List<Block> blocks = new ArrayList<>();
        if (!CollectionUtils.isEmpty(posterBlocks)) {
            for (PosterBlock pb : posterBlocks) {
                pb.setBlockType(BlockType.poster);
            }
            blocks.addAll(posterBlocks);
        }
        if (!CollectionUtils.isEmpty(areaBlocks)) {
            for (AreaBlock pb : areaBlocks) {
                pb.setBlockType(BlockType.area);
            }
            blocks.addAll(areaBlocks);
        }
        if (!CollectionUtils.isEmpty(fontBlocks)) {
            for (FontBlock pb : fontBlocks) {
                pb.setBlockType(BlockType.font);
            }
            blocks.addAll(fontBlocks);
        }
        if (!CollectionUtils.isEmpty(imageBlocks)) {
            for (ImageBlock pb : imageBlocks) {
                pb.setBlockType(BlockType.image);
            }
            blocks.addAll(imageBlocks);
        }
        blocks.sort((x1, x2) -> {
            int z = x1.getZ() - x2.getZ();
            return z == 0 ? x1.getSort() - x2.getSort() : z;
        });
        return blocks;
    }

    @Override
    public String toString() {
        return "PosterBlock{" +
                "images=" + images +
                ", imageBlocks=" + imageBlocks +
                ", posterBlocks=" + posterBlocks +
                ", fontBlocks=" + fontBlocks +
                ", areaBlocks=" + areaBlocks +
                ", hints=" + hints +
                "} " + super.toString();
    }
}
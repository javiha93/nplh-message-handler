package org.example.domain.message.entity.list;

import lombok.Data;
import org.example.domain.message.Reflection;
import org.example.domain.message.entity.Block;
import org.example.domain.message.entity.Slide;
import org.example.domain.message.entity.Specimen;

import java.util.ArrayList;
import java.util.List;

@Data
public class BlocksList extends Reflection implements Cloneable {
    private List<Block> blockList;

    public BlocksList() {
        Block block = new Block();
        List<Block> blocks = new ArrayList<>();
        blocks.add(block);

        this.blockList = blocks;
    }

    public void mergeBlock(Block newBlock) {
        boolean isNewBlock = true;
        if ((blockList == null) || (blockList.isEmpty())) {
            blockList = new ArrayList<>();
            blockList.add(newBlock);
        }
        for (Block block : blockList) {
            if (areBlockWithoutId(newBlock, block) || block.getId().equals(newBlock.getId())) {
                isNewBlock = false;
                List<Slide> slideList = newBlock.getAllSlides();
                for (Slide slide : slideList) {
                    block.getSlides().mergeSlide(slide);
                }
            }
        }
        if (isNewBlock) {
            blockList.add(newBlock);
        }
    }

    private boolean areBlockWithoutId(Block existingBlock ,Block newBlock) {
        return (existingBlock.getId() == null) && (newBlock.getId() == null)  && (blockList.size() == 1);
    }

    @Override
    public BlocksList clone() {
        try {
            BlocksList cloned = (BlocksList) super.clone();
            if (blockList == null) {
                return cloned;
            }
            List<Block> clonedBlockList = new ArrayList<>();
            for (Block block : this.blockList) {
                clonedBlockList.add(block.clone());
            }
            cloned.setBlockList(clonedBlockList);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning not supported for BlocksList", e);
        }
    }
}

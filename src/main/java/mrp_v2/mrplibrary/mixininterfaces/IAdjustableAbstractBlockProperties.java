package mrp_v2.mrplibrary.mixininterfaces;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.material.MaterialColor;

public interface IAdjustableAbstractBlockProperties
{
    AbstractBlock.Properties setMaterialColor(MaterialColor color);
    AbstractBlock.Properties setTicksRandomly(boolean ticksRandomly);
}

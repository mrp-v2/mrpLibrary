package mrp_v2.mrplibrary.mixininterfaces;

import net.minecraft.block.Block;
import net.minecraft.block.material.MaterialColor;

public interface IAdjustableAbstractBlockProperties
{
    Block.Properties setMaterialColor(MaterialColor color);
    Block.Properties setTicksRandomly(boolean ticksRandomly);
}

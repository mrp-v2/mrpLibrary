package mrp_v2.mrplibrary.mixininterfaces;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.material.MaterialColor;

public interface ISetMaterialColor
{
    AbstractBlock.Properties setMaterialColor(MaterialColor color);
}

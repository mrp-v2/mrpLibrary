package mrp_v2.mrplibrary.mixin;

import mrp_v2.mrplibrary.mixininterfaces.IAdjustableAbstractBlockProperties;
import net.minecraft.block.Block;
import net.minecraft.block.material.MaterialColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Block.Properties.class) public abstract class BlockPropertiesMixin implements IAdjustableAbstractBlockProperties
{
    @Shadow private MaterialColor mapColor;
    @Shadow private boolean ticksRandomly;

    @Override public Block.Properties setMaterialColor(MaterialColor materialColor)
    {
        mapColor = materialColor;
        return (Block.Properties) (Object) this;
    }

    @Override public Block.Properties setTicksRandomly(boolean ticksRandomly)
    {
        this.ticksRandomly = ticksRandomly;
        return (Block.Properties) (Object) this;
    }
}

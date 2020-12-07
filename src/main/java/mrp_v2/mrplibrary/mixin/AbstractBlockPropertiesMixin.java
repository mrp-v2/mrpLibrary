package mrp_v2.mrplibrary.mixin;

import mrp_v2.mrplibrary.mixininterfaces.IAdjustableAbstractBlockProperties;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Function;

@Mixin(AbstractBlock.Properties.class) public abstract class AbstractBlockPropertiesMixin
        implements IAdjustableAbstractBlockProperties
{
    @Shadow private Function<BlockState, MaterialColor> blockColors;
    @Shadow private boolean ticksRandomly;

    @Override public AbstractBlock.Properties setMaterialColor(MaterialColor materialColor)
    {
        this.blockColors = (state) -> materialColor;
        return (AbstractBlock.Properties) (Object) this;
    }

    @Override public AbstractBlock.Properties setTicksRandomly(boolean ticksRandomly)
    {
        this.ticksRandomly = ticksRandomly;
        return (AbstractBlock.Properties) (Object) this;
    }
}

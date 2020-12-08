package mrp_v2.mrplibrary.mixin;

import mrp_v2.mrplibrary.mixininterfaces.IModifiableModelBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ModelBuilder.class) public abstract class ModelBuilderMixin<T extends ModelBuilder<T>> extends ModelFile
        implements IModifiableModelBuilder<T>
{
    @Final @Shadow protected List<ModelBuilder<T>.ElementBuilder> elements;

    protected ModelBuilderMixin(ResourceLocation location)
    {
        super(location);
    }

    @Override public List<ModelBuilder<T>.ElementBuilder> getElements()
    {
        return this.elements;
    }
}

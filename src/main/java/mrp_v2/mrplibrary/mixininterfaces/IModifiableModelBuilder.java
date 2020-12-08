package mrp_v2.mrplibrary.mixininterfaces;

import net.minecraftforge.client.model.generators.ModelBuilder;

import java.util.List;

public interface IModifiableModelBuilder<T extends ModelBuilder<T>>
{
    List<ModelBuilder<T>.ElementBuilder> getElements();
}

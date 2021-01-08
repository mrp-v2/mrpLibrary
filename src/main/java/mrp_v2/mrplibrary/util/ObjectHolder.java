package mrp_v2.mrplibrary.util;

import mrp_v2.mrplibrary.MrpLibrary;
import mrp_v2.mrplibrary.item.crafting.ShapedRecipe;
import mrp_v2.mrplibrary.item.crafting.ShapelessRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ObjectHolder
{
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MrpLibrary.ID);
    public static final RegistryObject<IRecipeSerializer<?>> CRAFTING_SHAPED_SERIALIZER;
    public static final RegistryObject<IRecipeSerializer<?>> CRAFTING_SHAPELESS_SERIALIZER;

    static
    {
        CRAFTING_SHAPED_SERIALIZER = RECIPE_SERIALIZERS.register("crafting_shaped", ShapedRecipe.Serializer::new);
        CRAFTING_SHAPELESS_SERIALIZER =
                RECIPE_SERIALIZERS.register("crafting_shapeless", ShapelessRecipe.Serializer::new);
    }

    public static void registerListeners(IEventBus bus)
    {
        RECIPE_SERIALIZERS.register(bus);
    }
}

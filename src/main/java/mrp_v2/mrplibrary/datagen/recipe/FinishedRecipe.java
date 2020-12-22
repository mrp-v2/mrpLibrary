package mrp_v2.mrplibrary.datagen.recipe;

import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public abstract class FinishedRecipe implements IFinishedRecipe
{
    protected ResourceLocation id;
    protected RecipeResult result;
    protected String group;
    protected Advancement.Builder advancementBuilder;
    protected ResourceLocation advancementID;

    public FinishedRecipe(ResourceLocation id, RecipeResult result, String group,
            Advancement.Builder advancementBuilder, ResourceLocation advancementID)
    {
        this.id = id;
        this.result = result;
        this.group = group;
        this.advancementBuilder = advancementBuilder;
        this.advancementID = advancementID;
    }

    @Override public void serialize(JsonObject json)
    {
        if (!group.isEmpty())
        {
            json.addProperty("group", group);
        }
        JsonObject resultJson = new JsonObject();
        resultJson.addProperty("item", result.getItemID().toString());
        if (result.getCount() > 1)
        {
            resultJson.addProperty("count", result.getCount());
        }
        json.add("result", resultJson);
    }

    @Override public ResourceLocation getID()
    {
        return id;
    }

    @Nullable @Override public JsonObject getAdvancementJson()
    {
        return advancementBuilder.serialize();
    }

    @Nullable @Override public ResourceLocation getAdvancementID()
    {
        return advancementID;
    }
}

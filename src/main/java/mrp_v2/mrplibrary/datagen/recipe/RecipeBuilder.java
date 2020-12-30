package mrp_v2.mrplibrary.datagen.recipe;

import com.google.gson.JsonSyntaxException;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class RecipeBuilder
{
    public static final Logger LOGGER = LogManager.getLogger();
    protected RecipeResult result;
    @Nullable protected String group = null;
    protected Advancement.Builder advancementBuilder = Advancement.Builder.builder();

    public RecipeBuilder setGroup(@Nullable String groupIn)
    {
        group = groupIn;
        return this;
    }

    public void build(Consumer<IFinishedRecipe> consumerIn)
    {
        build(consumerIn, result.getItemID());
    }

    public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id)
    {
        validate(id);
        buildAdvancement(id);
        buildRecipe(consumerIn, id);
    }

    protected void validate(ResourceLocation id)
    {
        if (this.advancementBuilder.getCriteria().isEmpty())
        {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }

    protected void buildRecipe(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id)
    {
        consumerIn.accept(buildRecipe(id));
    }

    protected abstract IFinishedRecipe buildRecipe(ResourceLocation id);

    protected void buildAdvancement(ResourceLocation id)
    {
        advancementBuilder.withParentId(new ResourceLocation("recipes/root"))
                .withCriterion("has_the_recipe", new RecipeUnlockedTrigger.Instance(id))
                .withRewards(AdvancementRewards.Builder.recipe(id)).withRequirementsStrategy(IRequirementsStrategy.OR);
    }

    public RecipeBuilder addCriterion(String name, ICriterionInstance criterionIn)
    {
        advancementBuilder.withCriterion(name, criterionIn);
        return this;
    }

    public String getGroupString()
    {
        return group == null ? "" : group;
    }

    public ResourceLocation getAdvancementID(ResourceLocation recipeID)
    {
        return new ResourceLocation(recipeID.getNamespace(),
                "recipes/" + (result.hasGroup() ? result.getGroupPath() + "/" : "") + recipeID.getPath());
    }

    public void build(Consumer<IFinishedRecipe> consumerIn, String save)
    {
        ResourceLocation resultLoc = result.getItemID();
        ResourceLocation saveLoc = new ResourceLocation(save);
        if (saveLoc.equals(resultLoc))
        {
            throw new IllegalStateException("Recipe " + save + " should remove its 'save' argument");
        } else
        {
            build(consumerIn, saveLoc);
        }
    }

    public static <T extends ICraftingRecipe> T quietMissingItemError(Supplier<T> readFunc, ResourceLocation recipeId)
    {
        try
        {
            return readFunc.get();
        } catch (JsonSyntaxException e)
        {
            if (e.getMessage().startsWith("Unknown Item '"))
            {
                String itemID = e.getMessage().split("'")[1];
                LOGGER.info("Ignoring recipe " + recipeId.toString() + " because item '" + itemID + "' does not exist");
                return null;
            } else
            {
                throw e;
            }
        }
    }
}

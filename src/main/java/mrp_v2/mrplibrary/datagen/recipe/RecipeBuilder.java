package mrp_v2.mrplibrary.datagen.recipe;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public abstract class RecipeBuilder
{
    protected RecipeResult result;
    @Nullable protected String group;
    protected Advancement.Builder advancementBuilder;

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
                .withCriterion("has_the_recipe", RecipeUnlockedTrigger.create(id))
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
}

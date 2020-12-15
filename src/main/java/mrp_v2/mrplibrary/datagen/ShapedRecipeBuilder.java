package mrp_v2.mrplibrary.datagen;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ShapedRecipeBuilder extends net.minecraft.data.ShapedRecipeBuilder
{
    public ShapedRecipeBuilder(IItemProvider resultIn, int countIn)
    {
        super(resultIn, countIn);
    }

    @Override public ShapedRecipeBuilder key(Character symbol, ITag<Item> tagIn)
    {
        super.key(symbol, tagIn);
        return this;
    }

    @Override public ShapedRecipeBuilder key(Character symbol, IItemProvider itemIn)
    {
        super.key(symbol, itemIn);
        return this;
    }

    @Override public ShapedRecipeBuilder key(Character symbol, Ingredient ingredientIn)
    {
        super.key(symbol, ingredientIn);
        return this;
    }

    @Override public ShapedRecipeBuilder patternLine(String patternIn)
    {
        super.patternLine(patternIn);
        return this;
    }

    @Override public ShapedRecipeBuilder addCriterion(String name, ICriterionInstance criterionIn)
    {
        super.addCriterion(name, criterionIn);
        return this;
    }

    @Override public ShapedRecipeBuilder setGroup(String groupIn)
    {
        super.setGroup(groupIn);
        return this;
    }

    @Override public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id)
    {
        build(consumerIn, id, net.minecraft.data.ShapedRecipeBuilder.Result::new);
    }

    protected void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id, IResultConstructor constructor)
    {
        this.validate(id);
        this.advancementBuilder.withParentId(new ResourceLocation("recipes/root"))
                .withCriterion("has_the_recipe", RecipeUnlockedTrigger.create(id))
                .withRewards(AdvancementRewards.Builder.recipe(id)).withRequirementsStrategy(IRequirementsStrategy.OR);
        consumerIn.accept(constructor
                .apply(id, this.result, this.count, this.group == null ? "" : this.group, this.pattern, this.key,
                        this.advancementBuilder, new ResourceLocation(id.getNamespace(), "recipes/" +
                                (this.result.getGroup() == null ? "" : (this.result.getGroup().getPath() + "/")) +
                                id.getPath())));
    }

    @FunctionalInterface public interface IResultConstructor
    {
        net.minecraft.data.ShapedRecipeBuilder.Result apply(ResourceLocation idIn, Item resultIn, int countIn,
                String groupIn, List<String> patternIn, Map<Character, Ingredient> keyIn,
                Advancement.Builder advancementBuilderIn, ResourceLocation advancementIdIn);
    }
}

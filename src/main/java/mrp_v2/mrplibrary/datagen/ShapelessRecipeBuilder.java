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
import java.util.function.Consumer;

public class ShapelessRecipeBuilder extends net.minecraft.data.ShapelessRecipeBuilder
{
    public ShapelessRecipeBuilder(IItemProvider resultIn, int countIn)
    {
        super(resultIn, countIn);
    }

    public static ShapelessRecipeBuilder shapelessRecipe(IItemProvider resultIn)
    {
        return shapelessRecipe(resultIn, 1);
    }

    public static ShapelessRecipeBuilder shapelessRecipe(IItemProvider resultIn, int countIn)
    {
        return new ShapelessRecipeBuilder(resultIn, countIn);
    }

    @Override public ShapelessRecipeBuilder addIngredient(ITag<Item> tagIn)
    {
        super.addIngredient(tagIn);
        return this;
    }

    @Override public ShapelessRecipeBuilder addIngredient(IItemProvider itemIn)
    {
        super.addIngredient(itemIn);
        return this;
    }

    @Override public ShapelessRecipeBuilder addIngredient(IItemProvider itemIn, int quantity)
    {
        super.addIngredient(itemIn, quantity);
        return this;
    }

    @Override public ShapelessRecipeBuilder addIngredient(Ingredient ingredientIn)
    {
        super.addIngredient(ingredientIn);
        return this;
    }

    @Override public ShapelessRecipeBuilder addIngredient(Ingredient ingredientIn, int quantity)
    {
        super.addIngredient(ingredientIn, quantity);
        return this;
    }

    @Override public ShapelessRecipeBuilder addCriterion(String name, ICriterionInstance criterionIn)
    {
        super.addCriterion(name, criterionIn);
        return this;
    }

    @Override public ShapelessRecipeBuilder setGroup(String groupIn)
    {
        super.setGroup(groupIn);
        return this;
    }

    @Override public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id)
    {
        build(consumerIn, id, net.minecraft.data.ShapelessRecipeBuilder.Result::new);
    }

    protected void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id, IResultConstructor constructor)
    {
        this.validate(id);
        this.advancementBuilder.withParentId(new ResourceLocation("recipes/root"))
                .withCriterion("has_the_recipe", RecipeUnlockedTrigger.create(id))
                .withRewards(AdvancementRewards.Builder.recipe(id)).withRequirementsStrategy(IRequirementsStrategy.OR);
        consumerIn.accept(constructor
                .apply(id, this.result, this.count, this.group == null ? "" : this.group, this.ingredients,
                        this.advancementBuilder, new ResourceLocation(id.getNamespace(), "recipes/" +
                                (this.result.getGroup() == null ? "" : (this.result.getGroup().getPath() + "/")) +
                                id.getPath())));
    }

    @FunctionalInterface public interface IResultConstructor
    {
        net.minecraft.data.ShapelessRecipeBuilder.Result apply(ResourceLocation idIn, Item resultIn, int countIn,
                String groupIn, List<Ingredient> ingredientsIn, Advancement.Builder advancementBuilderIn,
                ResourceLocation advancementIdIn);
    }
}

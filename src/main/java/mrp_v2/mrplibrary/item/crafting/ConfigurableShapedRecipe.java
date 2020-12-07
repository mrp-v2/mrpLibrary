package mrp_v2.mrplibrary.item.crafting;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mrp_v2.mrplibrary.MrpLibrary;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

public class ConfigurableShapedRecipe extends ConfigurableCraftingRecipe implements IShapedRecipe<CraftingInventory>
{
    private final int recipeWidth;
    private final int recipeHeight;

    private ConfigurableShapedRecipe(ResourceLocation id, String group, ItemStack recipeOutput,
            NonNullList<Ingredient> recipeItems, int recipeWidth, int recipeHeight)
    {
        super(id, group, recipeOutput, recipeItems);
        this.recipeWidth = recipeWidth;
        this.recipeHeight = recipeHeight;
    }

    private ConfigurableShapedRecipe(ResourceLocation id, String group, ItemStack recipeOutput,
            NonNullList<Ingredient> recipeItems, int recipeWidth, int recipeHeight, Set<IngredientOverride> overrides)
    {
        super(id, group, recipeOutput, recipeItems, overrides);
        this.recipeWidth = recipeWidth;
        this.recipeHeight = recipeHeight;
    }

    private static Map<String, Ingredient> deserializeKey(JsonObject json)
    {
        Map<String, Ingredient> map = Maps.newHashMap();
        for (Map.Entry<String, JsonElement> entry : json.entrySet())
        {
            if (entry.getKey().length() != 1)
            {
                throw new JsonSyntaxException(
                        "Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            }
            if (" ".equals(entry.getKey()))
            {
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            }
            map.put(entry.getKey(), Ingredient.deserialize(entry.getValue()));
        }
        map.put(" ", Ingredient.EMPTY);
        return map;
    }

    private static String[] patternFromJson(JsonArray jsonArr)
    {
        String[] strings = new String[jsonArr.size()];
        if (strings.length > MAX_HEIGHT)
        {
            throw new JsonSyntaxException("Invalid pattern: too many rows, " + MAX_HEIGHT + " is maximum");
        } else if (strings.length == 0)
        {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        } else
        {
            for (int i = 0; i < strings.length; ++i)
            {
                String s = JSONUtils.getString(jsonArr.get(i), "pattern[" + i + "]");
                if (s.length() > MAX_WIDTH)
                {
                    throw new JsonSyntaxException("Invalid pattern: too many columns, " + MAX_WIDTH + " is maximum");
                }
                if (i > 0 && strings[0].length() != s.length())
                {
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                }
                strings[i] = s;
            }
            return strings;
        }
    }

    static String[] shrink(String... toShrink)
    {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;
        for (int i1 = 0; i1 < toShrink.length; ++i1)
        {
            String s = toShrink[i1];
            i = Math.min(i, firstNonSpace(s));
            int j1 = lastNonSpace(s);
            j = Math.max(j, j1);
            if (j1 < 0)
            {
                if (k == i1)
                {
                    ++k;
                }
                ++l;
            } else
            {
                l = 0;
            }
        }
        if (toShrink.length == l)
        {
            return new String[0];
        } else
        {
            String[] strings = new String[toShrink.length - l - k];
            for (int k1 = 0; k1 < strings.length; ++k1)
            {
                strings[k1] = toShrink[k1 + k].substring(i, j + 1);
            }
            return strings;
        }
    }

    private static int firstNonSpace(String str)
    {
        int i;
        for (i = 0; i < str.length() && str.charAt(i) == ' '; ++i)
        {
        }
        return i;
    }

    private static int lastNonSpace(String str)
    {
        int i;
        for (i = str.length() - 1; i >= 0 && str.charAt(i) == ' '; --i)
        {
        }
        return i;
    }

    private static NonNullList<Ingredient> deserializeIngredients(String[] pattern, Map<String, Ingredient> keys,
            int patternWidth, int patternHeight)
    {
        NonNullList<Ingredient> ingredients = NonNullList.withSize(patternWidth * patternHeight, Ingredient.EMPTY);
        Set<String> strings = Sets.newHashSet(keys.keySet());
        strings.remove(" ");
        for (int i = 0; i < pattern.length; ++i)
        {
            for (int j = 0; j < pattern[i].length(); ++j)
            {
                String s = pattern[i].substring(j, j + 1);
                Ingredient ingredient = keys.get(s);
                if (ingredient == null)
                {
                    throw new JsonSyntaxException(
                            "Pattern references symbol '" + s + "' but it's not defined in the key");
                }
                strings.remove(s);
                ingredients.set(j + patternWidth * i, ingredient);
            }
        }
        if (!strings.isEmpty())
        {
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + strings);
        } else
        {
            return ingredients;
        }
    }

    @Override public int getRecipeWidth()
    {
        return this.recipeWidth;
    }

    @Override public int getRecipeHeight()
    {
        return this.recipeHeight;
    }

    @Override public boolean matches(CraftingInventory inv, World worldIn)
    {
        for (int i = 0; i <= inv.getWidth() - this.recipeWidth; ++i)
        {
            for (int j = 0; j <= inv.getHeight() - this.recipeHeight; ++j)
            {
                if (this.checkMatch(inv, i, j, true))
                {
                    return true;
                }
                if (this.checkMatch(inv, i, j, false))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkMatch(CraftingInventory craftingInventory, int p_77573_2_, int p_77573_3_, boolean p_77573_4_)
    {
        NonNullList<Ingredient> recipeItems = this.getIngredients();
        for (int i = 0; i < craftingInventory.getWidth(); ++i)
        {
            for (int j = 0; j < craftingInventory.getHeight(); ++j)
            {
                int k = i - p_77573_2_;
                int l = j - p_77573_3_;
                Ingredient ingredient = Ingredient.EMPTY;
                if (k >= 0 && l >= 0 && k < this.recipeWidth && l < this.recipeHeight)
                {
                    if (p_77573_4_)
                    {
                        ingredient = recipeItems.get(this.recipeWidth - k - 1 + l * this.recipeWidth);
                    } else
                    {
                        ingredient = recipeItems.get(k + l * this.recipeWidth);
                    }
                }
                if (!ingredient.test(craftingInventory.getStackInSlot(i + j * craftingInventory.getWidth())))
                {
                    return false;
                }
            }
        }
        return true;
    }

    @Override public boolean canFit(int width, int height)
    {
        return width >= this.recipeWidth && height >= this.recipeHeight;
    }

    @Override public IRecipeSerializer<?> getSerializer()
    {
        return Serializer.INSTANCE;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
            implements IRecipeSerializer<ConfigurableShapedRecipe>
    {
        public static final Serializer INSTANCE = new Serializer();

        private Serializer()
        {
            this.setRegistryName(MrpLibrary.ID, "crafting_shaped_configurable");
        }

        @Override public ConfigurableShapedRecipe read(ResourceLocation recipeId, JsonObject json)
        {
            String[] pattern = ConfigurableShapedRecipe
                    .shrink(ConfigurableShapedRecipe.patternFromJson(JSONUtils.getJsonArray(json, "pattern")));
            int patternWidth = pattern[0].length();
            int patternHeight = pattern.length;
            return new ConfigurableShapedRecipe(recipeId, JSONUtils.getString(json, "group", ""),
                    ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result")), ConfigurableShapedRecipe
                    .deserializeIngredients(pattern,
                            ConfigurableShapedRecipe.deserializeKey(JSONUtils.getJsonObject(json, "key")), patternWidth,
                            patternHeight), patternWidth, patternHeight, IngredientOverride.getOverridesFromJson(json));
        }

        @Nullable @Override public ConfigurableShapedRecipe read(ResourceLocation recipeId, PacketBuffer buffer)
        {
            int recipeWidth = buffer.readVarInt();
            int recipeHeight = buffer.readVarInt();
            String group = buffer.readString(32767);
            NonNullList<Ingredient> recipeItems = NonNullList.withSize(recipeWidth * recipeHeight, Ingredient.EMPTY);
            for (int k = 0; k < recipeItems.size(); ++k)
            {
                recipeItems.set(k, Ingredient.read(buffer));
            }
            ItemStack recipeOutput = buffer.readItemStack();
            return new ConfigurableShapedRecipe(recipeId, group, recipeOutput, recipeItems, recipeWidth, recipeHeight);
        }

        @Override public void write(PacketBuffer buffer, ConfigurableShapedRecipe recipe)
        {
            buffer.writeVarInt(recipe.recipeWidth);
            buffer.writeVarInt(recipe.recipeHeight);
            buffer.writeString(recipe.group);
            for (Ingredient ingredient : recipe.getIngredients())
            {
                ingredient.write(buffer);
            }
            buffer.writeItemStack(recipe.recipeOutput);
        }
    }
}

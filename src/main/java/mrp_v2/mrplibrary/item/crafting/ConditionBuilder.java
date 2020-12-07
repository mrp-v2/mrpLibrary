package mrp_v2.mrplibrary.item.crafting;

import com.google.gson.JsonSyntaxException;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class ConditionBuilder
{
    public static Supplier<Boolean> build(String condition)
    {
        try
        {
            ArrayList<String> parts = new ArrayList<>();
            Collections.addAll(parts, splitStringIntoParts(condition));
            StringTree tree = createTree(parts);
            return tree.build();
        } catch (Exception e)
        {
            throw new JsonSyntaxException("Incorrect condition syntax.", e);
        }
    }

    private static String[] splitStringIntoParts(String str)
    {
        for (int i = 0; i < str.length() - 1; i++)
        {
            String character = str.substring(i, i + 1);
            switch (character)
            {
                case "&":
                case "|":
                case "(":
                case ")":
                    str = str.substring(0, i++) + " " + character + " " + str.substring(i++);
            }
        }
        return str.split(" ");
    }

    private static StringTree createTree(List<String> parts)
    {
        ArrayList<StringComponent> list = new ArrayList<>();
        StringTree parent = new StringTree(list);
        for (int index = 0; index < parts.size(); index++)
        {
            String str = parts.get(index);
            switch (str)
            {
                case "(":
                    List<String> subList = parts.subList(++index, parts.size());
                    StringTree subTree = createTree(subList);
                    list.add(subTree);
                    index += subTree.parts.size();
                    break;
                case ")":
                    return parent;
                default:
                    list.add(new StringPart(str));
                    break;
            }
        }
        return parent;
    }

    @FunctionalInterface public interface BooleanSupplier extends Supplier<Boolean>
    {
        default Boolean invertGet()
        {
            return !get();
        }
    }

    private abstract static class StringComponent
    {
        abstract Supplier<Boolean> build();

        @Nullable StringPart getAsPart()
        {
            return null;
        }
    }

    private static class StringTree extends StringComponent
    {
        private final ArrayList<StringComponent> parts;

        private StringTree(ArrayList<StringComponent> parts)
        {
            this.parts = parts;
        }

        @Override Supplier<Boolean> build()
        {
            for (int i = parts.size() - 2; i > 0; i--)
            {
                StringComponent stringComponent = parts.get(i);
                StringPart part = stringComponent.getAsPart();
                if (part != null && part.str.equals("&"))
                {
                    AndSegment andSegment = new AndSegment(parts.remove(i - 1), parts.remove(i));
                    parts.remove(i - 1);
                    parts.add(i - 1, andSegment);
                    i--;
                }
            }
            for (int i = parts.size() - 2; i > 0; i--)
            {
                StringComponent stringComponent = parts.get(i);
                StringPart part = stringComponent.getAsPart();
                if (part != null && part.str.equals("|"))
                {
                    OrSegment orSegment = new OrSegment(parts.remove(i - 1), parts.remove(i));
                    parts.remove(i - 1);
                    parts.add(i - 1, orSegment);
                    i--;
                }
            }
            if (parts.size() != 1)
            {
                throw new JsonSyntaxException(
                        "There should only be 1 part left here! The condition must have syntax errors.");
            }
            return parts.get(0).build();
        }
    }

    private static class StringPart extends StringComponent
    {
        private final String str;

        private StringPart(String str)
        {
            this.str = str;
        }

        @Override Supplier<Boolean> build()
        {
            boolean invert = str.startsWith("!");
            String condition = invert ? str.substring(1) : str;
            if (!ConfigurableCraftingRecipe.conditionMap.containsKey(condition))
            {
                throw new JsonSyntaxException("Condition '" + condition + "' has no mapping!");
            }
            return invert ? ConfigurableCraftingRecipe.conditionMap.get(condition)::invertGet :
                    ConfigurableCraftingRecipe.conditionMap.get(condition);
        }

        @Override StringPart getAsPart()
        {
            return this;
        }
    }

    private static class AndSegment extends Operator
    {
        private AndSegment(StringComponent left, StringComponent right)
        {
            super(left, right);
        }

        @Override Supplier<Boolean> build()
        {
            return () -> super.left.build().get() && super.right.build().get();
        }
    }

    private static class OrSegment extends Operator
    {
        private OrSegment(StringComponent left, StringComponent right)
        {
            super(left, right);
        }

        @Override Supplier<Boolean> build()
        {
            return () -> super.left.build().get() || super.right.build().get();
        }
    }

    private abstract static class Operator extends StringComponent
    {
        private final StringComponent left;
        private final StringComponent right;

        private Operator(StringComponent left, StringComponent right)
        {
            this.left = left;
            this.right = right;
        }

        @Override abstract Supplier<Boolean> build();
    }
}

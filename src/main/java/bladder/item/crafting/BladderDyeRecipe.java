package bladder.item.crafting;

import bladder.Bladder;
import bladder.item.AbstractBladderItem;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BladderDyeRecipe extends SpecialRecipe
{

    public static final SpecialRecipeSerializer<BladderDyeRecipe> SERIALIZER = new Serializer();

    public BladderDyeRecipe(ResourceLocation id)
    {
        super(id);
    }

    @Nullable
    private Pair<ItemStack, List<DyeItem>> getBucketAndDyes(@Nonnull CraftingInventory inv)
    {
        ItemStack bucket = ItemStack.EMPTY;
        List<DyeItem> dyeItems = Lists.newArrayList();
        for (int i = 0; i < inv.getSizeInventory(); ++i)
        {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty())
            {
                Item item = stack.getItem();
                if (item instanceof AbstractBladderItem)
                {
                    if (!bucket.isEmpty())
                    {
                        return null;
                    }
                    bucket = stack;
                }
                else
                {
                    if (!(item instanceof DyeItem))
                    {
                        return null;
                    }
                    dyeItems.add((DyeItem) item);
                }
            }
        }
        if (bucket.isEmpty() || dyeItems.isEmpty())
        {
            return null;
        }
        return new Pair<>(bucket, dyeItems);
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(@Nonnull CraftingInventory inv, @Nonnull World worldIn)
    {
        Pair<ItemStack, List<DyeItem>> bucketAndDyes = getBucketAndDyes(inv);
        return bucketAndDyes != null;
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    @Nonnull
    public ItemStack getCraftingResult(@Nonnull CraftingInventory inv)
    {
        Pair<ItemStack, List<DyeItem>> bucketAndDyes = getBucketAndDyes(inv);
        if (bucketAndDyes != null)
        {
            return AbstractBladderItem.dyeItem(bucketAndDyes.getFirst(), bucketAndDyes.getSecond());
        }
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv)
    {
        //override it to avoid remaining container items
        return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    public boolean canFit(int width, int height)
    {
        return width * height >= 2;
    }

    @Nonnull
    public IRecipeSerializer<?> getSerializer()
    {
        return SERIALIZER;
    }

    private static class Serializer extends SpecialRecipeSerializer<BladderDyeRecipe>
    {
        public Serializer()
        {
            super(BladderDyeRecipe::new);
            this.setRegistryName(Bladder.MOD_ID, "empty_bladder_dye_recipe");
        }
    }

}

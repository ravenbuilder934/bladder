package bladder.item;

import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nonnull;

public class BladderItem extends AbstractBladderItem
{

    public BladderItem(Item.Properties builder)
    {
        super(Fluids.EMPTY.delegate, builder);
    }

    @Nonnull
    @Override
    FluidHandlerItemStack getNewFluidHandlerInstance(@Nonnull ItemStack stack)
    {
        return new BladderFluidHandler(stack);
    }

}

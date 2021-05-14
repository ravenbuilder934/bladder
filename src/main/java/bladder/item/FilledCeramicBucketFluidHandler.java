package bladder.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nonnull;

public class FilledCeramicBucketFluidHandler extends FluidHandlerItemStack
{

    public FilledCeramicBucketFluidHandler(@Nonnull ItemStack container)
    {
        super(container, FluidAttributes.BUCKET_VOLUME);
    }

    @Override
    protected void setContainerToEmpty()
    {
        this.container = this.container.getContainerItem();
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action)
    {
        //only drain the bucket if there is enough space to drain the bucket completely
        if (maxDrain < FluidAttributes.BUCKET_VOLUME)
        {
            return FluidStack.EMPTY;
        }
        return super.drain(maxDrain, action);
    }
}

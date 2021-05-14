package bladder.item;

import bladder.util.BladderUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nonnull;

public class BladderFluidHandler extends FluidHandlerItemStack
{

    public BladderFluidHandler(@Nonnull ItemStack container)
    {
        super(container, FluidAttributes.BUCKET_VOLUME);
    }

    @Override
    protected void setFluid(FluidStack fluid)
    {
        this.container = BladderUtils.getFilledBladder(fluid.getFluid(), this.container);
    }

    @Override
    public int fill(FluidStack resource, FluidAction doFill)
    {
        //only fill the bucket, if there is enough fluid to fill the bucket completely
        if (resource.getAmount() < FluidAttributes.BUCKET_VOLUME)
        {
            return 0;
        }
        return super.fill(resource, doFill);
    }
}

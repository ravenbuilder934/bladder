package bladder.util;

import bladder.api.item.CeramicBucketItems;
import bladder.item.FilledCeramicBucketItem;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nonnull;

public class CeramicBucketUtils
{

    public static ItemStack getFilledCeramicBucket(Fluid fluid, ItemStack emptyBucket)
    {
        return ((FilledCeramicBucketItem) CeramicBucketItems.FILLED_CERAMIC_BUCKET).getFilledInstance(fluid, emptyBucket);
    }

    /**
     * Get the burn time of the bucket item of the given fluid.
     *
     * @param fluid fluid that should be checked.
     * @return burn time of the bucket item of the given fluid; -1 for Fluids.EMPTY
     */
    public static int getBurnTimeOfFluid(@Nonnull Fluid fluid)
    {
        if (fluid != Fluids.EMPTY)
        {
            //all fluids have their burn time in their bucket item.
            //get the burn time via ForgeHooks.getBurnTime to let other mods change burn times of buckets of vanilla and other fluids.
            return ForgeHooks.getBurnTime(new ItemStack(fluid.getFilledBucket()));
        }
        return -1;
    }

}

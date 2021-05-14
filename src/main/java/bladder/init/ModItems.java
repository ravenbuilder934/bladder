package bladder.init;

import bladder.api.item.CeramicBucketItems;
import bladder.item.CeramicBucketItem;
import bladder.item.CeramicMilkBucketItem;
import bladder.item.FilledCeramicBucketItem;
import bladder.util.CeramicBucketUtils;
import net.minecraft.block.*;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.DispenserTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

import static bladder.Bladder.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems
{

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        CeramicBucketItems.CERAMIC_BUCKET = registerItem("ceramic_bucket", new CeramicBucketItem((new Item.Properties()).maxStackSize(16).group(ItemGroup.MISC)));
        CeramicBucketItems.FILLED_CERAMIC_BUCKET = registerItem("filled_ceramic_bucket", new FilledCeramicBucketItem((new Item.Properties()).maxStackSize(1).group(ItemGroup.MISC)));
        CeramicBucketItems.CERAMIC_MILK_BUCKET = registerItem("ceramic_milk_bucket", new CeramicMilkBucketItem((new Item.Properties()).maxStackSize(1).group(ItemGroup.MISC)));

        //dispense behaviour empty bucket
        DispenserBlock.registerDispenseBehavior(CeramicBucketItems.CERAMIC_BUCKET, new DefaultDispenseItemBehavior()
        {
            private final DefaultDispenseItemBehavior dispenseBehavior = new DefaultDispenseItemBehavior();

            /**
             * Dispense the specified stack, play the dispense sound and spawn particles.
             */
            @Nonnull
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
            {
                IWorld iworld = source.getWorld();
                BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().get(DispenserBlock.FACING));
                BlockState blockstate = iworld.getBlockState(blockpos);
                Block block = blockstate.getBlock();
                if (block instanceof IBucketPickupHandler)
                {
                    Fluid fluid = ((IBucketPickupHandler) block).pickupFluid(iworld, blockpos, blockstate);
                    if (!(fluid instanceof FlowingFluid))
                    {
                        return super.dispenseStack(source, stack);
                    }
                    else
                    {
                        ItemStack bucket = CeramicBucketUtils.getFilledCeramicBucket(fluid, stack);
                        stack.shrink(1);
                        if (stack.isEmpty())
                        {
                            return bucket;
                        }
                        else
                        {
                            if (source.<DispenserTileEntity>getBlockTileEntity().addItemStack(bucket) < 0)
                            {
                                this.dispenseBehavior.dispense(source, bucket);
                            }

                            return stack;
                        }
                    }
                }
                else
                {
                    return super.dispenseStack(source, stack);
                }
            }
        });

        //dispense behaviour filled buckets
        IDispenseItemBehavior idispenseitembehavior = new DefaultDispenseItemBehavior()
        {
            private final DefaultDispenseItemBehavior dispenseBehaviour = new DefaultDispenseItemBehavior();

            /**
             * Dispense the specified stack, play the dispense sound and spawn particles.
             */
            @Nonnull
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
            {
                FilledCeramicBucketItem bucketItem = (FilledCeramicBucketItem) stack.getItem();
                BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().get(DispenserBlock.FACING));
                World world = source.getWorld();
                if (bucketItem.tryPlaceContainedLiquid(null, world, blockpos, null, stack))
                {
                    bucketItem.onLiquidPlaced(world, stack, blockpos);
                    return bucketItem.emptyBucket(stack, null);
                }
                else
                {
                    return this.dispenseBehaviour.dispense(source, stack);
                }
            }
        };
        DispenserBlock.registerDispenseBehavior(CeramicBucketItems.FILLED_CERAMIC_BUCKET, idispenseitembehavior);
        DispenserBlock.registerDispenseBehavior(CeramicBucketItems.CERAMIC_MILK_BUCKET, idispenseitembehavior);
    }

    private static Item registerItem(String name, Item item)
    {
        item.setRegistryName(name);
        ForgeRegistries.ITEMS.register(item);
        return item;
    }

}

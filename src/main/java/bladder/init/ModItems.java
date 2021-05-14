package bladder.init;

import bladder.api.item.BladderItems;
import bladder.item.BladderItem;
import bladder.item.FilledBladderItem;
import bladder.util.BladderUtils;
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
        BladderItems.EMPTY_BLADDER = registerItem("empty_bladder", new BladderItem((new Item.Properties()).maxStackSize(16).group(ItemGroup.MISC)));
        BladderItems.FULL_BLADDER = registerItem("full_bladder", new FilledBladderItem((new Item.Properties()).maxStackSize(1).group(ItemGroup.MISC)));

        //dispense behaviour empty bucket
        DispenserBlock.registerDispenseBehavior(BladderItems.EMPTY_BLADDER, new DefaultDispenseItemBehavior()
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
                        ItemStack bucket = BladderUtils.getFilledBladder(fluid, stack);
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
                FilledBladderItem bucketItem = (FilledBladderItem) stack.getItem();
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
        DispenserBlock.registerDispenseBehavior(BladderItems.FULL_BLADDER, idispenseitembehavior);
    }

    private static Item registerItem(String name, Item item)
    {
        item.setRegistryName(name);
        ForgeRegistries.ITEMS.register(item);
        return item;
    }

}

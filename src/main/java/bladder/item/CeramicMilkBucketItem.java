package bladder.item;

import bladder.util.CeramicBucketUtils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CeramicMilkBucketItem extends FilledCeramicBucketItem
{

    public CeramicMilkBucketItem(Properties builder)
    {
        super(builder);
    }

    @Override
    public ItemStack getFilledInstance(@Nonnull Fluid fluid, @Nullable ItemStack oldStack)
    {
        //can only be filled with milk
        if (CeramicBucketUtils.isMilkFluid(fluid))
        {
            return super.getFilledInstance(fluid, oldStack);
        }
        return super.getFilledInstance(Fluids.EMPTY, oldStack);
    }

    /**
     * Get a milk bucket with the first fluid that is milk. If there is no milk fluid, the bucket gets empty fluid.
     */
    public ItemStack getFilledInstance(boolean checkTag, @Nullable ItemStack oldStack)
    {
        for (Fluid fluid : ForgeRegistries.FLUIDS)
        {
            //search first milk fluid
            if (fluid.getDefaultState().isSource() && CeramicBucketUtils.isMilkFluid(fluid, checkTag))
            {
                return super.getFilledInstance(fluid, oldStack);
            }
        }
        return super.getFilledInstance(Fluids.EMPTY, oldStack);
    }

    /**
     * Get a milk bucket with the first fluid that is milk. If there is no milk fluid, the bucket gets empty fluid.
     */
    @Nonnull
    public ItemStack getFilledInstance(@Nullable ItemStack oldStack)
    {
        return this.getFilledInstance(true, oldStack);
    }

    @Nonnull
    @Override
    public ItemStack getDefaultInstance()
    {
        return this.getFilledInstance(null);
    }

    /**
     * Like vanilla milk bucket.
     */
    @Override
    @Nonnull
    public ItemStack onItemUseFinish(@Nonnull ItemStack stack, World worldIn, @Nonnull LivingEntity entityLiving)
    {
        ItemStack vanillaStack = new ItemStack(Items.MILK_BUCKET);
        if (!worldIn.isRemote)
            entityLiving.curePotionEffects(vanillaStack); // FORGE - move up so stack.shrink does not turn stack into air

        if (entityLiving instanceof ServerPlayerEntity)
        {
            ServerPlayerEntity serverplayerentity = (ServerPlayerEntity) entityLiving;
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayerentity, vanillaStack);
            serverplayerentity.addStat(Stats.ITEM_USED.get(this));
        }

        if (entityLiving instanceof PlayerEntity && !((PlayerEntity) entityLiving).abilities.isCreativeMode)
        {
            stack.shrink(1);
        }

        return stack.isEmpty() ? this.getContainerItem(stack) : stack;
    }

    /**
     * Like vanilla milk bucket.
     */
    @Override
    public int getUseDuration(@Nonnull ItemStack stack)
    {
        return 32;
    }

    /**
     * Like vanilla milk bucket.
     */
    @Override
    @Nonnull
    public UseAction getUseAction(@Nonnull ItemStack stack)
    {
        return UseAction.DRINK;
    }

    /**
     * Place milk or on failure drink it.
     */
    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn)
    {
        ActionResult<ItemStack> result = super.onItemRightClick(worldIn, playerIn, handIn);
        //when no fluid can be placed, drink it
        if (result.getType() != ActionResultType.SUCCESS)
        {
            playerIn.setActiveHand(handIn);
            result = new ActionResult<>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
        }
        return result;
    }

    @Override
    public void fillItemGroup(@Nonnull ItemGroup group, @Nonnull NonNullList<ItemStack> items)
    {
        if (this.isInGroup(group))
        {
            items.add(this.getFilledInstance(false, null));
        }
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName(@Nonnull ItemStack stack)
    {
        return new TranslationTextComponent("item.bladder.ceramic_milk_bucket");
    }

    @Override
    public boolean hasContainerItem(ItemStack stack)
    {
        //super method checks if a fluid is inside. Milk does not have to be a fluid.
        Fluid fluid = this.getFluid(stack);
        return fluid == Fluids.EMPTY;
    }

}

package bladder;

import bladder.api.crafting.FilledCeramicBucketIngredient;
import bladder.api.crafting.FluidIngredient;
import bladder.api.item.CeramicBucketItems;
import bladder.compat.ModCompat;
import bladder.item.AbstractCeramicBucketItem;
import bladder.item.CeramicMilkBucketItem;
import bladder.item.crafting.CeramicBucketDyeRecipe;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static bladder.Bladder.MOD_ID;

@Mod(MOD_ID)
@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Bladder
{

    public static final String MOD_ID = "bladder";

    public Bladder()
    {
        MinecraftForge.EVENT_BUS.addListener(Bladder::onEntityInteract);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void registerItemColors(ColorHandlerEvent.Item event)
    {
        event.getItemColors().register(
                (stack, color) -> (color > 0) ? -1 : ((AbstractCeramicBucketItem) stack.getItem()).getColor(stack),
                CeramicBucketItems.CERAMIC_BUCKET,
                CeramicBucketItems.FILLED_CERAMIC_BUCKET,
                CeramicBucketItems.CERAMIC_MILK_BUCKET);
    }

    @SubscribeEvent
    public static void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event)
    {
        //dye recipe serializer
        event.getRegistry().register(CeramicBucketDyeRecipe.SERIALIZER);
        //ingredient serializers
        CraftingHelper.register(FluidIngredient.Serializer.NAME, FluidIngredient.Serializer.INSTANCE);
        CraftingHelper.register(FilledCeramicBucketIngredient.Serializer.NAME, FilledCeramicBucketIngredient.Serializer.INSTANCE);
    }

    /**
     * Add milking and obtaining interaction.
     */
    private static void onEntityInteract(PlayerInteractEvent.EntityInteract event)
    {
        Entity entity = event.getTarget();
        if (entity == null) return;

        PlayerEntity player = event.getPlayer();
        ItemStack itemstack = player.getHeldItem(event.getHand());

        if (ModCompat.canEntityBeMilked(entity))
        {
            if (itemstack.getItem() == CeramicBucketItems.CERAMIC_BUCKET && !player.abilities.isCreativeMode)
            {
                player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
                if (!event.getWorld().isRemote())
                {
                    ItemStack milkBucket = ((CeramicMilkBucketItem) CeramicBucketItems.CERAMIC_MILK_BUCKET).getFilledInstance(itemstack);
                    itemstack.shrink(1);
                    if (itemstack.isEmpty())
                    {
                        player.setHeldItem(event.getHand(), milkBucket);
                    }
                    else if (!player.inventory.addItemStackToInventory(milkBucket))
                    {
                        player.dropItem(milkBucket, false);
                    }
                }
                event.setCanceled(true);
                event.setCancellationResult(ActionResultType.SUCCESS);
            }
        }
    }

}

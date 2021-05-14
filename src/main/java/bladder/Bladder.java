package bladder;

import bladder.api.crafting.FilledBladderIngredient;
import bladder.api.crafting.FluidIngredient;
import bladder.api.item.BladderItems;
import bladder.item.AbstractBladderItem;
import bladder.item.crafting.BladderDyeRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
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

    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void registerItemColors(ColorHandlerEvent.Item event)
    {
        event.getItemColors().register(
                (stack, color) -> (color > 0) ? -1 : ((AbstractBladderItem) stack.getItem()).getColor(stack),
                BladderItems.EMPTY_BLADDER,
                BladderItems.FULL_BLADDER);
    }

    @SubscribeEvent
    public static void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event)
    {
        //dye recipe serializer
        event.getRegistry().register(BladderDyeRecipe.SERIALIZER);
        //ingredient serializers
        CraftingHelper.register(FluidIngredient.Serializer.NAME, FluidIngredient.Serializer.INSTANCE);
        CraftingHelper.register(FilledBladderIngredient.Serializer.NAME, FilledBladderIngredient.Serializer.INSTANCE);
    }

}

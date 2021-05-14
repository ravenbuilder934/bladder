package bladder;

import bladder.api.crafting.FilledBladderIngredient;
import bladder.api.crafting.FluidIngredient;
import net.minecraft.item.crafting.IRecipeSerializer;
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
    public static void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event)
    {
        //ingredient serializers
        CraftingHelper.register(FluidIngredient.Serializer.NAME, FluidIngredient.Serializer.INSTANCE);
        CraftingHelper.register(FilledBladderIngredient.Serializer.NAME, FilledBladderIngredient.Serializer.INSTANCE);
    }

}

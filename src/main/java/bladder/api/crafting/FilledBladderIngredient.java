package bladder.api.crafting;

import bladder.Bladder;
import bladder.api.item.BladderItems;
import bladder.item.FilledBladderItem;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IIngredientSerializer;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * A Bladder filled with a fluid can be used in recipes with this ingredient class.
 * Usage is the same as for a vanilla ingredient.
 * Example JSON object: { "type": "bladder:full_bladder", "tag": "minecraft:lava" }
 * The tag is a fluid tag of the fluid that the Bladder should contain.
 */
public class FilledBladderIngredient extends Ingredient
{

    protected final ITag.INamedTag<Fluid> fluidTag;
    private ItemStack[] matchingStacks;

    public FilledBladderIngredient(ITag.INamedTag<Fluid> fluidTag)
    {
        super(Stream.of());
        this.fluidTag = fluidTag;
    }

    public FilledBladderIngredient(ResourceLocation resourceLocation)
    {
        this(FluidTags.bind(resourceLocation.toString()));
    }

    @Override
    @Nonnull
    public ItemStack[] getItems()
    {
        if (this.matchingStacks == null)
        {
            FilledBladderItem bucketItem = (FilledBladderItem) BladderItems.FULL_BLADDER;
            this.matchingStacks = this.fluidTag.getValues().stream()
                    .map(fluid -> bucketItem.getFilledInstance(fluid, null))
                    .filter(this)
                    .toArray(ItemStack[]::new);
        }
        return this.matchingStacks;
    }

    @Override
    public boolean isEmpty()
    {
        return false;
    }

    @Override
    public boolean isSimple()
    {
        return false;
    }

    @Override
    protected void invalidate()
    {
        this.matchingStacks = null;
    }

    @Override
    @Nonnull
    public IIngredientSerializer<? extends Ingredient> getSerializer()
    {
        return Serializer.INSTANCE;
    }

    @Nonnull
    public JsonElement toJson()
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", Serializer.NAME.toString());
        jsonObject.addProperty("tag", this.fluidTag.getName().toString());
        return jsonObject;
    }

    public static final class Serializer implements IIngredientSerializer<FilledBladderIngredient>
    {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation NAME = new ResourceLocation(Bladder.MOD_ID, "full_bladder");

        private Serializer()
        {
        }

        @Override
        public FilledBladderIngredient parse(PacketBuffer buffer)
        {
            ResourceLocation tag = buffer.readResourceLocation();
            return new FilledBladderIngredient(tag);
        }

        @Override
        public FilledBladderIngredient parse(@Nonnull JsonObject json)
        {
            String tag = JSONUtils.getAsString(json, "tag");
            return new FilledBladderIngredient(FluidTags.bind(tag));
        }

        @Override
        public void write(PacketBuffer buffer, FilledBladderIngredient ingredient)
        {
            buffer.writeUtf(ingredient.fluidTag.getName().toString());
        }
    }
}

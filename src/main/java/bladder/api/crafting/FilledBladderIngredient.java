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
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

/**
 * A Ceramic Bucket filled with a fluid can be used in recipes with this ingredient class.
 * Usage is the same as for a vanilla ingredient.
 * Example JSON object: { "type": "bladder:full_bladder", "tag": "minecraft:lava" }
 * The tag is a fluid tag of the fluid that the Ceramic Bucket should contain.
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
        this(FluidTags.makeWrapperTag(resourceLocation.toString()));
    }

    @Override
    public boolean test(ItemStack itemStack)
    {
        AtomicBoolean result = new AtomicBoolean(false);
        if (itemStack != null && itemStack.getItem() == BladderItems.FULL_BLADDER)
        {
            FluidUtil.getFluidContained(itemStack).ifPresent(fluidStack -> result.set(fluidStack.getFluid().isIn(fluidTag)));
        }
        return result.get();
    }

    @Override
    @Nonnull
    public ItemStack[] getMatchingStacks()
    {
        if (this.matchingStacks == null)
        {
            FilledBladderItem bucketItem = (FilledBladderItem) BladderItems.FULL_BLADDER;
            this.matchingStacks = this.fluidTag.getAllElements().stream()
                    .map(fluid -> bucketItem.getFilledInstance(fluid, null))
                    .filter(this)
                    .toArray(ItemStack[]::new);
        }
        return this.matchingStacks;
    }

    @Override
    public boolean hasNoMatchingItems()
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
    public JsonElement serialize()
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
            String tag = JSONUtils.getString(json, "tag");
            return new FilledBladderIngredient(FluidTags.makeWrapperTag(tag));
        }

        @Override
        public void write(PacketBuffer buffer, FilledBladderIngredient ingredient)
        {
            buffer.writeString(ingredient.fluidTag.getName().toString());
        }
    }
}

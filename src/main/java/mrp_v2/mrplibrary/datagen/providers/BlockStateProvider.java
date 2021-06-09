package mrp_v2.mrplibrary.datagen.providers;

import mrp_v2.mrplibrary.datagen.TintedBlockStateGenerator;
import mrp_v2.mrplibrary.util.IModLocProvider;
import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

public abstract class BlockStateProvider extends net.minecraftforge.client.model.generators.BlockStateProvider
        implements IModLocProvider
{
    public static final String ALL = "all";
    public static final String SIDE = "side";
    public static final String END = "end";
    public static final String FRONT = "front";
    public static final String TOP = "top";
    public static final String BOTTOM = "bottom";
    private final String modId;

    public BlockStateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper)
    {
        super(gen, modid, exFileHelper);
        this.modId = modid;
    }

    @Override public String getModId()
    {
        return modId;
    }

    protected <T extends ModelBuilder<T>> ModelBuilder<T> forEachElement(ModelBuilder<T> builder,
            Consumer<ModelBuilder<T>.ElementBuilder> elementConsumer)
    {
        for (int i = 0; i < builder.getElementCount(); i++)
        {
            elementConsumer.accept(builder.element(i));
        }
        return builder;
    }

    public void promiseGeneration(ResourceLocation model)
    {
        models().existingFileHelper.trackGenerated(model, ResourcePackType.CLIENT_RESOURCES, ".json", "models");
    }

    public void simpleBlockTinted(Block block)
    {
        simpleBlock(block, cubeAllTinted(block));
    }

    public ModelFile cubeAllTinted(Block block)
    {
        return models().withExistingParent(name(block), TintedBlockStateGenerator.CUBE_ALL_TINTED)
                .texture(ALL, blockTexture(block));
    }

    protected String name(Block block)
    {
        return block.getRegistryName().getPath();
    }

    public ModelFile cubeAllTinted(String name, ResourceLocation texture)
    {
        return models().withExistingParent(name, TintedBlockStateGenerator.CUBE_ALL_TINTED).texture(ALL, texture);
    }

    public void axisBlockTinted(RotatedPillarBlock block)
    {
        axisBlockTinted(block, blockTexture(block));
    }

    public void axisBlockTinted(RotatedPillarBlock block, ResourceLocation baseName)
    {
        axisBlockTinted(block, extend(baseName, "_side"), extend(baseName, "_end"));
    }

    protected ResourceLocation extend(ResourceLocation rl, String suffix)
    {
        return new ResourceLocation(rl.getNamespace(), rl.getPath() + suffix);
    }

    public void axisBlockTinted(RotatedPillarBlock block, ResourceLocation side, ResourceLocation end)
    {
        AxisBlockModels models = axisBlockTinted(name(block), side, end);
        axisBlock(block, models.getCubeColumn(), models.getCubeColumnHorizontal());
    }

    public AxisBlockModels axisBlockTinted(String name, ResourceLocation side, ResourceLocation end)
    {
        return new AxisBlockModels(
                models().withExistingParent(name, TintedBlockStateGenerator.CUBE_COLUMN_TINTED).texture(SIDE, side)
                        .texture(END, end), models().withExistingParent(name + "_horizontal",
                TintedBlockStateGenerator.CUBE_COLUMN_HORIZONTAL_TINTED).texture(SIDE, side).texture(END, end));
    }

    public void logBlockTinted(RotatedPillarBlock block)
    {
        axisBlockTinted(block, blockTexture(block), extend(blockTexture(block), "_top"));
    }

    public void horizontalBlockTinted(Block block, ResourceLocation side, ResourceLocation front, ResourceLocation top)
    {
        horizontalBlock(block, horizontalBlockTinted(name(block), side, front, top));
    }

    public ModelFile horizontalBlockTinted(String name, ResourceLocation side, ResourceLocation front,
            ResourceLocation top)
    {
        return models().withExistingParent(name, TintedBlockStateGenerator.ORIENTABLE_TINTED).texture(SIDE, side)
                .texture(FRONT, front).texture(TOP, top);
    }

    public void stairsBlockTinted(StairsBlock block, ResourceLocation texture)
    {
        stairsBlockTinted(block, texture, texture, texture);
    }

    public void stairsBlockTinted(StairsBlock block, ResourceLocation side, ResourceLocation bottom,
            ResourceLocation top)
    {
        stairsBlockInternalTinted(block, block.getRegistryName().toString(), side, bottom, top);
    }

    protected void stairsBlockInternalTinted(StairsBlock block, String baseName, ResourceLocation side,
            ResourceLocation bottom, ResourceLocation top)
    {
        StairsBlockModels models = stairsBlockTinted(baseName, side, bottom, top);
        stairsBlock(block, models.getStairs(), models.getStairsInner(), models.getStairsOuter());
    }

    public StairsBlockModels stairsBlockTinted(String name, ResourceLocation side, ResourceLocation bottom,
            ResourceLocation top)
    {
        return new StairsBlockModels(sideBottomTop(name, TintedBlockStateGenerator.STAIRS_TINTED, side, bottom, top),
                sideBottomTop(name + "_inner", TintedBlockStateGenerator.STAIRS_INNER_TINTED, side, bottom, top),
                sideBottomTop(name + "_outer", TintedBlockStateGenerator.STAIRS_OUTER_TINTED, side, bottom, top));
    }

    protected ModelFile sideBottomTop(String name, ResourceLocation parent, ResourceLocation side,
            ResourceLocation bottom, ResourceLocation top)
    {
        return models().withExistingParent(name, parent).texture(SIDE, side).texture(BOTTOM, bottom).texture(TOP, top);
    }

    public void stairsBlockTinted(StairsBlock block, String name, ResourceLocation texture)
    {
        stairsBlockTinted(block, name, texture, texture, texture);
    }

    public void stairsBlockTinted(StairsBlock block, String name, ResourceLocation side, ResourceLocation bottom,
            ResourceLocation top)
    {
        stairsBlockInternalTinted(block, name + "_stairs", side, bottom, top);
    }

    public void slabBlockTinted(SlabBlock block, ResourceLocation doubleslab, ResourceLocation texture)
    {
        slabBlockTinted(block, doubleslab, texture, texture, texture);
    }

    public void slabBlockTinted(SlabBlock block, ResourceLocation doubleslab, ResourceLocation side,
            ResourceLocation bottom, ResourceLocation top)
    {
        SlabBlockModels models = slabBlockTinted(name(block), doubleslab, side, bottom, top);
        slabBlock(block, models.getSlab(), models.getSlabTop(), models.getDoubleSlab());
    }

    public SlabBlockModels slabBlockTinted(String name, ResourceLocation doubleslab, ResourceLocation side,
            ResourceLocation bottom, ResourceLocation top)
    {
        return new SlabBlockModels(sideBottomTop(name, TintedBlockStateGenerator.SLAB_TINTED, side, bottom, top),
                sideBottomTop(name + "_top", TintedBlockStateGenerator.SLAB_TOP_TINTED, side, bottom, top),
                models().getExistingFile(doubleslab));
    }

    public void fenceBlockTinted(FenceBlock block, ResourceLocation texture)
    {
        FenceBlockModels models = fenceBlockTinted(block.getRegistryName().toString(), texture);
        fourWayBlock(block, models.getFencePost(), models.getFenceSide());
    }

    public FenceBlockModels fenceBlockTinted(String name, ResourceLocation texture)
    {
        return new FenceBlockModels(
                models().singleTexture(name + "_post", TintedBlockStateGenerator.FENCE_POST_TINTED, texture),
                models().singleTexture(name + "_side", TintedBlockStateGenerator.FENCE_SIDE_TINTED, texture));
    }

    public void fenceBlockTinted(FenceBlock block, String name, ResourceLocation texture)
    {
        fourWayBlock(block,
                models().singleTexture(name + "_fence_post", TintedBlockStateGenerator.FENCE_POST_TINTED, texture),
                models().singleTexture(name + "_fence_side", TintedBlockStateGenerator.FENCE_SIDE_TINTED, texture));
    }

    public void fenceGateBlockTinted(FenceGateBlock block, ResourceLocation texture)
    {
        fenceGateBlockInternalTinted(block, block.getRegistryName().toString(), texture);
    }

    protected void fenceGateBlockInternalTinted(FenceGateBlock block, String baseName, ResourceLocation texture)
    {
        FenceGateBlockModels models = fenceGateBlockTinted(baseName, texture);
        fenceGateBlock(block, models.getGate(), models.getGateOpen(), models.getGateWall(), models.getGateWallOpen());
    }

    public FenceGateBlockModels fenceGateBlockTinted(String name, ResourceLocation texture)
    {
        return new FenceGateBlockModels(
                models().singleTexture(name, TintedBlockStateGenerator.FENCE_GATE_TINTED, texture),
                models().singleTexture(name + "_open", TintedBlockStateGenerator.FENCE_GATE_OPEN_TINTED, texture),
                models().singleTexture(name + "_wall", TintedBlockStateGenerator.FENCE_GATE_WALL_TINTED, texture),
                models().singleTexture(name + "_wall_open", TintedBlockStateGenerator.FENCE_GATE_WALL_OPEN_TINTED,
                        texture));
    }

    public void fenceGateBlockTinted(FenceGateBlock block, String name, ResourceLocation texture)
    {
        fenceGateBlockInternalTinted(block, name + "_fence_gate", texture);
    }

    public void wallBlockTinted(WallBlock block, ResourceLocation texture)
    {
        wallBlockInternalTinted(block, block.getRegistryName().toString(), texture);
    }

    protected void wallBlockInternalTinted(WallBlock block, String baseName, ResourceLocation texture)
    {
        WallBlockModels models = wallBlockTinted(baseName, texture);
        wallBlock(block, models.getPost(), models.getSide(), models.getSideTall());
    }

    public WallBlockModels wallBlockTinted(String baseName, ResourceLocation texture)
    {
        return new WallBlockModels(
                models().singleTexture(baseName + "_post", TintedBlockStateGenerator.WALL_POST_TINTED, "wall", texture),
                models().singleTexture(baseName + "_side", TintedBlockStateGenerator.WALL_SIDE_TINTED, "wall", texture),
                models().singleTexture(baseName + "_side_tall", TintedBlockStateGenerator.WALL_SIDE_TALL_TINTED, "wall",
                        texture));
    }

    public void wallBlockTinted(WallBlock block, String name, ResourceLocation texture)
    {
        wallBlockInternalTinted(block, name + "_wall", texture);
    }

    public TrapDoorBlockModels trapDoorBlockTinted(String baseName, ResourceLocation texture)
    {
        return new TrapDoorBlockModels(
                models().singleTexture(baseName + "_bottom", TintedBlockStateGenerator.TEMPLATE_TRAPDOOR_BOTTOM_TINTED,
                        "texture", texture),
                models().singleTexture(baseName + "_top", TintedBlockStateGenerator.TEMPLATE_TRAPDOOR_TOP_TINTED,
                        "texture", texture),
                models().singleTexture(baseName + "_open", TintedBlockStateGenerator.TEMPLATE_TRAPDOOR_OPEN_TINTED,
                        "texture", texture));
    }

    public TrapDoorBlockModels trapDoorBlockOrientableTinted(String baseName, ResourceLocation texture)
    {
        return new TrapDoorBlockModels(models().singleTexture(baseName + "_bottom",
                TintedBlockStateGenerator.TEMPLATE_ORIENTABLE_TRAPDOOR_BOTTOM_TINTED, "texture", texture),
                models().singleTexture(baseName + "_top",
                        TintedBlockStateGenerator.TEMPLATE_ORIENTABLE_TRAPDOOR_TOP_TINTED, "texture", texture),
                models().singleTexture(baseName + "_open",
                        TintedBlockStateGenerator.TEMPLATE_ORIENTABLE_TRAPDOOR_OPEN_TINTED, "texture", texture));
    }

    public static class StairsBlockModels
    {
        private final ModelFile stairs, stairsInner, stairsOuter;

        public StairsBlockModels(ModelFile stairs, ModelFile stairsInner, ModelFile stairsOuter)
        {
            this.stairs = stairs;
            this.stairsInner = stairsInner;
            this.stairsOuter = stairsOuter;
        }

        public ModelFile getStairs()
        {
            return stairs;
        }

        public ModelFile getStairsInner()
        {
            return stairsInner;
        }

        public ModelFile getStairsOuter()
        {
            return stairsOuter;
        }
    }

    public static class WallBlockModels
    {
        private final ModelFile post, side, sideTall;

        public WallBlockModels(ModelFile post, ModelFile side, ModelFile sideTall)
        {
            this.post = post;
            this.side = side;
            this.sideTall = sideTall;
        }

        public ModelFile getPost()
        {
            return post;
        }

        public ModelFile getSide()
        {
            return side;
        }

        public ModelFile getSideTall()
        {
            return sideTall;
        }
    }

    public static class FenceGateBlockModels
    {
        private final ModelFile gate, gateOpen, gateWall, gateWallOpen;

        public FenceGateBlockModels(ModelFile gate, ModelFile gateOpen, ModelFile gateWall, ModelFile gateWallOpen)
        {
            this.gate = gate;
            this.gateOpen = gateOpen;
            this.gateWall = gateWall;
            this.gateWallOpen = gateWallOpen;
        }

        public ModelFile getGate()
        {
            return gate;
        }

        public ModelFile getGateOpen()
        {
            return gateOpen;
        }

        public ModelFile getGateWall()
        {
            return gateWall;
        }

        public ModelFile getGateWallOpen()
        {
            return gateWallOpen;
        }
    }

    public static class FenceBlockModels
    {
        private final ModelFile fencePost, fenceSide;

        public FenceBlockModels(ModelFile fencePost, ModelFile fenceSide)
        {
            this.fencePost = fencePost;
            this.fenceSide = fenceSide;
        }

        public ModelFile getFencePost()
        {
            return fencePost;
        }

        public ModelFile getFenceSide()
        {
            return fenceSide;
        }
    }

    public static class SlabBlockModels
    {
        private final ModelFile slab, slabTop, doubleSlab;

        public SlabBlockModels(ModelFile slab, ModelFile slabTop, ModelFile doubleSlab)
        {
            this.slab = slab;
            this.slabTop = slabTop;
            this.doubleSlab = doubleSlab;
        }

        public ModelFile getSlab()
        {
            return slab;
        }

        public ModelFile getSlabTop()
        {
            return slabTop;
        }

        public ModelFile getDoubleSlab()
        {
            return doubleSlab;
        }
    }

    public static class AxisBlockModels
    {
        private final ModelFile cubeColumn, cubeColumnHorizontal;

        public AxisBlockModels(ModelFile cubeColumn, ModelFile cubeColumnHorizontal)
        {
            this.cubeColumn = cubeColumn;
            this.cubeColumnHorizontal = cubeColumnHorizontal;
        }

        public ModelFile getCubeColumn()
        {
            return cubeColumn;
        }

        public ModelFile getCubeColumnHorizontal()
        {
            return cubeColumnHorizontal;
        }
    }

    public static class TrapDoorBlockModels
    {
        private final ModelFile bottom, top, open;

        public TrapDoorBlockModels(ModelFile bottom, ModelFile top, ModelFile open)
        {
            this.bottom = bottom;
            this.top = top;
            this.open = open;
        }

        public ModelFile getBottom()
        {
            return bottom;
        }

        public ModelFile getTop()
        {
            return top;
        }

        public ModelFile getOpen()
        {
            return open;
        }
    }
}

package mrp_v2.mrplibrary.datagen;

import mrp_v2.mrplibrary.MrpLibrary;
import mrp_v2.mrplibrary.datagen.providers.BlockStateProvider;
import mrp_v2.mrplibrary.datagen.providers.ItemModelProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class TintedBlockStateGenerator extends BlockStateProvider
{
    public static final String SUBSTITUTE = "#";
    public static final String PARTICLE = "particle";
    public static final String BOTTOM_BASE = BlockStateProvider.BOTTOM;
    public static final String BOTTOM = SUBSTITUTE + BOTTOM_BASE;
    public static final String TOP = SUBSTITUTE + BlockStateProvider.TOP;
    public static final String SIDE = SUBSTITUTE + BlockStateProvider.SIDE;
    public static final String END = SUBSTITUTE + BlockStateProvider.END;
    public static final String FRONT = SUBSTITUTE + BlockStateProvider.FRONT;
    public static final String WALL = SUBSTITUTE + ItemModelProvider.WALL;
    public static final String TEXTURE = SUBSTITUTE + ItemModelProvider.TEXTURE;
    public static final String ALL = SUBSTITUTE + BlockStateProvider.ALL;
    public static final ResourceLocation BASE_BLOCK = new ResourceLocation("block/block");
    public static final ResourceLocation CUBE_TINTED = makeTintedBlockLoc("cube");
    public static final ResourceLocation CUBE_ALL_TINTED = makeTintedBlockLoc("cube_all");
    public static final ResourceLocation CUBE_BOTTOM_TOP_TINTED = makeTintedBlockLoc("cube_bottom_top");
    public static final ResourceLocation CUBE_COLUMN_TINTED = makeTintedBlockLoc("cube_column");
    public static final ResourceLocation CUBE_COLUMN_HORIZONTAL_TINTED =
            makeTintedBlockLoc("cube_column_horizontal_tinted");
    public static final ResourceLocation CUBE_DIRECTIONAL_TINTED = makeTintedBlockLoc("cube_directional");
    public static final ResourceLocation CUBE_MIRRORED_TINTED = makeTintedBlockLoc("cube_mirrored");
    public static final ResourceLocation CUBE_MIRRORED_ALL_TINTED = makeTintedBlockLoc("cube_mirrored_all");
    public static final ResourceLocation CUBE_TOP_TINTED = makeTintedBlockLoc("cube_top");
    public static final ResourceLocation FENCE_GATE_TINTED = makeTintedBlockLoc("fence_gate");
    public static final ResourceLocation FENCE_GATE_OPEN_TINTED = makeTintedBlockLoc("fence_gate_open");
    public static final ResourceLocation FENCE_GATE_WALL_TINTED = makeTintedBlockLoc("fence_gate_wall");
    public static final ResourceLocation FENCE_GATE_WALL_OPEN_TINTED = makeTintedBlockLoc("fence_gate_wall_open");
    public static final ResourceLocation FENCE_INVENTORY_TINTED = makeTintedBlockLoc("fence_inventory");
    public static final ResourceLocation FENCE_POST_TINTED = makeTintedBlockLoc("fence_post");
    public static final ResourceLocation FENCE_SIDE_TINTED = makeTintedBlockLoc("fence_side");
    public static final ResourceLocation ORIENTABLE_TINTED = makeTintedBlockLoc("orientable");
    public static final ResourceLocation ORIENTABLE_VERTICAL_TINTED = makeTintedBlockLoc("orientable_vertical");
    public static final ResourceLocation ORIENTABLE_WITH_BOTTOM_TINTED = makeTintedBlockLoc("orientable_with_bottom");
    public static final ResourceLocation SLAB_TINTED = makeTintedBlockLoc("slab");
    public static final ResourceLocation SLAB_TOP_TINTED = makeTintedBlockLoc("slab_top");
    public static final ResourceLocation STAIRS_TINTED = makeTintedBlockLoc("stairs");
    public static final ResourceLocation STAIRS_INNER_TINTED = makeTintedBlockLoc("stairs_inner");
    public static final ResourceLocation STAIRS_OUTER_TINTED = makeTintedBlockLoc("stairs_outer");
    public static final ResourceLocation WALL_INVENTORY_TINTED = makeTintedBlockLoc("wall_inventory");
    public static final ResourceLocation WALL_POST_TINTED = makeTintedBlockLoc("wall_post");
    public static final ResourceLocation WALL_SIDE_TINTED = makeTintedBlockLoc("wall_side");
    public static final ResourceLocation WALL_SIDE_TALL_TINTED = makeTintedBlockLoc("wall_side_tall");
    public static final HashMap<Direction, Pair<Integer, Integer>> FACE_ROTATION_MAP;

    static
    {
        FACE_ROTATION_MAP = new HashMap<>();
        FACE_ROTATION_MAP.put(Direction.NORTH, Pair.of(0, 0));
        FACE_ROTATION_MAP.put(Direction.EAST, Pair.of(0, 90));
        FACE_ROTATION_MAP.put(Direction.SOUTH, Pair.of(0, 180));
        FACE_ROTATION_MAP.put(Direction.WEST, Pair.of(0, -90));
        FACE_ROTATION_MAP.put(Direction.UP, Pair.of(-90, 0));
        FACE_ROTATION_MAP.put(Direction.DOWN, Pair.of(90, 0));
    }

    public TintedBlockStateGenerator(DataGenerator gen, String modid, ExistingFileHelper exFileHelper)
    {
        super(gen, modid, exFileHelper);
        promiseGeneration(ORIENTABLE_WITH_BOTTOM_TINTED);
    }

    private static ResourceLocation makeTintedBlockLoc(String loc)
    {
        return makeBlockLoc(loc + "_tinted");
    }

    private static ResourceLocation makeBlockLoc(String loc)
    {
        return new ResourceLocation(MrpLibrary.ID, "block/" + loc);
    }

    @Override protected void registerStatesAndModels()
    {
        registerFullBlockModels();
        registerSlabModels();
        registerStairModels();
        registerFenceModels();
        registerFenceGateModels();
        registerWallModels();
    }

    private void registerFenceModels()
    {
        ModelBuilder<BlockModelBuilder>.ElementBuilder fenceInventoryLeftPost;
        ModelBuilder<BlockModelBuilder>.ElementBuilder fenceInventoryRightPost;
        ModelBuilder<BlockModelBuilder>.ElementBuilder fenceInventoryTopBar;
        ModelBuilder<BlockModelBuilder>.ElementBuilder fenceInventoryBottomBar;
        (fenceInventoryBottomBar = (fenceInventoryTopBar = (fenceInventoryRightPost = (fenceInventoryLeftPost =
                models().withExistingParent(FENCE_INVENTORY_TINTED.toString(), mcLoc("block/fence_inventory"))
                        .element()).from(6, 0, 0).to(10, 16, 4)
                .allFaces((direction, faceBuilder) -> faceBuilder.texture(TEXTURE)).face(Direction.DOWN)
                .cullface(Direction.DOWN).end().faces(TintedBlockStateGenerator::tintFunction).end().element())
                .from(6, 0, 12).to(10, 16, 16).allFaces((direction, faceBuilder) -> faceBuilder.texture(TEXTURE))
                .face(Direction.DOWN).cullface(Direction.DOWN).end().faces(TintedBlockStateGenerator::tintFunction)
                .end().element()).from(7, 13, -2).to(9, 15, 18)
                .allFaces((direction, faceBuilder) -> faceBuilder.texture(TEXTURE))
                .faces(TintedBlockStateGenerator::tintFunction).end().element()).from(7, 5, -2).to(9, 7, 18)
                .allFaces((direction, faceBuilder) -> faceBuilder.texture(TEXTURE))
                .faces(TintedBlockStateGenerator::tintFunction).end();
        Util.setUvs(fenceInventoryLeftPost, 6, 0, 10, 4, Direction.DOWN, Direction.UP);
        Util.setUvs(fenceInventoryLeftPost, 6, 0, 10, 16, Direction.NORTH, Direction.SOUTH);
        Util.setUvs(fenceInventoryLeftPost, 0, 0, 4, 16, Direction.WEST, Direction.EAST);
        Util.setUvs(fenceInventoryRightPost, 6, 12, 10, 16, Direction.DOWN, Direction.UP);
        Util.setUvs(fenceInventoryRightPost, 6, 0, 10, 16, Direction.NORTH, Direction.SOUTH);
        Util.setUvs(fenceInventoryRightPost, 12, 0, 16, 16, Direction.WEST, Direction.EAST);
        Util.setUvs(fenceInventoryTopBar, 7, 0, 9, 16, Direction.DOWN, Direction.UP);
        Util.setUvs(fenceInventoryTopBar, 7, 1, 9, 3, Direction.NORTH, Direction.SOUTH);
        Util.setUvs(fenceInventoryTopBar, 0, 1, 16, 3, Direction.WEST, Direction.EAST);
        Util.setUvs(fenceInventoryBottomBar, 7, 0, 9, 16, Direction.DOWN, Direction.UP);
        Util.setUvs(fenceInventoryBottomBar, 7, 9, 9, 11, Direction.NORTH, Direction.SOUTH);
        Util.setUvs(fenceInventoryBottomBar, 0, 9, 16, 11, Direction.WEST, Direction.EAST);
        ModelBuilder<BlockModelBuilder>.ElementBuilder fencePost;
        (fencePost = models().getBuilder(FENCE_POST_TINTED.toString()).texture(PARTICLE, TEXTURE).element())
                .from(6, 0, 6).to(10, 16, 10).allFaces((direction, faceBuilder) -> faceBuilder.texture(TEXTURE))
                .face(Direction.DOWN).cullface(Direction.DOWN).end().face(Direction.UP).cullface(Direction.UP).end()
                .faces(TintedBlockStateGenerator::tintFunction).end();
        Util.setUvs(fencePost, 6, 6, 10, 10, Direction.DOWN, Direction.UP);
        Util.setUvs(fencePost, 6, 0, 10, 16, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);
        ModelBuilder<BlockModelBuilder>.ElementBuilder fenceSideTop;
        ModelBuilder<BlockModelBuilder>.ElementBuilder fenceSideBottom;
        (fenceSideBottom =
                (fenceSideTop = models().getBuilder(FENCE_SIDE_TINTED.toString()).texture(PARTICLE, TEXTURE).element())
                        .from(7, 12, 0).to(9, 15, 9).face(Direction.DOWN).end().face(Direction.UP).end()
                        .face(Direction.NORTH).cullface(Direction.NORTH).end().face(Direction.WEST).end()
                        .face(Direction.WEST).end().face(Direction.EAST).end()
                        .faces((direction, faceBuilder) -> faceBuilder.texture(TEXTURE))
                        .faces(TintedBlockStateGenerator::tintFunction).end().element()).from(7, 6, 0).to(9, 9, 9)
                .face(Direction.DOWN).end().face(Direction.UP).end().face(Direction.NORTH).cullface(Direction.NORTH)
                .end().face(Direction.WEST).end().face(Direction.EAST).end()
                .faces((direction, faceBuilder) -> faceBuilder.texture(TEXTURE))
                .faces(TintedBlockStateGenerator::tintFunction).end();
        Util.setUvs(fenceSideTop, 7, 0, 9, 9, Direction.DOWN, Direction.UP);
        fenceSideTop.face(Direction.NORTH).uvs(7, 1, 9, 4);
        Util.setUvs(fenceSideTop, 0, 1, 9, 4, Direction.WEST, Direction.EAST);
        Util.setUvs(fenceSideBottom, 7, 0, 9, 9, Direction.DOWN, Direction.UP);
        fenceSideBottom.face(Direction.NORTH).uvs(7, 7, 9, 10);
        Util.setUvs(fenceSideBottom, 0, 7, 9, 10, Direction.WEST, Direction.EAST);
    }

    private void registerFenceGateModels()
    {
        ModelBuilder<BlockModelBuilder> fenceGateTintedBuilder =
                models().withExistingParent(FENCE_GATE_TINTED.toString(), mcLoc("block/template_fence_gate"));
        ModelBuilder<BlockModelBuilder> fenceGateOpenTintedBuilder =
                models().getBuilder(FENCE_GATE_OPEN_TINTED.toString()).texture(PARTICLE, TEXTURE);
        ModelBuilder<BlockModelBuilder> fenceGateWallTintedBuilder =
                models().getBuilder(FENCE_GATE_WALL_TINTED.toString()).texture(PARTICLE, TEXTURE);
        ModelBuilder<BlockModelBuilder> fenceGateWallOpenTintedBuilder =
                models().getBuilder(FENCE_GATE_WALL_OPEN_TINTED.toString()).texture(PARTICLE, TEXTURE);
        Util.Wall notWall = new Util.Wall(false);
        Util.Wall wall = new Util.Wall(true);
        Util.fenceGateElements(fenceGateTintedBuilder, false, notWall);
        Util.fenceGateElements(fenceGateOpenTintedBuilder, true, notWall);
        Util.fenceGateElements(fenceGateWallTintedBuilder, false, wall);
        Util.fenceGateElements(fenceGateWallOpenTintedBuilder, true, wall);
    }

    private void registerFullBlockModels()
    {
        models().withExistingParent(CUBE_TINTED.toString(), BASE_BLOCK).element().from(0, 0, 0).to(16, 16, 16)
                .allFaces(TintedBlockStateGenerator::cullfaceAndTextureFunction)
                .faces(TintedBlockStateGenerator::tintFunction).end();
        ModelBuilder<BlockModelBuilder> cubeAllTintedBuilder =
                models().withExistingParent(CUBE_ALL_TINTED.toString(), CUBE_TINTED).texture(PARTICLE, ALL);
        ModelBuilder<BlockModelBuilder> cubeBottomTopTintedBuilder =
                models().withExistingParent(CUBE_BOTTOM_TOP_TINTED.toString(), CUBE_TINTED).texture(PARTICLE, SIDE)
                        .texture(Direction.DOWN.toString(), BOTTOM).texture(Direction.UP.toString(), TOP);
        ModelBuilder<BlockModelBuilder> cubeColumnTintedBuilder =
                models().withExistingParent(CUBE_COLUMN_TINTED.toString(), CUBE_TINTED).texture(PARTICLE, SIDE);
        ModelBuilder<BlockModelBuilder> cubeColumnHorizontalTintedBuilder =
                models().withExistingParent(CUBE_COLUMN_HORIZONTAL_TINTED.toString(), BASE_BLOCK).element()
                        .from(0, 0, 0).to(16, 16, 16).allFaces(TintedBlockStateGenerator::cullfaceAndTextureFunction)
                        .face(Direction.UP).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).end()
                        .faces(TintedBlockStateGenerator::tintFunction).end().texture(PARTICLE, SIDE);
        models().withExistingParent(CUBE_DIRECTIONAL_TINTED.toString(), BASE_BLOCK).element().from(0, 0, 0)
                .to(16, 16, 16).allFaces(TintedBlockStateGenerator::cullfaceAndTextureFunction).face(Direction.DOWN)
                .rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).end().face(Direction.WEST)
                .rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).end().face(Direction.EAST)
                .rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end().faces(TintedBlockStateGenerator::tintFunction)
                .end();
        models().getBuilder(CUBE_MIRRORED_TINTED.toString()).element().from(0, 0, 0).to(16, 16, 16)
                .allFaces(TintedBlockStateGenerator::cullfaceAndTextureFunction)
                .faces((direction, faceBuilder) -> faceBuilder.uvs(16, 0, 0, 16))
                .faces(TintedBlockStateGenerator::tintFunction).end();
        ModelBuilder<BlockModelBuilder> cubeMirroredAllTintedBuilder =
                models().withExistingParent(CUBE_MIRRORED_ALL_TINTED.toString(), CUBE_MIRRORED_TINTED)
                        .texture(PARTICLE, ALL);
        ModelBuilder<BlockModelBuilder> cubeTopTintedBuilder =
                models().withExistingParent(CUBE_TOP_TINTED.toString(), CUBE_TINTED).texture(PARTICLE, SIDE);
        models().withExistingParent(ORIENTABLE_TINTED.toString(), ORIENTABLE_WITH_BOTTOM_TINTED)
                .texture(BOTTOM_BASE, TOP);
        ModelBuilder<BlockModelBuilder> orientableVerticalTintedBuilder =
                models().withExistingParent(ORIENTABLE_VERTICAL_TINTED.toString(), CUBE_TINTED).texture(PARTICLE, SIDE);
        ModelBuilder<BlockModelBuilder> orientableWithBottomTintedBuilder =
                models().withExistingParent(ORIENTABLE_WITH_BOTTOM_TINTED.toString(), CUBE_TINTED)
                        .texture(PARTICLE, FRONT).texture(Direction.DOWN.toString(), BOTTOM)
                        .texture(Direction.UP.toString(), TOP);
        for (Direction direction : Direction.values())
        {
            String name = direction.toString();
            cubeAllTintedBuilder.texture(name, ALL);
            cubeMirroredAllTintedBuilder.texture(name, ALL);
            cubeTopTintedBuilder.texture(name, SIDE);
            orientableVerticalTintedBuilder.texture(name, SIDE);
        }
        for (Direction direction : Direction.Plane.HORIZONTAL)
        {
            String name = direction.toString();
            cubeBottomTopTintedBuilder.texture(name, SIDE);
            cubeColumnTintedBuilder.texture(name, SIDE);
            cubeColumnHorizontalTintedBuilder.texture(name, SIDE);
            orientableWithBottomTintedBuilder.texture(name, SIDE);
        }
        for (Direction direction : Direction.Plane.VERTICAL)
        {
            String name = direction.toString();
            cubeColumnTintedBuilder.texture(name, END);
            cubeColumnHorizontalTintedBuilder.texture(name, END);
        }
        cubeTopTintedBuilder.texture(Direction.UP.toString(), TOP);
        orientableVerticalTintedBuilder.texture(Direction.UP.toString(), FRONT);
        orientableWithBottomTintedBuilder.texture(Direction.NORTH.toString(), FRONT);
    }

    private static void cullfaceAndTextureFunction(Direction direction,
            ModelBuilder<BlockModelBuilder>.ElementBuilder.FaceBuilder faceBuilder)
    {
        faceBuilder.cullface(direction).texture(SUBSTITUTE + direction.toString());
    }

    private static void tintFunction(Direction direction,
            ModelBuilder<BlockModelBuilder>.ElementBuilder.FaceBuilder faceBuilder)
    {
        faceBuilder.tintindex(0);
    }

    private void registerWallModels()
    {
        models().withExistingParent(WALL_INVENTORY_TINTED.toString(), mcLoc("block/wall_inventory")).element()
                .from(4, 0, 4).to(12, 16, 12).allFaces((direction, faceBuilder) -> faceBuilder.texture(WALL))
                .face(Direction.DOWN).cullface(Direction.DOWN).end().faces(TintedBlockStateGenerator::tintFunction)
                .end().element().from(5, 0, 0).to(11, 13, 16)
                .allFaces((direction, faceBuilder) -> faceBuilder.texture(WALL)).face(Direction.DOWN)
                .cullface(Direction.DOWN).end().face(Direction.NORTH).cullface(Direction.NORTH).end()
                .face(Direction.SOUTH).cullface(Direction.SOUTH).end().faces(TintedBlockStateGenerator::tintFunction)
                .end();
        models().withExistingParent(WALL_POST_TINTED.toString(), BASE_BLOCK).texture(PARTICLE, WALL).element()
                .from(4, 0, 4).to(12, 16, 12).allFaces((dir, builder) -> builder.texture(WALL)).face(Direction.DOWN)
                .cullface(Direction.DOWN).end().face(Direction.UP).cullface(Direction.UP).end()
                .faces(TintedBlockStateGenerator::tintFunction).end();
        models().withExistingParent(WALL_SIDE_TINTED.toString(), BASE_BLOCK).texture(PARTICLE, WALL).element()
                .from(5, 0, 0).to(11, 14, 8).face(Direction.DOWN).texture(WALL).cullface(Direction.DOWN).end()
                .face(Direction.UP).texture(WALL).end().face(Direction.NORTH).texture(WALL).cullface(Direction.NORTH)
                .end().face(Direction.WEST).texture(WALL).end().face(Direction.EAST).texture(WALL).end()
                .faces(TintedBlockStateGenerator::tintFunction).end();
        models().withExistingParent(WALL_SIDE_TALL_TINTED.toString(), BASE_BLOCK).texture(PARTICLE, WALL).element()
                .from(5, 0, 0).to(11, 16, 8).face(Direction.DOWN).texture(WALL).cullface(Direction.DOWN).end()
                .face(Direction.UP).texture(WALL).cullface(Direction.UP).end().face(Direction.NORTH).texture(WALL)
                .cullface(Direction.NORTH).end().face(Direction.WEST).texture(WALL).end().face(Direction.EAST)
                .texture(WALL).end().faces(TintedBlockStateGenerator::tintFunction).end();
    }

    private void registerSlabModels()
    {
        ModelBuilder<BlockModelBuilder>.ElementBuilder slabTintedBuilder =
                models().withExistingParent(SLAB_TINTED.toString(), BASE_BLOCK).texture(PARTICLE, SIDE).element();
        ModelBuilder<BlockModelBuilder>.ElementBuilder slabTopTintedBuilder =
                models().getBuilder(SLAB_TOP_TINTED.toString()).texture(PARTICLE, SIDE).element();
        slabTintedBuilder.from(0, 0, 0).to(16, 8, 16).face(Direction.DOWN).texture(BOTTOM).cullface(Direction.DOWN)
                .end().face(Direction.UP).texture(TOP).end();
        slabTopTintedBuilder.from(0, 8, 0).to(16, 16, 16).face(Direction.DOWN).texture(BOTTOM).end().face(Direction.UP)
                .texture(TOP).cullface(Direction.UP).end();
        BiConsumer<ModelBuilder<BlockModelBuilder>.ElementBuilder, Direction> sideBuilder =
                (builder, side) -> builder.face(side).texture(SIDE).cullface(side).end();
        for (Direction direction : Direction.Plane.HORIZONTAL)
        {
            sideBuilder.accept(slabTintedBuilder, direction);
            sideBuilder.accept(slabTopTintedBuilder, direction);
        }
        slabTintedBuilder.faces(TintedBlockStateGenerator::tintFunction).end();
        slabTopTintedBuilder.faces(TintedBlockStateGenerator::tintFunction).end();
    }

    private void registerStairModels()
    {
        Consumer<ModelBuilder<BlockModelBuilder>> firstElementBuilder = (builder) ->
        {
            ModelBuilder<BlockModelBuilder>.ElementBuilder elementBuilder =
                    builder.element().from(0, 0, 0).to(16, 8, 16).face(Direction.DOWN).texture(BOTTOM)
                            .cullface(Direction.DOWN).end().face(Direction.UP).texture(TOP).end();
            for (Direction side : Direction.Plane.HORIZONTAL)
            {
                elementBuilder.face(side).texture(SIDE).cullface(side).end();
            }
            elementBuilder.end();
            elementBuilder.faces(TintedBlockStateGenerator::tintFunction);
        };
        BiFunction<ModelBuilder<BlockModelBuilder>.ElementBuilder, Direction[], ModelBuilder<BlockModelBuilder>.ElementBuilder>
                basicCullFaceBuilder = (builder, directions) ->
        {
            for (Direction side : directions)
            {
                builder.face(side).texture(SIDE).cullface(side).end();
            }
            return builder;
        };
        Consumer<ModelBuilder<BlockModelBuilder>> stairsAndInnerSecondElementBuilder = (builder) ->
        {
            ModelBuilder<BlockModelBuilder>.ElementBuilder elementBuilder =
                    builder.element().from(8, 8, 0).to(16, 16, 16);
            basicCullFaceBuilder
                    .apply(elementBuilder, new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST});
            elementBuilder.face(Direction.UP).texture(TOP).cullface(Direction.UP).end();
            elementBuilder.face(Direction.WEST).texture(SIDE).end();
            elementBuilder.faces(TintedBlockStateGenerator::tintFunction);
        };
        ModelBuilder<BlockModelBuilder> stairsBuilder =
                models().withExistingParent(STAIRS_TINTED.toString(), BASE_BLOCK).transforms()
                        .transform(ModelBuilder.Perspective.GUI).rotation(30, 135, 0).scale(0.625F, 0.625F, 0.625F)
                        .end().transform(ModelBuilder.Perspective.HEAD).rotation(0, -90, 0).scale(1, 1, 1).end()
                        .transform(ModelBuilder.Perspective.THIRDPERSON_LEFT).rotation(75, -135, 0)
                        .translation(0, 2.5F, 0).scale(0.375F, 0.375F, 0.375F).end().end().texture(PARTICLE, SIDE);
        firstElementBuilder.accept(stairsBuilder);
        stairsAndInnerSecondElementBuilder.accept(stairsBuilder);
        ModelBuilder<BlockModelBuilder> stairsInnerBuilder =
                models().getBuilder(STAIRS_INNER_TINTED.toString()).texture(PARTICLE, SIDE);
        firstElementBuilder.accept(stairsInnerBuilder);
        stairsAndInnerSecondElementBuilder.accept(stairsInnerBuilder);
        basicCullFaceBuilder.apply(stairsInnerBuilder.element().from(0, 8, 8).to(8, 16, 16).face(Direction.UP)
                        .cullface(Direction.UP).texture(TOP).end().face(Direction.NORTH).texture(SIDE).end(),
                new Direction[]{Direction.SOUTH, Direction.WEST}).faces(TintedBlockStateGenerator::tintFunction).end();
        ModelBuilder<BlockModelBuilder> stairsOuterBuilder =
                models().getBuilder(STAIRS_OUTER_TINTED.toString()).texture(PARTICLE, SIDE);
        firstElementBuilder.accept(stairsOuterBuilder);
        stairsOuterBuilder.element().from(8, 8, 8).to(16, 16, 16).face(Direction.UP).cullface(Direction.UP).texture(TOP)
                .end().face(Direction.NORTH).texture(SIDE).end().face(Direction.SOUTH).cullface(Direction.SOUTH)
                .texture(SIDE).end().face(Direction.WEST).texture(SIDE).end().face(Direction.EAST)
                .cullface(Direction.EAST).texture(SIDE).end().faces(TintedBlockStateGenerator::tintFunction).end();
    }

    @Nonnull @Override public String getName()
    {
        return super.getName() + ": Tinted Blocks";
    }

    public static class Util
    {
        public static void fenceGateElements(ModelBuilder<BlockModelBuilder> builder, boolean isOpen, Wall wall)
        {
            ModelBuilder<BlockModelBuilder>.ElementBuilder leftPost =
                    builder.element().from(0, wall.apply(5), 7).to(2, wall.apply(16), 9)
                            .allFaces((direction, faceBuilder) -> faceBuilder.texture(TEXTURE)).face(Direction.WEST)
                            .cullface(Direction.WEST).end().faces(TintedBlockStateGenerator::tintFunction);
            setUvs(leftPost, 0, 7, 2, 9, Direction.DOWN, Direction.UP);
            setUvs(leftPost, 0, 0, 2, 11, Direction.NORTH, Direction.SOUTH);
            setUvs(leftPost, 7, 0, 9, 11, Direction.WEST, Direction.EAST);
            ModelBuilder<BlockModelBuilder>.ElementBuilder rightPost =
                    builder.element().from(14, wall.apply(5), 7).to(16, wall.apply(16), 9)
                            .allFaces((direction, faceBuilder) -> faceBuilder.texture(TEXTURE)).face(Direction.EAST)
                            .cullface(Direction.EAST).end().faces(TintedBlockStateGenerator::tintFunction);
            setUvs(rightPost, 14, 7, 16, 9, Direction.DOWN, Direction.UP);
            setUvs(rightPost, 14, 0, 16, 11, Direction.NORTH, Direction.SOUTH);
            setUvs(rightPost, 7, 0, 9, 11, Direction.WEST, Direction.EAST);
            ModelBuilder<BlockModelBuilder>.ElementBuilder leftInnerVerticalPost =
                    builder.element().from(isOpen ? 0 : 6, wall.apply(6), isOpen ? 13 : 7)
                            .to(isOpen ? 2 : 8, wall.apply(15), isOpen ? 15 : 9).allFaces(Util::textureFunction)
                            .faces(TintedBlockStateGenerator::tintFunction);
            setUvs(leftInnerVerticalPost, 0, 6, 13, 7, 2, 8, 15, 9, isOpen, Direction.DOWN, Direction.UP);
            setUvs(leftInnerVerticalPost, 0, 6, 1, 2, 8, 10, isOpen, Direction.NORTH, Direction.SOUTH);
            setUvs(leftInnerVerticalPost, 13, 7, 1, 15, 9, 10, isOpen, Direction.WEST, Direction.EAST);
            ModelBuilder<BlockModelBuilder>.ElementBuilder rightInnerVerticalPost =
                    builder.element().from(isOpen ? 14 : 8, wall.apply(6), isOpen ? 13 : 7)
                            .to(isOpen ? 16 : 10, wall.apply(15), isOpen ? 15 : 9).allFaces(Util::textureFunction)
                            .faces(TintedBlockStateGenerator::tintFunction);
            setUvs(rightInnerVerticalPost, 14, 8, 13, 7, 16, 10, 15, 9, isOpen, Direction.DOWN, Direction.UP);
            setUvs(rightInnerVerticalPost, 14, 8, 1, 16, 10, 10, isOpen, Direction.NORTH, Direction.SOUTH);
            setUvs(rightInnerVerticalPost, 13, 7, 1, 15, 9, 10, isOpen, Direction.WEST, Direction.EAST);
            ModelBuilder<BlockModelBuilder>.ElementBuilder leftLowerHorizontalBar =
                    builder.element().from(isOpen ? 0 : 2, wall.apply(6), isOpen ? 9 : 7)
                            .to(isOpen ? 2 : 6, wall.apply(9), isOpen ? 13 : 9).face(Direction.DOWN).end()
                            .face(Direction.UP).end();
            ModelBuilder<BlockModelBuilder>.ElementBuilder leftUpperHorizontalBar =
                    builder.element().from(isOpen ? 0 : 2, wall.apply(12), isOpen ? 9 : 7)
                            .to(isOpen ? 2 : 6, wall.apply(15), isOpen ? 13 : 9).face(Direction.DOWN).end()
                            .face(Direction.UP).end();
            ModelBuilder<BlockModelBuilder>.ElementBuilder rightLowerHorizontalBar =
                    builder.element().from(isOpen ? 14 : 10, wall.apply(6), isOpen ? 9 : 7)
                            .to(isOpen ? 16 : 14, wall.apply(9), isOpen ? 13 : 9).face(Direction.DOWN).end()
                            .face(Direction.UP).end();
            ModelBuilder<BlockModelBuilder>.ElementBuilder rightUpperHorizontalBar =
                    builder.element().from(isOpen ? 14 : 10, wall.apply(12), isOpen ? 9 : 7)
                            .to(isOpen ? 16 : 14, wall.apply(15), isOpen ? 13 : 9).face(Direction.DOWN).end()
                            .face(Direction.UP).end();
            if (isOpen)
            {
                leftLowerHorizontalBar.face(Direction.WEST).end().face(Direction.EAST).end();
                leftUpperHorizontalBar.face(Direction.WEST).end().face(Direction.EAST).end();
                rightLowerHorizontalBar.face(Direction.WEST).end().face(Direction.EAST).end();
                rightUpperHorizontalBar.face(Direction.WEST).end().face(Direction.EAST).end();
            } else
            {
                leftLowerHorizontalBar.face(Direction.NORTH).end().face(Direction.SOUTH).end();
                leftUpperHorizontalBar.face(Direction.NORTH).end().face(Direction.SOUTH).end();
                rightLowerHorizontalBar.face(Direction.NORTH).end().face(Direction.SOUTH).end();
                rightUpperHorizontalBar.face(Direction.NORTH).end().face(Direction.SOUTH).end();
            }
            leftLowerHorizontalBar.faces(Util::textureFunction);
            leftLowerHorizontalBar.faces(TintedBlockStateGenerator::tintFunction);
            setUvs(leftLowerHorizontalBar, 13, 2, 7, 15, 6, 10, isOpen);
            setUvs(leftLowerHorizontalBar, 0, 2, 9, 7, 2, 6, 13, 9, isOpen, Direction.DOWN, Direction.UP);
            leftUpperHorizontalBar.faces(Util::textureFunction);
            leftUpperHorizontalBar.faces(TintedBlockStateGenerator::tintFunction);
            setUvs(leftUpperHorizontalBar, 13, 2, 1, 15, 6, 4, isOpen);
            setUvs(leftUpperHorizontalBar, 0, 2, 9, 7, 2, 6, 13, 9, isOpen, Direction.DOWN, Direction.UP);
            rightLowerHorizontalBar.faces(Util::textureFunction);
            rightLowerHorizontalBar.faces(TintedBlockStateGenerator::tintFunction);
            setUvs(rightLowerHorizontalBar, 13, 10, 7, 15, 14, 10, isOpen);
            setUvs(rightLowerHorizontalBar, 14, 10, 9, 7, 16, 14, 13, 9, isOpen, Direction.DOWN, Direction.UP);
            rightUpperHorizontalBar.faces(Util::textureFunction);
            rightUpperHorizontalBar.faces(TintedBlockStateGenerator::tintFunction);
            setUvs(rightUpperHorizontalBar, 13, 10, 1, 15, 14, 4, isOpen);
            setUvs(rightUpperHorizontalBar, 14, 10, 9, 7, 16, 14, 13, 9, isOpen, Direction.DOWN, Direction.UP);
        }

        public static void setUvs(ModelBuilder<BlockModelBuilder>.ElementBuilder builder, float u1, float v1, float u2,
                float v2, Direction... directions)
        {
            for (Direction direction : directions)
            {
                builder.face(direction).uvs(u1, v1, u2, v2);
            }
        }

        public static void textureFunction(Direction direction,
                ModelBuilder<BlockModelBuilder>.ElementBuilder.FaceBuilder faceBuilder)
        {
            faceBuilder.texture(TEXTURE);
        }

        public static void setUvs(ModelBuilder<BlockModelBuilder>.ElementBuilder builder, float u1t, float u1f,
                float v1t, float v1f, float u2t, float u2f, float v2t, float v2f, boolean condition,
                Direction... directions)
        {
            for (Direction direction : directions)
            {
                if (condition)
                {
                    builder.face(direction).uvs(u1t, v1t, u2t, v2t);
                } else
                {
                    builder.face(direction).uvs(u1f, v1f, u2f, v2f);
                }
            }
        }

        public static void setUvs(ModelBuilder<BlockModelBuilder>.ElementBuilder builder, float u1t, float u1f,
                float v1, float u2t, float u2f, float v2, boolean condition, Direction... directions)
        {
            for (Direction direction : directions)
            {
                if (condition)
                {
                    builder.face(direction).uvs(u1t, v1, u2t, v2);
                } else
                {
                    builder.face(direction).uvs(u1f, v1, u2f, v2);
                }
            }
        }

        public static void setUvs(ModelBuilder<BlockModelBuilder>.ElementBuilder builder, float u1t, float u1f,
                float v1, float u2t, float u2f, float v2, boolean condition)
        {
            builder.faces((direction, faceBuilder) ->
            {
                if (condition)
                {
                    faceBuilder.uvs(u1t, v1, u2t, v2);
                } else
                {
                    faceBuilder.uvs(u1f, v1, u2f, v2);
                }
            });
        }

        public static float applyWall(float base, boolean isWall)
        {
            return isWall ? base - 3.0F : base;
        }

        public static class Wall
        {
            private final boolean isWall;

            public Wall(boolean isWall)
            {
                this.isWall = isWall;
            }

            public float apply(float f)
            {
                return applyWall(f, isWall);
            }
        }
    }
}

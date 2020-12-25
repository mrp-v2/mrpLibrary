package mrp_v2.mrplibrary.datagen.providers;

import com.google.common.base.Preconditions;
import com.google.common.hash.Hasher;
import com.google.gson.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.resources.IResource;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public abstract class TextureProvider implements IDataProvider
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static int ALPHA_MASK = 0xFF000000;
    public final String modId;
    protected final ExistingFileHelper existingFileHelper;
    protected final DataGenerator generator;
    protected final Map<ResourceLocation, BufferedImage> providedTextures;
    protected final Map<ResourceLocation, TextureMetaBuilder> providedTextureMetas;

    public static int color(int r, int g, int b)
    {
        Preconditions.checkArgument(r < 256 && r >= 0);
        Preconditions.checkArgument(g < 256 && g >= 0);
        Preconditions.checkArgument(b < 256 && b >= 0);
        return color(255, r, g, b);
    }

    public static int color(int a, int r, int g, int b)
    {
        Preconditions.checkArgument(a < 256 && a >= 0);
        Preconditions.checkArgument(r < 256 && r >= 0);
        Preconditions.checkArgument(g < 256 && g >= 0);
        Preconditions.checkArgument(b < 256 && b >= 0);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public TextureProvider(DataGenerator generator, ExistingFileHelper existingFileHelper, String modId)
    {
        this.generator = generator;
        this.existingFileHelper = existingFileHelper;
        this.modId = modId;
        this.providedTextures = new HashMap<>();
        this.providedTextureMetas = new HashMap<>();
    }

    public static void adjustLevels(BufferedImage texture, int startX, int startY, int w, int h, double levelAdjustment)
    {
        adjustLevels(texture, startX, startY, w, h, levelAdjustment, 0, 255, 0, 255);
    }

    public static void adjustLevels(BufferedImage texture, double levelAdjustment)
    {
        adjustLevels(texture, texture.getMinTileX(), texture.getMinTileY(), texture.getTileWidth(),
                texture.getTileHeight(), levelAdjustment);
    }

    public static void adjustLevels(BufferedImage texture, int startX, int startY, int w, int h, double levelAdjustment,
            int inLow, int inHigh, int outLow, int outHigh)
    {
        Preconditions.checkArgument(levelAdjustment > 0);
        Preconditions.checkArgument(inLow >= 0);
        Preconditions.checkArgument(inHigh <= 255);
        Preconditions.checkArgument(inLow < inHigh);
        Preconditions.checkArgument(outLow >= 0);
        Preconditions.checkArgument(outHigh <= 255);
        Preconditions.checkArgument(outLow < outHigh);
        for (int x = startX; x < startX + w; x++)
        {
            for (int y = startY; y < startY + h; y++)
            {
                int color = texture.getRGB(x, y);
                int r = (color >> 16) & 0xFF, g = (color >> 8) & 0xFF, b = color & 0xFF;
                r = adjustLevel(r, levelAdjustment, inLow, inHigh, outLow, outHigh);
                g = adjustLevel(g, levelAdjustment, inLow, inHigh, outLow, outHigh);
                b = adjustLevel(b, levelAdjustment, inLow, inHigh, outLow, outHigh);
                color = (color & ALPHA_MASK) | (r << 16) | (g << 8) | b;
                texture.setRGB(x, y, color);
            }
        }
    }

    protected static int adjustLevel(int i, double levelAdjustment, int inLow, int inHigh, int outLow, int outHigh)
    {
        i = i - inLow;
        if (i < 0)
        {
            return outLow;
        } else if (i + inLow >= inHigh)
        {
            return outHigh;
        } else
        {
            return clampToByte(outLow + (outHigh - outLow) * Math.pow(i / (double) (inHigh - inLow), levelAdjustment));
        }
    }

    protected static int adjustLevel(int i, double levelAdjustment)
    {
        return adjustLevel(i, levelAdjustment, 0, 255, 0, 255);
    }

    public static void adjustLevels(BufferedImage texture, double levelAdjustment, int inLow, int inHigh, int outLow,
            int outHigh)
    {
        adjustLevels(texture, texture.getMinTileX(), texture.getMinTileY(), texture.getTileWidth(),
                texture.getTileHeight(), levelAdjustment, inLow, inHigh, outLow, outHigh);
    }

    public static void makeGrayscale(BufferedImage texture, int startX, int startY, int w, int h)
    {
        for (int x = startX; x < startX + w; x++)
        {
            for (int y = startY; y < startY + h; y++)
            {
                int color = texture.getRGB(x, y);
                int r = (color >> 16) & 0xFF, g = (color >> 8) & 0xFF, b = color & 0xFF;
                int gray = (int) Math.round(r * 0.299d + g * 0.587d + b * 0.114d);
                color = (color & ALPHA_MASK) | (gray << 16) | (gray << 8) | gray;
                texture.setRGB(x, y, color);
            }
        }
    }

    public static int clampToByte(double value)
    {
        return (int) Math.max(0, Math.min(255, value));
    }

    public static void makeGrayscale(BufferedImage texture)
    {
        makeGrayscale(texture, texture.getMinTileX(), texture.getMinTileY(), texture.getTileWidth(),
                texture.getTileHeight());
    }

    public static void adjustHSB(BufferedImage texture, int startX, int startY, int w, int h, int hueChange,
            int saturation, int brightnessChange)
    {
        float hueShift = hueChange / 360f;
        int saturationFactor = saturation * 1024 / 100;
        int bWeight = Math.abs(brightnessChange) * 255 / 100;
        int cWeight = 255 - bWeight;
        int weightedB = (brightnessChange > 0 ? 255 : 0) * bWeight;
        for (int x = startX; x < startX + w; x++)
        {
            for (int y = startY; y < startY + h; y++)
            {
                Color color = new Color(texture.getRGB(x, y), true);
                int intensity =
                        clampToByte((color.getRed() * 19595 + color.getGreen() * 38470 + color.getBlue() * 7471) >> 16);
                int r = adjustSaturation(color.getRed(), intensity, saturationFactor), g =
                        adjustSaturation(color.getGreen(), intensity, saturationFactor), b =
                        adjustSaturation(color.getBlue(), intensity, saturationFactor);
                float[] hsb = Color.RGBtoHSB(r, g, b, null);
                hsb[0] += hueShift;
                int rgb = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
                int argb = (color.getAlpha() << 24) | rgb;
                color = new Color(argb, true);
                r = adjustBrightness(color.getRed(), cWeight, weightedB);
                g = adjustBrightness(color.getGreen(), cWeight, weightedB);
                b = adjustBrightness(color.getBlue(), cWeight, weightedB);
                color = new Color(r, g, b, color.getAlpha());
                texture.setRGB(x, y, color.getRGB() | (color.getAlpha() << 24));
            }
        }
    }

    public static int clampToByte(int value)
    {
        return Math.max(0, Math.min(255, value));
    }

    public static void adjustHSB(BufferedImage texture, int hueChange, int saturation, int brightnessChange)
    {
        adjustHSB(texture, texture.getMinTileX(), texture.getMinTileY(), texture.getTileWidth(),
                texture.getTileHeight(), hueChange, saturation, brightnessChange);
    }

    protected static int adjustSaturation(int i, int intensity, int saturationFactor)
    {
        return clampToByte(intensity * 1024 + (i - intensity) * saturationFactor >> 10);
    }

    public static int[] color(int color, int length)
    {
        int[] array = new int[length];
        for (int i = 0; i < length; i++)
        {
            array[i] = color;
        }
        return array;
    }

    protected static int adjustBrightness(int i, int iWeight, int weightedB)
    {
        return (i * iWeight + weightedB) / 256;
    }

    @Override public void act(DirectoryCache cache)
    {
        addTextures(new FinishedTextureConsumer()
        {
            @Override void acceptFinishedTexture(BufferedImage texture, @Nullable TextureMetaBuilder metaBuilder,
                    ResourceLocation id)
            {
                BufferedImage immutableTexture = copyTexture(texture);
                if (providedTextures.put(id, immutableTexture) != null)
                {
                    throw new IllegalStateException("Duplicate texture " + id);
                } else if (metaBuilder != null &&
                        !(immutableTexture.getTileHeight() / immutableTexture.getTileWidth() > 1))
                {
                    throw new IllegalStateException("Texture " + id +
                            " must have a height that is a multiple of its width in order to have metadata");
                } else
                {
                    saveTexture(cache, immutableTexture, getTexturePath(id));
                    if (metaBuilder != null)
                    {
                        saveTextureMeta(cache, metaBuilder, getTextureMetaPath(id));
                    }
                }
            }
        });
    }

    @Override public String getName()
    {
        return "Textures: " + modId;
    }

    protected abstract void addTextures(FinishedTextureConsumer consumer);

    protected void saveTextureMeta(DirectoryCache cache, TextureMetaBuilder metaBuilder, Path path)
    {
        String json = GSON.toJson(metaBuilder.toJson());
        String hash = HASH_FUNCTION.hashUnencodedChars(json).toString();
        if (!Objects.equals(cache.getPreviousHash(path), hash) || !Files.exists(path))
        {
            try
            {
                Files.createDirectories(path.getParent());
            } catch (IOException ioException)
            {
                LOGGER.error("Couldn't create directory for texture {}", path, ioException);
            }
            try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path))
            {
                bufferedWriter.write(json);
            } catch (IOException ioException)
            {
                LOGGER.error("Couldn't save metadata for texture {}", path, ioException);
            }
            cache.recordHash(path, hash);
        }
    }

    protected Path getTexturePath(ResourceLocation texture)
    {
        return this.generator.getOutputFolder()
                .resolve("assets/" + texture.getNamespace() + "/textures/" + texture.getPath() + ".png");
    }

    protected void saveTexture(DirectoryCache cache, BufferedImage texture, Path path)
    {
        Hasher hasher = HASH_FUNCTION.newHasher();
        for (int i : texture.getRGB(0, 0, texture.getWidth(), texture.getHeight(), null, 0, texture.getWidth()))
        {
            hasher.putInt(i);
        }
        String hash = hasher.hash().toString();
        if (!Objects.equals(cache.getPreviousHash(path), hash) || !Files.exists(path))
        {
            try
            {
                Files.createDirectories(path.getParent());
            } catch (IOException ioException)
            {
                LOGGER.error("Couldn't create directory for texture {}", path, ioException);
            }
            try
            {
                ImageIO.write(texture, "png", path.toFile());
            } catch (IOException ioException)
            {
                LOGGER.error("Couldn't save texture {}", path, ioException);
            }
        }
        cache.recordHash(path, hash);
    }

    protected Path getTextureMetaPath(ResourceLocation texture)
    {
        return this.generator.getOutputFolder()
                .resolve("assets/" + texture.getNamespace() + "/textures/" + texture.getPath() + ".png.mcmeta");
    }

    public void finish(BufferedImage texture, ResourceLocation id, @Nullable TextureMetaBuilder metaBuilder,
            FinishedTextureConsumer consumer)
    {
        consumer.acceptFinishedTexture(texture, metaBuilder, id);
    }

    public static class FinishedTextureConsumer
    {
        void acceptFinishedTexture(BufferedImage texture, @Nullable TextureMetaBuilder metaBuilder, ResourceLocation id)
        {
            throw new NotImplementedException();
        }
    }

    @Nullable public TextureMetaBuilder getTextureMeta(ResourceLocation textureLoc)
    {
        if (providedTextureMetas.containsKey(textureLoc))
        {
            return TextureMetaBuilder.copy(providedTextureMetas.get(textureLoc));
        }
        ResourceLocation loc =
                new ResourceLocation(textureLoc.getNamespace(), "textures/" + textureLoc.getPath() + ".png.mcmeta");
        Preconditions.checkArgument(existingFileHelper.exists(loc, ResourcePackType.CLIENT_RESOURCES),
                "Texture metadata %s does not exist in any known resource pack", loc);
        try
        {
            IResource resource = existingFileHelper.getResource(loc, ResourcePackType.CLIENT_RESOURCES);
            return TextureMetaBuilder.fromInputStream(resource.getInputStream());
        } catch (IOException ioException)
        {
            LOGGER.error("Couldn't read texture {}", textureLoc, ioException);
            return null;
        }
    }

    @Nullable public BufferedImage getTexture(ResourceLocation textureLoc)
    {
        if (providedTextures.containsKey(textureLoc))
        {
            return copyTexture(providedTextures.get(textureLoc));
        }
        ResourceLocation loc =
                new ResourceLocation(textureLoc.getNamespace(), "textures/" + textureLoc.getPath() + ".png");
        Preconditions.checkArgument(existingFileHelper.exists(loc, ResourcePackType.CLIENT_RESOURCES),
                "Texture %s does not exist in any known resource pack", loc);
        try
        {
            IResource resource = existingFileHelper.getResource(loc, ResourcePackType.CLIENT_RESOURCES);
            return ImageIO.read(resource.getInputStream());
        } catch (IOException ioException)
        {
            LOGGER.error("Couldn't read texture {}", textureLoc, ioException);
            return null;
        }
    }

    public static BufferedImage copyTexture(BufferedImage texture)
    {
        ColorModel colorModel = texture.getColorModel();
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        WritableRaster raster = texture.copyData(texture.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
    }

    public void promiseGeneration(ResourceLocation texture)
    {
        this.existingFileHelper.trackGenerated(texture, ResourcePackType.CLIENT_RESOURCES, ".png", "textures");
    }

    public static class TextureMetaBuilder
    {
        protected Optional<Boolean> interpolate = Optional.empty();
        protected Optional<Integer> frameTime = Optional.empty();
        protected Optional<int[]> frames = Optional.empty();

        public static TextureMetaBuilder copy(TextureMetaBuilder original)
        {
            TextureMetaBuilder metaBuilder = new TextureMetaBuilder();
            metaBuilder.interpolate = original.interpolate;
            metaBuilder.frameTime = original.frameTime;
            metaBuilder.frames = original.frames;
            return metaBuilder;
        }

        @Nullable public static TextureMetaBuilder fromInputStream(InputStream inputStream)
        {
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            JsonObject json =
                    JSONUtils.getJsonObject(JSONUtils.fromJson(GSON, bufferedReader, JsonElement.class), "top element");
            if (json.has("animation"))
            {
                JsonObject animationJson = json.getAsJsonObject("animation");
                TextureMetaBuilder metaBuilder = new TextureMetaBuilder();
                if (animationJson.has("interpolate"))
                {
                    metaBuilder.interpolate = Optional.of(animationJson.get("interpolate").getAsBoolean());
                }
                if (animationJson.has("frametime"))
                {
                    metaBuilder.frameTime = Optional.of(animationJson.get("frametime").getAsInt());
                }
                if (animationJson.has("frames"))
                {
                    JsonArray framesJson = animationJson.getAsJsonArray("frames");
                    int[] framesArray = new int[framesJson.size()];
                    int i = 0;
                    for (JsonElement element : framesJson)
                    {
                        framesArray[i++] = element.getAsInt();
                    }
                    metaBuilder.frames = Optional.of(framesArray);
                }
                return metaBuilder;
            } else
            {
                return null;
            }
        }

        public TextureMetaBuilder setInterpolate(boolean interpolate)
        {
            this.interpolate = Optional.of(interpolate);
            return this;
        }

        public TextureMetaBuilder setFrameTime(int frameTime)
        {
            this.frameTime = Optional.of(frameTime);
            return this;
        }

        public TextureMetaBuilder setFrames(int[] frames)
        {
            this.frames = Optional.of(frames);
            return this;
        }

        public JsonObject toJson()
        {
            JsonObject json = new JsonObject();
            JsonObject animation = new JsonObject();
            json.add("animation", animation);
            interpolate.ifPresent(interpolate -> animation.addProperty("interpolate", interpolate));
            frameTime.ifPresent(frameTime -> animation.addProperty("frametime", frameTime));
            frames.ifPresent((frames) ->
            {
                JsonArray framesJson = new JsonArray();
                for (int frame : frames)
                {
                    framesJson.add(frame);
                }
                animation.add("frames", framesJson);
            });
            return json;
        }
    }
}

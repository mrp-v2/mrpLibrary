package mrp_v2.mrplibrary.datagen.providers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mrp_v2.mrplibrary.util.IModLocProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public abstract class ParticleProvider implements DataProvider, IModLocProvider
{
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger LOGGER = LogManager.getLogger();
    public final String modId;
    private final DataGenerator generator;

    protected ParticleProvider(DataGenerator generator, String modId)
    {
        this.generator = generator;
        this.modId = modId;
    }

    public static ParticleBuilder makeBuilder(ResourceLocation location)
    {
        return new ParticleBuilder(location);
    }

    @Override public String getModId()
    {
        return modId;
    }

    @Override
    public void run(HashCache cache) throws IOException
    {
        Path path = generator.getOutputFolder();
        Set<ResourceLocation> locationSet = new HashSet<>();
        registerParticles((particleBuilder ->
        {
            if (!locationSet.add(particleBuilder.getLocation()))
            {
                throw new IllegalStateException("Duplicate recipe " + particleBuilder.getLocation());
            } else
            {
                saveParticle(cache, particleBuilder.toJson(), path.resolve(
                        "assets/" + particleBuilder.getLocation().getNamespace() + "/particles/" +
                                particleBuilder.getLocation().getPath() + ".json"));
            }
        }));
    }

    protected abstract void registerParticles(Consumer<ParticleBuilder> consumer);

    @Override public String getName()
    {
        return "Particles: " + modId;
    }

    private void saveParticle(HashCache cache, JsonObject particle, Path path)
    {
        try
        {
            String json = GSON.toJson(particle);
            String hash = SHA1.hashUnencodedChars(json).toString();
            if (!Objects.equals(cache.getHash(path), hash) || !Files.exists(path))
            {
                Files.createDirectories(path.getParent());
                try (BufferedWriter writer = Files.newBufferedWriter(path))
                {
                    writer.write(json);
                }
            }
            cache.putNew(path, hash);
        } catch (IOException ioException)
        {
            LOGGER.error("Couldn't save particle {}", path, ioException);
        }
    }

    public static class ParticleBuilder
    {
        private final ResourceLocation location;
        private final Set<ResourceLocation> textures;

        private ParticleBuilder(ResourceLocation location)
        {
            this.location = location;
            this.textures = new HashSet<>();
        }

        public ResourceLocation getLocation()
        {
            return location;
        }

        public ParticleBuilder addTextures(ResourceLocation... textures)
        {
            for (ResourceLocation texture : textures)
            {
                addTexture(texture);
            }
            return this;
        }

        public ParticleBuilder addTexture(ResourceLocation texture)
        {
            textures.add(texture);
            return this;
        }

        public JsonObject toJson()
        {
            JsonObject json = new JsonObject();
            if (textures.size() > 0)
            {
                JsonArray array = new JsonArray();
                for (ResourceLocation texture : textures)
                {
                    array.add(texture.toString());
                }
                json.add("textures", array);
            }
            return json;
        }
    }
}

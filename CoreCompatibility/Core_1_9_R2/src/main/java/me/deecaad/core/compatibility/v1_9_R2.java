package me.deecaad.core.compatibility;

import me.deecaad.core.compatibility.block.BlockCompatibility;
import me.deecaad.core.compatibility.block.Block_1_9_R2;
import me.deecaad.core.compatibility.entity.EntityCompatibility;
import me.deecaad.core.compatibility.entity.Entity_1_9_R2;
import me.deecaad.core.compatibility.nbt.NBTCompatibility;
import me.deecaad.core.compatibility.nbt.NBT_1_9_R2;
import me.deecaad.core.utils.LogLevel;
import me.deecaad.core.utils.ReflectionUtil;
import net.minecraft.server.v1_9_R2.EntityPlayer;
import net.minecraft.server.v1_9_R2.Packet;
import net.minecraft.server.v1_9_R2.PlayerConnection;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class v1_9_R2 implements ICompatibility {

    static {
        if (ReflectionUtil.getMCVersion() != 9) {
            me.deecaad.core.MechanicsCore.debug.log(
                    LogLevel.ERROR,
                    "Loaded " + v1_9_R2.class + " when not using Minecraft 9",
                    new InternalError()
            );
        }
    }

    private final EntityCompatibility entityCompatibility;
    private final BlockCompatibility blockCompatibility;
    private final NBTCompatibility nbtCompatibility;

    public v1_9_R2() {
        entityCompatibility = new Entity_1_9_R2();
        blockCompatibility = new Block_1_9_R2();
        nbtCompatibility = new NBT_1_9_R2();
    }

    @Override
    public int getPing(Player player) {
        return getEntityPlayer(player).ping;
    }

    @Override
    public Entity getEntityById(World world, int entityId) {
        net.minecraft.server.v1_9_R2.Entity e = ((CraftWorld) world).getHandle().getEntity(entityId);
        return e == null ? null : e.getBukkitEntity();
    }

    @Override
    public void sendPackets(Player player, Object packet) {
        getEntityPlayer(player).playerConnection.sendPacket((Packet<?>) packet);
    }

    @Override
    public void sendPackets(Player player, Object... packets) {
        PlayerConnection playerConnection = getEntityPlayer(player).playerConnection;
        for (Object packet : packets) {
            playerConnection.sendPacket((Packet<?>) packet);
        }
    }

    @Override
    public NBTCompatibility getNBTCompatibility() {
        return nbtCompatibility;
    }

    @Nonnull
    @Override
    public EntityCompatibility getEntityCompatibility() {
        return entityCompatibility;
    }

    @Nonnull
    @Override
    public BlockCompatibility getBlockCompatibility() {
        return blockCompatibility;
    }

    @Override
    public EntityPlayer getEntityPlayer(Player player) {
        return ((CraftPlayer) player).getHandle();
    }
}
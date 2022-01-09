package me.deecaad.weaponmechanics.compatibility.shoot;

import me.deecaad.core.utils.LogLevel;
import me.deecaad.core.utils.ReflectionUtil;
import me.deecaad.weaponmechanics.WeaponMechanics;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.world.damagesource.DamageSource;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Shoot_1_17_R1 implements IShootCompatibility {

    static {
        if (ReflectionUtil.getMCVersion() != 17) {
            WeaponMechanics.debug.log(
                    LogLevel.ERROR,
                    "Loaded " + Shoot_1_17_R1.class + " when not using Minecraft 17",
                    new InternalError()
            );
        }
    }

    private final Set<ClientboundPlayerPositionPacket.RelativeArgument> RELATIVE_FLAGS = new HashSet<>(Arrays.asList(
            ClientboundPlayerPositionPacket.RelativeArgument.X,
            ClientboundPlayerPositionPacket.RelativeArgument.Y,
            ClientboundPlayerPositionPacket.RelativeArgument.Z,
            ClientboundPlayerPositionPacket.RelativeArgument.X_ROT,
            ClientboundPlayerPositionPacket.RelativeArgument.Y_ROT));

    private final Set<ClientboundPlayerPositionPacket.RelativeArgument> ABSOLUTE_FLAGS = new HashSet<>(Arrays.asList(
            ClientboundPlayerPositionPacket.RelativeArgument.X,
            ClientboundPlayerPositionPacket.RelativeArgument.Y,
            ClientboundPlayerPositionPacket.RelativeArgument.Z));

    @Override
    public void modifyCameraRotation(Player player, float yaw, float pitch, boolean absolute) {
        pitch *= -1;
        ((CraftPlayer) player).getHandle().connection.send(new ClientboundPlayerPositionPacket(0, 0, 0, yaw, pitch, absolute ? ABSOLUTE_FLAGS : RELATIVE_FLAGS, 0, true));
    }

    @Override
    public void logDamage(org.bukkit.entity.LivingEntity victim, org.bukkit.entity.LivingEntity source, double health, double damage, boolean isMelee) {
        DamageSource damageSource;

        if (isMelee) {
            if (source instanceof Player) {
                damageSource = DamageSource.playerAttack(((org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer) source).getHandle());
            } else {
                damageSource = DamageSource.mobAttack(((CraftLivingEntity) source).getHandle());
            }
        } else {
            damageSource = DamageSource.thrown(null, ((CraftLivingEntity) source).getHandle());
        }

        net.minecraft.world.entity.LivingEntity nms = ((CraftLivingEntity) victim).getHandle();
        nms.combatTracker.recordDamage(damageSource, (float) damage, (float) health);
    }

    @Override
    public void setKiller(org.bukkit.entity.LivingEntity victim, Player killer) {
        ((CraftLivingEntity) victim).getHandle().lastHurtByMob = ((CraftPlayer) killer).getHandle();
    }
}
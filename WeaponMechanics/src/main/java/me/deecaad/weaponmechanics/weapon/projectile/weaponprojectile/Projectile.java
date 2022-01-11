package me.deecaad.weaponmechanics.weapon.projectile.weaponprojectile;

import me.deecaad.core.compatibility.CompatibilityAPI;
import me.deecaad.core.compatibility.entity.BitMutator;
import me.deecaad.core.compatibility.entity.EntityMetaFlag;
import me.deecaad.core.compatibility.entity.FakeEntity;
import me.deecaad.core.file.Serializer;
import me.deecaad.weaponmechanics.WeaponMechanics;
import me.deecaad.weaponmechanics.mechanics.CastData;
import me.deecaad.weaponmechanics.mechanics.Mechanics;
import me.deecaad.weaponmechanics.weapon.explode.Explosion;
import me.deecaad.weaponmechanics.weapon.explode.ExplosionTrigger;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.io.File;

import static me.deecaad.weaponmechanics.WeaponMechanics.getConfigurations;

public class Projectile implements Serializer<Projectile> {

    private ProjectileSettings projectileSettings;
    private Sticky sticky;
    private Through through;
    private Bouncy bouncy;
    private Mechanics mechanics;

    /**
     * Empty constructor to be used as serializer
     */
    public Projectile() { }

    public Projectile(ProjectileSettings projectileSettings, Sticky sticky, Through through, Bouncy bouncy, Mechanics mechanics) {
        this.projectileSettings = projectileSettings;
        this.sticky = sticky;
        this.through = through;
        this.bouncy = bouncy;
        this.mechanics = mechanics;
    }

    /**
     * Shoots this projectile with given location and motion
     *
     * @param shooter the living entity used to shoot
     * @param location the location from where to shoot
     * @param motion the motion of projectile
     * @param weaponStack the weapon stack used to shoot
     * @param weaponTitle the weapon title used to shoot
     */
    public WeaponProjectile shoot(LivingEntity shooter, Location location, Vector motion, ItemStack weaponStack, String weaponTitle) {
        return shoot(create(shooter, location, motion, weaponStack, weaponTitle), location);
    }

    /**
     * Shoots created projectile object
     *
     * @param projectile the created projectile object
     * @param location the location containing pitch and yaw
     */
    public WeaponProjectile shoot(WeaponProjectile projectile, Location location) {

        WeaponMechanics.getProjectilesRunnable().addProjectile(projectile);
        if (mechanics != null) mechanics.use(new CastData(projectile));
        EntityType type = projectileSettings.getProjectileDisguise();
        if (type != null) {

            FakeEntity fakeEntity;
            Object data = projectileSettings.getDisguiseData();
            if (type == EntityType.ARMOR_STAND && data != null) {
                // Armor stand height * eye height multiplier
                // 1.975 * 0.85 = 1.67875
                Location offset = new Location(location.getWorld(), 0, -1.67875, 0);

                // Add the first offset before actually spawning
                location.add(offset);

                fakeEntity = CompatibilityAPI.getEntityCompatibility().generateFakeEntity(location, type, data);

                fakeEntity.setEquipment(EquipmentSlot.HEAD, (ItemStack) data);
                fakeEntity.getMeta().setFlag(EntityMetaFlag.INVISIBLE, BitMutator.TRUE);

                // Set the offset for new packets
                fakeEntity.setOffset(offset);
            } else {
                fakeEntity = CompatibilityAPI.getEntityCompatibility().generateFakeEntity(location, type, data);
            }

            projectile.spawnDisguise(fakeEntity);
        }

        // Handle explosions
        Explosion explosion = getConfigurations().getObject(projectile.getWeaponTitle() + ".Explosion", Explosion.class);
        if (explosion != null) explosion.handleExplosion(projectile.getShooter(), projectile, ExplosionTrigger.SPAWN);

        return projectile;
    }

    /**
     * Creates this projectile with given location and motion without shooting it
     *
     * @param shooter the living entity used to shoot
     * @param location the location from where to shoot
     * @param motion the motion of projectile
     * @param weaponStack the weapon stack used to shoot
     * @param weaponTitle the weapon title used to shoot
     */
    public WeaponProjectile create(LivingEntity shooter, Location location, Vector motion, ItemStack weaponStack, String weaponTitle) {
        return new WeaponProjectile(projectileSettings, shooter, location, motion, weaponStack, weaponTitle, sticky, through, bouncy);
    }

    @Override
    public String getKeyword() {
        return "Projectile";
    }

    @Override
    public Projectile serialize(File file, ConfigurationSection configurationSection, String path) {
        ProjectileSettings projectileSettings = new ProjectileSettings().serialize(file, configurationSection, path + ".Projectile_Settings");
        if (projectileSettings == null) return null;

        Sticky sticky = new Sticky().serialize(file, configurationSection, path + ".Sticky");
        Through through = new Through().serialize(file, configurationSection, path + ".Through");
        Bouncy bouncy = new Bouncy().serialize(file, configurationSection, path + ".Bouncy");
        Mechanics mechanics = new Mechanics().serialize(file, configurationSection, path + ".Mechanics");
        return new Projectile(projectileSettings, sticky, through, bouncy, mechanics);
    }
}
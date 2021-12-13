package me.deecaad.weaponmechanics.wrappers;

import org.bukkit.entity.LivingEntity;

import javax.annotation.Nonnull;

/**
 * This class wraps an Entity. Contains
 * basic information for this plugin for
 * each LivingEntity
 *
 * @since 1.0
 */
public interface IEntityWrapper {

    /**
     * @return the living entity held by entity wrapper
     */
    LivingEntity getEntity();

    /**
     * @return the move task bukkit runnable task id
     */
    int getMoveTask();

    /**
     * @return true if entity is standing
     */
    boolean isStanding();

    /**
     * Automatically sets walking to false and calls events
     *
     * @param standing whether or not to stand
     */
    void setStanding(boolean standing);

    /**
     * @return true if entity is walking
     */
    boolean isWalking();

    /**
     * Automatically sets standing to false and calls events
     *
     * @param walking whether or not to walk
     */
    void setWalking(boolean walking);

    /**
     * Automatically calls event
     *
     * @return true if entity is in midair
     */
    boolean isInMidair();

    /**
     * Automatically calls event
     *
     * @param inMidair whether or not to be in midair
     */
    void setInMidair(boolean inMidair);

    /**
     * @return true if entity is swimming
     */
    boolean isSwimming();

    /**
     * @param swimming whether or not to swim
     */
    void setSwimming(boolean swimming);

    /**
     * @return true if entity is sneaking
     */
    boolean isSneaking();

    /**
     * @return true if entity is sprinting
     */
    boolean isSprinting();

    /**
     * @return true if entity is gliding
     */
    boolean isGliding();

    /**
     * This is only accurate for swords(1.8) and shields(1.9 and above).
     *
     * Otherwise the right click detection is determined by time between last right click
     * and time when this method is called.
     *
     * Right click event is only called every 195-215 millis
     * so this isn't fully accurate. Basically that means that right click
     * detection is about 4 ticks accurate.
     *
     * This also takes player's ping in account. If ping is more than 215 then
     * millis passed check is done with (player ping + 15). Otherwise millis
     * passed check is done with 215 millis.
     *
     * @return true if player may be right clicking
     */
    boolean isRightClicking();

    /**
     * This holds all data about main hand. Things like is using full auto, last shot time, etc.
     *
     * @return the main hand data
     */
    @Nonnull
    HandData getMainHandData();

    /**
     * This holds all data about off hand. Things like is using full auto, last shot time, etc.
     *
     * @return the off hand data
     */
    @Nonnull
    HandData getOffHandData();
}
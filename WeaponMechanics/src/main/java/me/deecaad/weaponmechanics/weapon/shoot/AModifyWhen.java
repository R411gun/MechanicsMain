package me.deecaad.weaponmechanics.weapon.shoot;

import me.deecaad.core.file.Serializer;
import me.deecaad.weaponmechanics.wrappers.EntityWrapper;

public abstract class AModifyWhen implements Serializer<AModifyWhen> {

    private NumberModifier always;
    private NumberModifier zooming;
    private NumberModifier sneaking;
    private NumberModifier standing;
    private NumberModifier walking;
    private NumberModifier swimming;
    private NumberModifier inMidair;
    private NumberModifier gliding;
    private NumberModifier dualWielding;

    public AModifyWhen() { }

    public AModifyWhen(NumberModifier always, NumberModifier zooming, NumberModifier sneaking,
                       NumberModifier standing, NumberModifier walking, NumberModifier swimming,
                       NumberModifier inMidair, NumberModifier gliding, NumberModifier dualWielding) {
        this.always = always;
        this.zooming = zooming;
        this.sneaking = sneaking;
        this.standing = standing;
        this.walking = walking;
        this.swimming = swimming;
        this.inMidair = inMidair;
        this.gliding = gliding;
        this.dualWielding = dualWielding;
    }

    /**
     * Applies all changes from this modifier to given number
     *
     * @param entityWrapper the entity wrapper used to check circumstances
     * @param tempNumber the number
     * @return the modified number
     */
    public double applyChanges(EntityWrapper entityWrapper, double tempNumber) {
        if (always != null) {
            tempNumber = always.applyTo(tempNumber);
        }
        if (zooming != null && (entityWrapper.getMainHandData().getZoomData().isZooming() || entityWrapper.getOffHandData().getZoomData().isZooming())) {
            tempNumber = zooming.applyTo(tempNumber);
        }
        if (sneaking != null && entityWrapper.isSneaking()) {
            tempNumber = sneaking.applyTo(tempNumber);
        }
        if (standing != null && entityWrapper.isStanding()) {
            tempNumber = standing.applyTo(tempNumber);
        }
        if (walking != null && entityWrapper.isWalking()) {
            tempNumber = walking.applyTo(tempNumber);
        }
        if (swimming != null && entityWrapper.isSwimming()) {
            tempNumber = swimming.applyTo(tempNumber);
        }
        if (inMidair != null && entityWrapper.isInMidair()) {
            tempNumber = inMidair.applyTo(tempNumber);
        }
        if (gliding != null && entityWrapper.isGliding()) {
            tempNumber = gliding.applyTo(tempNumber);
        }
        if (dualWielding != null && entityWrapper.isDualWielding()) {
            tempNumber = dualWielding.applyTo(tempNumber);
        }

        return Math.max(tempNumber, 0.0);
    }
}
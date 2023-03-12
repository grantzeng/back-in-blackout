package helpers;

import unsw.utils.Angle;

public class AngleNormaliser {
    /**
     * Given an angle in radians, normalises it to be between [0, 2π)
     *
     * @param radians
     * @return
     */
    public final Angle normalise(Angle radians) {
        // https://stackoverflow.com/questions/1628386/normalise-orientation-between-0-and-360
        double angle = radians.toDegrees();
        return Angle.fromDegrees(angle + Math.ceil(-angle / 360) * 360);
    }

}

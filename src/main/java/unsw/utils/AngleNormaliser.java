package unsw.utils;

public class AngleNormaliser {
    /**
     * Given an angle in radians, normalises it to be between [0, 2π)
     * 
     * @param radians
     * @return
     */
    public final static Angle normalise(Angle radians) {
        // https://stackoverflow.com/questions/24234609/standard-way-to-normalize-an-angle-to-%CF%80-radians-in-java
        return Angle.fromRadians(Math.atan2(Math.sin(radians.toRadians()), Math.cos(radians.toRadians())))
                .add(Angle.fromDegrees(360.0));
    }

}

package blackout;

import unsw.blackout.BlackoutController;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

public class Sandbox {

    public static void main(String[] args) {
        BlackoutController bc = new BlackoutController();
    
        for (int i = 0; i < 18; i++) {
            // bc.createSatellite("r" + i, "RelaySatellite", RADIUS_OF_JUPITER + 3000,
            // Angle.fromDegrees(i));
            bc.createSatellite("r" + i, "RelaySatellite", RADIUS_OF_JUPITER + 5000, Angle.fromDegrees(i * 20));
        }

        bc.createDevice("d1", "LaptopDevice", Angle.fromDegrees(0));
        bc.createSatellite("s1", "StandardSatellite", RADIUS_OF_JUPITER + 3000, Angle.fromDegrees(180));
        String z = "z".repeat(80);
        bc.addFileToDevice("d1", "z", z);
        
        assertDoesNotThrow(() -> bc.sendFile("z", "d1", "s1"));
        assertEquals(new FileInfoResponse("z", "", 80, false), bc.getInfo("s1").getFiles().get("z"));
        bc.simulate();
        assertEquals(new FileInfoResponse("z", "z", 80, false), bc.getInfo("s1").getFiles().get("z"));

        for (int i = 0; i < 80; i++) {
            assertEquals(new FileInfoResponse("z", "z".repeat(i), z.length(), false),
                    bc.getInfo("s1").getFiles().get("z"));
            bc.simulate();
        }
        bc.simulate(90);

        assertEquals(new FileInfoResponse("z", z, z.length(), true), bc.getInfo("s1").getFiles().get("z"));
    
    }
    
}

package blackout;

import unsw.blackout.BlackoutController;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

import java.util.Arrays;
import java.util.List;



import static blackout.TestHelpers.assertListAreEqualIgnoringOrder;


public class Sandbox {
    public static void main(String[] args) {
          // Example from the specification
          BlackoutController controller = new BlackoutController();

          // Creates 1 satellite and 2 devices
          // Gets a device to send a file to a satellites and gets another device to download it.
          // StandardSatellites are slow and transfer 1 byte per minute.
          controller.createSatellite("Satellite1", "StandardSatellite", 1000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
          controller.createSatellite("Satellite2", "StandardSatellite", 1000 + RADIUS_OF_JUPITER, Angle.fromDegrees(315));
          controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
          controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));
          controller.createDevice("DeviceD", "HandheldDevice", Angle.fromDegrees(180));
          controller.createSatellite("Satellite3", "StandardSatellite", 2000 + RADIUS_OF_JUPITER, Angle.fromDegrees(175));
  
          assertListAreEqualIgnoringOrder(Arrays.asList("DeviceC", "Satellite2"),
                  controller.communicableEntitiesInRange("Satellite1"));
          assertListAreEqualIgnoringOrder(Arrays.asList("DeviceB", "DeviceC", "Satellite1"),
                  controller.communicableEntitiesInRange("Satellite2"));
          assertListAreEqualIgnoringOrder(Arrays.asList("Satellite2"), controller.communicableEntitiesInRange("DeviceB"));
  
          assertListAreEqualIgnoringOrder(Arrays.asList("DeviceD"), controller.communicableEntitiesInRange("Satellite3"));
    
    
        // Test communicability updates properly
        // - it doesn't.

        // Check visibility graph is updated correctly
        // - visibility gprah updates correctly. 
        
        // Check communicability graph is updated correctly
        // - communicability is not updating properly (basically just list everything found in a bfs)
        // - seems to work properly now?
        /* 
        BlackoutController bc = new BlackoutController();
        bc.createDevice("d1", "LaptopDevice", Angle.fromDegrees(0));
        bc.createSatellite("s1", "StandardSatellite", RADIUS_OF_JUPITER + 3000, Angle.fromDegrees(180));

        System.out.println(bc.communicableEntitiesInRange("s1"));
        System.out.println(bc.communicableEntitiesInRange("d1"));

        for (int i = 0; i < 7; i++) {
            bc.createSatellite("r" + i, "RelaySatellite", RADIUS_OF_JUPITER + 3000, Angle.fromDegrees(i * 30));
        }

        System.out.println(bc.communicableEntitiesInRange("s1"));
        System.out.println(bc.communicableEntitiesInRange("d1"));
        */
        /*
         * String z = "z".repeat(80); bc.addFileToDevice("d1", "z", z);
         * 
         * assertDoesNotThrow(() -> bc.sendFile("z", "d1", "s1")); assertEquals(new
         * FileInfoResponse("z", "", 80, false), bc.getInfo("s1").getFiles().get("z"));
         * bc.simulate(); assertEquals(new FileInfoResponse("z", "z", 80, false),
         * bc.getInfo("s1").getFiles().get("z"));
         * 
         * for (int i = 0; i < 80; i++) { assertEquals(new FileInfoResponse("z",
         * "z".repeat(i), z.length(), false), bc.getInfo("s1").getFiles().get("z"));
         * bc.simulate(); } bc.simulate(90);
         * 
         * assertEquals(new FileInfoResponse("z", z, z.length(), true),
         * bc.getInfo("s1").getFiles().get("z"));
         */
    }

   

}

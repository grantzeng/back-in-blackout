package blackout;

import unsw.blackout.BlackoutController;
import unsw.blackout.FileTransferException;
import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

import org.junit.jupiter.api.BeforeEach;

import static blackout.TestHelpers.assertListAreEqualIgnoringOrder;

public class TestTask2Transmission {
    public static void main(String[] args) {
        BlackoutController controller = new BlackoutController();
        controller.createSatellite("Satellite1", "StandardSatellite", 5000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

        String msg = "Hey";
        controller.addFileToDevice("DeviceC", "FileAlpha", msg);
        assertThrows(FileTransferException.VirtualFileNotFoundException.class,
                () -> controller.sendFile("NonExistentFile", "DeviceC", "Satellite1"));

        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));

        controller.simulate(msg.length() * 2);
        
        assertThrows(FileTransferException.VirtualFileAlreadyExistsException.class,
                () -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
        
        /* 
        BlackoutController bc = new BlackoutController();
        bc.createSatellite("s1", "StandardSatellite", 100, Angle.fromDegrees(0));
        bc.createDevice("d1", "HandheldDevice", Angle.fromDegrees(0));
        bc.addFileToDevice("d1", "asdf", "asdf");
        assertDoesNotThrow(() -> bc.sendFile("asdf", "d1", "s1"));
        */
        /*
         * System.out.println(); System.out.println(bc.getInfo("d1"));
         * System.out.println(); System.out.println();
         * System.out.println(bc.getInfo("s1")); System.out.println();
         */
         
         /*
        bc.simulate();
        bc.simulate();
        bc.simulate();
        bc.simulate();
        bc.simulate();
        bc.simulate();
        //System.out.println(bc.getInfo("s1"));
        */
    }

}

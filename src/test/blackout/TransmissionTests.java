package blackout;

import unsw.blackout.BlackoutController;
import static unsw.blackout.FileTransferException.VirtualFileAlreadyExistsException;
import static unsw.blackout.FileTransferException.VirtualFileNotFoundException;
import static unsw.blackout.FileTransferException.VirtualFileNoBandwidthException;
import static unsw.blackout.FileTransferException.VirtualFileNoStorageSpaceException;
import unsw.response.models.FileInfoResponse;
import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;

import static blackout.TestHelpers.assertListAreEqualIgnoringOrder;

public class TransmissionTests {
    BlackoutController bc;

    @BeforeEach
    public void setup() {
        bc = new BlackoutController();

    }

    /*
     * Testing transmission registering
     */

    // Test exceptions file not found works if no file or file is partial download
    @Test
    public void testFileNotExistOrOnlyPartialThrows() {

        bc.createSatellite("s1", "StandardSatellite", RADIUS_OF_JUPITER + 3000, Angle.fromDegrees(45));
        bc.createDevice("d1", "HandheldDevice", Angle.fromDegrees(45));

        // File does not exist on d1, cannot send to s1
        assertThrows(VirtualFileNotFoundException.class, () -> bc.sendFile("hello", "d1", "s1"));

        bc.addFileToDevice("d1", "hello", "hello");
        assertDoesNotThrow(() -> bc.sendFile("hello", "d1", "s1"));
        bc.simulate(2);

        // s1 only has partial file, cannot transfer to s2
        bc.createSatellite("s2", "TeleportingSatellite", RADIUS_OF_JUPITER, Angle.fromDegrees(47));
        assertThrows(VirtualFileNotFoundException.class, () -> bc.sendFile("hello", "s1", "s2"));

        // File should be complete so can transfer from s1 to s2
        bc.simulate(5);
        assertDoesNotThrow(() -> bc.sendFile("hello", "s1", " s2"));
    }

    // Test file already exists on target or is currently downloading
    @Test
    public void testFileAlreadyExistsOnTargetThrows() {

        bc.createSatellite("s1", "StandardSatellite", RADIUS_OF_JUPITER + 3000, Angle.fromDegrees(45));
        bc.createDevice("d1", "HandheldDevice", Angle.fromDegrees(45));

        bc.addFileToDevice("d1", "hi", "foo");

        // file does not exist on s1, should not throw
        assertDoesNotThrow(() -> bc.sendFile("hi", "d1", "s1"));
        bc.simulate();

        // s1 already downloading from d1, should throw
        assertThrows(VirtualFileAlreadyExistsException.class, () -> bc.sendFile("hi", "d1", "s1"));

        bc.simulate(4);

        // s1 has complete file, should throw
        assertThrows(VirtualFileAlreadyExistsException.class, () -> bc.sendFile("hi", "d1", "s1"));
    }

    @Test
    public void testUploadingBandwidthFullThrows() {

        bc.createSatellite("s1", "StandardSatellite", RADIUS_OF_JUPITER + 3000, Angle.fromDegrees(45));
        bc.createDevice("d1", "HandheldDevice", Angle.fromDegrees(45));

        bc.addFileToDevice("d1", "hi", "hello");
        assertDoesNotThrow(() -> bc.sendFile("hi", "d1", "d2"));

        bc.createSatellite("s2", "StandardSatellite", RADIUS_OF_JUPITER + 3000, bc.getInfo("s1").getPosition());
        bc.createSatellite("s3", "StandardSatellite", RADIUS_OF_JUPITER + 3000, bc.getInfo("s1").getPosition());
        bc.simulate(5);

        assertDoesNotThrow(() -> bc.sendFile("h1", "s1", "s2"));
        assertThrows(VirtualFileNoBandwidthException.class, () -> bc.sendFile("hi", "s1", "s3"));
    }

    @Test
    public void testUploadingBandwidthFullStandardSatelliteThrows() {
        bc.createDevice("d2", "DesktopDevice", Angle.fromDegrees(0));
        bc.createSatellite("t1", "TeleportingSatellite", RADIUS_OF_JUPITER + 5000, Angle.fromDegrees(0));
        bc.addFileToDevice("d2", "hey", "hey");
        assertDoesNotThrow(() -> bc.sendFile("hey", "d2", "t1"));
        bc.simulate(4);

        List<Integer> range = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        for (Integer i : range) {
            bc.createSatellite("c" + i, "StandardSatellite", bc.getInfo("t1").getHeight(),
                    bc.getInfo("ti").getPosition());
        }
        range.remove(10);

        for (Integer i : range) {
            assertDoesNotThrow(() -> bc.sendFile("hey", "t1", "c" + i));
        }
        // Should throw since teleporting satellite has upload bandwidth of 10 and we
        // are already sending 10 files.
        assertThrows(VirtualFileNoBandwidthException.class, () -> bc.sendFile("hey", "t1", "c10"));
    }

    // Test storage cap reached

    // Test multiple exception throwing
    // TODO

    /*
     * Testing transmission occurs properly
     */

    // Test transmission works through relays
    @Test
    public void testTransmissionWorksThroughRelays() {
        for (int i = 0; i < 360; i++) {
            // bc.createSatellite("r" + i, "RelaySatellite", RADIUS_OF_JUPITER + 3000,
            // Angle.fromDegrees(i));
            bc.createSatellite("r" + i, "RelaySatellite", RADIUS_OF_JUPITER + 5000, Angle.fromDegrees(i));
        }

        bc.createDevice("d1", "LaptopDevice", Angle.fromDegrees(0));
        bc.createSatellite("s1", "StandardSatellite", RADIUS_OF_JUPITER + 3000, Angle.fromDegrees(180));
        String z = "z".repeat(80);
        bc.addFileToDevice("d1", "z", z);
        assertDoesNotThrow(() -> bc.sendFile("z", "d1", "s1"));

        for (int i = 0; i < 80; i++) {
            assertEquals(new FileInfoResponse("z", "z".repeat(i), z.length(), false),
                    bc.getInfo("s1").getFiles().get("z"));
            bc.simulate();
        }
        bc.simulate(90);

        assertEquals(new FileInfoResponse("z", z, z.length(), true), bc.getInfo("s1").getFiles().get("z"));
    }

    // Test upload bandwidth is divided evenly
    @Test
    public void testDownloadBandwidthChanges() {
        String z = "z".repeat(20);
        String a = "a".repeat(20);

        bc.createDevice("d1", "HandheldDevice", Angle.fromDegrees(0));

        for (int i = 0; i < 3; i++) {
            bc.createSatellite("t" + i, "TeleportingSatellite", RADIUS_OF_JUPITER + 100000000, Angle.fromDegrees(0));
        }

        bc.addFileToDevice("d1", "z", z);
        bc.addFileToDevice("d1", "a", a);
        assertDoesNotThrow(() -> bc.sendFile("z", "d1", "t0"));
        assertDoesNotThrow(() -> bc.sendFile("a", "d1", "t0"));
        bc.simulate(z.length() + 1);
        assertEquals(new FileInfoResponse("z", z, z.length(), true), bc.getInfo("t0").getFiles().get("z"));

        assertDoesNotThrow(() -> bc.sendFile("z", "t0", "t1"));
        assertDoesNotThrow(() -> bc.sendFile("a", "t0", "t2"));
        bc.simulate();
        assertEquals(new FileInfoResponse("z", "z".repeat(5), 20, false), bc.getInfo("t1").getFiles().get("z"));
        assertEquals(new FileInfoResponse("a", "a".repeat(5), 20, false), bc.getInfo("t2").getFiles().get("a"));
        bc.removeSatellite("t2");
        bc.simulate();
        assertEquals(new FileInfoResponse("z", "z".repeat(15), 20, false), bc.getInfo("t1").getFiles().get("z"));
        ;
    }

    // bandwidth

    /*
     * Testing transmission ends properly
     * 
     */

    // Test goes out of range
    // - client network node should remove partial file
    @Test
    public void testDownloadingFileRemovedIfReceipientOutOfRange() {

        bc.createDevice("d1", "DesktopDevice", Angle.fromDegrees(135));
        bc.createSatellite("s1", "StandardSatellite", RADIUS_OF_JUPITER + 1, Angle.fromDegrees(140));
        bc.addFileToDevice("d1", "z", "z".repeat(80));
        assertDoesNotThrow(() -> bc.sendFile("z", "d1", "s1"));
        bc.simulate();
        assertEquals(new FileInfoResponse("z", "z", 80, false), bc.getInfo("s1").getFiles().get("z"));

        bc.simulate(60);
        assertNull(bc.getInfo("s1").getFiles().get("z"));

    }

    // Test teleporting satellite's insane behaviour
    @Test
    public void testTeleportingSatelliteCorrruptDeviceSource() {

        String msg = "It's a great day for the Irish";
        String corruptMsg = "I's a grea day for he Irish";

        bc.createDevice("d1", "DesktopDevice", Angle.fromDegrees(180));
        bc.createSatellite("t1", "TeleportingSatellite", RADIUS_OF_JUPITER + 100, Angle.fromDegrees(179));
        bc.addFileToDevice("d1", "msg", msg);
        assertEquals(new FileInfoResponse("msg", msg, msg.length(), true), bc.getInfo("d1").getFiles().get("msg"));
        bc.simulate(5);
        assertEquals(new FileInfoResponse("msg", corruptMsg, corruptMsg.length(), true),
                bc.getInfo("d1").getFiles().get("msg"));

    }

    @Test
    public void testTeleportingSatelliteCorruptsRemaining() {
        String msg = "zztttz";
        String corruptMsg = msg.replaceAll("[tT]", "");
        bc.createDevice("d1", "LaptopDevice", Angle.fromDegrees(177));
        bc.createSatellite("t1", "TeleportingSatellite", RADIUS_OF_JUPITER + 10000, Angle.fromDegrees(175));
        bc.addFileToDevice("d1", "msg", msg);
        assertDoesNotThrow(() -> bc.sendFile("msg", "d1", "t1"));
        bc.simulate(msg.length());

        bc.createSatellite("s1", "StandardSatellite", RADIUS_OF_JUPITER + 1000, bc.getInfo("t1").getPosition());
        assertDoesNotThrow(() -> bc.sendFile("msg", "t1", "s1"));
        assertDoesNotThrow(() -> bc.simulate(3));

        assertEquals(new FileInfoResponse("msg", corruptMsg, corruptMsg.length(), true),
                bc.getInfo("s1").getFiles().get("msg"));

    }
}

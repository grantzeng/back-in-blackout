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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;

import static blackout.TestHelpers.assertListAreEqualIgnoringOrder;

public class TestTransmission {
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
        bc.simulate(2);

        // s1 only has partial file, cannot transfer to s2
        bc.createSatellite("s2", "TeleportingSatellite", RADIUS_OF_JUPITER, Angle.fromDegrees(47));
        assertThrows(VirtualFileNotFoundException.class, () -> bc.sendFile("hello", "s1", "s2"));

        // File should be complete so can transfer from s1 to s2
        bc.simulate(4);
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
        for (int i = 0; i < 37; i++) {
            bc.createSatellite("r" + i, "RelaySatellite", RADIUS_OF_JUPITER + 3000, Angle.fromDegrees(i * 10));
        }
        
        bc.createDevice("d1", "LaptopDevice", Angle.fromDegrees(0));
        bc.createSatellite("s1", "StandardSatellite", RADIUS_OF_JUPITER + 3000, Angle.fromDegrees(180));
        String z = "z".repeat(80);
        bc.addFileToDevice("d1", "z", z);
        assertDoesNotThrow(() -> bc.sendFile("z","d1", "s1"));
        
        for (int i = 0; i < 80; i++) {
            assertEquals(new FileInfoResponse("z", z, z.length(), false),bc.getInfo("s1").getFiles().get("z"));
            bc.simulate();
        }
        bc.simulate(90);
    
        assertEquals(new FileInfoResponse("z", z, z.length(), true),bc.getInfo("s1").getFiles().get("z"));
    }

    // Test upload bandwidth is divided evenly
    @Test
    public void testDownloadBandwidthChanges() {
        
    }

    // Test bandwidth updates correctly if goes out of range

    // Test server network node can send to multiple clients at once if have
    @Test
    public void testCanSendMultipleFiles() {
        
    }
    // bandwidth

    /*
     * Testing transmission ends properly
     * 
     */

    // Test goes out of range
    // - client network node should remove partial file
    @Test
    public void testDownloadingFileRemovedIfReceipientOutOfRange(){ 
        
    }

    // Test file transmission completes
    // - client network node should have complete file
    @Test
    public void testNormalTransmissionOK() {
    
    }

    // Test teleporting satellite's insane behaviour
    @Test
    public void testPsychoticTeleportingSatelliteBehaviour() {
    
        // TODO
    }
}   

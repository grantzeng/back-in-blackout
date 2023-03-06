package blackout;

import unsw.blackout.BlackoutController;
import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;

import static blackout.TestHelpers.assertListAreEqualIgnoringOrder;

public class TestTask2Transmission {
    public static void main(String[] args) {
        BlackoutController bc = new BlackoutController();
        bc.createSatellite("s1", "StandardSatellite", 100, Angle.fromDegrees(0));
        bc.createDevice("d1", "HandheldDevice", Angle.fromDegrees(0));
        bc.addFileToDevice("d1", "asdf", "asdf");
        assertDoesNotThrow(() -> bc.sendFile("asdf", "d1", "s1"));
        System.out.println();
        System.out.println(bc.getInfo("d1"));
        System.out.println();
        System.out.println();
        System.out.println(bc.getInfo("s1"));
        System.out.println();
        bc.simulate();
    }

}

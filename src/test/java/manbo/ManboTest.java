package manbo;

import manbo.exceptions.ManboException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ManboTest {

    @Test
    void checkDateRanges_validDate() {
        // assertDoesNotThrow
        // Runs the code and passes if no exception is thrown.
        assertDoesNotThrow(() -> Manbo.checkDateRanges("2024-12-15"));
    }

    @Test
    void checkDateRanges_invalidMonth() {
        //assertThrows:
        // Runs the code and expects a ManboException to be thrown.
        // If no exception (or a different type), the test fails.
        ManboException ex = assertThrows(
                ManboException.class,
                () -> Manbo.checkDateRanges("2024-13-10")
        );

        // assertTrue:
        // Checks that the exception message contains "Invalid month".
        // Good for verifying the exception is not only thrown,
        // but also carries the correct error message.
        assertTrue(ex.getMessage().contains("Invalid month"));
    }

    @Test
    void checkTimeRanges_validTime_noException() {
        // Check that a valid time (HHmm) does not throw an exception
        assertDoesNotThrow(() -> Manbo.checkTimeRanges("0930"));
    }

    @Test
    void checkTimeRanges_invalidHour_throwsException() {
        // Expects ManboException for invalid hour (25 is not 00-23)
        ManboException ex = assertThrows(
                ManboException.class,
                () -> Manbo.checkTimeRanges("2500")
        );
        assertTrue(ex.getMessage().contains("Invalid hour"));
    }

    @Test
    void checkTimeRanges_invalidMinute_throwsException() {
        // Expects ManboException for invalid minute (61 is not 00-59)
        ManboException ex = assertThrows(
                ManboException.class,
                () -> Manbo.checkTimeRanges("1261")
        );
        assertTrue(ex.getMessage().contains("Invalid minute"));
    }
}

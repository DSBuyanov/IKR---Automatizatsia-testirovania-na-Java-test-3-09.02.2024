import com.max.BookingService;
import com.max.CantBookException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(BookingServiceTest.class);

    @Spy
    private BookingService bookingService = new BookingService();

    @Test
    public void testBookingSuccess() throws CantBookException {
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = from.plusHours(2);
        String userId = "user123";

        doReturn(true).when(bookingService).checkTimeInBD(from, to);
        doReturn(true).when(bookingService).createBook(anyString(), any(LocalDateTime.class), any(LocalDateTime.class));

        boolean result = bookingService.book(userId, from, to);

        assertTrue(result);
        verify(bookingService).checkTimeInBD(from, to);
        verify(bookingService).createBook(userId, from, to);
        logger.info("Позитивный тест на бронирование прошел успешно");
    }

    @Test
    public void testBookingFailure() {
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = from.plusHours(2);
        String userId = "user456";

        doReturn(false).when(bookingService).checkTimeInBD(from, to);

        assertThrows(CantBookException.class, () -> bookingService.book(userId, from, to));

        verify(bookingService).checkTimeInBD(from, to);
        verify(bookingService, never()).createBook(userId, from, to);
        logger.info("Негативный тест на бронирование прошел успешно");
    }
}
package com.hotel.util;

import com.hotel.model.Booking;
import javafx.application.Platform;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * EmailService — simulates async email sending using threads.
 *
 * Demonstrates: Multithreading (Thread / ExecutorService), Runnable,
 *               Platform.runLater for JavaFX thread safety.
 */
public class EmailService {

    private static final ExecutorService executor = Executors.newCachedThreadPool(r -> {
        Thread t = new Thread(r, "EmailWorker");
        t.setDaemon(true);   // don't block JVM shutdown
        return t;
    });

    /**
     * Sends a simulated e-bill email asynchronously.
     *
     * @param booking   The booking whose bill to send.
     * @param onSuccess Callback invoked on the JavaFX thread on success.
     * @param onError   Callback invoked on the JavaFX thread on error.
     */
    public static void sendEBill(Booking booking,
                                  Consumer<String> onSuccess,
                                  Consumer<String> onError) {
        executor.submit(() -> {
            try {
                // Simulate network / SMTP delay
                System.out.println("[EmailService] Preparing e-bill for " + booking.getCustomerName() + "...");
                Thread.sleep(2500);

                String bill = HotelDataManager.getInstance().generateBillText(booking);
                System.out.println("[EmailService] ===== E-BILL SENT =====");
                System.out.println(bill);
                System.out.println("[EmailService] E-bill delivered to " +
                        booking.getCustomer().getContactNumber() + "@hotel.email");

                // Return to JavaFX thread
                Platform.runLater(() ->
                        onSuccess.accept("E-bill sent successfully for booking " + booking.getBookingId()));

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Platform.runLater(() -> onError.accept("Email sending was interrupted."));
            } catch (Exception e) {
                Platform.runLater(() -> onError.accept("Email failed: " + e.getMessage()));
            }
        });
    }

    /** Shuts down the thread pool gracefully (call on app exit). */
    public static void shutdown() { executor.shutdown(); }
}

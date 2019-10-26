package dev.muskrat.delivery.payment;

public interface PaymentSystem {

    String getPaymentURL();
    String getName();
    boolean isAvailable();
    boolean isOnline();
}

package com.stripe.stripe_payments.commons.enums;

public enum StripeEventEnum {
    PAYMENT_INTENT_SUCCEED("payment_intent.succeeded"),
    CHECKOUT_SESSION_COMPLETED("checkout.session.completed");

    public final String value;

    StripeEventEnum(String value) {
        this.value = value;
    }
}

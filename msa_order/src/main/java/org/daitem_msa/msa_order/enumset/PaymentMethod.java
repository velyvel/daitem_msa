package org.daitem_msa.msa_order.enumset;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@AllArgsConstructor
@Getter
public enum PaymentMethod {

    NONE(null,  0),
    CARD("카드", 1),
    SEND("계좌이체",  2),
    KAKAOBANK("카카오뱅크", 3),
    TOSSBANK("토스", 4),;

    private final String paymentMethod;
    private final int value;

    public static PaymentMethod ofPaymentMethodStatus(String paymentMethod) {
        return EnumSet.allOf(PaymentMethod.class).stream()
                .filter(v -> v != PaymentMethod.NONE && v.getPaymentMethod().equals(paymentMethod))
                .findAny()
                .orElse(PaymentMethod.NONE);
    }

    @Override
    public String toString() {
        return this.getPaymentMethod();
    }

    @Converter
    public static class PaymentMethodConverter implements AttributeConverter<PaymentMethod, String> {

        @Override
        public String convertToDatabaseColumn(PaymentMethod attribute) {
            return  attribute != null ? attribute.getPaymentMethod() : PaymentMethod.NONE.getPaymentMethod();
        }

        @Override
        public PaymentMethod convertToEntityAttribute(String dbData) {
            return PaymentMethod.ofPaymentMethodStatus(dbData);
        }
    }
}

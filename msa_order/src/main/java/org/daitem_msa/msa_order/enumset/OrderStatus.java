package org.daitem_msa.msa_order.enumset;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@AllArgsConstructor
@Getter
public enum OrderStatus {

    NONE(null,  0),
    ORDERED("ORDERED", 1),
    DELIVERING("DELIVERING",  2),
    DELIVERED("DELIVERED", 3),
    DETERMINE("DETERMINE", 4),
    CANCELLED("CANCELLED", 5);

    private final String orderStatus;
    private final int value;

    public static OrderStatus ofOrderStatus(String orderStatus) {
        return EnumSet.allOf(OrderStatus.class).stream()
                .filter(v -> v != OrderStatus.NONE && v.getOrderStatus().equals(orderStatus))
                .findAny()
                .orElse(OrderStatus.NONE);
    }

    @Override
    public String toString() {
        return this.getOrderStatus();
    }

    @Converter
    public static class OrdersConverter implements AttributeConverter<OrderStatus, String> {

        @Override
        public String convertToDatabaseColumn(OrderStatus attribute) {
            return  attribute != null ? attribute.getOrderStatus() : OrderStatus.NONE.getOrderStatus();
        }

        @Override
        public OrderStatus convertToEntityAttribute(String dbData) {
            return OrderStatus.ofOrderStatus(dbData);
        }
    }
}

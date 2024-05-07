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
    ORDER_START("ORDERED", 1),
    ORDERED("ORDERED", 2),
    DELIVERING("DELIVERING",  3),
    DELIVERED("DELIVERED", 4),
    DETERMINE("DETERMINE", 5),
    CANCELLED("CANCELLED", 6);

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

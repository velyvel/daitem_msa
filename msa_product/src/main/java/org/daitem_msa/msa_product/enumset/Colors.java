package org.daitem_msa.msa_product.enumset;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@AllArgsConstructor
@Getter
public enum Colors {

    NONE(null, "", 0),
    GOLD("COLOR_GOLD", "금색", 1),
    SILVER("COLOR_SILVER", "은색", 2),
    PINK("COLOR_PINK", "분홍", 3),
    BLACK("COLOR_BLACK", "검정", 4),
    MIX("CATEGORY_MIX", "기타", 5);

    private final String color;
    private final String description;
    private final int value;

    public static Colors ofColor(String color) {
        return EnumSet.allOf(Colors.class).stream()
                .filter(v -> v != Colors.NONE && v.getColor().equals(color))
                .findAny()
                .orElse(Colors.NONE);
    }

    @Override
    public String toString() {
        return this.getColor();
    }

    @Converter
    public static class ColorsConverter implements AttributeConverter<Colors, String> {

        @Override
        public String convertToDatabaseColumn(Colors attribute) {
            return  attribute != null ? attribute.getColor() : Colors.NONE.getColor();
        }

        @Override
        public Colors convertToEntityAttribute(String dbData) {
            return Colors.ofColor(dbData);
        }
    }
}

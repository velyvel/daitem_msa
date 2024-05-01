package org.daitem_msa.msa_product.enumset;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@AllArgsConstructor
@Getter
public enum Size {

    NONE(null, 0),
    XS("EXTRA_SMALL", 1),
    S("SMALL",  2),
    M("MEDIUM", 3),
    L("LARGE",  4),
    XL("EXTRA_LARGE", 5);

    private final String size;
    private final int value;

    public static Size ofSize(String size) {
        return EnumSet.allOf(Size.class).stream()
                .filter(v -> v != Size.NONE && v.getSize().equals(size))
                .findAny()
                .orElse(Size.NONE);
    }

    @Override
    public String toString() {
        return this.getSize();
    }

    @Converter
    public static class SizeConverter implements AttributeConverter<Size, String> {

        @Override
        public String convertToDatabaseColumn(Size attribute) {
            return  attribute != null ? attribute.getSize() : Size.NONE.getSize();
        }

        @Override
        public Size convertToEntityAttribute(String dbData) {
            return Size.ofSize(dbData);
        }
    }
}

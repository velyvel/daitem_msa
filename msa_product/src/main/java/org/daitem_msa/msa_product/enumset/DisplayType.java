package org.daitem_msa.msa_product.enumset;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@AllArgsConstructor
@Getter
public enum DisplayType {

    NONE(null, "", 0),
    DISPLAYED("DISPLAY_DISPLAYED", "판매중", 1),
    HIDDEN("DISPLAY_HIDDEN", "숨김", 2),
    SOLDOUT("DISPLAY_SOLDOUT", "매진", 3),
    DELETED("DISPLAY_DELETED", "삭제", 4);

    private final String displayType;
    private final String description;
    private final int value;

    public static DisplayType ofDisplay(String displayType) {
        return EnumSet.allOf(DisplayType.class).stream()
                .filter(v -> v != DisplayType.NONE && v.getDisplayType().equals(displayType))
                .findAny()
                .orElse(DisplayType.NONE);
    }

    @Override
    public String toString() {
        return this.getDisplayType();
    }

    @Converter
    public static class DisplayConverter implements AttributeConverter<DisplayType, String> {

        @Override
        public String convertToDatabaseColumn(DisplayType attribute) {
            return  attribute != null ? attribute.getDisplayType() : DisplayType.NONE.getDisplayType();

        }

        @Override
        public DisplayType convertToEntityAttribute(String dbData) {
            return DisplayType.ofDisplay(dbData);
        }
    }
}

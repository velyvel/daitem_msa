package org.daitem_msa.msa_product.enumset;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@AllArgsConstructor
@Getter
public enum Categories {

    NONE(null, "", 0),
    RING("CATEGORY_RING", "반지", 1),
    NECKLACE("CATEGORY_NECKLACE", "목걸이", 2),
    EARRINGS("CATEGORY_EARRINGS", "귀걸이", 3),
    PHONECASE("CATEGORY_PHONECASE", "핸드폰케이스", 4),
    KEYRING("CATEGORY_KEYRING", "키링", 5);
    private final String category;
    private final String description;
    private final int value;

    public static Categories ofCategory(String category) {
        return EnumSet.allOf(Categories.class).stream()
                .filter(v -> v != Categories.NONE && v.getCategory().equals(category))
                .findAny()
                .orElse(Categories.NONE);
    }

    @Override
    public String toString() {
        return this.getCategory();
    }

    @Converter
    public static class CategoriesConverter implements AttributeConverter<Categories, String> {

        @Override
        public String convertToDatabaseColumn(Categories attribute) {
            return  attribute != null ? attribute.getCategory() : Categories.NONE.getCategory();

        }

        @Override
        public Categories convertToEntityAttribute(String dbData) {
            return Categories.ofCategory(dbData);
        }
    }
}

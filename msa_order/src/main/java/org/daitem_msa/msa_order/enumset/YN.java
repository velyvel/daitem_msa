package org.daitem_msa.msa_order.enumset;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum YN {
    Y("Y", true, 1),
    N("N", false, 0);

    private final String yn;
    private final boolean bool;
    private final int bit;

    public static YN ofYn(String yn) {
        return YN.Y.getYn().equals(yn) ? YN.Y : YN.N;
    }

    public static YN ofBool(boolean bool) {
        return bool ? YN.Y : YN.N;
    }

    public static YN ofBit(int bit) {
        return bit == 1 ? YN.Y : YN.N;
    }

    @Override
    public String toString() {
        return this.yn;
    }


    @Converter(autoApply = true)
    public static class YNConverter implements AttributeConverter<YN, String> {

        @Override
        public String convertToDatabaseColumn(YN attribute) {
            return (attribute != null && "Y".equals(attribute.getYn())) ? YN.Y.getYn() : YN.N.getYn();
        }

        @Override
        public YN convertToEntityAttribute(String dbData) {
            return YN.ofYn(dbData);
        }

    }
}

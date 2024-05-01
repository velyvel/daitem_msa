package org.daitem_msa.msa_product.common.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.core.GenericTypeResolver;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Converter
public abstract class JsonConverter<T> implements AttributeConverter<T, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonConverter() {
    }

    @Override
    public String convertToDatabaseColumn(T attribute) {
        if (!ObjectUtils.isEmpty(attribute))
            try {
                return objectMapper.writeValueAsString(attribute);
            } catch (JsonProcessingException ignored) {}

        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T convertToEntityAttribute(String dbData) {
        if (StringUtils.hasText(dbData)) {
            Class<?> aClass = GenericTypeResolver.resolveTypeArgument(getClass(), JsonConverter.class);

            try {
                return (T) objectMapper.readValue(dbData, aClass);
            } catch (IOException ignored) {}
        }

        return null;
    }

}

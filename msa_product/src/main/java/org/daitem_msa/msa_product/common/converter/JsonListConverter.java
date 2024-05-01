package org.daitem_msa.msa_product.common.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Converter
public abstract class JsonListConverter<T> implements AttributeConverter<List<T>, String> {

    private final TypeReference<List<T>> typeReference;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonListConverter(TypeReference<List<T>> typeReference) {
        this.typeReference = typeReference;
    }

    @Override
    public String convertToDatabaseColumn(List<T> attribute) {
        if (!ObjectUtils.isEmpty(attribute))
            try {
                return objectMapper.writeValueAsString(attribute);
            } catch (JsonProcessingException ignored) {}

        return "[]";
    }

    @Override
    public List<T> convertToEntityAttribute(String dbData) {
        if (StringUtils.hasText(dbData))
            try {
                return objectMapper.readValue(dbData, typeReference);
            } catch (IOException ignored) {}

        return new ArrayList<>();
    }

}

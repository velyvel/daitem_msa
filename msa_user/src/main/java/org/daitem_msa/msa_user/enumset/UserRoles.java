package org.daitem_msa.msa_user.enumset;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@AllArgsConstructor
@Getter
public enum UserRoles {
    NONE(null, "", 0),
    NORMAL("ROLE_NORMAL", "일반", 1),
    ADMIN("ROLE_ADMIN", "관리자", 11);

    private final String role;
    private final String name;
    private final int value;

    public static UserRoles ofRole(String role) {
        return EnumSet.allOf(UserRoles.class).stream()
                .filter(v -> v !=UserRoles.NONE && v.getRole().equals(role))
                .findAny()
                .orElse(UserRoles.NONE);
    }

    @Override
    public String toString() {
        return this.getRole();
    }

    @Converter
    public static class UserRolesConverter implements AttributeConverter<UserRoles, String> {

        @Override
        public String convertToDatabaseColumn(UserRoles attribute) {
            return  attribute != null ? attribute.getRole() : UserRoles.NONE.getRole();
        }

        @Override
        public UserRoles convertToEntityAttribute(String dbData) {
            return UserRoles.ofRole(dbData);
        }
    }

}

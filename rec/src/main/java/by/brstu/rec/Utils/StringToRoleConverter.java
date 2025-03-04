package by.brstu.rec.Utils;

import by.brstu.rec.enums.Role;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToRoleConverter implements Converter<String, Role> {

    @Override
    public Role convert(String source) {
        for (Role role : Role.values()) {
            if (role.getDisplayName().equalsIgnoreCase(source)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + source);
    }
}
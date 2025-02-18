package by.brstu.rec.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    PATIENT,
    DOCTOR;

    @Override
    public String getAuthority() {
        return name();
    }

    public String getDisplayName() {
        return switch (this) {
            case PATIENT -> "Пациент";
            case DOCTOR -> "Доктор";
            case ADMIN -> "Администратор";
        };
    }
}

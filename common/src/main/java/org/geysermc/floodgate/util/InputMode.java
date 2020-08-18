package org.geysermc.floodgate.util;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum InputMode {
    @JsonEnumDefaultValue
    UNKNOWN,
    KEYBOARD_MOUSE,
    TOUCH, // I guess Touch?
    CONTROLLER,
    VR;

    public static final InputMode[] VALUES = values();

    public static InputMode getById(int id) {
        return VALUES.length > id ? VALUES[id] : UNKNOWN;
    }
}
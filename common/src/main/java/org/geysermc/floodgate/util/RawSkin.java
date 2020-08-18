package org.geysermc.floodgate.util;

import lombok.AllArgsConstructor;

import java.nio.charset.StandardCharsets;

@AllArgsConstructor
public class RawSkin {
    public int width;
    public int height;
    public byte[] data;

    private RawSkin() {}

    public static RawSkin parse(String data) {
        if (data == null) return null;
        String[] split = data.split(":");
        if (split.length != 3) return null;

        RawSkin skin = new RawSkin();
        skin.width = Integer.parseInt(split[0]);
        skin.height = Integer.parseInt(split[1]);
        skin.data = split[2].getBytes(StandardCharsets.UTF_8);
        return skin;
    }

    @Override
    public String toString() {
        return Integer.toString(width) + ':' + height + ':' + new String(data);
    }
}

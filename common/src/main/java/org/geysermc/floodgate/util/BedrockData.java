package org.geysermc.floodgate.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BedrockData {
    public static final int EXPECTED_LENGTH = 7;
    public static final String FLOODGATE_IDENTIFIER = "Geyser-Floodgate";

    private String xuid;
    private String username;
    private String version;
    private DeviceOS device;
    private String languageCode;
    private InputMode inputMode;
    private String ip;
    private int dataLength;

    private RawSkin skin;

    public BedrockData(String xuid, String username, String version, DeviceOS device,
                       String languageCode, InputMode inputMode, String ip, RawSkin skin) {
        this(xuid, username, version, device, languageCode, inputMode, ip, EXPECTED_LENGTH, skin);
    }

    public static BedrockData fromString(String data, String skin) {
        String[] split = data.split("\0");
        if (split.length != EXPECTED_LENGTH) return null;

        return new BedrockData(
                split[0], split[1], split[2], DeviceOS.getById(Integer.parseInt(split[3])),
                split[4], InputMode.getById(Integer.parseInt(split[5])),
                split[6], split.length, RawSkin.parse(skin)
        );
    }

    public static BedrockData fromRawData(byte[] data, String skin) {
        return fromString(new String(data), skin);
    }

    @Override
    public String toString() {
        return version +'\0'+ username +'\0'+ xuid +'\0'+ device +'\0'+
                languageCode +'\0'+ inputMode.ordinal() +'\0'+ ip;
    }
}

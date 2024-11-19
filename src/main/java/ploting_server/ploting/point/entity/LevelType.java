package ploting_server.ploting.point.entity;

import ploting_server.ploting.core.code.error.PointErrorCode;
import ploting_server.ploting.core.exception.PointException;

public enum LevelType {
    씨앗(1, 5),
    새싹(6, 10),
    잎사귀(11, 15),
    나뭇가지(16, 20),
    열매(21, 25),
    나무(26, 30),
    숲(31, Integer.MAX_VALUE);

    private final int minLevel;
    private final int maxLevel;

    LevelType(int minLevel, int maxLevel) {
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
    }

    public static LevelType findLevelTypeByLevel(int level) {
        for (LevelType levelType : values()) {
            if (level >= levelType.minLevel && level <= levelType.maxLevel) {
                return levelType;
            }
        }
        throw new PointException(PointErrorCode.INVALID_LEVEL);
    }
}

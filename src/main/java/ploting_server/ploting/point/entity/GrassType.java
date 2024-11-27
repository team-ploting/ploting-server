package ploting_server.ploting.point.entity;

import lombok.Getter;
import ploting_server.ploting.core.code.error.PointErrorCode;
import ploting_server.ploting.core.exception.PointException;

public enum GrassType {
    ZERO(0, 0, 0),
    ONE(1, 1, 19),
    TWO(2, 20, 29),
    THREE(3, 30, 39),
    FOUR(4, 40, Integer.MAX_VALUE);

    @Getter
    private final int step;
    private final int minPoint;
    private final int maxPoint;

    GrassType(int step, int minPoint, int maxPoint) {
        this.step = step;
        this.minPoint = minPoint;
        this.maxPoint = maxPoint;
    }

    public static int findGrassStepByPoint(int point) {
        for (GrassType grassType : values()) {
            if (grassType.minPoint <= point && grassType.maxPoint >= point) {
                return grassType.step;
            }
        }
        throw new PointException(PointErrorCode.INVALID_GRASS_TYPE);
    }
}

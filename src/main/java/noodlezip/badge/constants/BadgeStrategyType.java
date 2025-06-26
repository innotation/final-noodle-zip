package noodlezip.badge.constants;

import lombok.Getter;

@Getter
public enum BadgeStrategyType {

    SINGLE(null, 1, 0),
    LEVEL(null, 1, 0),
    SINGLE_ACCUMULATIVE(1, 1, 0),
    LEVEL_ACCUMULATIVE(1, 1, 0);

    private final Integer initAccumulativeValue;
    private final int initCurrentValue;
    private final Integer initCurrentValueForNextLevel;

    BadgeStrategyType(Integer initAccumulativeValue,
                      int initCurrentValue,
                      Integer initNextLevelCurrentValue) {
        this.initAccumulativeValue = initAccumulativeValue;
        this.initCurrentValue = initCurrentValue;
        this.initCurrentValueForNextLevel = initNextLevelCurrentValue;
    }

}
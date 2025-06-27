package noodlezip.badge.constants;

import lombok.Getter;

@Getter
public enum BadgeStrategyType {

    SINGLE(false,null, 1, 0),
    SINGLE_ACCUMULATIVE(true,1, 1, 0),

    LEVEL(false, null, 1, 0),
    LEVEL_ACCUMULATIVE(true,1, 1, 0);

    private final boolean isAccumulative;
    private final Integer initAccumulativeValue;
    private final int initCurrentValue;
    private final Integer initCurrentValueForNextLevel;

    BadgeStrategyType(boolean isAccumulative, Integer initAccumulativeValue,
                      int initCurrentValue,
                      Integer initNextLevelCurrentValue) {
        this.isAccumulative = isAccumulative;
        this.initAccumulativeValue = initAccumulativeValue;
        this.initCurrentValue = initCurrentValue;
        this.initCurrentValueForNextLevel = initNextLevelCurrentValue;
    }

}
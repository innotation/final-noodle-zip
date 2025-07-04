package noodlezip.badge.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BadgeStrategyType {

    SINGLE(true, false, null, 1, 0),
    SINGLE_ACCUMULATIVE(true, true, 1, 1, 0),

    LEVEL(false, false, null, 1, 0),
    LEVEL_ACCUMULATIVE(false, true, 1, 1, 0);

    private final boolean isSingle;
    private final boolean isAccumulative;
    private final Integer initAccumulativeValue;
    private final int initCurrentValue;
    private final Integer initCurrentValueForNextLevel;

}
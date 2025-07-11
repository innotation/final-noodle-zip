package noodlezip.subscription.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class SubscriptionPagePolicy {

    public static final String DEFAULT_PAGE = "1";
    public static final int PAGE_SIZE = 30;
    public static final String SORT_PROPERTY = "createdAt";


    public static Pageable getPageable(int page) {
        return PageRequest.of(
                page - 1,
                PAGE_SIZE,
                Sort.by(Sort.Direction.DESC, SORT_PROPERTY)
        );
    }

}

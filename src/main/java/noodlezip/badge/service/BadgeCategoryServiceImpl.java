package noodlezip.badge.service;

import lombok.RequiredArgsConstructor;
import noodlezip.badge.entity.BadgeCategory;
import noodlezip.badge.exception.BadgeErrorStatus;
import noodlezip.badge.repository.BadgeCategoryRepository;
import noodlezip.common.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BadgeCategoryServiceImpl implements BadgeCategoryService {

    private final BadgeCategoryRepository badgeCategoryRepository;

    @Override
    @Transactional(readOnly = true)
    public BadgeCategory getBadgeCategoryById(Long badgeCategoryId) {
        return badgeCategoryRepository.findById(badgeCategoryId)
                .orElseThrow(() -> new CustomException(BadgeErrorStatus._NOT_FOUND_BADGE_CATEGORY));
    }

}

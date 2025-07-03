package noodlezip.badge.service;

import noodlezip.badge.entity.BadgeCategory;
import noodlezip.badge.exception.BadgeErrorStatus;
import noodlezip.badge.repository.BadgeCategoryRepository;
import noodlezip.common.exception.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BadgeCategoryServiceImplTest {

    @InjectMocks
    private BadgeCategoryServiceImpl badgeCategoryService;

    @Mock
    private BadgeCategoryRepository badgeCategoryRepository;

    @Test
    void getBadgeCategoryById_존재하는ID_성공() {
        // given
        Long badgeCategoryId = 1L;
        BadgeCategory badgeCategory = BadgeCategory.builder()
                .id(badgeCategoryId)
                .badgeCategoryName("좋아요 카테고리")
                .isActive(true)
                .build();

        when(badgeCategoryRepository.findById(badgeCategoryId)).thenReturn(Optional.of(badgeCategory));

        // when
        BadgeCategory result = badgeCategoryService.getBadgeCategoryById(badgeCategoryId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getBadgeCategoryName()).isEqualTo("좋아요 카테고리");
    }

    @Test
    void getBadgeCategoryById_존재하지않는ID_예외발생() {
        // given
        Long badgeCategoryId = 1000L;
        when(badgeCategoryRepository.findById(badgeCategoryId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() ->
                badgeCategoryService.getBadgeCategoryById(badgeCategoryId)
        ).isInstanceOf(CustomException.class)
                .hasMessageContaining(BadgeErrorStatus._NOT_FOUND_BADGE_CATEGORY.getMessage());
    }

}
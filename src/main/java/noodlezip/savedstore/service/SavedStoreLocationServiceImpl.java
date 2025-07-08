package noodlezip.savedstore.service;

import lombok.RequiredArgsConstructor;
import noodlezip.mypage.dto.request.savedstore.SavedStoreCategoryFilterRequest;
import noodlezip.mypage.dto.response.savedstore.SavedStoreMapResponse;
import noodlezip.savedstore.repository.SavedStoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SavedStoreLocationServiceImpl implements SavedStoreLocationService {

    private final SavedStoreRepository savedStoreRepository;

    @Override
    @Transactional(readOnly = true)
    public SavedStoreMapResponse getStoreLocationList(Long userId,
                                                      SavedStoreCategoryFilterRequest filter,
                                                      boolean isOwner
    ) {
        return savedStoreRepository.getStoreLocationList(userId, filter.getCategoryIdList(), isOwner);
    }

}

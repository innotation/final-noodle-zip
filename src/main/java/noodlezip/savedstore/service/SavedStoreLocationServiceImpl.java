package noodlezip.savedstore.service;

import lombok.RequiredArgsConstructor;
import noodlezip.common.exception.CustomException;
import noodlezip.savedstore.dto.request.SavedStoreCategoryFilterRequest;
import noodlezip.savedstore.dto.response.SavedStoreMapResponse;
import noodlezip.savedstore.repository.SavedStoreRepository;
import noodlezip.savedstore.status.SavedStoreErrorStatus;
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
        try {
            return savedStoreRepository.getStoreLocationList(userId, filter, isOwner);
        } catch (Exception e) {
            throw new CustomException(SavedStoreErrorStatus._FAIL_SAVED_STORE_MAP);
        }
    }

}

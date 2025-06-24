package noodlezip.search.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.store.dto.StoreDto;
import noodlezip.store.entity.Store;
import noodlezip.store.repository.StoreRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SearchService {

    private final StoreRepository storeRepository;
    private final ModelMapper modelMapper;

    public List<StoreDto> findAllStore(){

        List<Store> storeList = storeRepository.findAll();

        return storeList.stream()
                .map(store -> modelMapper.map(store, StoreDto.class))
                .collect(Collectors.toList());
    }

}

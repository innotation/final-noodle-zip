package noodlezip.common.util;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PageUtil {

    public Map<String, Object> getPageInfo(Page page, int pagePerBlock){

        int currPage = page.getNumber() + 1;
        int beginPage = (currPage - 1) / pagePerBlock * pagePerBlock + 1;
        int endPage = Math.min( beginPage + pagePerBlock - 1, page.getTotalPages());

        Map<String, Object> paginationData = new HashMap<>();

        paginationData.put("page", currPage);
        paginationData.put("beginPage", beginPage);
        paginationData.put("endPage", endPage);
        paginationData.put("totalCount", page.getTotalElements());
        paginationData.put("size", page.getSize());
        paginationData.put("pagePerBlock", pagePerBlock);
        paginationData.put("totalPage", page.getTotalPages());
        paginationData.put("isFirst", page.isFirst());
        paginationData.put("isLast", page.isLast());

        return paginationData;
    }

}
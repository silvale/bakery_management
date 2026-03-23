package com.bakery.bakery_management.service;

import com.bakery.bakery_management.domain.PageResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface AdminBaseService<Req, Res, ID> {

    ID create(Req request);

    Res getDetail(ID id);

    PageResult<Res> getList(Pageable pageable);

    void update(ID id, Req request);

    void delete(ID id);

}

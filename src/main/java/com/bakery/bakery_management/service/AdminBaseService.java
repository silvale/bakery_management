package com.bakery.bakery_management.service;

import java.util.List;


public interface AdminBaseService<Req, Res, ID> {

    ID create(Req request);

    Res getDetail(ID id);

    List<Res> getList();

    void update(ID id, Req request);

    void delete(ID id);
}

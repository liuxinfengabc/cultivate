package com.lz.ht.service;

import com.lz.ht.dto.OrgcommentDto;
import com.lz.ht.model.Orgcomment;
import com.lz.ht.page.PageModel;
import java.util.List;
import java.util.HashMap;

public interface OrgcommentService {

    void add(Orgcomment record);

    void updateById(Orgcomment record);

    int deleteById(Long orgcommentId);

    Orgcomment findById(Long orgcommentId);

    List<Orgcomment> findAll();

    Orgcomment findOne(Orgcomment record);

    List<Orgcomment> findList(Orgcomment record);

    List<Orgcomment> findPageList(PageModel<Orgcomment> page, Orgcomment orgcomment) throws Exception;

    List<Orgcomment> findListByMapLimit(HashMap<String,Object> hashMap, long first, long last);

    long findCount(Orgcomment orgcomment) throws Exception;

    long findCountByMap(HashMap<String,Object> hashMap);

	List<OrgcommentDto> findPageListByDto(PageModel<OrgcommentDto> page, OrgcommentDto orgcommentDto) throws Exception;

	long findCountByDto(OrgcommentDto orgcommentDto) throws Exception;

}

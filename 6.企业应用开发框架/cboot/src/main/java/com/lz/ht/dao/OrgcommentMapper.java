package com.lz.ht.dao;

import com.lz.ht.dto.OrgcommentDto;
import com.lz.ht.model.Orgcomment;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.HashMap;
import org.apache.ibatis.annotations.Mapper;

//@Repository
@Mapper
public interface OrgcommentMapper {

    void add(Orgcomment record);

    void updateById(Orgcomment record);

    int deleteById(Long OrgcommentId);

    Orgcomment  findById(Long id);

    List<Orgcomment> findAll();

    List<Orgcomment> findList(Orgcomment record);

    long findCount(Orgcomment orgcomment);

    long findCountByMap(HashMap<String,Object> hashMap);

    List<Orgcomment> findListByMapLimit(HashMap<String,Object> hashMap);

	List<OrgcommentDto> findPageListByDto(HashMap<String, Object> hashMap);

	long findCountByDto(HashMap<String, Object> hashMap);

}

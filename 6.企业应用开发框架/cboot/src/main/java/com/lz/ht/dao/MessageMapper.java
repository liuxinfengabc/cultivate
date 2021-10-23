package com.lz.ht.dao;

import com.lz.ht.dto.MessageDto;
import com.lz.ht.model.Message;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.HashMap;
import org.apache.ibatis.annotations.Mapper;

//@Repository
@Mapper
public interface MessageMapper {

    void add(Message record);

    void updateById(Message record);

    int deleteById(Long MessageId);

    Message  findById(Long id);

    List<Message> findAll();

    List<Message> findList(Message record);

    long findCount(Message message);

    long findCountByMap(HashMap<String,Object> hashMap);

    List<Message> findListByMapLimit(HashMap<String,Object> hashMap);

	List<MessageDto> findListByMapLimitDto(HashMap<String, Object> hashMap);
	
	long findCountDto(HashMap<String, Object> hashMap);

	List<MessageDto> findSubListByDto(HashMap<String, Object> hashMap);
	
	long findSubListNumberByCid(String conversationId);

	Long findToUserByCid(String conversationId);

	MessageDto findConversationByCid(String conversationId);

}

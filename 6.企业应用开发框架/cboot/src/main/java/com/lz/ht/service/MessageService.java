package com.lz.ht.service;

import com.lz.ht.dto.MessageDto;
import com.lz.ht.model.Message;
import com.lz.ht.page.PageModel;
import java.util.List;
import java.util.HashMap;

public interface MessageService {

    void add(Message record);

    void updateById(Message record);

    int deleteById(Long messageId);

    Message findById(Long messageId);

    List<Message> findAll();

    Message findOne(Message record);

    List<Message> findList(Message record);

    List<Message> findPageList(PageModel<Message> page, Message message) throws Exception;

    List<Message> findListByMapLimit(HashMap<String,Object> hashMap, long first, long last);

    long findCount(Message message) throws Exception;

    long findCountByMap(HashMap<String,Object> hashMap);

	List<MessageDto> findPageListDto(PageModel<MessageDto> page, MessageDto messageDto) throws Exception;

	long findCountDto(MessageDto messageDto) throws Exception;

	List<MessageDto> findSubListByDto(MessageDto messageDto) throws Exception;
	
	long findSubListNumberByCid(String conversationId);

	Long findToUserByCid(String conversationId);

	MessageDto findConversationByCid(String conversationId);

}

package com.lz.ht.service.serviceImpl;

import com.lz.ht.dao.MessageMapper;
import com.lz.ht.dto.MessageDto;
import com.lz.ht.model.Message;
import com.lz.ht.service.MessageService;
import com.lz.ht.page.PageModel;
import com.lz.ht.util.ToolKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.apache.ibatis.exceptions.TooManyResultsException;
import java.util.List;
import java.util.HashMap;

@Service
@Transactional(rollbackFor = Exception.class)
public class MessageServiceImpl  implements  MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public void add(Message record) {
        messageMapper.add(record);
    }

    @Override
    public void updateById(Message record) {
        messageMapper.updateById(record);
    }

    @Override
    public int deleteById(Long MessageId) {
        return  messageMapper.deleteById(MessageId);
    }

    @Override
    public Message findById(Long MessageId) {
        return  messageMapper.findById(MessageId);
    }

    @Override
    public List<Message> findAll() {
        return messageMapper.findAll();
    }


    @Override
    public Message findOne(Message record) {
        List<Message>  list = messageMapper.findList(record);
        if ((list!=null)&&(list.size() == 1)) {
            return list.get(0);
        } else if ((list!=null)&&(list.size() > 1)) {
            throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
        } else {
            return null;
        }
    }

    @Override
    public List<Message> findList(Message record){
         return  messageMapper.findList(record);
    }

    @Override
    public List<Message> findPageList(PageModel<Message> page, Message message) throws Exception {
        HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(message);
        List<Message> pageList = findListByMapLimit(hashMap, page.getMsFirst(), page.getMsLast());
        return pageList;
    }

    @Override
    public List<Message> findListByMapLimit(HashMap<String,Object> hashMap,long first,long last){
        hashMap.put("msFirst",first);
        hashMap.put("msLast",last);
        return messageMapper.findListByMapLimit(hashMap);
    }

    @Override
    public long findCount(Message  message) throws Exception {
        HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(message);
        return  findCountByMap(hashMap);
    }

    @Override
    public long findCountByMap(HashMap<String,Object> hashMap){
        return messageMapper.findCountByMap(hashMap);
    }

	@Override
	public List<MessageDto> findPageListDto(PageModel<MessageDto> page, MessageDto messageDto) throws Exception {
		  HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(messageDto);
	      List<MessageDto> pageList = findListByMapLimitDto(hashMap, page.getMsFirst(), page.getMsLast());
	      return pageList;
	}

	private List<MessageDto> findListByMapLimitDto(HashMap<String, Object> hashMap, long msFirst, long msLast) {
		hashMap.put("msFirst",msFirst);
        hashMap.put("msLast",msLast);
        return messageMapper.findListByMapLimitDto(hashMap);
	}

	@Override
	public long findCountDto(MessageDto messageDto) throws Exception {
		 HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(messageDto);
	    return  messageMapper.findCountDto(hashMap);
	}

	@Override
	public List<MessageDto> findSubListByDto(MessageDto messageDto) throws Exception {
		 HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(messageDto);
		return messageMapper.findSubListByDto(hashMap);
	}

	@Override
	public long findSubListNumberByCid(String conversationId) { 
		return messageMapper.findSubListNumberByCid(conversationId);
	}

	@Override
	public Long findToUserByCid(String conversationId) { 
		return messageMapper.findToUserByCid(conversationId);
	}

	@Override
	public MessageDto findConversationByCid(String conversationId) {
		 
		return messageMapper.findConversationByCid(conversationId);
	}


 }

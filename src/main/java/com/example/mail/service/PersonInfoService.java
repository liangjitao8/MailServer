package com.example.mail.service;

import com.example.mail.dao.LocalAuthMapper;
import com.example.mail.dao.PersonInfoMapper;
import com.example.mail.dto.Result;
import com.example.mail.entity.LocalAuth;
import com.example.mail.entity.PersonInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonInfoService {
    @Autowired
    PersonInfoMapper personInfoMapper;
    @Autowired
    LocalAuthMapper localAuthMapper;
    @Autowired
    RedisTemplate redisTemplate;

    @Transactional(rollbackFor=Exception.class)
    public Result savePersonInfo(PersonInfo personInfo, LocalAuth localAuth){
        if(personInfo==null){
            return new Result(false,"personInfo is null!",-1);
        }else if(localAuth==null){
            return new Result(false,"localAuth is null",-2);
        }else try{
            personInfo.setEnableStatus(0);
           int effectedNum = personInfoMapper.insert(personInfo);
           localAuth.setUserId(personInfo.getUserId());
           int effectedNum2= localAuthMapper.insert(localAuth);
           if(effectedNum<=0||effectedNum2<=0)
               return new Result(false,"insert error",-100);
           else {
               return new Result(true,personInfo);
           }
        }catch (Exception e){
            return new Result(false,"runtime error",-200);
        }
    }

    public Result enableAuth(String userName, String verifyCode) {
        redisTemplate.opsForValue().get(userName);
        if (verifyCode.equals(redisTemplate.opsForValue().get(userName))) {
            personInfoMapper.setEnableStatus(userName);
            return new Result(true,userName);
        }
        else
            return new Result(false,"激活失败！",-300);
    }
}

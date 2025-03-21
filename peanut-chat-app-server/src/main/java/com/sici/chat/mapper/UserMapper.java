package com.sici.chat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sici.chat.model.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 20148
* @description 针对表【user】的数据库操作Mapper
* @createDate 2024-12-05 17:27:51
* @Entity generator.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}





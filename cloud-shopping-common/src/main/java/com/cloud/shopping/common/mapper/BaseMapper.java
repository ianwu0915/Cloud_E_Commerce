package com.cloud.shopping.common.mapper;

import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;

/*
 * Base Mapper: selectOne, select, selectAll, selectCount, selectByPrimaryKey, insert,
 * insertSelective, updateByPrimaryKey, updateByPrimaryKeySelective, deleteByPrimaryKey
 * By extending Mapper, it provides basic CRUD operations for any entity T
 * By extending IdListMapper, it provides batch operations for ID lists: selectByIdList, deleteByIdList, existsWithPrimaryKey
 * By extending InsertListMapper, it provides batch insert operations insertList
 */
@RegisterMapper
public interface BaseMapper<T> extends Mapper<T>,IdListMapper<T,Long>,InsertListMapper<T> {
}


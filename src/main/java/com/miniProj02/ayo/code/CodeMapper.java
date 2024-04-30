package com.miniProj02.ayo.code;

import com.miniProj02.ayo.entity.CodeVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CodeMapper {
    public List<CodeVO> getList();
}

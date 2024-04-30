package com.miniProj02.ayo.code;

import com.miniProj02.ayo.entity.CodeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CodeService {
    private final CodeMapper codeMapper;

    public List<CodeVO> getList() {
        List<CodeVO> list = null;
        list = codeMapper.getList();
        return list;
    }
}

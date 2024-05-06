package com.miniProj02.ayo.board;

import lombok.Getter;

public interface FileVO {
    String getReal_filename();

    String getOriginal_filename();

    Long getSize();

    String getContent_type();
}

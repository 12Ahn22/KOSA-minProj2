package com.miniProj02.ayo.scheduler;

import com.miniProj02.ayo.board.BoardImageFileMapper;
import com.miniProj02.ayo.board.BoardTokenMapper;
import com.miniProj02.ayo.entity.BoardImageFileVO;
import com.miniProj02.ayo.entity.BoardTokenVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
@EnableScheduling
public class SchedulerService {
    private final BoardTokenMapper boardTokenMapper;
    private final BoardImageFileMapper boardImageFileMapper;

    @Scheduled(fixedDelay = 60000) // 60초마다 실행
    public void fileTokenAutoDelete() {
        log.info("첨부 파일 업로드 중 완료되지 않음 파일을 삭제한다");

        // 현재 30분 전에 생성되고 임시 상태인 token 목록을 얻는다
        List<BoardTokenVO> boardTokenList = boardTokenMapper.listToken();

        if (boardTokenList.size() != 0) {
            log.info("fileTokenList : " + boardTokenList);

            Map<String, Object> map = new HashMap<>(); // 삭제할 토큰들
            map.put("list", boardTokenList);

            // 해당 token을 사용하는 ImageFile들을 얻는다.
            List<BoardImageFileVO> boardImageFileList = boardImageFileMapper.getBoardImageFileList(map);
            log.info("boardImageFileList => {}", boardImageFileList);

            // 실제 물리 저장소에 저장된 이미지 파일을 삭제한다
            for (BoardImageFileVO fileUpload : boardImageFileList) {
                log.info("삭제 파일 : " + fileUpload.getReal_filename());
                File deleteFile = new File(fileUpload.getReal_filename());
                if(deleteFile.exists()) deleteFile.delete();
            }

            if (boardImageFileList.size() != 0) {
                //게시물 내용에 추가된 이미지 정보를 삭제한다
                boardImageFileMapper.deleteBoardImageFilesByTokens(map);
            }

            // 토큰 테이블에서 임시로 사용된 게시물 토큰을 삭제한다
            boardTokenMapper.deleteTokens(map);
        }
    }
}

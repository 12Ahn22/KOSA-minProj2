package com.miniProj02.ayo;

import com.miniProj02.ayo.exception.MyException;
import com.miniProj02.ayo.exception.enums.AuthErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class HomeController {
    @GetMapping("/")
    public String home(){
        log.info("=HOME=");
        return "index";
    }

    @GetMapping("/intro")
    public String intro(){
        log.info("=INTRO=");
        return "intro";
    }
}

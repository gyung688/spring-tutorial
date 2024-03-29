package com.jojodu.book.springboot.web;

import com.jojodu.book.springboot.config.auth.LoginUser;
import com.jojodu.book.springboot.config.auth.dto.SessionUser;
import com.jojodu.book.springboot.domain.user.User;
import com.jojodu.book.springboot.service.PostsService;
import com.jojodu.book.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user){
        // Model : 서버 템플릿 엔진에서 사용할 수 있는 객체를 저장할 수 있다.
        // @LoginUser : 기존에 httpSession.getAttribute("user")로 가져오던 세션 정보 값이 개선됐다.
        //              이제는 어느 컨트롤러든지 @LoginUser만 사용하면 세션정보를 가져올 수 있게 됐다.

        model.addAttribute("posts", postsService.findAllDesc());
        // 머스테치 스타터 덕분에 컨트롤러에서 문자열 반환할 때 앞의 경로와 파일 확장자가 자동으로 지정되어 뷰 리졸버가 처리한다.

        //CustomOAuth2UserService에서 로그인 성공 시 세션에 SessionUser를 저장하도록 구성
        //로그인 성공 시 httpSession.getAttribute("user")에서 값을 가져올 수 있다.
//        user = (SessionUser) httpSession.getAttribute("user"); // 상단의 @LoginUser덕분에 getAttribute 필요없음
        if(user != null){
            model.addAttribute("userName", user.getName());
        }
        return "posts-index";
    }

    @GetMapping("/posts/save")
    public String postsSave(){
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model){
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);
        return "posts-update";
    }
}

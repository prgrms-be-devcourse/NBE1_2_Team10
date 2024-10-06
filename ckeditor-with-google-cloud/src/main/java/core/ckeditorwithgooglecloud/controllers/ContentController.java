package core.ckeditorwithgooglecloud.controllers;

import core.ckeditorwithgooglecloud.repositories.PostingContentEntity;
import core.ckeditorwithgooglecloud.services.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    @GetMapping("/content")
    public String contentPage(@RequestParam("id") Long id, Model model) {

        PostingContentEntity posting = contentService.getContent(id);

        model.addAttribute("posting", posting);

        return "content";
    }
}

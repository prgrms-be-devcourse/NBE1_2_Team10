package core.ckeditorwithgooglecloud.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EditorController {

    @GetMapping("/")
    public String mainPage() {
        return "redirect:/editor";
    }

    @GetMapping("/editor")
    public String editorPage() {
        return "editor";
    }
}

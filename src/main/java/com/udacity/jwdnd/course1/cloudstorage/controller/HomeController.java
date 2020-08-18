package com.udacity.jwdnd.course1.cloudstorage.controller;


import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class HomeController {

    private UserService userService;
    private NoteService noteService;
    private CredentialService credentialService;
    private FileService fileService;
    private EncryptionService encryptionService;


    @Autowired
    public HomeController(UserService userService,NoteService noteService, CredentialService credentialService, FileService fileService) {
        this.userService=userService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.fileService = fileService;
    }

    @GetMapping(value = {"/", "/home"})
    public String getHome(Authentication authentication,
                          @ModelAttribute("note") Note note,
                          @ModelAttribute("credential") Credential credential,
                          @ModelAttribute("file") File file,
                          Model model){
        User appUser = userService.getUser(authentication.getName());
        Integer userId = appUser.getUserId();

        model.addAttribute("notes",noteService.getNotesByUserId(userId));
        model.addAttribute("credentials",credentialService.getCredentialsByUserId(userId));
        model.addAttribute("files",fileService.getFilesByUserId(userId));

        return "home";
    }

    @GetMapping("/result")
    public String getResult(){
        return "result";
    }



}

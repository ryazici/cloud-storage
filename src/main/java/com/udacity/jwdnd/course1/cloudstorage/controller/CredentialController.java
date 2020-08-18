package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping
public class CredentialController {

    private UserService userService;
    private CredentialService credentialService;

    @Autowired
    public CredentialController(UserService userService, CredentialService credentialService) {
        this.userService = userService;
        this.credentialService = credentialService;
    }

    @PostMapping("/credential")
    public String postCredential(Authentication authentication, @ModelAttribute("credential") Credential credential,
                                 RedirectAttributes redirectAttributes) {
        try {
            User appUser = userService.getUser(authentication.getName());
            Integer userId = appUser.getUserId();
            credential.setUserId(userId);

            if (credential.getCredentialId() == null)
                credentialService.createCredential(credential);
            else
                credentialService.updateCredential(credential);

            return "redirect:/result?success";
        } catch (Exception e) {
            redirectAttributes.addAttribute("errorMessage", e.getCause().getMessage());
            return "redirect:/result?error";
        }
    }

    @PostMapping("/remove-credential")
    public String removeNote(@ModelAttribute("credential") Credential credential, RedirectAttributes redirectAttributes) {
        try {
            credentialService.removeCredential(credential.getCredentialId());

            return "redirect:/result?success";
        } catch (Exception e) {
            redirectAttributes.addAttribute("errorMessage", e.getCause().getMessage());
            return "redirect:/result?error";
        }
    }


}

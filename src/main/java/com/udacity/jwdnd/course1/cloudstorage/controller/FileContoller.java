package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping
public class FileContoller {
    private UserService userService;
    private FileService fileService;

    @Autowired
    public FileContoller(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable("fileId") Integer fileId) {
        File file = fileService.getFileById(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment:filename\"" + file.getFileName() + "\"")
                .body(new ByteArrayResource(file.getFileData()));
    }


    @PostMapping("/file")
    public String postCredential(Authentication authentication, MultipartFile fileToUpload,
                                 RedirectAttributes redirectAttributes)
            throws IOException {
        try {
            User appUser = userService.getUser(authentication.getName());
            Integer userId = appUser.getUserId();
            fileService.uploadFile(fileToUpload, userId);
            return "redirect:/result?success";
        } catch (Exception e) {
            redirectAttributes.addAttribute("errorMessage", e.getCause().getMessage());
            return "redirect:/result?error";
        }


    }

    @PostMapping("/remove-file")
    public String removeNote(@ModelAttribute("file") File file, RedirectAttributes redirectAttributes) {
        try {
            fileService.removeFile(file.getFileId());

            return "redirect:/result?success";
        } catch (Exception e) {
            redirectAttributes.addAttribute("errorMessage", e.getCause().getMessage());
            return "redirect:/result?error";
        }
    }


}

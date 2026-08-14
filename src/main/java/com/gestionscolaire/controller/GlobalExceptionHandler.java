package com.gestionscolaire.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public String gererIntrouvable(IllegalArgumentException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "erreur";
    }
}

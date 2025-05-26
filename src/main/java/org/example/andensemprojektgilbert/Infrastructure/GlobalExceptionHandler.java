package org.example.andensemprojektgilbert.Infrastructure;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception e, Model model) {
        model.addAttribute("exception", "Unexpected error: " + e.getClass().getSimpleName() + ": " + e.getMessage());
        e.printStackTrace();
        return "error";
    }

    @ExceptionHandler
    public String handleEmptyResultDataAccessException(EmptyResultDataAccessException e, Model model) {
        model.addAttribute("exception", "Username or password does not exist");
        e.printStackTrace();
        return "error";
    }

    @ExceptionHandler
    public String handleSessionAuthenticationException(SessionAuthenticationException e, Model model) {
        model.addAttribute("exception", "You are not logged in");
        return "error";
    }

    @ExceptionHandler
    public String handleNullPointerException(NullPointerException e, Model model) {
        model.addAttribute("exception", "You have to be logged in");
        e.printStackTrace();
        return "error";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrityViolation(DataIntegrityViolationException e, Model model) {
        String message = e.getRootCause() != null ? e.getRootCause().getMessage() : e.getMessage();
        if (message.contains("null value") || message.contains("not-null constraint")) {
            model.addAttribute("exception", "Something is missing. Check that you wrote in everything");
        } else if (message.contains("duplicate key") || message.contains("Duplicate entry")) {
            model.addAttribute("exception", "Post already exists – use another ID or email.");
        } else if (message.contains("foreign key constraint")) {
            model.addAttribute("exception", "The action could not be completed due to dependencies in the database.");
            e.printStackTrace();
        } else {
            model.addAttribute("exception", "A database error occurred – please try again.");
        }
        return "error";
    }

    @ExceptionHandler
    public String handleNoResourceFoundException(NoResourceFoundException e, Model model) {
        model.addAttribute("exception", "Page does not exist or you are not logged in. Error: " + e.getClass().getSimpleName() + ": " + e.getMessage());
        return "error";
    }
}

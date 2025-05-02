package com.zwt.controller;

import com.zwt.model.User;
import com.zwt.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> getProfile(@RequestHeader("user-id") Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.ok(userOptional.get());
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(@RequestHeader("user-id") Long userId, @RequestBody User updatedUser) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        User existingUser = userOptional.get();
        // Only update fields that are allowed to be updated
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setUsername(updatedUser.getUsername());
        userRepository.save(existingUser);
        return ResponseEntity.ok(existingUser);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestHeader("user-id") Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        User user = userOptional.get();
        user.setEmailVerified(true);
        userRepository.save(user);
        return ResponseEntity.ok("Email verified successfully");
    }
}

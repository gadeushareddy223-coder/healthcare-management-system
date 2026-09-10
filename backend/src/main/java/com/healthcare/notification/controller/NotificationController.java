package com.healthcare.notification.controller;

import com.healthcare.notification.Notification;
import com.healthcare.notification.repository.NotificationRepository;
import com.healthcare.user.User;
import com.healthcare.user.repository.UserRepository;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationController(
            NotificationRepository notificationRepository,
            UserRepository userRepository) {

        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    /*
     * GET ALL NOTIFICATIONS FOR LOGGED-IN USER
     */
    @GetMapping
    public ResponseEntity<List<Notification>> getMyNotifications(
            Authentication authentication) {

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        List<Notification> notifications =
                notificationRepository
                        .findByUserOrderByCreatedAtDesc(user);

        return ResponseEntity.ok(notifications);
    }

    /*
     * GET UNREAD NOTIFICATIONS
     */
    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(
            Authentication authentication) {

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        List<Notification> notifications =
                notificationRepository
                        .findByUserAndIsReadFalseOrderByCreatedAtDesc(user);

        return ResponseEntity.ok(notifications);
    }

    /*
     * CREATE NOTIFICATION
     */
    @PostMapping
    public ResponseEntity<?> createNotification(
            @Valid @RequestBody NotificationRequest request) {

        User user = userRepository.findById(request.userId())
                .orElse(null);

        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "error",
                            "User not found"
                    ));
        }

        Notification notification = new Notification();

        notification.setUser(user);
        notification.setMessage(request.message());
        notification.setType(request.type());
        notification.setIsRead(false);

        Notification savedNotification =
                notificationRepository.save(notification);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedNotification);
    }

    /*
     * MARK NOTIFICATION AS READ
     */
    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(
            @PathVariable Long id,
            Authentication authentication) {

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Notification notification =
                notificationRepository.findById(id)
                        .orElse(null);

        if (notification == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "error",
                            "Notification not found"
                    ));
        }

        if (!notification.getUser().getId().equals(user.getId())) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "error",
                            "You cannot update this notification"
                    ));
        }

        notification.setIsRead(true);

        Notification updatedNotification =
                notificationRepository.save(notification);

        return ResponseEntity.ok(updatedNotification);
    }

    /*
     * DELETE NOTIFICATION
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(
            @PathVariable Long id,
            Authentication authentication) {

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Notification notification =
                notificationRepository.findById(id)
                        .orElse(null);

        if (notification == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "error",
                            "Notification not found"
                    ));
        }

        if (!notification.getUser().getId().equals(user.getId())) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "error",
                            "You cannot delete this notification"
                    ));
        }

        notificationRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    /*
     * REQUEST BODY FOR CREATING NOTIFICATION
     */
    public record NotificationRequest(
            Long userId,
            String message,
            String type
    ) {
    }
}
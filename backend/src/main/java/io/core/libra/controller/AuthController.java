package io.core.libra.controller;

import io.core.libra.dtos.ApiResponse;
import io.core.libra.dtos.UserModel;
import io.core.libra.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/auth")
@Validated
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserModel>> createUser(@Valid @RequestBody UserModel userModel){
        ApiResponse<UserModel> response = userService.createUser(userModel);
        HttpStatus status = response.getStatus() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<UserModel>> findUserByEmail(@Email @RequestParam("email") String email){
        ApiResponse<UserModel> response = userService.userSearchByEmail(email);
        HttpStatus status = response.getStatus() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(response, status);
    }
}

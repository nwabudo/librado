package io.core.libra.controller;

import io.core.libra.dtos.ApiResponse;
import io.core.libra.dtos.UserDTO;
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
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@Valid @RequestBody UserDTO userDTO){
        ApiResponse<UserDTO> response = userService.createUser(userDTO);
        HttpStatus status = response.getStatus() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<UserDTO>> findUserByEmail(@Email @RequestParam("email") String email){
        ApiResponse<UserDTO> response = userService.userSearchByEmail(email);
        HttpStatus status = response.getStatus() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(response, status);
    }
}

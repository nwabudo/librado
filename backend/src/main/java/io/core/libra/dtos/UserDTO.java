package io.core.libra.dtos;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserDTO {

    private long id;

    @Email(message = "Enter a valid Email")
    @EqualsAndHashCode.Include
    private String email;

    @NotEmpty(message = "FirstName cannot be Empty")
    private String firstName;

    @NotEmpty(message = "LastName cannot be Empty")
    private String lastName;

    public UserDTO(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}

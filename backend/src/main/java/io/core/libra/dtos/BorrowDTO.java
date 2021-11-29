package io.core.libra.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowDTO {

    @NotEmpty
    private String isbnCode;

    @NotNull
    private long userId;
}

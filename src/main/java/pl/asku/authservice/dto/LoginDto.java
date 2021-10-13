package pl.asku.authservice.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 3, max = 100)
    private String password;
}

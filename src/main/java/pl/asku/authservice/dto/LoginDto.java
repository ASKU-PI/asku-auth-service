package pl.asku.authservice.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

   @NotNull
   @Pattern(regexp =  "^[a-zA-Z0-9.\\-\\/@_$]*$")
   @Size(min = 3, max = 100)
   private String username;

   @NotNull
   @Size(min = 3, max = 100)
   private String password;
}

package com.consumerservice.dtos;

import jakarta.validation.constraints.*;

public class UserDTO {
    @NotBlank(message = "O nome é obrigatório")
    private String name;

    @NotBlank(message = "O email é obrigatório") 
    @Email(message = "Email inválido")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
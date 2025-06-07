package com.consumerservice.dtos;

import jakarta.validation.constraints.*;

public class ProductUserDTO {
    @NotBlank(message = "O nome é obrigatório")
    private String name;

    @NotBlank(message = "A descrição é obrigatória")
    private String description;

    @NotNull(message = "O preço é obrigatório")
    @Positive(message = "O preço deve ser positivo")
    private Double price;

    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 0, message = "A quantidade não pode ser negativa")
    private Integer quantity;
    
    @NotBlank(message = "O nome do usuário é obrigatório")
    private String userName;

    @NotBlank(message = "O email do usuário é obrigatório")
    @Email(message = "Email inválido")
    private String userEmail;

    // getters e setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
}
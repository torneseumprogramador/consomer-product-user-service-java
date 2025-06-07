package com.consumerservice.dtos;

import jakarta.validation.constraints.*;

public class ProductDTO {
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

    @NotNull(message = "O userId é obrigatório")
    private Long userId;

    // getters e setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
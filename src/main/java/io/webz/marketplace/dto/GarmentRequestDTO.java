package io.webz.marketplace.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GarmentRequestDTO {

    @NotNull(message = "Garment type cannot be null")
    private String type;
    private String description;
    @NotNull(message = "Garment size cannot be null")
    @Size(min = 1, max = 5, message = "Size must be a valid size (e.g., S, M, L, XL)")
    private String size;
    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private Double price;

}

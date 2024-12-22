package io.webz.marketplace.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GarmentResponseDTO {

    private Long id;
    private String type;
    private String description;
    private String size;
    private Double price;
    private String publisherFullName;

}

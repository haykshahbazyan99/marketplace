package io.webz.marketplace.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.webz.marketplace.dto.GarmentRequestDTO;
import io.webz.marketplace.dto.GarmentResponseDTO;
import io.webz.marketplace.models.Garment;
import io.webz.marketplace.security.CustomUserDetails;
import io.webz.marketplace.services.GarmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/garments")
public class GarmentController {

    private final GarmentService garmentService;

    public GarmentController(GarmentService garmentService) {
        this.garmentService = garmentService;
    }


    @PostMapping
    @PreAuthorize("hasAuthority('PUBLISHER')")
    @Operation(summary = "Publish a garment: authenticated users only")
    public ResponseEntity<GarmentResponseDTO> publishGarment(@Valid @RequestBody GarmentRequestDTO garmentRequestDTO,
                                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        Garment garment = new Garment();
        garment.setType(garmentRequestDTO.getType());
        garment.setDescription(garmentRequestDTO.getDescription());
        garment.setSize(garmentRequestDTO.getSize());
        garment.setPrice(garmentRequestDTO.getPrice());
        garment.setPublisher(userDetails.getUser());

        GarmentResponseDTO publishedGarment = garmentService.publishGarment(garment, userDetails.getUsername());
        return ResponseEntity.ok(publishedGarment);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PUBLISHER')")
    @Operation(summary = "Update a garment: authenticated users only")
    public ResponseEntity<String> updateGarment(@PathVariable Long id,
                                                            @Valid @RequestBody GarmentRequestDTO garmentRequestDTO,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        garmentService.updateGarment(id, garmentRequestDTO, userDetails.getUsername());
        return ResponseEntity.ok("Garment successfully updated.");
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a garment: authenticated users only")
    public ResponseEntity<String> unpublishGarment(@PathVariable Long id,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        garmentService.unpublishGarment(id, userDetails.getUsername());
        return ResponseEntity.ok("Garment successfully deleted.");
    }

}

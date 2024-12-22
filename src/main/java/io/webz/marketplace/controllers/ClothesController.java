package io.webz.marketplace.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.webz.marketplace.dto.GarmentResponseDTO;
import io.webz.marketplace.services.GarmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clothes")
public class ClothesController {

    private final GarmentService garmentService;

    public ClothesController(GarmentService garmentService) {
        this.garmentService = garmentService;
    }


    @GetMapping
    @Operation(summary = "Retrieve clothes optionally by their type, size or price segment")
    public ResponseEntity<List<GarmentResponseDTO>> listClothes(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {

        List<GarmentResponseDTO> garments = garmentService.listClothes(type, size, minPrice, maxPrice);
        return ResponseEntity.ok(garments);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a garment by id")
    public ResponseEntity<GarmentResponseDTO> getClothesDetail(@PathVariable Long id) {
        GarmentResponseDTO garment = garmentService.getClothesDetail(id);
        return ResponseEntity.ok(garment);
    }

}

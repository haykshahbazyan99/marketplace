package io.webz.marketplace.services;

import io.webz.marketplace.dto.GarmentRequestDTO;
import io.webz.marketplace.dto.GarmentResponseDTO;
import io.webz.marketplace.models.Garment;
import io.webz.marketplace.models.User;
import io.webz.marketplace.repositories.GarmentRepository;
import io.webz.marketplace.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GarmentService {

    private static final Logger logger = LoggerFactory.getLogger(GarmentService.class);

    private final GarmentRepository garmentRepository;
    private final UserRepository userRepository;

    public GarmentService(GarmentRepository garmentRepository, UserRepository userRepository) {
        this.garmentRepository = garmentRepository;
        this.userRepository = userRepository;
    }


    @Transactional
    public GarmentResponseDTO publishGarment(Garment garment, String username) {
        User publisher = this.userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User not found: {}", username);
                    return new UsernameNotFoundException("User not found");
                });

        garment.setPublisher(publisher);
        Garment savedGarment = this.garmentRepository.save(garment);
        logger.info("Garment published successfully with ID: {}", savedGarment.getId());

        return GarmentResponseDTO.builder()
                .id(savedGarment.getId())
                .type(savedGarment.getType())
                .description(savedGarment.getDescription())
                .size(savedGarment.getSize())
                .price(savedGarment.getPrice())
                .publisherFullName(savedGarment.getPublisher().getFullName())
                .build();
    }


    @Transactional
    public void updateGarment(Long garmentId, GarmentRequestDTO garmentRequestDTO, String username) {
        Garment garment = garmentRepository.findById(garmentId)
                .orElseThrow(() -> {
                    logger.error("Garment not found with ID: {}", garmentId);
                    return new EntityNotFoundException("Garment not found");
                });

        if (!garment.getPublisher().getUsername().equals(username)) {
            logger.error("Access denied for user: {} to update garment with ID: {}", username, garmentId);
            throw new RuntimeException("You can only update your own garments");
        }

        garment.setType(garmentRequestDTO.getType());
        garment.setDescription(garmentRequestDTO.getDescription());
        garment.setSize(garmentRequestDTO.getSize());
        garment.setPrice(garmentRequestDTO.getPrice());

        this.garmentRepository.save(garment);
        logger.info("Garment with ID: {} updated successfully", garmentId);
    }

    @Transactional
    public void unpublishGarment(Long garmentId, String username) {
        Garment garment = garmentRepository.findById(garmentId)
                .orElseThrow(() -> {
                    logger.error("Garment not found with ID: {}", garmentId);
                    return new EntityNotFoundException("Garment not found");
                });

        if (!garment.getPublisher().getUsername().equals(username)) {
            logger.error("Access denied for user: {} to unpublish garment with ID: {}", username, garmentId);
            throw new RuntimeException("You can only unpublish your own garments");
        }

        this.garmentRepository.delete(garment);
        logger.info("Garment with ID: {} unpublished successfully", garmentId);
    }


    @Transactional(readOnly = true)
    public List<GarmentResponseDTO> listClothes(String type, String size, Double minPrice, Double maxPrice) {
        List<Garment> garments = this.garmentRepository.findClothes(type, size, minPrice, maxPrice);

        return garments.stream()
                .map(garment -> GarmentResponseDTO.builder()
                        .id(garment.getId())
                        .type(garment.getType())
                        .description(garment.getDescription())
                        .size(garment.getSize())
                        .price(garment.getPrice())
                        .publisherFullName(garment.getPublisher().getFullName())
                        .build())
                .toList();
    }


    @Transactional(readOnly = true)
    public GarmentResponseDTO getClothesDetail(Long id) {
        Garment garment = this.garmentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Garment not found with ID: {}", id);
                    return new EntityNotFoundException("Garment not found");
                });

        return GarmentResponseDTO.builder()
                .id(garment.getId())
                .type(garment.getType())
                .description(garment.getDescription())
                .size(garment.getSize())
                .price(garment.getPrice())
                .publisherFullName(garment.getPublisher().getFullName())
                .build();
    }

}

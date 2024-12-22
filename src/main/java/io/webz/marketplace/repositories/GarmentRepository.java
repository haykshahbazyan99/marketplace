package io.webz.marketplace.repositories;

import io.webz.marketplace.models.Garment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GarmentRepository extends JpaRepository<Garment, Long> {

    @Query("SELECT g FROM Garment g WHERE " +
            "(:type IS NULL OR g.type = :type) AND " +
            "(:size IS NULL OR g.size = :size) AND " +
            "(:minPrice IS NULL OR g.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR g.price <= :maxPrice)")
    List<Garment> findClothes(@Param("type") String type, @Param("size") String size,
                              @Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

}

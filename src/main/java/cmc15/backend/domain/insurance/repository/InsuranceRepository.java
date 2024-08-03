package cmc15.backend.domain.insurance.repository;

import cmc15.backend.domain.insurance.entity.Insurance;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsuranceRepository extends JpaRepository<Insurance, Long> {

    @Query(value = "SELECT * FROM insurance WHERE insurance_type = :insuranceType ORDER BY RAND() LIMIT 4", nativeQuery = true)
    List<Insurance> findRandomByInsuranceType(@Param("insuranceType") String insuranceType);

}

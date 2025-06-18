package com.example.HealPoint.repository;

import com.example.HealPoint.entity.Specialist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecialistRepository extends JpaRepository<Specialist, String> {

    @Query(value = "SELECT * FROM specialist s WHERE " +
            "LOWER(s.speciality) LIKE LOWER(CONCAT('%', :search, '%')) ",
            nativeQuery = true)
    List<Specialist> searchAll(@Param("search") String search);


    List<Specialist> findAllBySpecialistIdIn(List<String> specialistIdsFromModel);
}

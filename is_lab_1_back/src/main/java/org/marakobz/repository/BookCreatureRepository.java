package org.marakobz.repository;

import org.marakobz.model.BookCreature;
import org.marakobz.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

@Repository
public interface BookCreatureRepository extends JpaRepository<BookCreature, Long> {

    List<BookCreature> findByName(String name);

    List<BookCreature> findByCreatureType(String creatureType);

    List<BookCreature> findByAttackLevelGreaterThan(Float attackLevel);

    List<BookCreature> findByDefenseLevelLessThan(double defenseLevel);

    List<BookCreature> findByCreatureLocation_Id(Long locationId);

    @Query("SELECT bc FROM BookCreature bc WHERE bc.ring.weight = (SELECT MIN(bc2.ring.weight) FROM BookCreature bc2)")
    BookCreature findBookCreatureWithMinRingWeight();

    @Query("SELECT COUNT(bc) FROM BookCreature bc WHERE bc.ring.name = :ringName")
    long countByRingName(@Param("ringName") String ringName);

    @Query("SELECT bc FROM BookCreature bc WHERE LOWER(bc.name) LIKE LOWER(CONCAT('%', :substring, '%'))")
    List<BookCreature> findByNameContaining(@Param("substring") String substring);

    @Query("SELECT bc FROM BookCreature bc WHERE bc.ring.weight = (SELECT MAX(bc2.ring.weight) FROM BookCreature bc2)")
    BookCreature findBookCreatureWithMaxRingWeight();

}

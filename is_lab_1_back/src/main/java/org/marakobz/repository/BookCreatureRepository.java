package org.marakobz.repository;

import org.marakobz.model.BookCreature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookCreatureRepository extends JpaRepository<BookCreature, Long> {

    List<BookCreature> findByName(String name);

    List<BookCreature> findByCreatureType(String creatureType);

    List<BookCreature> findByAttackLevelGreaterThan(Float attackLevel);

    List<BookCreature> findByDefenseLevelLessThan(double defenseLevel);

    List<BookCreature> findByCreatureLocation_Id(Long locationId);

}

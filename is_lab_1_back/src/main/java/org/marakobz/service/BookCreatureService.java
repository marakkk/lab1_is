package org.marakobz.service;

import org.marakobz.model.BookCreature;
import org.marakobz.model.BookCreatureType;
import org.marakobz.model.Ring;
import org.marakobz.model.User;
import org.marakobz.repository.BookCreatureRepository;
import org.marakobz.security.JWTUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookCreatureService {

    private final BookCreatureRepository bookCreatureRepository;

    public BookCreatureService(BookCreatureRepository bookCreatureRepository, AuthService authService) {
        this.bookCreatureRepository = bookCreatureRepository;

    }




    @Transactional
    public void save(BookCreature creature) {
        if (creature.getAge() <= 0) {
            throw new IllegalArgumentException("Age must be greater than zero");
        }
        bookCreatureRepository.save(creature);
    }


    @Transactional(readOnly = true)
    public List<BookCreature> findAll() {
        return bookCreatureRepository.findAll();
    }

    @Transactional(readOnly = true)
    public BookCreature findById(Long id) {
        return bookCreatureRepository.findById(id).orElse(null);
    }

    @Transactional
    public void update(BookCreature creature) {
        bookCreatureRepository.save(creature);
    }

    @Transactional
    public void delete(Long id) {
        bookCreatureRepository.deleteById(id);
    }

    @Transactional
    public void deleteByAttackLevel(Float attackLevel) {
        List<BookCreature> creatures = bookCreatureRepository.findByAttackLevelGreaterThan(attackLevel);
        bookCreatureRepository.deleteAll(creatures);
    }

    @Transactional(readOnly = true)
    public List<BookCreature> findByFilters(String name, Long age) {
        if (name != null && !name.isEmpty()) {
            return bookCreatureRepository.findByName(name);
        }
        return findAll();
    }

    @Transactional
    public BookCreature findCreatureWithMinRingWeight() {
        return bookCreatureRepository.findBookCreatureWithMinRingWeight();
    }

    @Transactional(readOnly = true)
    public long countCreaturesByRing(String ringName) {
        return bookCreatureRepository.countByRingName(ringName);
    }

    @Transactional(readOnly = true)
    public List<BookCreature> findCreaturesByNameSubstring(String substring) {
        return bookCreatureRepository.findByNameContaining(substring);
    }


    @Transactional
    public void exchangeRings(Long id1, Long id2) {
        BookCreature creature1 = findById(id1);
        BookCreature creature2 = findById(id2);

        if (creature1 == null || creature2 == null) {
            throw new IllegalArgumentException("One or both creatures not found");
        }

        Ring tempRing = creature1.getRing();
        creature1.setRing(creature2.getRing());
        creature2.setRing(tempRing);

        update(creature1);
        update(creature2);
    }

    @Transactional(readOnly = true)
    public BookCreature findCreatureWithMaxRingWeight() {
        return bookCreatureRepository.findBookCreatureWithMaxRingWeight();
    }


    @Transactional(readOnly = true)
    public List<BookCreature> findByFilters(String name, Long age, String creatureType, Float location,
                                            Long population, Double populationDensity,
                                            Float attackLevel, Double defenseLevel,
                                            Float ringPower, Float ringWeight) {
        List<BookCreature> creatures = bookCreatureRepository.findAll();

        if (name != null && !name.isEmpty()) {
            creatures = creatures.stream()
                    .filter(creature -> creature.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (age != null) {
            creatures = creatures.stream()
                    .filter(creature -> creature.getAge() != null && creature.getAge().equals(age))
                    .collect(Collectors.toList());
        }

        if (creatureType != null && !creatureType.isEmpty()) {
            try {
                BookCreatureType.valueOf(creatureType);
                creatures = creatures.stream()
                        .filter(creature -> creature.getCreatureType().name().equalsIgnoreCase(creatureType))
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid creature type: " + creatureType);
            }
        }

        if (location != null ) {
            creatures = creatures.stream()
                    .filter(creature -> creature.getCreatureLocation() != null &&
                            creature.getCreatureLocation().equals(location))
                    .collect(Collectors.toList());
        }

        if (attackLevel != null) {
            creatures = creatures.stream()
                    .filter(creature -> creature.getAttackLevel() != null && creature.getAttackLevel() >= attackLevel)
                    .collect(Collectors.toList());
        }

        if (defenseLevel != null) {
            creatures = creatures.stream()
                    .filter(creature -> creature.getDefenseLevel() >= defenseLevel)
                    .collect(Collectors.toList());
        }

        // Filter for population in MagicCity
        if (population != null) {
            creatures = creatures.stream()
                    .filter(creature -> creature.getCreatureLocation() != null &&
                            creature.getCreatureLocation().getPopulation() == population)
                    .collect(Collectors.toList());
        }

        // Filter for population density in MagicCity
        if (populationDensity != null) {
            creatures = creatures.stream()
                    .filter(creature -> creature.getCreatureLocation() != null &&
                            creature.getCreatureLocation().getPopulationDensity() != null &&
                            creature.getCreatureLocation().getPopulationDensity().equals(populationDensity))
                    .collect(Collectors.toList());
        }

        // The ringPower and ringWeight filters would depend on the Ring class
        if (ringPower != null) {
            creatures = creatures.stream()
                    .filter(creature -> creature.getRing() != null &&
                            creature.getRing().getPower() != null &&
                            creature.getRing().getPower().equals(ringPower))
                    .collect(Collectors.toList());
        }

        if (ringWeight != null) {
            creatures = creatures.stream()
                    .filter(creature -> creature.getRing() != null &&
                            creature.getRing().getWeight() != null &&
                            creature.getRing().getWeight().equals(ringWeight))
                    .collect(Collectors.toList());
        }

        return creatures;
    }





}

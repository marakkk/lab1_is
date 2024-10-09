package org.marakobz.service;

import org.marakobz.model.BookCreature;
import org.marakobz.repository.BookCreatureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookCreatureService {

    private final BookCreatureRepository bookCreatureRepository;
    public BookCreatureService(BookCreatureRepository bookCreatureRepository) {
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

}

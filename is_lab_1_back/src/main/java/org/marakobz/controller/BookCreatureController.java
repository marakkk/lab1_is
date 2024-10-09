package org.marakobz.controller;

import org.marakobz.model.BookCreature;
import org.marakobz.model.BookDto;
import org.marakobz.service.BookCreatureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/book-creatures")
public class BookCreatureController {

    private final BookCreatureService bookCreatureService;

    public BookCreatureController(BookCreatureService bookCreatureService) {
        this.bookCreatureService = bookCreatureService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<BookCreature>> getAllCreatures() {
        List<BookCreature> creatures = bookCreatureService.findAll();
        return ResponseEntity.ok(creatures);
    }

    @PostMapping
    public ResponseEntity<Void> createBookCreature(@RequestBody BookCreature creature) {
        System.out.println(creature.toString());
        if (creature == null || creature.getName() == null || creature.getAge() <= 0) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }

        bookCreatureService.save(creature);
        return new ResponseEntity<>(HttpStatus.CREATED); // 201 Created
    }


    @PutMapping("/{id}")
    public ResponseEntity<BookCreature> updateBookCreature(
            @PathVariable("id") Long id, @RequestBody BookDto updatedCreature) { // BookCreature

        if (updatedCreature == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // 400 Bad Request
        }

        var oldBook = bookCreatureService.findById(id);
        oldBook.setName(updatedCreature.getName());
        oldBook.setCoordinates(updatedCreature.getCoordinates());
        oldBook.setAge(updatedCreature.getAge());
        oldBook.setCreatureType(updatedCreature.getCreatureType());
        oldBook.setCreatureLocation(updatedCreature.getCreatureLocation());
        oldBook.setAttackLevel(updatedCreature.getAttackLevel());
        oldBook.setDefenseLevel(updatedCreature.getDefenseLevel());
        oldBook.setRing(updatedCreature.getRing());
        bookCreatureService.update(oldBook);

        return ResponseEntity.ok(oldBook); // 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        bookCreatureService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK); // 200 OK
    }

    @DeleteMapping("/delete-by-attack-level")
    public ResponseEntity<Void> deleteByAttackLevel(@RequestParam("attackLevel") Float attackLevel) {
        bookCreatureService.deleteByAttackLevel(attackLevel);
        return new ResponseEntity<>(HttpStatus.OK); // 200 OK
    }

    @GetMapping("/find")
    public ResponseEntity<List<BookCreature>> findByFilters(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "age", required = false) Long age) {

        List<BookCreature> creatures = bookCreatureService.findByFilters(name, age);
        return ResponseEntity.ok(creatures); // 200 OK
    }


}

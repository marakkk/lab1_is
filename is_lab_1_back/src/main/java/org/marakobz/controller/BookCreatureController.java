package org.marakobz.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.marakobz.model.BookCreature;
import org.marakobz.model.BookDto;
import org.marakobz.model.Ring;
import org.marakobz.model.User;
import org.marakobz.repository.UserRepository;
import org.marakobz.security.JWTUtil;
import org.marakobz.service.AuthService;
import org.marakobz.service.BookCreatureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/book-creatures")
public class BookCreatureController {

    private final BookCreatureService bookCreatureService;
    private final AuthService authService;
    private static final Logger logger = LoggerFactory.getLogger(BookCreatureController.class);
    private final UserRepository userRepository;

    // Constructor without JWTUtil
    public BookCreatureController(BookCreatureService bookCreatureService, AuthService authService, UserRepository userRepository) {
        this.bookCreatureService = bookCreatureService;
        this.authService = authService;
        this.userRepository = userRepository;
    }



    @GetMapping("/admin")
    public ResponseEntity<List<User>> getAdminUsers() {
        List<User> adminUsers = authService.findAllAdminUsers();
        return ResponseEntity.ok(adminUsers);
    }

    @PostMapping("/users/approve")
    public ResponseEntity<Void> approveUser(@RequestBody User user) {
        logger.info("Approving user: {}", user.getUsername());
        try {

            User existingUser = authService.getUserByUsername(user.getUsername());

            if (existingUser == null) {
                logger.error("User not found: {}", user.getUsername());
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }


            existingUser.setIsApproved(user.isApproved());
            existingUser.setIsAdmin(user.isAdmin());


            userRepository.save(existingUser);

            logger.info("User {} approved with isAdmin={}, isApproved={}", existingUser.getUsername(), existingUser.isAdmin(), existingUser.isApproved());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error approving user: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/all")
    public ResponseEntity<List<BookDto>> getAllCreatures() {
        List<BookCreature> creatures = bookCreatureService.findAll();

        // Convert BookCreature to BookDto, including the creator's name
        List<BookDto> bookDtos = creatures.stream().map(creature -> {
            BookDto dto = new BookDto();
            dto.setName(creature.getName());
            dto.setCoordinates(creature.getCoordinates());
            dto.setAge(creature.getAge());
            dto.setCreatureType(creature.getCreatureType());
            dto.setCreatureLocation(creature.getCreatureLocation());
            dto.setAttackLevel(creature.getAttackLevel());
            dto.setDefenseLevel(creature.getDefenseLevel());
            dto.setRing(creature.getRing());
            dto.setApproveUpdates(creature.isApproveUpdates());

            dto.setCreatorName(creature.getCreator() != null ? creature.getCreator().getUsername() : null);


            logger.info("Mapping creature {} to DTO with creator {}", creature.getName(), creature.getCreator() != null ? creature.getCreator().getUsername() : "null");

            return dto;
        }).toList();



        return ResponseEntity.ok(bookDtos);
    }




    @PostMapping
    public ResponseEntity<Void> createBookCreature(@RequestBody BookCreature creature, HttpServletRequest request) {

        // Extract username from the JWT token
        String username = JWTUtil.extractUsernameFromRequest(request);
        if (username == null) {
            logger.warn("Unauthorized access attempt: No username extracted from token.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Retrieve the User object based on the username
        User creator = authService.getUserByUsername(username);
        if (creator == null) {
            logger.warn("User not found: {}", username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Validate the incoming BookCreature object
        if (creature == null || creature.getName() == null || creature.getAge() <= 0) {
            logger.warn("Invalid BookCreature data: {}", creature);
            return ResponseEntity.badRequest().build();
        }


        creature.setCreator(creator);
        logger.info("Creator set for creature: {}", creator.getUsername());

        creature.setApproveUpdates(creature.isApproveUpdates());


        bookCreatureService.save(creature);
        logger.info("BookCreature created successfully: {}", creature);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookCreature> updateBookCreature(
            @PathVariable("id") Long id, @RequestBody BookDto updatedCreature, HttpServletRequest request) {

        String username = JWTUtil.extractUsernameFromRequest(request);
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = authService.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        BookCreature existingCreature = bookCreatureService.findById(id);
        if (existingCreature == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Check if the user is allowed to update: they must be the creator or have "isApproved" permission
        if (!existingCreature.getCreator().getUsername().equals(user.getUsername()) && !user.isApproved()) {
            logger.warn("User {} is not allowed to update creature {}", username, existingCreature.getId());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden if not the owner or approved user
        }

        // Additional condition: approved users can only update if the creature has "approveUpdates" set to true
        if (user.isApproved() && !Boolean.TRUE.equals(existingCreature.isApproveUpdates())) {
            logger.warn("User {} is approved but cannot update creature {} because approveUpdates is not true", username, existingCreature.getId());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden if approveUpdates is false
        }

        // Proceed to update the creature's details
        existingCreature.setName(updatedCreature.getName());
        existingCreature.setCoordinates(updatedCreature.getCoordinates());
        existingCreature.setAge(updatedCreature.getAge());
        existingCreature.setCreatureType(updatedCreature.getCreatureType());
        existingCreature.setCreatureLocation(updatedCreature.getCreatureLocation());
        existingCreature.setAttackLevel(updatedCreature.getAttackLevel());
        existingCreature.setDefenseLevel(updatedCreature.getDefenseLevel());
        existingCreature.setRing(updatedCreature.getRing());

        try {
            bookCreatureService.update(existingCreature);
        } catch (Exception e) {
            logger.error("Error updating creature: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(existingCreature);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id, HttpServletRequest request) {
        String username = JWTUtil.extractUsernameFromRequest(request);
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = authService.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        BookCreature existingCreature = bookCreatureService.findById(id);
        if (existingCreature == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (!existingCreature.getCreator().getUsername().equals(user.getUsername()) && !user.isApproved() ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        bookCreatureService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/find")
    public ResponseEntity<List<BookCreature>> findByFilters(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "age", required = false) Long age,
            @RequestParam(value = "creatureType", required = false) String creatureType,
            @RequestParam(value = "location", required = false) Float location,
            @RequestParam(value = "population", required = false) Long population,
            @RequestParam(value = "populationDensity", required = false) Double populationDensity,
            @RequestParam(value = "attackLevel", required = false) Float attackLevel,
            @RequestParam(value = "defenseLevel", required = false) Double defenseLevel,
            @RequestParam(value = "ringPower", required = false) Float ringPower,
            @RequestParam(value = "ringWeight", required = false) Float ringWeight) {

        List<BookCreature> creatures = bookCreatureService.findByFilters(
                name != null && !name.isEmpty() ? name : null,
                age,
                creatureType != null && !creatureType.isEmpty() ? creatureType : null,
                location,
                population,
                populationDensity,
                attackLevel,
                defenseLevel,
                ringPower,
                ringWeight
        );

        return ResponseEntity.ok(creatures); // 200 OK
    }

    @GetMapping("/min-ring-weight")
    public ResponseEntity<BookCreature> getBookCreatureWithMinRingWeight() {
        BookCreature creature = bookCreatureService.findCreatureWithMinRingWeight();
        if (creature != null) {
            return ResponseEntity.ok(creature);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/count-by-ring")
    public ResponseEntity<Long> getCreatureCountByRing(@RequestParam("ringName") String ringName) {
        try {
            long count = bookCreatureService.countCreaturesByRing(ringName);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            logger.error("Error fetching count for ring {}: {}", ringName, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/search-by-name")
    public ResponseEntity<List<BookCreature>> findByNameSubstring(
            @RequestParam("substring") String substring) {
        try {
            List<BookCreature> creatures = bookCreatureService.findCreaturesByNameSubstring(substring);
            return ResponseEntity.ok(creatures);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/exchange-rings")
    public ResponseEntity<Void> exchangeRings(@RequestParam("id1") Long id1, @RequestParam("id2") Long id2) {
        logger.info("Exchanging rings between creatures with IDs: {} and {}", id1, id2);

        BookCreature creature1 = bookCreatureService.findById(id1);
        BookCreature creature2 = bookCreatureService.findById(id2);

        if (creature1 == null || creature2 == null) {
            logger.error("One or both creatures not found: ID1: {}, ID2: {}", id1, id2);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Perform the ring exchange
        Ring tempRing = creature1.getRing();
        creature1.setRing(creature2.getRing());
        creature2.setRing(tempRing);

        try {
            bookCreatureService.update(creature1);
            bookCreatureService.update(creature2);
            logger.info("Rings exchanged successfully between creatures {} and {}", id1, id2);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error during ring exchange: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/max-ring-weight")
    public ResponseEntity<BookCreature> getBookCreatureWithMaxRingWeight() {
        BookCreature creature = bookCreatureService.findCreatureWithMaxRingWeight();
        if (creature != null) {
            return ResponseEntity.ok(creature);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}



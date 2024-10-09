package org.marakobz.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalTime;

@RestController
public class UpdateSSEController {

    @GetMapping(value = "/api/book-creatures/updates", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamUpdates() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> "Update at " + LocalTime.now().toString());
    }
}


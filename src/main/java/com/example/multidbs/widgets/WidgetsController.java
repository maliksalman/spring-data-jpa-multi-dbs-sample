package com.example.multidbs.widgets;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/widgets")
public class WidgetsController {

    private final WidgetsRepository repository;

    public WidgetsController(WidgetsRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Widget add(@RequestParam("name") String name,
                    @RequestParam("width") int width,
                    @RequestParam("height") int height,
                    @RequestParam("unit") String unit) {
        return repository.save(Widget.builder()
                .name(name)
                .dimensions(new Dimensions(width, height, unit))
                .build());
    }

    @GetMapping
    public Iterable<Widget> list() {
        return repository.findAll();
    }
}

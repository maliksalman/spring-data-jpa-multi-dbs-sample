package com.example.multidbs.heroes;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/heroes")
public class HeroesController {

    private final HeroRepository repository;

    public HeroesController(HeroRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Hero add(@RequestParam("name") String name,
                    @RequestParam("age") int age) {
        return repository.save(Hero.builder()
                .age(age)
                .name(name)
                .build());
    }

    @GetMapping
    public Iterable<Hero> list() {
        return repository.findAll();
    }
}

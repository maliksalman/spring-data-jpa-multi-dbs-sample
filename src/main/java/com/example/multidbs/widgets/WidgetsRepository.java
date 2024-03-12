package com.example.multidbs.widgets;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WidgetsRepository extends CrudRepository<Widget, Long> {
}
package com.exercise.depthchart.repository;

import com.exercise.depthchart.model.Sport;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SportRepository extends JpaRepository<Sport, Integer> {

  List<Sport> findByName(String name);
}

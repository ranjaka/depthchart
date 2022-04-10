package com.sportsbet.depthchart.repository;

import com.sportsbet.depthchart.model.Sport;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SportRepository extends JpaRepository<Sport, Integer> {

  List<Sport> findByName(String name);
}

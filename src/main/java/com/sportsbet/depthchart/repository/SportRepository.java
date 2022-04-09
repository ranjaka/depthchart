package com.sportsbet.depthchart.repository;

import com.sportsbet.depthchart.model.Sport;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SportRepository extends JpaRepository<Sport, String> {

  Optional<Sport> findByName(String name);
}

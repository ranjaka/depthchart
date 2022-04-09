package com.sportsbet.depthchart.repository;

import com.sportsbet.depthchart.model.Position;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position, String> {

  Optional<Position> findByName(String name);

  List<Position> findBySport(String sport);
}

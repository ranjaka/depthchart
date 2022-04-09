package com.sportsbet.depthchart.repository;

import com.sportsbet.depthchart.model.Player;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {

  Optional<Player> findById(Integer id);

  List<Player> findByName(String name);
}

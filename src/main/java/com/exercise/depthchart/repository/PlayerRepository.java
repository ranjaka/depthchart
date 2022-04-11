package com.exercise.depthchart.repository;

import com.exercise.depthchart.model.Player;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {

  Optional<Player> findById(Integer id);

  List<Player> findByName(String name);
}

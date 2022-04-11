package com.sportsbet.depthchart.repository.dao;

import com.sportsbet.depthchart.model.Player;
import com.sportsbet.depthchart.repository.PlayerRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerDao {

  @Autowired PlayerRepository playerRepository;

  public Player savePlayer(Player player) {

    return playerRepository.saveAndFlush(player);
  }

  public void deletePlayer(Player player) {

    playerRepository.delete(player);
  }

  public Optional<Player> getPlayerById(Integer id) {

    return playerRepository.findById(id);
  }

  public List<Player> getPlayerByName(String name) {
    return playerRepository.findByName(name);
  }
}

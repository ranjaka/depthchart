package com.sportsbet.depthchart.repository.dao;

import com.sportsbet.depthchart.model.Player;
import com.sportsbet.depthchart.repository.PlayerRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerDao {

  @Autowired PlayerRepository playerRepository;

  public void savePlayer(Player player) {

    playerRepository.saveAndFlush(player);
  }

  public Player getPlayerById(Integer id) throws Exception {

    var player = playerRepository.findById(id);

    if (player.isEmpty()) {
      throw new Exception("Player not found for the given player id");
    } else return player.get();
  }

  public List<Player> getPlayerByName(String name) throws Exception {
    var players = playerRepository.findByName(name);

    if (players.isEmpty()) {
      throw new Exception("Player not found for given name");
    } else return players;
  }
}

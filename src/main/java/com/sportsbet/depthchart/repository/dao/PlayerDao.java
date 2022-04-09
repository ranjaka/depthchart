package com.sportsbet.depthchart.repository.dao;

import com.sportsbet.depthchart.model.Player;
import com.sportsbet.depthchart.repository.PlayerRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerDao {

  @Autowired PlayerRepository playerRepository;

  public void savePlayer(Player player) {
    Player pl = Player.builder().name(player.getName()).position(player.getPosition()).build();
    playerRepository.saveAndFlush(pl);
  }

  public Optional<Player> getPlayerById(Integer id) {
    return playerRepository.findById(id);
  }
}

package com.sportsbet.depthchart.repository.dao;

import com.sportsbet.depthchart.model.Player;
import com.sportsbet.depthchart.repository.PlayerRepository;
import com.sportsbet.depthchart.repository.PositionRepository;
import com.sportsbet.depthchart.repository.SportRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerDao {

  @Autowired PlayerRepository playerRepository;
  @Autowired PositionRepository positionRepository;

  @Autowired SportRepository sportRepository;

  public Integer savePlayer(Player player) {

    var playerWithId = playerRepository.saveAndFlush(player);

    //    var position = positionRepository.findByName(positionName);
    var position = player.getPosition();
    var currentPlayers = position.getPlayers();
    var currentPlayerCount = currentPlayers.size();
    var depth = player.getDepth();
    if (depth == null) {

      if (currentPlayerCount == 0) {
        player.setDepth(0);
        currentPlayers.add(player);
      } else {
        player.setDepth(currentPlayerCount);
        currentPlayers.add(player.getDepth(), player);
      }

    } else {
      if (depth > currentPlayerCount) {
        player.setDepth(currentPlayerCount);
        currentPlayers.add(player.getDepth(), player);

      } else {

        currentPlayers.add(player.getDepth(), player);
      }
    }

    position.setPlayers(currentPlayers);
    var newPos = positionRepository.save(position);

    var listOfPlayers = newPos.getPlayers();
    for (Player pl : listOfPlayers) {
      var index = listOfPlayers.indexOf(pl);
      if (index != pl.getDepth()) {
        pl.setDepth(index);
        newPos.getPlayers().remove(index);
        newPos.getPlayers().add(pl.getDepth(), pl);
      }

      pl.setPosition(newPos);
      position = pl.getPosition();
      playerRepository.saveAndFlush(pl);
    }

    positionRepository.save(position);
    return playerWithId.getId();
  }

  public void deletePlayer(Player player, String positionName) {}

  public Optional<Player> getPlayerById(Integer id) {
    return playerRepository.findById(id);
  }
}

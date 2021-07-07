package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import model.interfaces.GameEngine;
import model.interfaces.Player;
import model.interfaces.PlayingCard;
import model.interfaces.PlayingCard.Suit;
import model.interfaces.PlayingCard.Value;
import view.interfaces.GameEngineCallback;

public class GameEngineImpl implements GameEngine {

	ArrayList<GameEngineCallback> listGameEngineCallBack;
	ArrayList<Player> listPlayer;
	Deque<PlayingCard> playingDeck;

	public GameEngineImpl() {
		listGameEngineCallBack = new ArrayList<>();
		listPlayer = new ArrayList<>();
		playingDeck = new LinkedList<>();
		createPlayingDeck();
	}

	private void createPlayingDeck() {
		Suit[] suits = PlayingCard.Suit.values();
		Value[] values = PlayingCard.Value.values();

		for (Suit suit : suits) {
			for (Value value : values) {
				this.playingDeck.add(new PlayingCardImpl(suit, value));
			}
		}
		this.getShuffledHalfDeck();
	}

	@Override
	public void dealPlayer(Player player, int delay) throws IllegalArgumentException {
		createPlayingDeck();
		int playerScore = 0;
		PlayingCard card;

		while (true) {
			card = this.playingDeck.pop();
			int updateScore = playerScore + card.getScore();

			if (updateScore <= GameEngine.BUST_LEVEL) {
				playerScore = updateScore;
			}

			if (updateScore < GameEngine.BUST_LEVEL) {
				for (GameEngineCallback gameEngineCallBack : this.listGameEngineCallBack) {
					gameEngineCallBack.nextCard(player, card, this);
				}
			} else {

				for (GameEngineCallback gameEngineCallBack : this.listGameEngineCallBack) {
					if (updateScore > GameEngine.BUST_LEVEL) {
						gameEngineCallBack.bustCard(player, card, this);
					}
					gameEngineCallBack.result(player, playerScore, this);
				}
				player.setResult(playerScore);
				break;
			}
		}

		// Delay
		try {
			Thread.sleep(delay);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	@Override
	public void dealHouse(int delay) throws IllegalArgumentException {
		createPlayingDeck();
		int houseScore = 0;
		PlayingCard card;

		while (true) {
			card = this.playingDeck.pop();
			int updateScore = houseScore + card.getScore();

			if (updateScore <= GameEngine.BUST_LEVEL) {
				houseScore = updateScore;
			}

			if (updateScore < GameEngine.BUST_LEVEL) {
				for (GameEngineCallback gameEngineCallBack : this.listGameEngineCallBack) {
					gameEngineCallBack.nextHouseCard(card, this);
				}
			} else {

				for (GameEngineCallback gameEngineCallBack : this.listGameEngineCallBack) {
					if (updateScore > GameEngine.BUST_LEVEL) {
						gameEngineCallBack.houseBustCard(card, this);
					}
					for (Player player: this.listPlayer) {
						this.applyWinLoss(player, houseScore);
					}
					gameEngineCallBack.houseResult(houseScore, this);
					
					
				}
				break;
			}

			// Delay
			try {
				Thread.sleep(delay);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}

	}

	@Override
	public void applyWinLoss(Player player, int houseResult) {
		
		if (player.getResult() > houseResult) {
			player.setPoints(player.getPoints() + player.getBet());
		} else {
			player.setPoints(player.getPoints() - player.getBet());
		}
	}

	@Override
	public void addPlayer(Player player) {
		int index = listPlayer.indexOf(player);
		if (index == -1) {
			this.listPlayer.add(player);
		} else {
			this.listPlayer.set(index, player);
		}
	}

	@Override
	public Player getPlayer(String id) {
		for (Player player : this.listPlayer) {
			if (player.getPlayerId().equals(id)) {
				return player;
			}
		}

		return null;
	}

	@Override
	public boolean removePlayer(Player player) {

		// find and remove the player
		Player playerToRemove = this.getPlayer(player.getPlayerId());
		if (playerToRemove != null) {
			this.listPlayer.remove(player);
			return true;
		}

		// can not find the player
		return false;
	}

	@Override
	public boolean placeBet(Player player, int bet) {
		// TODO Auto-generated method stub
		return player.setBet(bet);
	}

	@Override
	public void addGameEngineCallback(GameEngineCallback gameEngineCallback) {
		listGameEngineCallBack.add(gameEngineCallback);

	}

	@Override
	public boolean removeGameEngineCallback(GameEngineCallback gameEngineCallback) {
		int index = this.listGameEngineCallBack.indexOf(gameEngineCallback);
		if (index == -1)
			return false;
		this.listGameEngineCallBack.remove(gameEngineCallback);
		return true;
	}

	@Override
	public Collection<Player> getAllPlayers() {

		ArrayList<Player> listPlayerCopy = new ArrayList<Player>(this.listPlayer);
		Collections.sort(listPlayerCopy);

		return listPlayerCopy;
	}

	@Override
	public Deque<PlayingCard> getShuffledHalfDeck() {

		Collections.shuffle((List<?>) this.playingDeck);

		return this.playingDeck;
	}

}

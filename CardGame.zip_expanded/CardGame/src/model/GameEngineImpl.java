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

	private void deal(Player player, int delay, boolean isPlayer) throws IllegalArgumentException {

		int score = 0; // for playerScore and houseScore

		if (delay <= 0 || (isPlayer && player == null)) {
			throw new IllegalArgumentException();
		}

		getShuffledHalfDeck();
		// int playerScore = 0;
		PlayingCard card;

		while (true) {
			card = this.playingDeck.pop();
			int updateScore = score + card.getScore();

			if (updateScore <= GameEngine.BUST_LEVEL) {
				score = updateScore;
			}

			if (updateScore < GameEngine.BUST_LEVEL) {
				for (GameEngineCallback gameEngineCallBack : this.listGameEngineCallBack) {

					if (player != null) {
						gameEngineCallBack.nextCard(player, card, this);
					} else {
						gameEngineCallBack.nextHouseCard(card, this);
					}
				}
			} else {

				for (GameEngineCallback gameEngineCallBack : this.listGameEngineCallBack) {
					if (updateScore > GameEngine.BUST_LEVEL) {
						if (player != null) {
							gameEngineCallBack.bustCard(player, card, this);
						} else {
							gameEngineCallBack.houseBustCard(card, this);
						}
					}
					if (player != null) {
						gameEngineCallBack.result(player, score, this);
					} else {
						for (Player playerItem : this.listPlayer) {
							this.applyWinLoss(playerItem, score);
						}
						gameEngineCallBack.houseResult(score, this);
					}

				}
				if (player != null) {
					player.setResult(score);
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
	public void dealPlayer(Player player, int delay) throws IllegalArgumentException {

		if (delay <= 0 || player == null) {
			throw new IllegalArgumentException();
		}
		deal(player, delay, true);

	}

	@Override
	public void dealHouse(int delay) throws IllegalArgumentException {
		deal(null, 100, false);

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

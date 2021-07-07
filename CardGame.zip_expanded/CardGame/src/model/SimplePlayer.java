package model;

import model.interfaces.Player;

public class SimplePlayer implements Player {
	private String playerId;
	private String playerName;
	private int points;
	
	private int bet;
	private int result;
	
	public SimplePlayer(String playerId, String playerName, int points) {
		this.playerId = playerId;
		this.playerName = playerName;
		this.points = points;
	}

	@Override
	public String getPlayerName() {
		return this.playerName;
	}

	@Override
	public void setPlayerName(String playerName) {
		this.playerName = playerName;

	}

	@Override
	public int getPoints() {
		
		return this.points;
	}

	@Override
	public void setPoints(int points) {
		this.points = points;

	}

	@Override
	public String getPlayerId() {
		
		return this.playerId;
	}

	@Override
	public boolean setBet(int bet) {
		if (bet > 0 && bet <= this.points) {
			this.bet = bet;
			return true;
		} 
		
		return false;
	}

	@Override
	public int getBet() {
		
		return this.bet;
	}

	@Override
	public void resetBet() {
		
		this.bet = 0;
	}

	@Override
	public int getResult() {
		
		return this.result;
	}

	@Override
	public void setResult(int result) {
		this.result = result;

	}

	@Override
	public boolean equals(Player player) {
		if (player.getPlayerId().equals(this.playerId)) {
			return true;
		}
		return false;
	}

	@Override
	public int compareTo(Player player) {
		
		return this.playerId.compareTo(player.getPlayerId());
	}

	@Override
	public String toString() {
		return String.format("Player: id=%s, name=%s, bet=%d, points=%d, RESULT .. %d", this.getPlayerId(), this.playerName
				,this.bet, this.points, this.result);
	}

}

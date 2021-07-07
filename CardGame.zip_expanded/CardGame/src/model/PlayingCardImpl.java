package model;

import com.sun.xml.internal.ws.util.StringUtils;

import model.interfaces.PlayingCard;

public class PlayingCardImpl implements PlayingCard {
	private Suit suit;
	private Value value;
	

	public PlayingCardImpl(Suit suit, Value value) {
		this.suit = suit;
		this.value = value;
	}
	
//	public void setSuit(Suit suit) {
//		this.suit = suit;
//	}
//
//	public void setValue(Value value) {
//		this.value = value;
//	}

	@Override
	public Suit getSuit() {
		return this.suit;
	}

	@Override
	public Value getValue() {
		return this.value;
	}

	@Override
	public int getScore() {
		if (this.value == Value.TEN || this.value == Value.JACK || this.value == Value.QUEEN || this.value == Value.KING) {
			return 10;
		} else if (this.value == Value.ACE) {
			return 11;
		} else if (this.value == Value.EIGHT) {
			return 8;
		} else {
			return 9;
		}
	}

	@Override
	public boolean equals(PlayingCard card) {
		
		return (this.suit == card.getSuit() && this.value == card.getValue());
	}
	
	@Override
	public int hashCode() {
		
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		
		return super.equals(obj);
	}

	@Override
	public String toString() {
		String suitName = StringUtils.capitalize(suit.name().toLowerCase());
		String valueName = StringUtils.capitalize(value.name().toLowerCase());
		return String.format("Suit: %s, Value: %s, Score: %d", suitName, valueName, this.getScore());
	}

}

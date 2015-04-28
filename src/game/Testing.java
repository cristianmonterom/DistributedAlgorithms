package game;

import common.BagOfItems;
import common.Token;

public class Testing {

	public static void main(String[] args){
		BagOfItems bag = new BagOfItems(4);
		System.out.println(bag.ToJSON());
		
		Token token = new Token();
		System.out.println("token: " + token.getTokenSession());
	}
}

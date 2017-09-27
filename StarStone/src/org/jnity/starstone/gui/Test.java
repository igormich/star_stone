package org.jnity.starstone.gui;

import java.io.IOException;

import org.jnity.starstone.cards.CreatureCard;
import org.jnity.starstone.core.TextHolder;

public class Test {

	public static void main(String[] args) throws IOException {
		TextHolder.load("./text/ru.inf");
		CreatureCard marine = new CreatureCard("MARINE", 1, 0, 2, 1);
		System.out.println(marine.getName());
		System.out.println(marine.getDescription());
		System.out.println(marine.getAbout());
		System.out.println(marine.getCurrentHits());
		marine.takeDamage(1);
		System.out.println(marine.getCurrentHits());
	}

}

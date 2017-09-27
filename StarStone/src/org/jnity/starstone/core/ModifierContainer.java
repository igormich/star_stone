package org.jnity.starstone.core;

import java.util.ArrayList;
import java.util.List;

import org.jnity.starstone.modifier.Modifier;

public class ModifierContainer extends GamePart {
	private final List<Modifier> modifiers = new ArrayList<>();
	private final List<Modifier> modifiersToRemove = new ArrayList<>();

	public List<Modifier> getModifiers() {
		modifiers.removeAll(modifiersToRemove);
		modifiersToRemove.clear();
		return modifiers;
	}

	public void addModifier(Modifier modifier) {
		modifiers.add(modifier);
	}

	public void removeModifier(Modifier modifier) {
		modifiersToRemove.add(modifier);
	}
}

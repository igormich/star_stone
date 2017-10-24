package org.jnity.starstone.core;

import java.util.ArrayList;
import java.util.List;

import org.jnity.starstone.modifier.Modifier;

public class ModifierContainer extends GamePart {
	private final ArrayList<Modifier> modifiers = new ArrayList<>();
	private final List<Modifier> modifiersToRemove = new ArrayList<>();
	private final List<Modifier> modifiersToAdd = new ArrayList<>();
	
	public List<Modifier> getModifiers() {
		modifiers.addAll(modifiersToAdd);
		modifiersToAdd.clear();
		modifiers.removeAll(modifiersToRemove);
		modifiersToRemove.clear();
		return (List<Modifier>) modifiers.clone();
	}

	public void addModifier(Modifier modifier) {
		modifiersToAdd.add(modifier);
	}

	public void removeModifier(Modifier modifier) {
		modifiersToRemove.add(modifier);
	}
}

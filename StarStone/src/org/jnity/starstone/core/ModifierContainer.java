package org.jnity.starstone.core;

import java.util.ArrayList;
import java.util.List;

import org.jnity.starstone.modifier.Modifier;

public class ModifierContainer extends GamePart {
	private final ArrayList<Modifier> modifiers = new ArrayList<>();
	
	public List<Modifier> getModifiers() {
		return (List<Modifier>) modifiers.clone();
	}

	public void addModifier(Modifier modifier) {
		if(!modifier.canBeDuplicated())
			modifiers.removeIf(m -> m.getClass() == modifier.getClass());
		modifiers.add(modifier);
	}

	public void removeModifier(Modifier modifier) {
		modifiers.remove(modifier);
	}
}

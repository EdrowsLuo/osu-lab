package com.edplan.nso.storyboard.elements;

import java.util.List;

public interface CommandTimelineSelecter<T> {
	public List<TypedCommand<T>> select(CommandTimeLineGroup group);
}

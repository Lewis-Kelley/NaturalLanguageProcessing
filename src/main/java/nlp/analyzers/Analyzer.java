package nlp.analyzers;

import java.util.Collection;

import nlp.Frame;

public interface Analyzer {
	Collection<Frame> analyze();
}

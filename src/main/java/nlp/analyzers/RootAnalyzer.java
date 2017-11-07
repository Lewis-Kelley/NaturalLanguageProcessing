package nlp.analyzers;

import java.util.*;

import edu.stanford.nlp.trees.Tree;
import nlp.*;

public class RootAnalyzer implements Analyzer {
	private Tree root;
	private SentenceAnalyzer sentence;

	public RootAnalyzer(Tree tree) {
		root = tree;
		root.getChildrenAsList().forEach(this::findSentence);
	}

	private void findSentence(Tree child) {
		if (child.value().equals(Phrases.SENTENCE.toString()))
			sentence = new SentenceAnalyzer(child);
	}

	@Override
	public Collection<Frame> analyze() {
		if (sentence == null)
			return new LinkedList<Frame>();
		else
			return sentence.analyze();
	}
}

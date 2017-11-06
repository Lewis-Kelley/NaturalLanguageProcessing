package nlp.analyzers;

import java.util.Collection;

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
		return sentence.analyze();
	}
}

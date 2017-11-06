package nlp.analyzers;

import java.util.Collection;

import edu.stanford.nlp.trees.Tree;
import nlp.*;

public class TreeAnalyzer implements Analyzer {
	private RootAnalyzer root;

	public TreeAnalyzer(Tree tree) {
		if (tree.value().equals(Phrases.ROOT.toString()))
			root = new RootAnalyzer(tree);
		else
			System.err.println("Tree analyzer not created with ROOT.");
	}

	@Override
	public Collection<Frame> analyze() {
		return root.analyze();
	}
}

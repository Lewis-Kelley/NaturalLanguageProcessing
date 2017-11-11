package nlp.analyzers;

import java.util.*;

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
		return root == null ? new LinkedList<>() : root.analyze();
	}

	public static boolean treeIs(Tree tree, PartOfSpeech... parts) {
		for (PartOfSpeech part : parts) {
			if (tree.value().equals(part.toString()))
				return true;
		}

		return false;
	}
}

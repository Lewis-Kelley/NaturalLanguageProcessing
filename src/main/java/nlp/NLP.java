package nlp;

import java.io.*;
import java.util.*;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;
import nlp.analyzers.TreeAnalyzer;

public class NLP {
	public static void main(String[] args) throws IOException {
		String filename = "lincoln cleaned";

		Parser parser = new Parser();
		FileOutputStream fileOut = new FileOutputStream(new File("resources/" + filename + ".dat"));

		Annotation document = parser.processFile(new File("resources/" + filename + ".txt"));
		System.out.println("Finished parsing.");

		// these are all the sentences in this document
		// a CoreMap is essentially a Map that uses class objects as keys and has values
		// with custom types
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		Collection<Frame> frames = new LinkedList<>();
		float sentenceNum = sentences.size();
		int parsed = 0;
		for (CoreMap sentence : sentences) {
			//analyzeDependencies(sentence);
			Tree tree = sentence.get(TreeAnnotation.class);
			Collection<Frame> treeFrames = new TreeAnalyzer(tree).analyze();
			if(!treeFrames.isEmpty()){
				parsed++;
			}
			else{
				System.out.println("failed to analyze: " + sentence + "\n" + tree + "\n");
			}

			frames.addAll(treeFrames);
		}
		System.out.println("successfully evaluated: " + Math.round(parsed / sentenceNum * 100) + "%");
		ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
		objectOut.writeObject(frames);
		objectOut.close();

		System.out.println("Done!");
	}

	private static void analyzeDependencies(CoreMap sentence) {
		printHeader("Analysing Dependencies");
		DependencyAnalyzer dependencies = new DependencyAnalyzer(sentence);
		dependencies.analyseDependencies();
		dependencies.analyseRelations();
	}

	private static void printHeader(String text) {
		System.out.printf("===== %s =====\n", text);
	}
}
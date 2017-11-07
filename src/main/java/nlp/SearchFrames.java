package nlp;

import java.io.*;
import java.util.*;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;
import nlp.analyzers.TreeAnalyzer;

public class SearchFrames {
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		Collection<Frame> frames = loadFrames();

		Scanner scanner = new Scanner(System.in);
		Parser parser = new Parser();

		while (true) {
			System.out.println("Enter a statement or nothing to quit:");
			String line = scanner.nextLine();
			if (line.equals(""))
				break;

			Annotation annotation = parser.processString(line);
			List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
			TreeAnalyzer analyzer = new TreeAnalyzer(sentences.get(0).get(TreeAnnotation.class));
			analyzer.analyze().forEach(frame -> {
				if (frames.contains(frame))
					System.out.println("Yes!");
				else
					System.out.println("No!");
			});
		}

		scanner.close();
	}

	@SuppressWarnings("unchecked")
	private static Collection<Frame> loadFrames() throws IOException, FileNotFoundException, ClassNotFoundException {
		ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream("sample.dat"));
		Collection<Frame> frames = (Collection<Frame>) objectIn.readObject();
		objectIn.close();

		return frames;
	}
}

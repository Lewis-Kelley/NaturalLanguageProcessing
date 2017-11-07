package nlp;

import java.io.*;
import java.util.Properties;

import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.pipeline.*;

public class Parser {
	private StanfordCoreNLP pipeline;

	public Parser() {
		pipeline = createCore();
	}

	private StanfordCoreNLP createCore() {
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");

		return new StanfordCoreNLP(props);
	}

	public Annotation processFile(File file) {
		try {
			StringBuilder builder = fileToString(file);
			return processString(builder.toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private StringBuilder fileToString(File file) throws IOException {
		BufferedReader reader = IOUtils.readerFromFile(file);
		StringBuilder builder = new StringBuilder();

		while (reader.ready())
			builder.append(reader.readLine());
		return builder;
	}

	public Annotation processString(String text) {
		Annotation document = new Annotation(text);
		pipeline.annotate(document);
		return document;
	}
}

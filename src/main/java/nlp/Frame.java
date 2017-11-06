package nlp;

public class Frame {
	public String subject;
	public String verb;

	public Frame(String subject, String verb) {
		this.subject = subject;
		this.verb = verb;
	}

	@Override
	public String toString() {
		return "Subject: '" + subject +"' Verb: '" + verb + "'";
	}
}

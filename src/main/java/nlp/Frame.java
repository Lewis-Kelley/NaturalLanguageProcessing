package nlp;

import java.io.Serializable;

public class Frame implements Serializable {
	private static final long serialVersionUID = -7064814874765960263L;

	public String subject;
	public String verb;
	public String object;

	public Frame(String subject, String verb) {
		this.subject = subject.toLowerCase();
		this.verb = verb.toLowerCase();
		this.object = "";
	}

	public Frame(String subject, String verb, String object) {
		this.subject = subject.toLowerCase();
		this.verb = verb.toLowerCase();
		this.object = object.toLowerCase();
	}

	@Override
	public String toString() {
		if (object == null || object.equals(""))
			return "Subject: '" + subject
					+ "' Verb: '" + verb + "'";
		else
			return "Subject: '" + subject
					+ "' Verb: '" + verb
					+ "' Object: '" + object + "'";
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Frame))
			return false;
		Frame other = (Frame)obj;
		return subject.equals(other.subject)
				&& verb.equals(other.verb)
				&& object.equals(other.object);
	}
}

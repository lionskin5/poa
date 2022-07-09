package elements.phases;

import jade.content.Concept;
import jade.content.onto.annotations.Slot;

@SuppressWarnings("serial")
public class Phase implements Concept{
	
	private String name;
	private int start;
	private int end;
	
	@Slot(mandatory = true)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Slot(mandatory = true)
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	@Slot(mandatory = true)
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	
	public String toString() {
		return "\n" + "[start=" + start + ",\n" + "end=" + end + "]";
	}

}

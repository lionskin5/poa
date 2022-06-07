package elements.lot;

public enum Fish {
	
	SARDINA(0, Range.LOW), BOQUERON(1, Range.LOW), CABALLA(2, Range.LOW),
	SALMON(3, Range.MEDIUM), DORADA(4, Range.MEDIUM), TRUCHA(5, Range.MEDIUM),
	PULPO(6, Range.HIGH), LANGOSTA(7, Range.HIGH), CAVIAR(8, Range.HIGH);
	
	private int index;
	private Range range;
	
	private Fish(int index, Range range) {
		this.index = index;
		this.range = range;
	}
	
	public int getIndex() {
		return this.index;
	}

	public Range getRange() {
		return range;
	}

}

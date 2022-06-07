package makers;

import java.util.ArrayList;
import java.util.List;
import elements.register.Register;
import jade.core.AID;


public class RegisterStorage {
	
	private List<Register> registers;
	
	public RegisterStorage() {
		this.registers = new ArrayList<Register>();
	}

	public List<Register> getRegisters() {
		return registers;
	}

	public void addRegister(Register r) {
		registers.add(r);
	}
	
	// En vez de con lote debería de ser de otra manera
	public void deleteRegister(Register l) {
		registers.remove(l);
	}
	
	public Register getRegist(AID agent) {
		for(Register r : registers) {
			if(r.getMemberAID() == agent)
				return r;
		}
		return null;
	}

}

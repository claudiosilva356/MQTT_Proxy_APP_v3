package Test;

import java.io.FileNotFoundException;
import java.util.EmptyStackException;

public class b implements a{
	
	public b() {
		super();
	}
	
	public void actB() throws Exception {
		actA();
		throw new FileNotFoundException();
	}

	@Override
	public void actA() {
		// TODO Auto-generated method stub
		
	}

}

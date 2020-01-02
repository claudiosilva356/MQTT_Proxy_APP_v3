package Test;

import java.rmi.RemoteException;
import java.util.EmptyStackException;

public class c extends b {
	
	public c() {
		super();
	}
	
	public void actC() throws Exception {
		actB();
		throw new RemoteException();
	}

}

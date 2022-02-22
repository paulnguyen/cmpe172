
package com.example.gumballmachine ;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class GumballMachineTests {

	@Test
	void testHappyPath() {
		GumballMachine m = new GumballMachine();

		System.out.println(m);

		assertEquals( "com.example.gumballmachine.NoQuarterState", m.getState().getClass().getName() ) ;
		m.insertQuarter();
		assertEquals( "com.example.gumballmachine.HasQuarterState", m.getState().getClass().getName() ) ;
		m.turnCrank();
		assertEquals( "com.example.gumballmachine.NoQuarterState", m.getState().getClass().getName() ) ;

		System.out.println(m);
	}

}

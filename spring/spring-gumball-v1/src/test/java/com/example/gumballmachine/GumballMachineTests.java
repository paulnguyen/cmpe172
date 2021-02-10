
package com.example.gumballmachine ;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class GumballMachineTests {

	@Test
	void testHappyPath() {
		GumballMachine m = new GumballMachine(5);

		System.out.println(m);

		assertEquals( 5, m.getCount() ) ;
		assertEquals( "com.example.gumballmachine.NoQuarterState", m.getState().getClass().getName() ) ;
		
		m.insertQuarter();
		assertEquals( "com.example.gumballmachine.HasQuarterState", m.getState().getClass().getName() ) ;

		m.turnCrank();
		assertEquals( 4, m.getCount() ) ;
		assertEquals( "com.example.gumballmachine.NoQuarterState", m.getState().getClass().getName() ) ;

		System.out.println(m);
	}

	@Test
	void testSoldOut() {
		GumballMachine m = new GumballMachine(5);

		System.out.println(m);

		assertEquals( 5, m.getCount() ) ;
		assertEquals( "com.example.gumballmachine.NoQuarterState", m.getState().getClass().getName() ) ;
		
		m.insertQuarter();
		m.turnCrank();
		m.insertQuarter();
		m.turnCrank();
		m.insertQuarter();
		m.turnCrank();
		m.insertQuarter();
		m.turnCrank();
		m.insertQuarter();
		m.turnCrank();

		assertEquals( 0, m.getCount() ) ;
		assertEquals( "com.example.gumballmachine.SoldOutState", m.getState().getClass().getName() ) ;

		System.out.println(m);
	}


}

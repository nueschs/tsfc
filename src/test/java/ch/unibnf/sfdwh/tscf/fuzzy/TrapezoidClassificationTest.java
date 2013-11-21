package ch.unibnf.sfdwh.tscf.fuzzy;

import junit.framework.Assert;

import org.junit.Test;

public class TrapezoidClassificationTest {


	@Test
	public void testDouble(){
		TrapezoidClassification<Double> doubleClass = new TrapezoidClassification<Double>("test", 0.36, 1.72, 4.83, 6.29);
		Assert.assertEquals(0.0, doubleClass.getClassification(0.12));
		Assert.assertEquals(0.0, doubleClass.getClassification(6.3));
		Assert.assertEquals(1.0, doubleClass.getClassification(2.4));
		Assert.assertEquals(1.0, doubleClass.getClassification(4.83));
		Assert.assertEquals(Math.round(0.64/1.36), Math.round(doubleClass.getClassification(1.0)));
		Assert.assertEquals(Math.round(1.24/1.36), Math.round(doubleClass.getClassification(1.6)));
		Assert.assertEquals(Math.round(1.0 - (0.77/1.46)), Math.round(doubleClass.getClassification(5.6)));
		Assert.assertEquals(Math.round(1.0 - (1.31/1.46)), Math.round(doubleClass.getClassification(6.14)));
	}

	@Test
	public void testInteger(){
		TrapezoidClassification<Integer> intClass = new TrapezoidClassification<Integer>("test", -13, -4, 5, 14);
		Assert.assertEquals(0.0, intClass.getClassification(-14));
		Assert.assertEquals(0.0, intClass.getClassification(28));
		Assert.assertEquals(1.0, intClass.getClassification(-4));
		Assert.assertEquals(1.0, intClass.getClassification(0));
		Assert.assertEquals(1.0, intClass.getClassification(5));
		Assert.assertEquals(3.0/9.0, intClass.getClassification(-10));
		Assert.assertEquals(8.0/9.0, intClass.getClassification(-5));
		Assert.assertEquals(1.0 - (1.0/9.0), intClass.getClassification(6));
		Assert.assertEquals(1.0 - (7.0/9.0), intClass.getClassification(12));
	}

	@Test
	public void testLeftOpen(){
		TrapezoidClassification<Integer> loClass = new TrapezoidClassification<Integer>("test", null, null,23, 31);
		Assert.assertEquals(1.0, loClass.getClassification(18));
		Assert.assertEquals(1.0 - (1.0/8.0), loClass.getClassification(24));
		Assert.assertEquals(1.0 - (4.0/8.0), loClass.getClassification(27));
		Assert.assertEquals(0.125, loClass.getClassification(30));
		Assert.assertEquals(0.0, loClass.getClassification(31));
	}

	@Test
	public void testRightOpen(){
		TrapezoidClassification<Integer> loClass = new TrapezoidClassification<Integer>("test", 14, 218, null, null);
		Assert.assertEquals(1.0, loClass.getClassification(220));
		Assert.assertEquals(28.0/204.0, loClass.getClassification(42));
		Assert.assertEquals(196.0/204.0, loClass.getClassification(210));
		Assert.assertEquals(0.0, loClass.getClassification(0));
	}




}

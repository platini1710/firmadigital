package cl.fonasa.pdf.test;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cl.fonasa.controller.SignDesAtendidaController;

@RunWith(Parameterized.class)
public class PrimeNumberCheckerTest{
	private static final Logger log = LoggerFactory.getLogger(SignDesAtendidaController.class);

	private String inputZona;
	   private String expectedResult;
	   private PrimeNumberChecker primeNumberChecker;

	   @Before
	   public void initialize() {
	      primeNumberChecker = new PrimeNumberChecker();
	   }

	   // Each parameter should be placed as an argument here
	   // Every time runner triggers, it will pass the arguments
	   // from parameters we defined in primeNumbers() method
		
	   public PrimeNumberCheckerTest(String inputZona, String expectedResult) {
	      this.inputZona = inputZona;
	      this.expectedResult = expectedResult;
	   }
		/*********************************************************************
		 * 
		 * @param zona
		 * @return
		 */
		public String calcOrdinario(String zona) {
			String ord = "4.1K/N° ";
			if ("DZN".equals(zona)) {
				ord = "1P/N° ";
			} else if ("DZCN".equals(zona)) {
				ord = "1R/N° ";
			} else if ("DZCS".equals(zona)) {
				ord = "1S/N° ";
			} else if ("DZS".equals(zona)) {
				ord = "1T/N° ";
			}
			return ord;
		}

	   @Parameterized.Parameters
	   public static Collection primeNumbers() {
	      return Arrays.asList(new Object[][] {
	         { "DZN", "1P/N°" },
	         { "DZCN", "1R/N°" },
	         { "DZCS", "1S/N°" },
	         { "DZS", "1T/N°" },
	         { "NC", "4.1K/N°" }
	      });
	   }

	   // This test will run 4 times since we have 5 parameters defined
	   @Test
	   public void testPrimeNumberChecker() {
	      System.out.println("Parameterized expectedResult is : " + expectedResult);
	      assertEquals(true, 
	      primeNumberChecker.validate(inputZona,expectedResult));
	   }

}

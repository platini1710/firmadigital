package cl.fonasa.pdf.test;

public class PrimeNumberChecker {
	   public Boolean validate(final String zona, String res) {
			String ord = "4.1K/N°";
			
			if ("NC".equals(zona)) {
				ord = "4.1K/N°";
			}
			else if ("DZN".equals(zona)) {
				ord = "1P/N°";
			} else if ("DZCN".equals(zona)) {
				ord = "1R/N°";
			} else if ("DZCS".equals(zona)) {
				ord = "1S/N°";
			} else if ("DZS".equals(zona)) {
				ord = "1T/N°";
			}
			System.out.println(ord);
			System.out.println(res);
			if (ord.equals(res)) {
				System.out.println("true");
				return true;
			} else {
			System.out.println("false");
			}
	      return false;
	   }
	}
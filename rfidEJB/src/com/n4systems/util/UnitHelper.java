package com.n4systems.util;

public class UnitHelper {
	public static final double lbsPerKg = 2.20462262;
	
	static public double convertKgToLbs(double kgs) {
		return kgs * lbsPerKg;
	}
	
}

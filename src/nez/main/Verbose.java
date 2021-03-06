package nez.main;

import nez.expr.Expression;
import nez.util.ConsoleUtils;

public class Verbose {
	public static boolean General = true;
	public static boolean Grammar = false;
	public static boolean Expression = false;
	public static boolean VirtualMachine = false;
	public static boolean PackratParsing = false;
	public static boolean Debug = false;
	public static boolean Backtrack = false;
	
	public static void setAll() {
		General = true;
		Grammar = true;
		Expression = true;
		VirtualMachine = true;
		PackratParsing = true;
		Backtrack = true;
	}

	public final static void print(String msg) {
		if(General) {
			ConsoleUtils.println(msg);
		}
	}

	public final static void println(String msg) {
		if(General) {
			ConsoleUtils.println(msg);
		}
	}

	public static void noticeOptimize(String key, Expression p) {
		if(Expression) {
			ConsoleUtils.println("optimizing " + key + "\n\t" + p);
		}
	}

	public static void noticeOptimize(String key, Expression p, Expression pp) {
		if(Expression) {
			ConsoleUtils.println("optimizing " + key + "\n\t" + p + "\n\t => " + pp);
		}
	}

	public static void debug(Object s) {
		if(Debug) {
			ConsoleUtils.println("debug: " + s);
		}
	}

}

package nl.kqcreations.cityrp.util;

public class StringUtils {

	public static String builder(String[] args, int startNum) {
		StringBuilder builder = new StringBuilder();
		int startArg = startNum;
		int endArg = args.length;
		for (int i = startArg; i < endArg; i++) {
			builder.append(args[i] + (args.length > (i + 1) ? " " : ""));
		}

		return builder.toString(); // your message from all args after "startArg - 0"
	}

}

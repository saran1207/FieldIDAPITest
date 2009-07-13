
public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String labelID = "<button class=\"productLink\" productid=\"456788\">";
		String attribute = "productid=\"";
		int index = labelID.indexOf(attribute);
		labelID = labelID.substring(index + attribute.length());
		index = labelID.indexOf('\"');
		labelID = labelID.substring(0, index);
		System.out.println("'" + labelID + "'");
	}
}

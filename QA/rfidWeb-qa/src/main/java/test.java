
public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String temp = "'Forged Center Pull (Small)' will be renamed to 'Forged Center Pull (Small) (jergens)(1)'";
		String original = temp.split("'")[1];
		String renamed = temp.split("'")[3];
		System.out.println(original);
		System.out.println(renamed);
	}
}
//	'*' will be renamed to '* (jergens)(2)'
//	'Forged Center Pull (Small)' will be renamed to 'Forged Center Pull (Small) (jergens)(1)'
//	'Side Pull Hoist Ring' will be renamed to 'Side Pull Hoist Ring (jergens)(1)'
//	'Std Center Pull Hoist Ring - Medium' will be renamed to 'Std Center Pull Hoist Ring - Medium (jergens)(1)'
//	'Center Pull Hoist Ring Visual' will be renamed to 'Center Pull Hoist Ring Visual(jergens)(1)'
//	'Pull Test' will be renamed to 'Pull Test(jergens)(1)'

import java.util.Random;


public class test {
	
	static Random r = new Random();

	public static void main(String[] args) throws Exception {
		for(int i = 0; i < 10; i++) {
			System.out.println(getRandomString(16));
		}
	}
	
	public static int getRandomInteger(int limit) throws Exception {
		return r.nextInt(limit);
	}

	public static String getRandomString(int length) throws Exception {
		String s = "";
		int range = 'z' - 'a';
		for(int i = 0; i < length; i++) {
			int c = getRandomInteger(range) + 'a';
			if(i == 0) {
				c = Character.toUpperCase(c);
			}
			s += (char)c;
		}
		return s;
	}
}

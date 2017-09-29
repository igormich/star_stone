import java.util.Random;

public class Test {
	static long l = 0;
	public static void main(String[] args) {
		System.out.println(new Random().ints(1, 20+1).parallel().distinct().limit(5)
				.sequential().sum());
		System.out.println(new Random().ints(1, 20+1).peek(i -> l++).parallel().distinct().limit(5)
				.sequential().parallel().sum());
		
	}

}

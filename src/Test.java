import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;


public class Test {

	
	public static void main(String[] args) {
		long var = 222;		
		
		ByteArrayOutputStream decoding = new ByteArrayOutputStream();
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.putLong(var);
		try {
			decoding.write(bb.array());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		byte[] array = decoding.toByteArray();
		for (int i =0; i< array.length; i++)
			System.out.print(array[i]);
		
		ByteBuffer buffer = ByteBuffer.wrap(array);
		System.out.println();
		System.out.println(buffer.getLong());

	}

}

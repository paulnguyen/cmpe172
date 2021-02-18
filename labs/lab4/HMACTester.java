
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64.Encoder;

class HMACTester {

	private static String key = "kwRg54x2Go9iEdl49jFENRM12Mp711QI" ;
	private static String message1 = "Hello World!" ;
	private static String message2 = "Goodby World!" ;

	public static void main(String[] args) {

		byte[] hash1 = hmac_sha256( key, message1 ) ;
		byte[] hash2 = hmac_sha256( key, message2 ) ;

		java.util.Base64.Encoder encoder = java.util.Base64.getEncoder() ;

		System.out.println ( "MSG1: " + message1 ) ;
		System.out.println ( "HASH2: " + encoder.encodeToString(hash1) )  ;

		System.out.println ( "MSG2: " + message2 ) ;
		System.out.println ( "HASH2: " + encoder.encodeToString(hash2) )  ;

	}

	private static byte[] hmac_sha256(String secretKey, String data) {
		try {
			Mac mac = Mac.getInstance("HmacSHA256") ;
			SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256") ;
			mac.init(secretKeySpec) ;
			byte[] digest = mac.doFinal(data.getBytes()) ;
			return digest ;
		} catch (InvalidKeyException e1) {
			throw new RuntimeException("Invalid key exception while converting to HMAC SHA256") ;
		} catch (NoSuchAlgorithmException e2) {
			throw new RuntimeException("Java Exception Initializing HMAC Crypto Algorithm") ;
		}
	}

}


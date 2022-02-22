package com.example.springcybersource;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;


/* 
    https://developer.cybersource.com/
    https://developer.cybersource.com/hello-world.html
    https://www.youtube.com/watch?v=NXU1XKT3m90
    https://github.com/CyberSource/cybersource-rest-samples-java
    https://developer.cybersource.com/api-reference-assets/index.html
    https://developer.cybersource.com/hello-world/testing-guide.html
    https://developer.cybersource.com/api/developer-guides/dita-payments/GettingStarted.html
 */

public class CyberSourceAPI {

    private boolean DEBUG = false ;

	/* Try with your own credentaials
     * Get  Key ID, Secret Key and Merchant Id from EBC portal
     */

    private static String requestHost ;
    private static String merchantKeyId ;
    private static String merchantsecretKey ;
    private static String merchantId ;
	
    public static void setHost( String h ) { requestHost = h ; }
    public static void setKey( String k ) { merchantKeyId = k ; }
    public static void setSecret( String s ) { merchantsecretKey = s ; }
    public static void setMerchant( String m ) { merchantId = m ; }

    public static void debugConfig() {
        System.out.println( requestHost ) ;
        System.out.println( merchantKeyId ) ;
        System.out.println( merchantsecretKey ) ;
        System.out.println( merchantId ) ;          
    }

    /* Sandbox Host: apitest.cybersource.com
     * Production Host: api.cybersource.com 
     */
    private final String USER_AGENT = "Mozilla/5.0";
    public static String gmtDateTime = "DATE_PLACEHOLDER";
    public static String postRequestTarget = "REQUEST_TARGET_PALCEHOLDER";
    public static String APINAME = "APINAME_PLACEHOLDER";
    public static String resource = "resource_PLACEHOLDER";
    public static String  payload = null;


    /* CyberSource Card Types:

        001 - Visa
        002 - Mastercard, Eurocard1: European regional brand of Mastercard.
        003 - American Express
        004 - Discover
        005 - Diners Club

        REF:  https://developer.cybersource.com/library/documentation/dev_guides/Retail_SO_API/html/Topics/app_card_types.htm
    */

    public AuthResponse authorize( AuthRequest req ) {

        // Send Authorization
        postRequestTarget = "post /pts/v2/payments";
        APINAME = "payments";
        resource = "/pts/v2/payments";
        
        payload = "{\n" +
                "  \"clientReferenceInformation\": {\n" +
                "    \"code\": \""+req.reference+"\"\n" +
                "  },\n" +
                "  \"processingInformation\": {\n" +
                "    \"commerceIndicator\": \"internet\"\n" +
                "  },\n" +
                "  \"orderInformation\": {\n" +
                "    \"billTo\": {\n" +
                "      \"firstName\": \""+req.billToFirstName+"\",\n" +
                "      \"lastName\": \""+req.billToLastName+"\",\n" +
                "      \"address1\": \""+req.billToAddress+"\",\n" +
                "      \"postalCode\": \""+req.billToZipCode+"\",\n" +
                "      \"locality\": \""+req.billToCity+"\",\n" +
                "      \"administrativeArea\": \""+req.billToState+"\",\n" +
                "      \"country\": \"US\",\n" +
                "      \"phoneNumber\": \""+req.billToPhone+"\",\n" +
                "      \"email\": \""+req.billToEmail+"\"\n" +
                "    },\n" +
                "    \"amountDetails\": {\n" +
                "      \"totalAmount\": \""+req.transactionAmount+"\",\n" +
                "      \"currency\": \""+req.transactionCurrency+"\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"paymentInformation\": {\n" +
                "    \"card\": {\n" +
                "      \"expirationYear\": \""+req.cardExpYear+"\",\n" +
                "      \"number\": \""+req.cardNumnber+"\",\n" +
                "      \"securityCode\": \""+req.cardCVV+"\",\n" +
                "      \"expirationMonth\": \""+req.cardExpMonth+"\",\n" +
                "      \"type\": \""+req.cardType+"\"\n" +
                "    }\n" +
                "  }\n" +
                "}";

        if (DEBUG) System.out.println("\n\nAuth Request: " + req.toJson() ) ;
        AuthResponse response = new AuthResponse() ;
        PostResponse res = sendPost("https://" + requestHost + resource) ;
        response.code = res.code ;
        if ( res.exception != null ) {
            response.status = "ERROR" ;
            response.message = res.exception ;
        } else {
            String authResult = res.response ;
            if (DEBUG) System.out.println( authResult ) ;
            response = AuthResponse.fromJson( authResult ) ;
            if (DEBUG) System.out.println("\n\nAuth Response: " + response.toJson() ) ;            
        }

        return response ;
    }

    public CaptureResponse capture( CaptureRequest req ) {

        postRequestTarget = "post /pts/v2/payments/"+req.paymentId+"/captures" ;
        APINAME = "payments";
        resource = "/pts/v2/payments/"+req.paymentId+"/captures";
        
        payload = "{\n" +
                "  \"clientReferenceInformation\": {\n" +
                "    \"code\": \""+req.reference+"\"\n" +
                "  },\n" +
                "  \"orderInformation\": {\n" +
                "    \"amountDetails\": {\n" +
                "      \"totalAmount\": \""+req.transactionAmount+"\",\n" +
                "      \"currency\": \""+req.transactionCurrency+"\"\n" +
                "    }\n" +
                "  }\n" +
                "}";

        if (DEBUG) System.out.println("\n\nCapture Request: " + req.toJson() ) ;
        PostResponse res = sendPost("https://" + requestHost + resource) ;
        CaptureResponse response = new CaptureResponse() ;
        response.code = res.code ;
        if ( res.exception != null ) {
            response.status = "ERROR" ;
            response.message = res.exception ;
        } else {
            String captureResult = res.response ;
            if (DEBUG) System.out.println( captureResult ) ;
            response = CaptureResponse.fromJson( captureResult ) ;
            if (DEBUG) System.out.println("\n\nCapture Response: " + response.toJson() ) ;            
        }

        return response ;
    }

    public RefundResponse refund( RefundRequest req ) {

        postRequestTarget = "post /pts/v2/captures/"+req.captureId+"/refunds" ;
        APINAME = "payments";
        resource = "/pts/v2/captures/"+req.captureId+"/refunds";
        
        payload = "{\n" +
                "  \"clientReferenceInformation\": {\n" +
                "    \"code\": \""+req.reference+"\"\n" +
                "  },\n" +
                "  \"orderInformation\": {\n" +
                "    \"amountDetails\": {\n" +
                "      \"totalAmount\": \""+req.transactionAmount+"\",\n" +
                "      \"currency\": \""+req.transactionCurrency+"\"\n" +
                "    }\n" +
                "  }\n" +
                "}";

        if (DEBUG) System.out.println("\n\nRefund Request: " + req.toJson() ) ;
        PostResponse res = sendPost("https://" + requestHost + resource) ;
        RefundResponse response = new RefundResponse() ;
        response.code = res.code ;
        if ( res.exception != null ) {
            response.status = "ERROR" ;
            response.message = res.exception ;
        } else {
            String refundResult = res.response ;
            if (DEBUG) System.out.println( refundResult ) ;
            response = RefundResponse.fromJson( refundResult ) ;
            if (DEBUG) System.out.println("\n\nRefund Response: " + response.toJson() ) ;  
        }     

        return response ;

    }



    // HTTP GET request
    private String sendGet(String url) throws Exception {
        /* HTTP connection */
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        /* Add Request Header 
		 * "v-c-merchant-id" set value to Cybersource Merchant ID or v-c-merchant-id
         * This ID can be found on EBC portal 
         */
        con.setRequestProperty("v-c-merchant-id", merchantId);
        con.setRequestProperty("v-c-correlation-id", "123");
        
        /* Add Request Header
         * "Date" The date and time that the message was originated from.
         * "HTTP-date" format as defined by RFC7231. 
         */
        gmtDateTime = getdate();
        
        con.setRequestProperty("date", gmtDateTime);

        /* Add Request Header
         * "Host" Host to send the request to.
         */
        con.setRequestProperty("Host", requestHost);

        /* Add Request Header 
         * "Signature" Contains keyId, algorithm, headers and signature as paramters
         * Check getSignatureHeader() method for more details 
         */
        StringBuilder signatureHeaderValue = getSignatureHeader("GET");
        
        con.setRequestProperty("Signature", signatureHeaderValue.toString());

        /* HTTP Method GET */
        con.setRequestMethod("GET");

        /* Establishing HTTP connection*/
        int responseCode = con.getResponseCode();
        String responseHeader = con.getHeaderField("v-c-correlation-id");

        System.out.println("\n -- RequestURL -- ");
        System.out.println("\tURL : " + url);
        System.out.println("\n -- HTTP Headers -- ");
        System.out.println("\tContent-Type : " + "application/json");
        System.out.println("\tv-c-merchant-id : " + merchantId);
        System.out.println("\tDate : " + gmtDateTime);
        System.out.println("\tHost : " + requestHost);
        System.out.println("\tSignature : " + signatureHeaderValue);
        System.out.println("\n -- Response Message -- " );
        System.out.println("\tResponse Code :" + responseCode);
        System.out.println("\tv-c-correlation-id :" + responseHeader);

        /* Reading Response Message */
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        
        in.close();

        /* print Response */
        // System.out.println("\tResponse Payload :\n" + response.toString());

        return response.toString() ;
    }

    // HTTP POST request
    class PostResponse {
        int code ;
        String exception ;
        String response ;
    }
    private PostResponse sendPost(String url) {

        HttpURLConnection con = null ;
        int responseCode = 0 ;

        try {

            /* HTTP connection */
            URL obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();

            /* Add Request Header 
             * "v-c-merchant-id" set value to Cybersource Merchant ID or v-c-merchant-id
             * This ID can be found on EBC portal 
             */
            con.setRequestProperty("v-c-merchant-id", merchantId);
            con.setRequestProperty("v-c-correlation-id", "123");
            con.setRequestProperty("profile-id", "93B32398-AD51-4CC2-A682-EA3E93614EB1");

            // Oleg's profile-ID
            // con.setRequestProperty("profile-id", "DE14A8CD-CC3E-4889-AC46-67ACFB7CD27C");

            /* Add Request Header 
             * "Date" The date and time that the message was originated from.
             * "HTTP-date" format as defined by RFC7231.
             */
            gmtDateTime = getdate();

            con.setRequestProperty("date", gmtDateTime);

            /* Add Request Header
             * "Host" Name of the host to send the request to.
             */
            con.setRequestProperty("Host", requestHost);

            /* Add Request Header 
    		 * "Digest" SHA-256 hash of payload that is BASE64 encoded 
    		 */
            con.setRequestProperty("Digest", getDigest());

            /* Add Request Header 
             * "Signature" Contains keyId, algorithm, headers and signature as paramters
             * Check getSignatureHeader() method for more details 
             */
            StringBuilder signatureHeaderValue = getSignatureHeader("POST");
            
            con.setRequestProperty("Signature", signatureHeaderValue.toString());

            /* HTTP Method POST */
            con.setRequestMethod("POST");

            /* Additional Request Headers */
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Content-Type", "application/json");

            // Send POST request
            con.setDoOutput(true);
            con.setDoInput(true);
            
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(getPayload().getBytes("UTF-8"));
            wr.flush();
            wr.close();

            /* Establishing HTTP connection*/
            responseCode = con.getResponseCode();

            String responseHeader = con.getHeaderField("v-c-correlation-id");
            System.out.println("\n -- RequestURL -- ");
            System.out.println("\tURL : " + url);
            System.out.println("\n -- HTTP Headers -- ");
            System.out.println("\tContent-Type : " + "application/json");
            System.out.println("\tv-c-merchant-id : " + merchantId);
            System.out.println("\tDate : " + gmtDateTime);
            System.out.println("\tHost : " + requestHost);
            System.out.println("\tDigest : " + getDigest());
            System.out.println("\tSignature : " + signatureHeaderValue);
            System.out.println("\n -- Response Message -- " );
            System.out.println("\tResponse Code :" + responseCode);
            System.out.println("\tv-c-correlation-id :" + responseHeader);
        
        	/* Reading Response Message */
            BufferedReader in  = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            
            in.close();
            
            /* print Response */
            //System.out.println("\tResponse Payload :\n" + response.toString());
            PostResponse res = new PostResponse() ;
            res.code = responseCode ;
            res.response = response.toString() ;
            return res ;

        } catch (Exception exception) {

            // Output unexpected IOExceptions
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            try {
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            } catch( Exception e ) { }
        
        
            /* print Response */
            //System.out.println("Response Payload : " + response.toString());
            System.out.println(exception);
            PostResponse res = new PostResponse() ;
            res.code = responseCode ;
            res.exception =  exception.toString() ;
            res.response = response.toString() ;
            return res ;            
        }
    }

    private String getdate() {
        /*  This Method returns Date in GMT format as defined by RFC7231. */
        return(DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT"))));
    }

    private StringBuilder getSignatureHeader(String httpMethod) throws Exception {
        /* This method return SignatureHeader Value that contains following paramters
         * keyid -- Merchant ID obtained from EBC portal
         * algorithm -- Should have value as "HmacSHA256"
         * headers -- List of all header name passed in the Signature paramter below
         *            String getHeaders = "host date (request-target)" + " " + "v-c-merchant-id";
         *            String postHeaders = "host date (request-target) digest v-c-merchant-id";
         *            Note: Digest is not passed for GET calls
         * signature -- Signature header has paramter called signature
         *              Paramter 'Signature' must contain all the paramters mentioned in header above in given order  
         */
        StringBuilder signatureHeaderValue = new StringBuilder();

        /* KeyId is the key obtained from EBC */
        signatureHeaderValue.append("keyid=\"" + merchantKeyId + "\"");


        /* Algorithm should be always HmacSHA256 for http signature */
        signatureHeaderValue.append(", algorithm=\"HmacSHA256\"");

        /* Headers - list is choosen based on HTTP method. Digest is not required for GET Method */
        String getHeaders = "host date (request-target)" + " " + "v-c-merchant-id";
        String postHeaders = "host date (request-target) digest v-c-merchant-id";

        if(httpMethod.equalsIgnoreCase("GET"))
            signatureHeaderValue.append(", headers=\"" + getHeaders + "\"");
        else if(httpMethod.equalsIgnoreCase("POST"))
            signatureHeaderValue.append(", headers=\"" + postHeaders + "\"");

        /* Get Value for paramter 'Signature' to be passed to Signature Header */
        String signatureValue = getSignatureParam(httpMethod);
        signatureHeaderValue.append(", signature=\"" + signatureValue + "\"");

        return signatureHeaderValue;
    }

    private String getSignatureParam(String httpMethod)  throws Exception{
        /* This method returns value for paramter Signature which is then passed to Signature header
         * paramter 'Signature' is calucated based on below key values and then signed with SECRET KEY -
         * host: Sandbox (apitest.cybersource.com) or Production (api.cybersource.com) hostname
         * date: "HTTP-date" format as defined by RFC7231.
         * (request-target): Should be in format of httpMethod: path
         *                   Example: "post /pts/v2/payments"
         * Digest: Only needed for POST calls.
         *          digestString = BASE64( HMAC-SHA256 ( Payload ));
         *          Digest: "SHA-256=" + digestString;
         * v-c-merchant-id: set value to Cybersource Merchant ID
         *                   This ID can be found on EBC portal
         */
        StringBuilder signatureString = new StringBuilder();
        signatureString.append('\n');
        signatureString.append("host");
        signatureString.append(": ");
        signatureString.append(requestHost);
        signatureString.append('\n');
        signatureString.append("date");
        signatureString.append(": ");
        signatureString.append(gmtDateTime);
        signatureString.append('\n');
        signatureString.append("(request-target)");
        signatureString.append(": ");

        String getRequestTarget = "get " + resource;
        
        if(httpMethod.equalsIgnoreCase("GET"))
            signatureString.append(getRequestTarget);
        else if(httpMethod.equalsIgnoreCase("POST"))
            signatureString.append(postRequestTarget);
        
        signatureString.append('\n');

        if(httpMethod.equalsIgnoreCase("POST")) {
            signatureString.append("digest");
            signatureString.append(": ");
            signatureString.append(getDigest());
            signatureString.append('\n');
        }

        signatureString.append("v-c-merchant-id");
        signatureString.append(": ");
        signatureString.append(merchantId);
        signatureString.delete(0, 1);
        
        String signatureStr = signatureString.toString();
                
        /* Signature string generated from above parameters is Signed with SecretKey hased with SHA256 and base64 encoded.
         *  Secret Key is Base64 decoded before signing 
         */
        SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(merchantsecretKey), "HmacSHA256");
        Mac aKeyId = Mac.getInstance("HmacSHA256");
        aKeyId.init(secretKey);
        aKeyId.update(signatureStr.getBytes());
        byte[] aHeaders = aKeyId.doFinal();
        String base64EncodedSignature = Base64.getEncoder().encodeToString(aHeaders);

        return base64EncodedSignature;
    }

    private String getDigest() throws NoSuchAlgorithmException, IOException {
        /* This method return Digest value which is SHA-256 hash of payload that is BASE64 encoded */
        String messageBody = getPayload();
        
        MessageDigest digestString = MessageDigest.getInstance("SHA-256");
        
        byte[] digestBytes = digestString.digest(messageBody.getBytes("UTF-8"));
        
        String bluePrint = Base64.getEncoder().encodeToString(digestBytes);
        bluePrint = "SHA-256="+ bluePrint;

        return bluePrint;
    }

    private String getPayload() throws IOException {
        String messageBody = payload;
        return messageBody;
    }
}
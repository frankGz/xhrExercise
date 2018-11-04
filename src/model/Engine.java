package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Engine {
	
	private static Engine instance = null;



	private Engine()

	{

	}



	public synchronized static Engine getInstance()

	{

		if (instance == null) instance = new Engine();

		return instance;

	}
	
	final private static String CREDENTIAL_KEY = "AIzaSyC5Xd1CgXi7jVAb0gKDzfzPFVdBVxenkpU";
	final private static String GEOCODING_PATH = "https://maps.googleapis.com/maps/api/geocode/";
	final private static String DISTANCE_MARTIX_PATH = "https://maps.googleapis.com/maps/api/distancematrix/";	
	final private static double RADIUS = 12742.00; 
	final private static int DRONE_SPEED = 150;
	final private static int HOUR_TO_MINUTE = 60;
	final private static int MIN_TO_SEC = 60;
	
	
	
	/**
	 * @param min min value
	 * @param max the prime found cannot bigger than this
	 * @return -1 if no prime in range, else the int value of the prime
	 */
	public int doPrime(int min, int max) {
		int result;
		BigInteger bigInteger = BigInteger.valueOf(min);
		int next = bigInteger.nextProbablePrime().intValue();
		result = (next > max) ? -1 : next;
		return result;
	}
	
	/**
	 * @param n1 from latitude 
	 * @param t1 from longitude 
	 * @param n2 to latitude
	 * @param t2 to longitude
	 * @return 12742 * atan2[sqrt(X), sqrt(1-X)], where: X = sin2[(t2-t1)/2] + Y * sin2[(n2-n1)/2] and Y = cos(t1) * cos(t2)
	 * 
	 */
	public Double doGps(Double t1 ,Double n1 ,Double t2 ,Double n2) {
		double t1r = Math.toRadians(t1);
		double n1r = Math.toRadians(n1);
		double t2r = Math.toRadians(t2);
		double n2r = Math.toRadians(n2);		
		Double Y = Math.cos(t1r) * Math.cos(t2r);		
		Double X = Math.pow(Math.sin((t2r - t1r) / 2.0), 2) + Y * Math.pow(Math.sin((n2r - n1r) / 2.0), 2);		
		Double result = RADIUS *  Math.atan2(Math.sqrt(X), Math.sqrt(1-X));
		return result;
	}
	
	
	/**
	 * @param address1 
	 * @param address2
	 * @return Time need for the drone with speed 150 km/h in minutes.
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public double doDrone(String address1, String address2) throws IOException, SAXException, ParserConfigurationException {
		
		String url1 = GEOCODING_PATH + "xml?address=" + address1.trim().replace(' ', '+') + "&key=" + CREDENTIAL_KEY;
		String url2 = GEOCODING_PATH + "xml?address=" + address2.replace(' ', '+') + "&key=" + CREDENTIAL_KEY;
		
		String reponse1 = getHttpResponseContent(url1);
		String reponse2 = getHttpResponseContent(url2);
		
		Document xml1 = StringToXml(reponse1);
		Document xml2 = StringToXml(reponse2);
		
		Double t1 = Double.parseDouble(xml1.getElementsByTagName("lat").item(0).getTextContent());
		Double n1 = Double.parseDouble(xml1.getElementsByTagName("lng").item(0).getTextContent());
		
		Double t2 = Double.parseDouble(xml2.getElementsByTagName("lat").item(0).getTextContent());
		Double n2 = Double.parseDouble(xml2.getElementsByTagName("lng").item(0).getTextContent());
		
		return doGps(t1, n1, t2, n2) / DRONE_SPEED  * HOUR_TO_MINUTE;
		
		
	}
	
	
	/**
	 * @param from
	 * @param dest
	 * @return cost in $ with model of 50 cents per minute
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public double doRide(String from, String dest) throws IOException, SAXException, ParserConfigurationException {
		
		final Double COST_PER_MINUTE = 0.5;
		
		String url = DISTANCE_MARTIX_PATH + "xml?origins=" + from.trim().replace(' ', '+') + "&destinations="
				+ dest.trim().replace(' ', '+') + "&departure_time=now&key=" + CREDENTIAL_KEY;
		
		String reponse = getHttpResponseContent(url);
		Document xml = StringToXml(reponse);
		Double time = Double.parseDouble(xml.getElementsByTagName("value").item(2).getTextContent());
		Double cost = time / MIN_TO_SEC * COST_PER_MINUTE;
		return round(cost, 2);
		
	}
	
	
	/**
	 * @param namePrefix
	 * @param minGpa
	 * @param sorting
	 * @return
	 * @throws Exception
	 */
	public List<StudentBean> doSis(String namePrefix, String minGpa, String sorting) throws Exception{
		StudentDAO studentDAO = new StudentDAO();
		List<StudentBean> list = studentDAO.retrieve(namePrefix, minGpa, sorting);
		return list;
	}
	

	private static String getHttpResponseContent(String url) throws IOException {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		return response.toString();
	}
	
	private static Document StringToXml(String string) throws SAXException, IOException, ParserConfigurationException {
		Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new InputSource(new StringReader(string)));
		return doc;
	}
	
	private static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	

}

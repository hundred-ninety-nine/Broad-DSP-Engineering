package client;

import static java.util.stream.Collectors.joining;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.ws.http.HTTPException;

/**
 * A Base Http Client that implements the GET protocol.
 * 
 *  @author Ivan Chang
 */
public class HttpClient {

	final static String charset = java.nio.charset.StandardCharsets.UTF_8.name();
	static int timeout = 5000;
	private String url;

	private String UncheckedURLEncode(String value) {
		try {
			value = URLEncoder.encode(value, charset);
		} catch (UnsupportedEncodingException e) {
		}
		return value;
	}

	/**
	 * Initializes a HttpClient with specified root url
	 */
	public HttpClient(String url) {
		this.url = url;
	}

	/**
	 * Returns a HTTP GET response string?
	 *
	 * @param path is the path relative to root of url
	 * @param qp   is a Map of key value pairs representing query parameters
	 * @return HTTP response
	 * @throws MalformedURLException, IOException, HTTPException
	 */
	public String GET(String path, Map<String, String> qp)
			throws MalformedURLException, IOException, HTTPException, UnsupportedEncodingException {
		String query = qp.entrySet().stream().map(e -> {
			return UncheckedURLEncode(e.getKey()) + "=" + UncheckedURLEncode(e.getValue());
		}).collect(joining("&"));

		HttpsURLConnection c = (HttpsURLConnection) new URL(url + "/" + path + "?" + query).openConnection();
		c.setRequestMethod("GET");
		c.setRequestProperty("Accept-Charset", charset);
		c.setRequestProperty("Accept", "application/json");
		c.setRequestProperty("Content-length", "0");
		c.setUseCaches(false);
		c.setAllowUserInteraction(false);
		c.setConnectTimeout(timeout);
		c.setReadTimeout(timeout);
		c.connect();
		int status = c.getResponseCode();
		if (status != 200)
			throw new HTTPException(status);
		Reader in = new BufferedReader(new InputStreamReader(c.getInputStream(), charset));
		StringBuilder sb = new StringBuilder();
		for (int ch; (ch = in.read()) >= 0;)
			sb.append((char) ch);
		String response = sb.toString();
		return response;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}

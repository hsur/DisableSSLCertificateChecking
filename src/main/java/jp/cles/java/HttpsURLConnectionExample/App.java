package jp.cles.java.HttpsURLConnectionExample;

import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class App {
	public static void main(String[] args) throws Exception {
		String targetUrl = "https://blog.cles.jp";

		disableSSLCertificateChecking();

		URL url = new URL(targetUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		System.out.println(targetUrl + " (" + conn.getResponseCode() + ")");
	}

	// ***For development use only (This code contains some security issues) ***
	public static void disableSSLCertificateChecking() throws Exception {
		System.out.println("[WARN] *** Disable SSLCertificate Checking ***");

		// ホスト名の検証を行わない
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String s, SSLSession ses) {
				System.out.println("[WARN] Hostname is not matched.");
				return true;
			}
		};
		HttpsURLConnection.setDefaultHostnameVerifier(hv);
		// 証明書の検証を行わない
		KeyManager[] km = null;
		TrustManager[] tm = { new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			}
			public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			}
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		} };
		SSLContext sslcontext = SSLContext.getInstance("SSL");
		sslcontext.init(km, tm, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
	}
}

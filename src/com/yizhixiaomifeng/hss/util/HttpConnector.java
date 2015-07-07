package com.yizhixiaomifeng.hss.util;

import java.security.KeyStore;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

public class HttpConnector {
	/* 从连接池中取连接的超时时间 */
	private final static int TIMEOUT = 1000;
	/* 连接超时 */
	private final static int CONNECT_TIMEOUT = 3*1000;
	/* 请求超时 */
	private final static int SO_TIMEOUT = 15*1000;
	
	private final static int DEFAULT_HOST_CONNECTIONS = 1;
	private final static int DEFAULT_MAX_CONNECTIONS = 4;
	private final static int DEFAULT_SOCKET_BUFFER_SIZE = 8*1024;

	private static HttpConnector instance = new HttpConnector();

	// 服务器秘钥
	private static KeyStore trustStore;

	public static KeyStore getTrustStore() {
		return trustStore;
	}

	public static void setTrustStore(KeyStore trustStore) {
		HttpConnector.trustStore = trustStore;
	}

	private HttpClient httpClient;
	private HttpClient imageHttpClient;

	public static HttpConnector getInstance() {
		return instance;
	}
	
	public void closeHttpClient(){
		if(httpClient != null) {
			httpClient.getConnectionManager().shutdown();
			httpClient=null;
		}
	}
	/**
	 * 相当于配置浏览器
	 */
	public synchronized HttpClient getHttpClient() {
		if(httpClient == null) {
	        httpClient = initHttpClient(); 
		}		
		return httpClient;
	}
	/**
	 * 相当于配置浏览器
	 */
	public synchronized HttpClient getImageHttpClient() {
		if(imageHttpClient == null) {
			imageHttpClient = initHttpClient();
		}		
		return imageHttpClient;
	}
	
	public synchronized HttpClient initHttpClient(){

		final HttpParams httpParams = new BasicHttpParams();  
		
		// timeout: get connections from connection pool
        ConnManagerParams.setTimeout(httpParams, TIMEOUT);  
        // timeout: connect to the server
        HttpConnectionParams.setConnectionTimeout(httpParams, CONNECT_TIMEOUT);
        // timeout: transfer data from server
        HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT); 
        
        // set max connections per host
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(DEFAULT_HOST_CONNECTIONS));
        // set max total connections
        ConnManagerParams.setMaxTotalConnections(httpParams, DEFAULT_MAX_CONNECTIONS);
        
        // use expect-continue handshake
        HttpProtocolParams.setUseExpectContinue(httpParams, true);
        // disable stale check
        HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);
        
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);  
        HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8); 
          
        HttpClientParams.setRedirecting(httpParams, false);
        
        // set user agent
        String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
        HttpProtocolParams.setUserAgent(httpParams, userAgent);
        
        // disable Nagle algorithm
        HttpConnectionParams.setTcpNoDelay(httpParams, true); 
        
        HttpConnectionParams.setSocketBufferSize(httpParams, DEFAULT_SOCKET_BUFFER_SIZE);
        
        // scheme: http and https
        SchemeRegistry schemeRegistry = new SchemeRegistry();  
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        try {
			schemeRegistry.register(new Scheme("https", new SSLSocketFactory(trustStore), 443));
		} catch (Exception e) {
//			e.printStackTrace();
			schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
		}

        ClientConnectionManager manager = new ThreadSafeClientConnManager(httpParams, schemeRegistry);  
        return new DefaultHttpClient(manager, httpParams); 
	}
}

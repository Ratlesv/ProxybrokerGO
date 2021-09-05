package com.ziloka.ProxyBroker.services;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;

public class ProxyThread implements Runnable {

    private static final Logger logger = LogManager.getLogger(ProxyCollector.class);

    final HashMap<String, LookupResult> onlineProxies;
    String proxy;
    String host;
    int port;
    String types;
    String lvl;
    ProxyChecker proxyChecker;
    ProxyLookup proxyLookup;

    /**
     * @param dbReader - MaxMind GeoIp2 Database reader
     * @param onlineProxies - Online Proxy Hashmp
     * @param proxy - Proxy syntax host:port
     * @param types - Proxy types
     * @param lvl - Proxy anonymity level
     */
    public ProxyThread(DatabaseReader dbReader, HashMap<String, LookupResult> onlineProxies, String proxy, String types, String lvl) {
        this.onlineProxies = onlineProxies;
        this.proxy = proxy;
        this.host = proxy.split(":")[0];
        this.port = Integer.parseInt(proxy.split(":")[1]);
        this.types = types;
        this.lvl = lvl;
        this.proxyChecker = new ProxyChecker(dbReader, this.onlineProxies, this.proxy, types);
        this.proxyLookup = new ProxyLookup(dbReader, this.host);
    }

    /**
     * Method executed when thread is executed
     */
    public void run(){
        try {
            if(this.proxyChecker.check()){
                synchronized (this.onlineProxies){
                    this.onlineProxies.put(this.proxy, this.proxyLookup.getInfo());
                }
            }
        } catch (IOException | GeoIp2Exception e) {
            // Don't print anything
            e.printStackTrace();
        }
    }

}

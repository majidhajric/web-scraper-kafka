package dev.scraper.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Getter
@EqualsAndHashCode(of = "hash")
@ToString(of = "hash")
public class Hash implements Serializable {

    private final String value;

    public Hash(String pageURL) {
       this.value = calculateMD5(pageURL);
    }

    private static String calculateMD5(String url) {
        final String defaultScheme = "https";
        URI uri;
        try {
            if (url.startsWith("http")) {
                uri = new URI(defaultScheme +
                        url.substring(url.indexOf(":")));
            } else {
                uri = new URI(defaultScheme + "://" + url);
            }
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL");
        }
        String query = uri.getQuery();
        String sortedQuery = "";
        if (query != null) {
            sortedQuery = Stream.of(query.split("&"))
                    .sorted(String::compareTo)
                    .collect(Collectors.joining("&"));
        }

        String formattedURI = uri.getScheme()
                + "://"
                + uri.getHost()
                + (uri.getPort() > 0 ? ":" + uri.getPort() : "")
                + uri.getPath()
                + "?" + sortedQuery
                + (uri.getFragment() != null ? "#" + uri.getFragment() : "");

        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Objects.requireNonNull(m).reset();
        m.update(formattedURI.getBytes());
        byte[] digest = m.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        String hashtext = bigInt.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }
}

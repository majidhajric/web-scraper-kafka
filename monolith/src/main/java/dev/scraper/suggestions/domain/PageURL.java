package dev.scraper.suggestions.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.io.Serializable;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@Getter
@ToString
@EqualsAndHashCode(of = "uri")
public class PageURL implements Serializable {

    private static final String DEFAULT_SCHEME = "https";
    private final String url;
    private final String hashtext;

    public PageURL(String urlString) {
        Objects.requireNonNull(urlString);
        try {
            this.url = urlString;
            this.hashtext = calculateMD5(urlString);
        } catch (URISyntaxException e) {
            log.error(e);
            throw new IllegalArgumentException(urlString);
        }

    }

    private String calculateMD5(String urlString) throws URISyntaxException {
        URI uri;
        try {
            if (urlString.startsWith("http")) {
                uri = new URI(DEFAULT_SCHEME +
                        urlString.substring(urlString.indexOf(":")));
            } else {
                uri = new URI(DEFAULT_SCHEME + "://" + urlString);
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
        BigInteger bigInt = new BigInteger(1,digest);
        String hashtext = bigInt.toString(16);
        while(hashtext.length() < 32 ){
            hashtext = "0"+hashtext;
        }
        return hashtext;
    }
}

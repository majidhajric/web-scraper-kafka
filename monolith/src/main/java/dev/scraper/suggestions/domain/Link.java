package dev.scraper.suggestions.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "pageHash")
public class Link implements Serializable {

    private static final String DEFAULT_SCHEME = "https";

    @MongoId
    private String id;

    private String userId;

    private String pageURL;

    private String pageHash;

    private List<String> tags;

    public static Link create(String userId, String pageURL, List<String> tags) {
        String hash = null;
        try {
            hash = calculateMD5(pageURL);
        } catch (URISyntaxException e) {
            log.debug("Invalid URL", e);
        }
        Link link = new Link(UUID.randomUUID().toString(), userId, pageURL, hash,tags);
        return link;
    }

    private static String calculateMD5(String urlString) throws URISyntaxException {
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

package de.fau.cs.mad.fablab.rest.server.resources;


import de.fau.cs.mad.fablab.rest.server.configuration.SpaceApiConfiguration;
import de.fau.cs.mad.fablab.rest.server.remote.SpaceAPIService;
import de.fau.cs.mad.fablab.rest.api.SpaceApi;

import net.spaceapi.HackerSpace;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.*;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.commons.codec.binary.Hex;
import org.glassfish.jersey.client.proxy.WebResourceFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/***
 * Resource class for our /spaceapi uri
 * NOTE: This class is being used by multiple threads concurrently
 */
public class SpaceAPIResource implements SpaceApi
{
    private final SpaceApiConfiguration config;

    public static class UpdateData
    {
        enum State
        {
            open,
            closed;
        }

        UpdateData(String input)
        {
            String dataArray[] = input.split(":");

            state = State.valueOf(dataArray[0]);
            time = Integer.valueOf(dataArray[1]);
        }

        public int time;
        public State state;
    }

    public SpaceAPIResource(SpaceApiConfiguration config) {
        this.config = config;
    }

    @Override
    public String updateDoorState(String hash, String data) {

        if (hash == null || data == null || hash.isEmpty() || data.isEmpty())
            throw new NotAuthorizedException("no credentials provided");

        if (config.getKeyFile() == null || config.getKeyFile().isEmpty() ||
                config.getHashAlgorithm() == null || config.getHashAlgorithm().isEmpty())
            throw new ServiceUnavailableException("keyfile or hash algorithm is missing in configuration");

        String myHash = calculateHash(data);

        if (!hash.equals(myHash))
            throw new NotAuthorizedException("Hash is incorrect");

        UpdateData d = parseData(data);

        // now we have to fire push event

        return "{success:true}";
    }

    public static UpdateData parseData(String data)
    {
        if (!data.matches("^(open|closed):\\d+$"))
            throw new BadRequestException("data is not in valid format");

        return new UpdateData(data);
    }

    private String calculateHash(String data)
    {
        Path keyFile = Paths.get(config.getKeyFile());

        try {
            // Get an hmac_sha256 key from the raw key bytes
            byte[] keyBytes = Files.readAllBytes(keyFile);
            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, config.getHashAlgorithm());

            // Get an Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance(config.getHashAlgorithm());
            mac.init(signingKey);

            // Compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(data.getBytes());

            // Convert raw bytes to Hex
            byte[] hexBytes = new Hex().encode(rawHmac);

            //  Covert array of Hex bytes to a String
            return new String(hexBytes, "UTF-8");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw  new InternalServerErrorException("error calculating hash");
    }

    @Override
    public HackerSpace getSpace(String name) {

        if (!name.equalsIgnoreCase(config.getSpace()))
            throw new NotAllowedException("This is not allowed");

        WebTarget target = ClientBuilder.newClient().register(JacksonJsonProvider.class).target(config.getEndpoint());
        SpaceAPIService api = WebResourceFactory.newResource(SpaceAPIService.class, target);

        return api.space(name);
    }
}

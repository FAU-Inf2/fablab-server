package de.fau.cs.mad.fablab.rest.server.core.spaceapi;

import de.fau.cs.mad.fablab.rest.core.DoorState;
import de.fau.cs.mad.fablab.rest.server.configuration.SpaceApiConfiguration;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.ServiceUnavailableException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class handles a request to determine if the door is currently opened or closed.
 */
public class DoorStateRequest {

    /**
     * holds configuration/information for the key file and hash algorithm
     */
    private final SpaceApiConfiguration config;

    /**
     * current door state for this request
     */
    private DoorState doorstate;

    /**
     * private Constructor for DoorStateRequest
     * please use static fromData() method to create a new request
     * some checking and parsing will be done there
     *
     * @param config Configuration which holds information about keyfile and hashing algorithm.
     */
    public DoorStateRequest(SpaceApiConfiguration config) {
        this.config = config;
    }

    /**
     * Creates a new instance of DoorStateRequest
     *
     * @param config Configuration which holds information about key file and hashing algorithm
     * @param hash Contains a hashed representation of data, to be able to check integrity of request
     * @param data Contains data with includes timestamp and current door state
     * @return a new instance of the DoorStateRequest class
     */
    public static DoorStateRequest fromData(SpaceApiConfiguration config, String hash, String data) {

        DoorStateRequest request = new DoorStateRequest(config);

        String myHash = request.calculateHash(data);

        if (!hash.equals(myHash))
            throw new NotAuthorizedException("Hash is incorrect");

        request.parseData(data);
        request.checkData(request.doorstate);

        return request;
    }

    /**
     *  Performs some checks to data:
     *  - is timestamp in future
     *  - is timestamp in past
     *
     * @param data the data itself
     * @return returns true, if all checks passed, otherwise Exception is thrown
     */
    public boolean checkData(DoorState data){

        long currentTime = System.currentTimeMillis() / 1000L;

        if (data.time > currentTime + config.getMaximumTimeOffset())
            throw new BadRequestException("Time (" + data.time + ") is in future, current time is " + currentTime + ". Offset is " + (data.time - currentTime));

        if (data.time < currentTime - config.getMaximumTimeOffset())
            throw new BadRequestException("Time (" + data.time + ") is in past, current time is " + currentTime + ". Offset is " + (currentTime - data.time));

        return true;
    }

    /**
     * Parses data in a DoorState class(POJO)
     *
     * @param data the data itself
     * @return Returns the parsed data inside a DoorState POJO
     */
    public DoorState parseData(String data) {
        doorstate = null;

        try {
            doorstate = new DoorState(data);

            return doorstate;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        throw new BadRequestException("Data is not in valid format");
    }

    /**
     * Calculates a hash on the String given in data, uses information about hashing algorithm and keyfile from config (SpaceApiConfiguration)
     *
     * @param data the data itself
     * @return hashed representation of data
     */
    private String calculateHash(String data) {

        if (config == null ||
                config.getKeyFile() == null || config.getKeyFile().isEmpty() ||
                config.getHashAlgorithm() == null || config.getHashAlgorithm().isEmpty())
            throw new ServiceUnavailableException("Config for key file and hashing algorithm is missing");

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
        catch (Exception e) {
            e.printStackTrace();
        }
        throw new InternalServerErrorException("error calculating hash");
    }

    /**
     * Check if door state has been changed
     *
     * @param oldState old door state
     * @return returns true if door state has been changed.
     */
    public boolean checkIfChanged(DoorState oldState) {
        if (oldState == null)
            return false;

        if (doorstate == null)
            return false;

        if (oldState.state == DoorState.State.invalid)
            return false;

        if (doorstate.state == DoorState.State.invalid)
            return false;

        if (doorstate.time < oldState.time + config.getMinimumDurationUntilChange())
            return false;

        return oldState.state != doorstate.state;
    }

    /**
     * Returns the current DoorState for this request
     *
     * @return door state inside of a DoorState POJO
     */
    public DoorState getDoorState()
    {
        return doorstate;
    }
}

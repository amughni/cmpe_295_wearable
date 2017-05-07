package com.access;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by amu on 5/6/2017.
 */
@Path("smartthings/resource")
public class SmartThingsResource {
    private static String ENDPOINT_BASE;
    private static String CLIENT_ID;
    private static String BEARER;
    private static String AUTHORIZATION_SECRET_KEY;

    public void SmartThingsResource() {
        loadDefaults();
    }

    private void loadDefaults() {
        PropertyLoader properties;
        properties = new PropertyLoader();
        String base = properties.getValue("base");
        String clientId = properties.getValue("clientid");
        String bearer = properties.getValue("bearer");
        String secretKey = properties.getValue("secretkey");

        /*REPLACE NULL VALUES WITH DEFAULTS*/
        ENDPOINT_BASE = (base != null) ? base : "https://graph-na02-useast1.api.smartthings.com/api/smartapps/installations/";
        CLIENT_ID = (clientId != null) ? clientId : "360d4bd1-cb8b-4e33-9191-23f4bf400934";
        BEARER = (bearer != null) ? bearer : "Bearer ";
        AUTHORIZATION_SECRET_KEY = (secretKey != null) ? secretKey : "36f93610-0e36-4eed-a51a-4a0c4db957b6";
    }

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * @return String that will be returned as a application/json response.
     */
    @GET
    @Path("switches")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSwitches() {
        loadDefaults();
        String uri =
                ENDPOINT_BASE + CLIENT_ID + "/switches";
        HttpURLConnection connection =
                null;
        URL url;
        try {


            url = new URL(uri);

            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", BEARER + AUTHORIZATION_SECRET_KEY);

            InputStream stream = connection.getInputStream();

            if (stream != null) {
                StringBuilder response = new StringBuilder();
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        stream));
                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    response.append(inputLine);
                in.close();
                return response.toString();
            }
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(connection != null)
                connection.disconnect();
        }

        return "No data found";
    }

    /**
     * Method handling HTTP PUT requests. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * @request
     * @return String that will be returned as a application/json response.
     */
    @GET
    @Path("switches/{command}")
    public Boolean onOffSwitches(final @PathParam("command") String command) {
        loadDefaults();
        String uri =
                ENDPOINT_BASE + CLIENT_ID + "/switches/" + command;
        HttpURLConnection connection =
                null;
        URL url;
        try {


            url = new URL(uri);

            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", BEARER + AUTHORIZATION_SECRET_KEY);

            InputStream stream = connection.getInputStream();

            if (stream != null) {
                return true;
            }
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(connection != null)
                connection.disconnect();
        }
        return false;
    }
}

package com.access;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.SynthesisException;
import marytts.util.MaryRuntimeUtils;

import javax.servlet.ServletContext;
import javax.sound.sampled.*;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.*;
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

    @Context private ServletContext applicationContext;

    public void SmartThingsResource() {
        loadDefaults();
    }

    private void loadDefaults() {
        PropertyLoader properties;
        properties = new PropertyLoader();
        String base, clientId, bearer, secretKey;
        if(properties != null) {
            base = properties.getValue("base");
            clientId = properties.getValue("clientid");
            bearer = properties.getValue("bearer");
            secretKey = properties.getValue("secretkey");
        }
        else{
            base = "https://graph-na02-useast1.api.smartthings.com/api/smartapps/installations/";
            clientId = "72a7d6ec-1a3d-4d51-9fac-57508d3af415";
            bearer = "Bearer";
            secretKey = "0b09e761-a25d-488c-a83f-2c8e8370ade7";
        }

        /*REPLACE NULL VALUES WITH DEFAULTS*/
        ENDPOINT_BASE = (base != null) ? base : "https://graph-na02-useast1.api.smartthings.com/api/smartapps/installations/";
        CLIENT_ID = (clientId != null) ? clientId : "72a7d6ec-1a3d-4d51-9fac-57508d3af415";
        BEARER = (bearer != null) ? bearer : "Bearer ";
        AUTHORIZATION_SECRET_KEY = (secretKey != null) ? secretKey : "0b09e761-a25d-488c-a83f-2c8e8370ade7";
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


    /**
     * Method handling HTTP PUT requests. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * @request
     * @return String that will be returned as a application/json response.
     */
    @GET
    @Path("alexa-tts/{command}")
    public String alexaTTS(final @PathParam("command") String command) {
        return processCommand(command, "Alexa! ");
    }

    /**
     * Generic to support other voice devices like Google voice etc*/
    @GET
    @Path("voice/{type}/{command}")
    public String alexa(final @PathParam("type") String type , final @PathParam("command") String command) {
        return processCommand(command, type);
    }

    private String processCommand(String command, String type) {
        MaryInterface mary = (MaryInterface) applicationContext.getAttribute("mary-interface-instance");

        if (mary == null) {
            try {
                System.setProperty("log.config", new File(getClass().getResource("log4j.properties").getPath()).getAbsolutePath());

                mary = new LocalMaryInterface();

                MaryRuntimeUtils.ensureMaryStarted();

                applicationContext.setAttribute("mary-interface-instance", mary);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        final MaryInterface finalMary = mary;

        new Thread(() -> {
            try {
                AudioInputStream audioInputStream = finalMary.generateAudio(type + command + "!");

                Clip clip = AudioSystem.getClip();

                final Clip clipClose = clip;

                clip.addLineListener(event -> {
                    if (clipClose != null && (LineEvent.Type.STOP.equals(event.getType())
                            || LineEvent.Type.CLOSE.equals(event.getType()))) {
                        clipClose.close();
                    }
                });

                clip.open(audioInputStream);

                clip.start();
            } catch (SynthesisException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }).start();

        return "Sending command: " + command;
    }

    /**
     * Method handling HTTP PUT requests. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * @request
     * @return String that will be returned as a application/json response.
     */
    @GET
    @Path("alexa/{command}")
    public String alexa(final @PathParam("command") String command) {
        StringBuilder outputBuilder = new StringBuilder();

        URL resource = SmartThingsResource.class.getClassLoader().getResource(command + ".WAV");

        if (resource == null) return "Command not found!";

        String path = resource.getPath();

        File audioFile = new File(path).getAbsoluteFile();

        outputBuilder.append(audioFile.getAbsolutePath()).append("<br>exists: ").append(audioFile.exists())
                .append("<br>isFile: ").append(audioFile.isFile()).append("<br>canRead: ")
                .append(audioFile.canRead());

        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);

            Clip clip = AudioSystem.getClip();

            final Clip clipClose = clip;

            clip.addLineListener(event -> {
                if (clipClose != null && (LineEvent.Type.STOP.equals(event.getType())
                        || LineEvent.Type.CLOSE.equals(event.getType()))) {
                    clipClose.close();
                }
            });

            outputBuilder.append("<br>Gotten clip");

            clip.open(audioInputStream);

            outputBuilder.append("<br>Opened clip");

            clip.start();

            outputBuilder.append("<br>Started clip");
        } catch (Exception e) {
            outputBuilder.append(e.getMessage());
        }
        return outputBuilder.toString();
    }


}

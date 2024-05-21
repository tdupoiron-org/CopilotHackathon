package com.microsoft.hackathon.quarkus;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import java.io.InputStream;
import java.io.InputStreamReader;
import com.google.gson.Gson;

import java.util.List;
import java.util.Optional;
import java.io.Reader;
import com.google.gson.reflect.TypeToken;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URL;
import com.google.gson.JsonObject;

/* 
* The Demo resource should be mapped to the root path.
* 
* Create a GET operation to return the value of a key passed as query parameter in the request. 
* 
* If the key is not passed, return "key not passed".
* If the key is passed, return "hello <key>".
* 
*/

// create the class
@Path("/")
public class DemoResource {

    // create the method /hello
    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String get(@QueryParam("key") String key) {
        if (key == null) {
            return "key not passed";
        } else {
            return "hello " + key;
        }
    }

    /*New operation under /diffdates that calculates the difference between two dates. The operation should receive two dates as parameter in format dd-MM-yyyy and return the difference in days.  */
    @GET
    @Path("/diffdates")
    @Produces(MediaType.TEXT_PLAIN)
    public String diffDates(@QueryParam("date1") String date1, @QueryParam("date2") String date2) {
        if (date1 == null || date2 == null) {
            return "Both dates must be provided";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate startDate = LocalDate.parse(date1, formatter);
        LocalDate endDate = LocalDate.parse(date2, formatter);
        long diff = ChronoUnit.DAYS.between(startDate, endDate);
        return "Difference in days: " + diff;
    } 

    // new operation to Validate the format of a spanish phone number (+34 prefix, then 9 digits, starting with 6, 7 or 9). The operation should receive a phone number as parameter and return true if the format is correct, false otherwise. 
    @GET
    @Path("/validatephonenumber")
    @Produces(MediaType.TEXT_PLAIN)
    public boolean validatePhoneNumber(@QueryParam("phone") String phone) {
        if (phone == null) {
            return false;
        }
        return phone.matches("^\\+34[6-9]\\d{8}$");
    }

    // new operation Validate the format of a spanish DNI (8 digits and 1 letter). The operation should receive a DNI as parameter and return true if the format is correct, false otherwise. 
    @GET
    @Path("/validatedni")
    @Produces(MediaType.TEXT_PLAIN)
    public boolean validateDNI(@QueryParam("dni") String dni) {
        if (dni == null) {
            return false;
        }
        return dni.matches("^\\d{8}[A-Z]$");
    }

    @GET
    @Path("/getHexColor")
    @Produces(MediaType.TEXT_PLAIN)
    public String getHexColor(@QueryParam("color") String color) {
        if (color == null) {
            return "Color not provided";
        }

        try {
            // Read the JSON file
            InputStream is = getClass().getClassLoader().getResourceAsStream("colors.json");
            Reader reader = new InputStreamReader(is);

            // Parse the JSON file into a List<Color>
            List<Color> colors = new Gson().fromJson(reader, new TypeToken<List<Color>>(){}.getType());

            // Find the color in the list
            Optional<Color> colorObj = colors.stream().filter(c -> c.getColor().equals(color)).findFirst();

            // Return the hex value of the color
            if (colorObj.isPresent()) {
                return colorObj.get().getCode().getHex();
            } else {
                return "Color not found";
            }
        } catch (Exception e) {
            return "Error reading file";
        }
    }

    @GET
    @Path("/joke")
    @Produces(MediaType.TEXT_PLAIN)
    public String getJoke() {
        Client client = ClientBuilder.newClient();
        Response response = client.target("https://api.chucknorris.io/jokes/random").request().get();

        if (response.getStatus() != 200) {
            return "Error: Unable to get joke";
        }

        try {
            String jsonResponse = response.readEntity(String.class);
            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
            return jsonObject.get("value").getAsString();
        } catch (Exception e) {
            return "Error reading response";
        }
    }

    @GET
    @Path("/parse-url")
    @Produces(MediaType.APPLICATION_JSON)
    public String parseUrl(@QueryParam("url") String urlString) {
        try {
            URL url = new URL(urlString);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("protocol", url.getProtocol());
            jsonObject.addProperty("host", url.getHost());
            jsonObject.addProperty("port", url.getPort());
            jsonObject.addProperty("path", url.getPath());
            jsonObject.addProperty("query", url.getQuery());
            return jsonObject.toString();
        } catch (Exception e) {
            return "{\"error\": \"Invalid URL\"}";
        }
    }

}

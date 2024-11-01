package cab302softwaredevelopment.outbackweathertrackerapplication.services;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.CustomAlertCondition;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.WeatherAlert;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlertsService {
    @Getter
    private static AlertsService instance = new AlertsService();
    private static final String ALERTS_FILE = "alerts.dat";

    private Map<Long, List<WeatherAlert>> locationWarnings = new HashMap<>();
    @Getter @Setter
    private List<CustomAlertCondition> alertConfigs = new ArrayList<>();

    public void addAlert(CustomAlertCondition alert) {
        alertConfigs.add(alert);
    }

    public void removeAlert(CustomAlertCondition selectedCondition) {
        alertConfigs.remove(selectedCondition);
    }

    public void updateAlert(int index, CustomAlertCondition newCondition) {
        alertConfigs.set(index, newCondition);
    }

    public List<WeatherAlert> getBOMAlertsForLocation(Location location) {
        if (locationWarnings.isEmpty()) updateBOMAlertsForCurrentUserLocations();

        if (locationWarnings.containsKey(location.getId())) {
            return locationWarnings.get(location.getId());
        } else {
            updateBOMAlertsForLocation(location);
            List<WeatherAlert> temp = locationWarnings.get(location.getId());
            return temp;
        }
    }

    public void updateBOMAlertsForLocation(Location location) {
        try {
            List<WeatherAlert> alerts = new ArrayList<>();
            String region = LocationService
                    .getInstance()
                    .getStateFromCoordinates(location.getLatitude(), location.getLongitude());

            String rssFeedUrl = getRegionRssUrlLand(region);
            Document rssDocument = fetchRssFeed(rssFeedUrl);

            if (rssDocument != null) {
                NodeList itemList = rssDocument.getElementsByTagName("item");
                for (int i = 0; i < itemList.getLength(); i++) {
                    Element item = (Element) itemList.item(i);
                    String title = item.getElementsByTagName("title").item(0).getTextContent();
                    String link = item.getElementsByTagName("link").item(0).getTextContent();
                    String pubDate = item.getElementsByTagName("pubDate").item(0).getTextContent();
                    alerts.add(new WeatherAlert( "BOM Weather warning", title, link, pubDate));
                }
            }
            locationWarnings.put(location.getId(), alerts);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Document fetchRssFeed(String rssUrl) {
        try {
            URL url = new URI(rssUrl).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                System.out.println("Failed to fetch RSS feed. Response code: " + responseCode);
                return null;
            }
            InputStream stream = connection.getInputStream();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getRegionRssUrlLand(String region) {
        return switch (region.toLowerCase()) {
            case "nsw", "new south wales" -> "http://www.bom.gov.au/fwo/IDZ00061.warnings_land_nsw.xml";
            case "act", "australian capital territory" -> "http://www.bom.gov.au/fwo/IDZ00085.warnings_act.xml";
            case "vic", "victoria" -> "http://www.bom.gov.au/fwo/IDZ00066.warnings_land_vic.xml";
            case "qld", "queensland" -> "http://www.bom.gov.au/fwo/IDZ00063.warnings_land_qld.xml";
            case "wa", "western australia" -> "http://www.bom.gov.au/fwo/IDZ00067.warnings_land_wa.xml";
            case "sa", "south australia" -> "http://www.bom.gov.au/fwo/IDZ00064.warnings_land_sa.xml";
            case "tas", "tasmania" -> "http://www.bom.gov.au/fwo/IDZ00065.warnings_land_tas.xml";
            case "nt", "northern territory" -> "http://www.bom.gov.au/fwo/IDZ00062.warnings_land_nt.xml";
            default -> null;
        };
    }
    private String getRegionRssUrlMarine(String region) {
        return switch (region.toLowerCase()) {
            case "nsw", "new south wales", "act", "australian capital territory" -> "http://www.bom.gov.au/fwo/IDZ00068.warnings_marine_nsw.xml";
            case "vic", "victoria" -> "http://www.bom.gov.au/fwo/IDZ00073.warnings_marine_vic.xml";
            case "qld", "queensland" -> "http://www.bom.gov.au/fwo/IDZ00070.warnings_marine_qld.xml";
            case "wa", "western australia" -> "http://www.bom.gov.au/fwo/IDZ00074.warnings_marine_wa.xml";
            case "sa", "south australia" -> "http://www.bom.gov.au/fwo/IDZ00071.warnings_marine_sa.xml";
            case "tas", "tasmania" -> "http://www.bom.gov.au/fwo/IDZ00072.warnings_marine_tas.xml";
            case "nt", "northern territory" -> "http://www.bom.gov.au/fwo/IDZ00069.warnings_marine_nt.xml";
            default -> null;
        };
    }
    private String getRegionRssUrlBoth(String region) {
        return switch (region.toLowerCase()) {
            case "nsw", "new south wales", "act", "australian capital territory" -> "http://www.bom.gov.au/fwo/IDZ00054.warnings_nsw.xml";
            case "vic", "victoria" -> "http://www.bom.gov.au/fwo/IDZ00059.warnings_vic.xml";
            case "qld", "queensland" -> "http://www.bom.gov.au/fwo/IDZ00056.warnings_qld.xml";
            case "wa", "western australia" -> "http://www.bom.gov.au/fwo/IDZ00060.warnings_wa.xml";
            case "sa", "south australia" -> "http://www.bom.gov.au/fwo/IDZ00057.warnings_sa.xml";
            case "tas", "tasmania" -> "http://www.bom.gov.au/fwo/IDZ00058.warnings_tas.xml";
            case "nt", "northern territory" -> "http://www.bom.gov.au/fwo/IDZ00055.warnings_nt.xml";
            default -> null;
        };
    }

    public void updateBOMAlertsForCurrentUserLocations() {
        List<Location> locations = LocationService.getInstance().getCurrentUserLocations();
        for (Location location : locations) {
            updateBOMAlertsForLocation(location);
        }
    }
}

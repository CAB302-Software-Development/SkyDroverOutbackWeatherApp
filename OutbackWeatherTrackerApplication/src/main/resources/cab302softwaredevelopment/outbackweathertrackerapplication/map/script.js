// Set up the map with local tile server
var map = new ol.Map({
    target: 'map',
    layers: [
        new ol.layer.Tile({
            source: new ol.source.XYZ({
                url: 'http://localhost:8080/styles/basic-preview/{z}/{x}/{y}.png'  // Your local tile server URL
            })
        })
    ],
    view: new ol.View({
        center: ol.proj.fromLonLat([153.0251, -27.4698]), // Brisbane coordinates
        zoom: 10
    })
});

// Vector source for markers
var vectorSource = new ol.source.Vector({
});

// Vector layer to display markers
var vectorLayer = new ol.layer.Vector({
    source: vectorSource,
    style: new ol.style.Style({
        image: new ol.style.Icon({
            anchor: [0.5, -0.03],
            src: "../images/map_icons/cattle.png", // Example marker icon
            scale: 0.1
        })
    })
});

map.addLayer(vectorLayer);

// Add a marker at a specific coordinate
function addMarker(coordinate, info) {
    var marker = new ol.Feature({
        geometry: new ol.geom.Point(coordinate),
        location: info.location,
        name: info.name,
        temperature: info.temperature,
        feelsLike: info.feelsLike
    });

    vectorSource.addFeature(marker);

    // Create a popup for this marker
    var popup = new ol.Overlay.Popup();
    map.addOverlay(popup);

    // Event listener for click on the marker
    marker.set('popup', popup); // Store the popup in the marker feature

    map.on('singleclick', function(evt) {
        map.forEachFeatureAtPixel(evt.pixel, function(feature) {
            const geom = feature.getGeometry();
            if (geom instanceof ol.geom.Point) {
                const coordinate = geom.getCoordinates();
                const name = feature.get('name');
                const temperature = feature.get('temperature');
                const feelsLike = feature.get('feelsLike');

                // Show the popup at the feature's coordinates
                popup.setPosition(coordinate);
                popup.show(coordinate, `<div><strong>${name}</strong><br/>Feels like: ${temperature}°C</div>`);
            }
        });
    });
}

    const testData = [
        {
            location: "Test Location 1",
            latitude: -27.4698,
            longitude: 153.0251,
            userName: "User1",
            actualTemp: 30,
            feelsLikeTemp: 32
        },
        {
            location: "Test Location 2",
            latitude: -27.4750,
            longitude: 153.0255,
            userName: "User2",
            actualTemp: 25,
            feelsLikeTemp: 26
        },
        {
            location: "Test Location 3",
            latitude: -27.4700,
            longitude: 153.0200,
            userName: "User3",
            actualTemp: 28,
            feelsLikeTemp: 29
        }
    ];

    // Use the hardcoded test data instead of the received data
    const markers = testData;

    console.log("Test data being used:", markers); // Log the hardcoded data

    markers.forEach(function(item) {
        if (item.latitude && item.longitude) {
            const lonLat = [item.longitude, item.latitude];
            console.log("Adding marker at coordinates:", lonLat);
            addMarker(ol.proj.fromLonLat(lonLat), {
                location: item.location,
                name: item.userName,
                temperature: item.actualTemp,
                feelsLike: item.feelsLikeTemp
            });
            console.log("Marker added for:", item.userName);
        } else {
            console.error("Invalid coordinates for marker:", item);
        }
    });

document.addEventListener("kys", function() {
    // Fetch the data from the JSON file
    fetch('data.json')
        .then(response => response.json())
        .then(data => {
            console.log("Raw data:", data); // Log the raw data string

            // Parse the string into JSON (if necessary)
            let parsedData;
            try {
                parsedData = JSON.parse(data); // Attempt to parse if it's a string
            } catch (e) {
                console.error("Failed to parse JSON:", e);
                return;
            }

            console.log("Parsed data:", parsedData); // Log the parsed data

            if (!Array.isArray(parsedData)) {
                console.error("Data is not an array", parsedData);
                return; // Exit if markers is not an array
            }

            // Proceed with processing the parsed data as an array
            parsedData.forEach(function(item) {
                if (item.latitude && item.longitude) {
                    addMarker(ol.proj.fromLonLat([item.longitude, item.latitude]), {
                        location: item.location,
                        name: item.userName,
                        temperature: item.actualTemp,
                        feelsLike: item.feelsLikeTemp
                    });
                    console.log("Marker added for:", item.userName);
                } else {
                    console.error("Invalid coordinates for marker:", item);
                }
            });
        })
        .catch(error => console.error("Error fetching data:", error));
});

document.addEventListener("DOMContentLoaded", function() {

    // Add marker button functionality
    document.getElementById("add-marker-btn").addEventListener("click", function() {
        const name = document.getElementById("marker-name").value;
        const lat = parseFloat(document.getElementById("marker-lat").value);
        const lon = parseFloat(document.getElementById("marker-lon").value);
        const temp = parseFloat(document.getElementById("marker-temp").value);
        const feelsLike = parseFloat(document.getElementById("marker-feelslike").value);

        if (name && !isNaN(lat) && !isNaN(lon) && !isNaN(temp) && !isNaN(feelsLike)) {
            addMarker(ol.proj.fromLonLat([lon, lat]), {
                name: name,
                temperature: temp,
                feelsLike: feelsLike
            });
            // Clear input fields after adding the marker
            document.getElementById("marker-name").value = "";
            document.getElementById("marker-lat").value = "";
            document.getElementById("marker-lon").value = "";
            document.getElementById("marker-temp").value = "";
            document.getElementById("marker-feelslike").value = "";
        } else {
            alert("Please fill in all fields correctly.");
        }
    });

});

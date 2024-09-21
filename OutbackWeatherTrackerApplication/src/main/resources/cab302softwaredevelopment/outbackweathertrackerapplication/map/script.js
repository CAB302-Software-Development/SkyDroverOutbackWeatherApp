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
var vectorSource = new ol.source.Vector();

// Vector layer to display markers
var vectorLayer = new ol.layer.Vector({
    source: vectorSource,
    style: new ol.style.Style({
        image: new ol.style.Icon({
            anchor: [0.5, -0.03],
            src: 'https://cdn-icons-png.flaticon.com/512/252/252025.png', // Example marker icon
            scale: 0.06
        })
    })
});

map.addLayer(vectorLayer);

// Add a marker at a specific coordinate
function addMarker(coordinate, info) {
    var marker = new ol.Feature({
        geometry: new ol.geom.Point(coordinate),
        name: info.name,
        temperature: info.temperature,
        feelsLike: info.feelsLike
    });

    vectorSource.addFeature(marker);

    // Attach popup functionality using ol-ext
    var popup = new ol.Overlay.Popup();
    map.addOverlay(popup);

    // Event listener for click on the marker
    map.on('click', function(evt) {
        map.forEachFeatureAtPixel(evt.pixel, function(feature) {
            popup.show(coordinate, `<div><strong>${feature.get('name')}</strong><br/>Temp: ${feature.get('temperature')}°C</div>`);
        });
    });
}

// Example usage: Add a default marker
addMarker(ol.proj.fromLonLat([153.0251, -27.4698]), {
    name: 'Brisbane',
    temperature: 26,
    feelsLike: 28
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

    // Add your addMarker function here
});
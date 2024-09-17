// Initialize the map and set its view to a specific location and zoom level
var map = L.map('map').setView([-27.4698, 153.0251], 13); // Coordinates for Brisbane, adjust as needed

// Set up the OpenStreetMap tile layer
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
  attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);

// Initialize the geocoder
var geocoder = L.Control.Geocoder.nominatim();

// Function to perform search
function searchLocation() {
  var query = document.getElementById('search-bar').value;
  geocoder.geocode(query, function (results) {
    if (results && results.length > 0) {
      var result = results[0];
      var latlng = result.center;
      map.setView(latlng, 13); // Adjust zoom level as needed
      L.marker(latlng).addTo(map)
      .bindPopup(result.name)
      .openPopup();
    } else {
      alert("Location not found");
    }
  });
}

// Event listener for the search button
document.getElementById('search-button').addEventListener('click',
    searchLocation);

// Optional: Allow pressing Enter to trigger search
document.getElementById('search-bar').addEventListener('keypress',
    function (e) {
      if (e.key === 'Enter') {
        searchLocation();
      }
    });
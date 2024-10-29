package cab302softwaredevelopment.outbackweathertrackerapplication.utils;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Pair;
import java.util.ArrayList;
import java.util.List;

public class PointMapLayer extends MapLayer {
    private List<Pair<MapPoint, Node>> points;
    private MapPoint selectedLocation = null;
    private MapView mapView;

    public PointMapLayer(MapView mapView) {
        this.mapView = mapView;
        this.points = new ArrayList<>();
    }

    public PointMapLayer(MapView mapView, List<Pair<MapPoint, Node>> points) {
        this.points = points;
    }

    public void addPoint(Location location, Node node) {
        addPoint(new MapPoint(location.getLatitude(), location.getLongitude()), node);
    }

    public void addPoint(MapPoint point, Node node) {
        points.add(new Pair<>(point, node));
        this.markDirty();
    }

    public void setSelectedLocation(MapPoint selectedLocation) {
        this.selectedLocation = selectedLocation;
        this.markDirty();
    }

    public void clearSelectedLocation() {
        selectedLocation = null;
    }

    @Override
    protected void layoutLayer() {
        this.getChildren().clear();
        for (Pair<MapPoint, Node> candidate : points) {
            Point2D mapPoint = getMapPoint( candidate.getKey().getLatitude(),  candidate.getKey().getLongitude());

            Node icon = candidate.getValue();
            icon.setTranslateX(mapPoint.getX());
            icon.setTranslateY(mapPoint.getY());
            icon.setVisible(true);

            this.getChildren().add(icon);
        }
        if (selectedLocation != null) {
            Point2D mapPoint = getMapPoint(selectedLocation.getLatitude(), selectedLocation.getLongitude());
            Node icon = new Circle(5, Color.RED);
            icon.setTranslateX(mapPoint.getX());
            icon.setTranslateY(mapPoint.getY());
            icon.setVisible(true);
            this.getChildren().add(icon);
        }
    }

    public boolean isInBounds(MapPoint point) {
        double mapWidth = this.mapView.getWidth();
        double mapHeight = this.mapView.getHeight();

        Point2D topLeftPixel = new Point2D(0, 0);
        Point2D bottomRightPixel = new Point2D(mapWidth, mapHeight);

        MapPoint topLeftGeo = mapView.getMapPosition(topLeftPixel.getX(), topLeftPixel.getY());
        MapPoint bottomRightGeo = mapView.getMapPosition(bottomRightPixel.getX(), bottomRightPixel.getY());

        double pointLat = point.getLatitude();
        double pointLon = point.getLongitude();

        boolean isLatInBounds = (pointLat >= bottomRightGeo.getLatitude() && pointLat <= topLeftGeo.getLatitude());
        boolean isLonInBounds = (pointLon >= topLeftGeo.getLongitude() && pointLon <= bottomRightGeo.getLongitude());

        return isLatInBounds && isLonInBounds;
    }



}

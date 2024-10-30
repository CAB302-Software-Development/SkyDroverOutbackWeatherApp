package cab302softwaredevelopment.outbackweathertrackerapplication.utils;

import cab302softwaredevelopment.outbackweathertrackerapplication.database.model.Location;
import cab302softwaredevelopment.outbackweathertrackerapplication.models.SelectablePoint;
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
import java.util.Optional;

public class PointMapLayer extends MapLayer {
    private List<SelectablePoint> points;
    private int selectedIndex = -1;
    private MapView mapView;
    private MapPoint selectedPoint;

    public PointMapLayer(MapView mapView) {
        this.mapView = mapView;
        this.points = new ArrayList<>();
    }

    public void addPoint(SelectablePoint point) {
        points.add(point);
        this.markDirty();
    }

    public void setSelectedLocation(MapPoint selectedLocation) {
        Optional<SelectablePoint> point = points.stream().filter(l -> l.getPoint().equals(selectedLocation)).findFirst();
        if (point.isPresent()) {
            selectedIndex = points.indexOf(point.get());
            selectedPoint = null;
        } else {
            selectedIndex = -1;
            selectedPoint = selectedLocation;
        }
        this.markDirty();
    }

    public void clearSelectedLocation() {
        selectedIndex = -1;
    }

    @Override
    protected void layoutLayer() {
        this.getChildren().clear();
        for (int i = 0; i < points.size(); i++) {
            SelectablePoint point = points.get(i);
            Point2D mapPoint = getMapPoint(point.getPoint().getLatitude(), point.getPoint().getLongitude());
            Node icon;
            if (selectedIndex == i) {
                icon = point.getSelectedStyle();
            } else {
                icon = point.getUnselectedStyle();
            }
            icon.setTranslateX(mapPoint.getX());
            icon.setTranslateY(mapPoint.getY());
            icon.setVisible(true);

            this.getChildren().add(icon);
        }
        if (selectedPoint != null) {
            Point2D mapPoint = getMapPoint(selectedPoint.getLatitude(), selectedPoint.getLongitude());
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
